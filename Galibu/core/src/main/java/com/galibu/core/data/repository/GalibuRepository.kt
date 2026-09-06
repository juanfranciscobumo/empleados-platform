package com.galibu.core.data.repository

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import java.util.Locale
import com.galibu.core.data.local.*
import com.galibu.core.data.model.*
import com.galibu.core.data.firebase.FirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import com.galibu.core.data.firebase.awaitTask
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class GalibuRepository(
    private val userProfileDao: UserProfileDao,
    private val jobRequestDao: JobRequestDao,
    private val bidDao: BidDao,
    private val chatMessageDao: ChatMessageDao,
    private val notificationDao: AppNotificationDao,
    private val firebaseService: FirebaseService
) {
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser = _currentUser.asStateFlow()

    suspend fun insertBidDirectly(bid: Bid) = withContext(Dispatchers.IO) {
        bidDao.insertBid(bid)
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO)
    private var activeJobsListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var activeUserListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var professionalBidsListener: com.google.firebase.firestore.ListenerRegistration? = null
    private var activeProfessionalsListener: com.google.firebase.firestore.ListenerRegistration? = null
    private val clientJobBidsListeners = mutableMapOf<String, com.google.firebase.firestore.ListenerRegistration>()

    init {
        // Start real-time Firestore professionals sync in background
        activeProfessionalsListener = firebaseService.listenToAllProfessionals { pros, isFromCache ->
            repositoryScope.launch {
                val currentUserId = _currentUser.value?.id ?: ""
                val proIds = pros.map { it.id }.filter { it.isNotEmpty() }
                Log.d("GalibuRepo", "Professionals sync: ${pros.size} pros from Firestore, isFromCache=$isFromCache, currentUserId=$currentUserId")
                
                // First, insert or update all professionals from Firestore
                pros.forEach { pro ->
                    Log.d("GalibuRepo", "  Inserting pro: id=${pro.id}, name=${pro.name}, role=${pro.role}")
                    userProfileDao.insertUser(pro)
                }
                
                // Then, clean up stale professionals no longer in Firestore (only if response is from server / NOT cache)
                if (!isFromCache) {
                    if (proIds.isEmpty()) {
                        Log.w("GalibuRepo", "WARNING: Server returned 0 professionals! Deleting all from Room.")
                        userProfileDao.deleteAllProfessionalsExcept(currentUserId)
                    } else {
                        Log.d("GalibuRepo", "Cleaning up stale pros not in: $proIds")
                        userProfileDao.deleteProfessionalsNotIn(proIds, currentUserId)
                    }
                }
            }
        }
    }

    private fun clearClientJobBidsListeners() {
        clientJobBidsListeners.values.forEach { it.remove() }
        clientJobBidsListeners.clear()
    }

    private fun setCurrentUser(user: UserProfile?) {
        _currentUser.value = user
        restartJobsListenerForCurrentUser(user)
    }

    private fun restartJobsListenerForCurrentUser(user: UserProfile?) {
        activeJobsListener?.remove()
        activeJobsListener = null
        clearClientJobBidsListeners()

        if (user == null || user.id.isBlank()) {
            return
        }

        val includeOpenJobs = user.role == "PROFESSIONAL" || user.role == "BOTH"
        activeJobsListener = firebaseService.listenToRelevantJobs(
            userId = user.id,
            includeOpenJobs = includeOpenJobs
        ) { jobs, isFromCache ->
            repositoryScope.launch {
                syncJobsSnapshot(jobs, isFromCache, user.id)
            }
        }
    }

    private suspend fun syncJobsSnapshot(jobs: List<JobRequest>, isFromCache: Boolean, currentUserId: String) {
        try {
            val jobIds = jobs.map { it.id }.filter { it.isNotEmpty() }

            jobs.forEach { job ->
                try {
                    if (job.professionalId == currentUserId && currentUserId.isNotEmpty()) {
                        val existingLocal = jobRequestDao.getJobByIdSync(job.id)
                        if (existingLocal == null) {
                            val notification = AppNotification(
                                userId = currentUserId,
                                title = "¡Nueva propuesta de contratación! 🛠️",
                                body = "${job.clientName} te ha enviado una solicitud de servicio directo de ${job.category}.",
                                timestamp = System.currentTimeMillis(),
                                isRead = false
                            )
                            notificationDao.insertNotification(notification)
                        }
                    }

                    if (job.clientId == currentUserId && currentUserId.isNotEmpty()) {
                        if (!clientJobBidsListeners.containsKey(job.id)) {
                            val listener = firebaseService.listenToBids(job.id) { bids ->
                                repositoryScope.launch {
                                    bids.forEach { bidDao.insertBid(it) }
                                }
                            }
                            if (listener != null) {
                                clientJobBidsListeners[job.id] = listener
                            }
                        }
                    }

                    jobRequestDao.insertJob(job)
                } catch (e: Exception) {
                    Log.e("GalibuRepository", "Error syncing/inserting single job ${job.id}: ${e.localizedMessage}")
                }
            }

            if (!isFromCache) {
                if (jobIds.isEmpty()) {
                    jobRequestDao.deleteAllJobs()
                    clearClientJobBidsListeners()
                } else {
                    jobRequestDao.deleteJobsNotIn(jobIds)
                    val staleKeys = clientJobBidsListeners.keys - jobIds.toSet()
                    staleKeys.forEach { key ->
                        clientJobBidsListeners[key]?.remove()
                        clientJobBidsListeners.remove(key)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("GalibuRepository", "General error in jobs background sync flow: ${e.localizedMessage}")
        }
    }

    private fun listenToCurrentUserProfile(userId: String) {
        activeUserListener?.remove()
        activeUserListener = firebaseService.listenToUserProfile(userId) { updatedProfile ->
            repositoryScope.launch {
                userProfileDao.insertUser(updatedProfile)
                if (_currentUser.value?.id == userId) {
                    setCurrentUser(updatedProfile)
                }
                
                if (updatedProfile.role == "PROFESSIONAL" || updatedProfile.role == "BOTH") {
                    startProfessionalBidsListener(updatedProfile.id)
                } else {
                    professionalBidsListener?.remove()
                    professionalBidsListener = null
                }
            }
        }
    }

    private fun startProfessionalBidsListener(professionalId: String) {
        professionalBidsListener?.remove()
        professionalBidsListener = firebaseService.listenToProfessionalBids(professionalId) { bids ->
            repositoryScope.launch {
                bids.forEach { bid ->
                    bidDao.insertBid(bid)
                }
            }
        }
    }

    fun syncBidsForJob(jobId: String): com.google.firebase.firestore.ListenerRegistration? {
        return firebaseService.listenToBids(jobId) { bids ->
            repositoryScope.launch {
                bids.forEach { bid ->
                    bidDao.insertBid(bid)
                }
            }
        }
    }

    fun syncChatsForJob(jobId: String): com.google.firebase.firestore.ListenerRegistration? {
        return firebaseService.listenToChats(jobId) { chats ->
            repositoryScope.launch {
                chats.forEach { chat ->
                    chatMessageDao.insertMessage(chat)
                }
            }
        }
    }

    private suspend fun seedInitialData() {
        // Disabled for real environment testing as requested
    }

    // --- Authentication & Profiles ---

    suspend fun loginWithGoogle(
        firebaseUid: String? = null,
        email: String,
        name: String,
        avatarUrl: String?,
        role: String,
        customPhone: String? = null,
        customDepartment: String? = null,
        customMunicipality: String? = null,
        customAddress: String? = null,
        customCategory: String? = null,
        customWorkPhotos: List<String> = emptyList(),
        customHourlyRate: Double? = null,
        context: Context? = null
    ): Boolean = withContext(Dispatchers.IO) {
        val normalizedEmail = email.trim().lowercase()
        var target = if (!firebaseUid.isNullOrEmpty()) {
            userProfileDao.getUserByIdSync(firebaseUid) ?: userProfileDao.getUserByEmailSync(normalizedEmail)
        } else {
            userProfileDao.getUserByEmailSync(normalizedEmail)
        }
        
        if (target == null) {
            val fromCloud = if (!firebaseUid.isNullOrEmpty()) {
                firebaseService.fetchUserProfile(firebaseUid) ?: firebaseService.fetchUserProfileByEmail(normalizedEmail)
            } else {
                firebaseService.fetchUserProfileByEmail(normalizedEmail)
            }
            if (fromCloud != null) {
                target = fromCloud
            }
        }
        
        // Upload work photos if we have a context
        var workPhotosCsv: String? = null
        if (role == "PROFESSIONAL" && customWorkPhotos.isNotEmpty() && context != null) {
            val uploadedWorkPhotos = mutableListOf<String>()
            customWorkPhotos.forEachIndexed { index, uriStr ->
                if (uriStr.startsWith("content://") || uriStr.startsWith("file://")) {
                    try {
                        val uri = uriStr.toUri()
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val bytes = inputStream?.readBytes()
                        inputStream?.close()
                        if (bytes != null) {
                            val url = firebaseService.uploadWorkPhoto(firebaseUid ?: "google_${email.replace(".", "_")}", "google_work_$index", bytes)
                            uploadedWorkPhotos.add(url)
                        }
                    } catch (e: Exception) {
                        Log.e("GalibuRepository", "Error uploading google work photo $index: ${e.localizedMessage}")
                    }
                } else {
                    uploadedWorkPhotos.add(uriStr)
                }
            }
            workPhotosCsv = if (uploadedWorkPhotos.isNotEmpty()) uploadedWorkPhotos.joinToString("|") else null
        }
        
        if (target != null) {
            val targetRole = if (target.role != role && target.role != "BOTH") "BOTH" else target.role
            val finalPhotoUrl = if ((targetRole == "CLIENT" || targetRole == "BOTH") && context != null && !avatarUrl.isNullOrEmpty()) {
                val defaultPhoto = if (targetRole == "CLIENT") {
                    "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop"
                } else {
                    "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop"
                }
                processProfilePhotoToBase64(context, avatarUrl, defaultPhoto)
            } else {
                avatarUrl ?: target.profilePhotoUrl
            }
            val updatedUser = target.copy(
                id = firebaseUid ?: target.id,
                name = if (target.name.isBlank() || target.name == "María García" || target.name == "Cliente Galibu") name else target.name,
                profilePhotoUrl = finalPhotoUrl,
                phone = customPhone ?: target.phone,
                department = customDepartment ?: target.department,
                municipality = customMunicipality ?: target.municipality,
                address = customAddress ?: target.address,
                role = targetRole,
                workCategory = customCategory ?: target.workCategory,
                workPhotos = workPhotosCsv ?: target.workPhotos,
                hourlyRate = customHourlyRate ?: target.hourlyRate
            )
            if (target.id != updatedUser.id) {
                userProfileDao.deleteUserById(target.id)
                try {
                    firebaseService.deleteUserProfileFromCloud(target.id)
                } catch (e: Exception) {
                    Log.e("GalibuRepository", "Error deleting old cloud profile on Google sign-in: ${e.localizedMessage}")
                }
            }
            userProfileDao.insertUser(updatedUser)
            firebaseService.syncUserProfileToCloud(updatedUser)
            setCurrentUser(updatedUser)
            listenToCurrentUserProfile(updatedUser.id)
            return@withContext true
        }
        
        val generatedId = firebaseUid ?: if (role == "CLIENT") "client_${UUID.randomUUID().hashCode()}" else "pro_${UUID.randomUUID().hashCode()}"
        val defaultPhoto = if (role == "CLIENT") {
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop"
        } else {
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop"
        }
        val finalPhotoUrl = if (role == "CLIENT" && context != null) {
            processProfilePhotoToBase64(context, avatarUrl ?: defaultPhoto, defaultPhoto)
        } else {
            avatarUrl ?: defaultPhoto
        }
        val user = UserProfile(
            id = generatedId,
            name = name,
            email = normalizedEmail,
            phone = customPhone ?: "+57 325 555 5555",
            role = role,
            verificationStatus = if (role == "CLIENT") "VERIFIED" else "PENDING",
            profilePhotoUrl = finalPhotoUrl,
            password = "",
            department = customDepartment ?: "Bogotá D.C.",
            municipality = customMunicipality ?: "Bogotá",
            address = customAddress ?: "",
            workCategory = customCategory,
            workPhotos = workPhotosCsv,
            hourlyRate = customHourlyRate
        )
        userProfileDao.insertUser(user)
        firebaseService.syncUserProfileToCloud(user)
        
        try {
            val welcomeNotification = AppNotification(
                userId = generatedId,
                title = "¡Te damos la bienvenida a Galibu!",
                body = "Gracias por registrarte de forma segura en Galibu con tu cuenta de Google. Aquí puedes solicitar servicios técnicos de confianza, recibir cotizaciones transparentes y realizar pagos ágiles con soporte garantizado.",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(welcomeNotification)
        } catch (e: Exception) {
            Log.e("GalibuRepository", "Error al guardar notificación de bienvenida: ${e.localizedMessage}")
        }
        
        setCurrentUser(user)
        listenToCurrentUserProfile(user.id)
        return@withContext true
    }

    suspend fun login(email: String, role: String): String? {
        return login(email, "", role)
    }

    suspend fun login(email: String, password: String, role: String): String? = withContext(Dispatchers.IO) {
        val normalizedEmail = email.trim().lowercase()
        // 1. Try real Firebase Auth Login if initialized
        var firebaseUid: String? = null
        var isNetworkError = false
        if (firebaseService.isFirebaseInitialized()) {
            try {
                firebaseUid = kotlinx.coroutines.withTimeout(6000) {
                    firebaseService.firebaseLogin(normalizedEmail, password)
                }
                Log.d("GalibuRepository", "Firebase Auth Login exitoso. UID: $firebaseUid")
            } catch (e: Exception) {
                Log.e("GalibuRepository", "Falla en Firebase Auth Login: ${e.localizedMessage}")
                val msg = e.localizedMessage ?: ""
                val cause = e.cause
                if (e is kotlinx.coroutines.TimeoutCancellationException ||
                    e is com.google.firebase.FirebaseNetworkException ||
                    cause is java.net.UnknownHostException ||
                    cause is java.net.ConnectException ||
                    msg.contains("Unable to resolve host", ignoreCase = true) ||
                    msg.contains("network", ignoreCase = true) ||
                    msg.contains("UNAVAILABLE", ignoreCase = true)
                ) {
                    isNetworkError = true
                    Log.w("GalibuRepository", "Problema de red o resolución de host. Intentando autenticación local offline...")
                } else {
                    return@withContext "Correo o contraseña incorrectos. Por favor, verifica tus datos de ingreso."
                }
            }
        }

        // 2. Try to load by email from local DB or Firebase UID
        var target = if (firebaseUid != null) {
            userProfileDao.getUserByIdSync(firebaseUid) ?: userProfileDao.getUserByEmailSync(normalizedEmail)
        } else {
            userProfileDao.getUserByEmailSync(normalizedEmail)
        }
        
        // 3. Fallback to Cloud Firestore download if missing locally
        if (target == null && !isNetworkError) {
            try {
                val cloudProfile = if (firebaseUid != null) {
                    firebaseService.fetchUserProfile(firebaseUid) ?: firebaseService.fetchUserProfileByEmail(normalizedEmail)
                } else {
                    firebaseService.fetchUserProfileByEmail(normalizedEmail)
                }
                if (cloudProfile != null) {
                    userProfileDao.insertUser(cloudProfile)
                    target = cloudProfile
                }
            } catch (e: Exception) {
                Log.e("GalibuRepository", "Error buscando perfil en Firestore: ${e.localizedMessage}")
            }
        }

        if (target != null) {
            if (target.role == role || target.role == "BOTH") {
                if (target.password == password || firebaseUid != null) {
                    var finalTarget = target
                    if (firebaseUid != null && target.id != firebaseUid) {
                        val oldId = target.id
                        finalTarget = target.copy(id = firebaseUid)
                        userProfileDao.deleteUserById(oldId)
                        try {
                            firebaseService.deleteUserProfileFromCloud(oldId)
                        } catch (e: Exception) {
                            Log.e("GalibuRepository", "Error deleting old cloud profile on login migration: ${e.localizedMessage}")
                        }
                        userProfileDao.insertUser(finalTarget)
                        if (!isNetworkError) {
                            try {
                                firebaseService.syncUserProfileToCloud(finalTarget)
                            } catch (e: Exception) {}
                        }
                    }
                    setCurrentUser(finalTarget)
                    listenToCurrentUserProfile(finalTarget.id)
                    return@withContext null
                } else {
                    return@withContext "Contraseña incorrecta. Por favor, inténtalo de nuevo."
                }
            } else {
                Log.w("GalibuRepository", "Login fallido por discrepancia de rol: registrado como ${target.role} pero intentando ingresar con $role.")
                return@withContext "Este correo está registrado bajo el rol de ${if (target.role == "CLIENT") "Cliente" else "Profesional"}. Selecciona el rol correspondiente para ingresar."
            }
        }

        // Si tenemos un error de red y el usuario no existe localmente, informamos el problema de conexión
        if (isNetworkError) {
            return@withContext "Error de red: No pudimos conectar con los servidores de Firebase. Verifica tu internet e inténtalo nuevamente."
        }

        // Auto-create profile inside Firestore and locally only if successfully authenticated via third-party Auth (firebaseUid != null) but no database profile exists yet
        if (firebaseUid == null) {
            Log.w("GalibuRepository", "Login fallido: el usuario $normalizedEmail no existe o fue eliminado.")
            return@withContext "No existe ninguna cuenta registrada con este correo electrónico. Por favor regístrate."
        }

        val generatedId = firebaseUid
        val defaultPhoto = if (role == "CLIENT") {
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop"
        } else {
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop"
        }
        val newProfile = UserProfile(
            id = generatedId,
            name = normalizedEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = normalizedEmail,
            phone = "+57 320 000 0000",
            role = role,
            rating = 5.0f,
            completedJobs = 0,
            balance = 0.0,
            isVerified = (role == "CLIENT"),
            verificationStatus = if (role == "CLIENT") "VERIFIED" else "NOT_STARTED",
            workCategory = if (role == "PROFESSIONAL") "Plomero" else null,
            password = password,
            profilePhotoUrl = defaultPhoto,
            acceptedTerms = true
        )
        userProfileDao.insertUser(newProfile)
        try {
            firebaseService.syncUserProfileToCloud(newProfile)
        } catch (e: Exception) {}
        setCurrentUser(newProfile)
        listenToCurrentUserProfile(newProfile.id)
        return@withContext null
    }

    suspend fun registerUser(context: Context, name: String, email: String, phone: String, role: String, category: String? = null, department: String? = "Bogotá D.C.", municipality: String? = "Bogotá", profilePhotoUrl: String? = null, address: String? = "", workPhotos: List<String> = emptyList(), hourlyRate: Double? = null, acceptedTerms: Boolean = false, googleIdToken: String? = null) {
        registerUser(context, name, email, phone, "", role, category, department, municipality, profilePhotoUrl, address, workPhotos, hourlyRate, acceptedTerms, googleIdToken)
    }

    suspend fun registerUser(context: Context, name: String, email: String, phone: String, password: String, role: String, category: String? = null, department: String? = "Bogotá D.C.", municipality: String? = "Bogotá", profilePhotoUrl: String? = null, address: String? = "", workPhotos: List<String> = emptyList(), hourlyRate: Double? = null, acceptedTerms: Boolean = false, googleIdToken: String? = null) = withContext(Dispatchers.IO) {
        var finalId = if (role == "CLIENT") "client_${UUID.randomUUID()}" else "pro_${UUID.randomUUID()}"
        
        // Try registering inside Firebase Auth first if initialized
        if (firebaseService.isFirebaseInitialized()) {
            try {
                val firebaseUid = if (!googleIdToken.isNullOrBlank()) {
                    firebaseService.authenticateGoogleCredential(googleIdToken)
                } else if (password.isNotBlank()) {
                    firebaseService.firebaseRegister(email, password)
                } else {
                    null
                }
                if (firebaseUid != null) {
                    finalId = firebaseUid
                }
            } catch (e: Exception) {
                Log.e("GalibuRepository", "Falla registrando usuario en Firebase Auth: ${e.localizedMessage}")
                throw e
            }
        }

        val defaultPhoto = if (role == "CLIENT") {
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop"
        } else {
            "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop"
        }

        var uploadedPhotoUrl = profilePhotoUrl
        if (uploadedPhotoUrl.isNullOrEmpty()) {
            uploadedPhotoUrl = defaultPhoto
        }

        if (role == "CLIENT") {
            uploadedPhotoUrl = processProfilePhotoToBase64(context, uploadedPhotoUrl, defaultPhoto)
        } else {
            if (!uploadedPhotoUrl.isNullOrEmpty() && uploadedPhotoUrl.startsWith("http")) {
                val base64 = com.galibu.core.ui.utils.ImageUtils.downloadUrlToBase64(uploadedPhotoUrl)
                if (base64 != null) {
                    uploadedPhotoUrl = base64
                }
            } else if (!uploadedPhotoUrl.isNullOrEmpty() && !uploadedPhotoUrl.startsWith("data:image")) {
                try {
                    val bytes = if (uploadedPhotoUrl.startsWith("content://") || uploadedPhotoUrl.startsWith("file://")) {
                        val uri = uploadedPhotoUrl.toUri()
                        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    } else {
                        val file = java.io.File(uploadedPhotoUrl)
                        if (file.exists()) file.readBytes() else null
                    }
                    if (bytes != null) {
                        uploadedPhotoUrl = firebaseService.uploadProfilePhoto(finalId, bytes)
                    }
                } catch (e: Exception) {
                    Log.e("GalibuRepository", "Error uploading photo with final authorized UID: ${e.localizedMessage}")
                }
            }
        }

        // Upload work photos
        val uploadedWorkPhotos = mutableListOf<String>()
        workPhotos.forEachIndexed { index, uriStr ->
            if (uriStr.isNotBlank() && !uriStr.startsWith("http") && !uriStr.startsWith("data:image")) {
                try {
                    val bytes = if (uriStr.startsWith("content://") || uriStr.startsWith("file://")) {
                            val uri = uriStr.toUri()
                            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    } else {
                        val file = java.io.File(uriStr)
                        if (file.exists()) file.readBytes() else null
                    }
                    if (bytes != null) {
                        val url = firebaseService.uploadWorkPhoto(finalId, "work_$index", bytes)
                        uploadedWorkPhotos.add(url)
                    }
                } catch (e: Exception) {
                    Log.e("GalibuRepository", "Error uploading work photo $index: ${e.localizedMessage}")
                }
            } else {
                uploadedWorkPhotos.add(uriStr)
            }
        }
        val workPhotosCsv = if (uploadedWorkPhotos.isNotEmpty()) uploadedWorkPhotos.joinToString("|") else null

        val user = UserProfile(
            id = finalId,
            name = name,
            email = email.trim().lowercase(),
            phone = phone,
            role = role,
            rating = 5.0f,
            completedJobs = 0,
            balance = 0.0,
            isVerified = (role == "CLIENT"),
            verificationStatus = if (role == "CLIENT") "VERIFIED" else "NOT_STARTED",
            workCategory = category,
            password = password,
            profilePhotoUrl = uploadedPhotoUrl ?: defaultPhoto,
            department = department,
            municipality = municipality,
            address = address,
            workPhotos = workPhotosCsv,
            hourlyRate = hourlyRate,
            acceptedTerms = acceptedTerms
        )
        userProfileDao.insertUser(user)
        firebaseService.syncUserProfileToCloud(user)
        // Add welcome notification for the newly registered user
        try {
            val welcomeNotification = AppNotification(
                userId = finalId,
                title = "¡Te damos la bienvenida a Galibu!",
                body = "Gracias por registrarte de forma segura en Galibu. Aquí puedes solicitar servicios técnicos de confianza, recibir cotizaciones transparentes y realizar pagos ágiles con soporte garantizado.",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(welcomeNotification)
        } catch (e: Exception) {
            Log.e("GalibuRepository", "Error al guardar notificación de bienvenida: ${e.localizedMessage}")
        }
        setCurrentUser(user)
        listenToCurrentUserProfile(user.id)
    }

    suspend fun updateProfile(name: String, phone: String, workCategory: String?, profilePhotoUrl: String?, department: String? = "Bogotá D.C.", municipality: String? = "Bogotá", address: String? = null, hourlyRate: Double? = null) = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val updated = user.copy(
            name = name,
            phone = phone,
            workCategory = workCategory,
            profilePhotoUrl = profilePhotoUrl,
            department = department,
            municipality = municipality,
            address = address ?: user.address,
            hourlyRate = hourlyRate
        )
        userProfileDao.insertUser(updated)
        firebaseService.syncUserProfileToCloud(updated)
        setCurrentUser(updated)
    }

    suspend fun updateUserDirectly(profile: UserProfile) = withContext(Dispatchers.IO) {
        userProfileDao.insertUser(profile)
        firebaseService.syncUserProfileToCloud(profile)
        setCurrentUser(profile)
    }

    suspend fun changePassword(newPassword: String) = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val updated = user.copy(password = newPassword)
        userProfileDao.insertUser(updated)
        firebaseService.syncUserProfileToCloud(updated)
        setCurrentUser(updated)
    }

    suspend fun getUserByEmail(email: String): UserProfile? = withContext(Dispatchers.IO) {
        val local = userProfileDao.getUserByEmailSync(email.trim().lowercase())
        if (local != null) return@withContext local
        
        val remote = firebaseService.fetchUserProfileByEmail(email)
        if (remote != null) {
            userProfileDao.insertUser(remote)
            return@withContext remote
        }
        null
    }

    suspend fun getUserById(id: String): UserProfile? = withContext(Dispatchers.IO) {
        val local = userProfileDao.getUserByIdSync(id)
        if (local != null) return@withContext local
        
        // As fallback, search remote
        val remote = firebaseService.fetchUserProfile(id)
        if (remote != null) {
            userProfileDao.insertUser(remote)
            return@withContext remote
        }
        null
    }

    suspend fun deleteAccount() = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        
        // Disconnect real-time listener first to avoid receiving deletes or updates in retro
        activeUserListener?.remove()
        activeUserListener = null
        professionalBidsListener?.remove()
        professionalBidsListener = null
        
        // Disassociate user across remote Firestore and Authenticator
        firebaseService.deleteUserProfileFromCloud(user.id)
        firebaseService.deleteCurrentUserAuth()
        
        // Delete user's row inside local Room DB
        userProfileDao.deleteUserById(user.id)
        setCurrentUser(null)
    }

    suspend fun rechargeBalance(amount: Double) = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val updated = user.copy(balance = user.balance + amount)
        userProfileDao.insertUser(updated)
        firebaseService.syncUserProfileToCloud(updated)
        setCurrentUser(updated)
        
        // Add push notification
        val alert = AppNotification(
            userId = user.id,
            title = "Recarga de Saldo Exitosa 💰",
            body = "Has ingresado $$amount COP a tu billetera virtual de Galibu de forma inmediata."
        )
        notificationDao.insertNotification(alert)
    }

    suspend fun logout() {
        activeUserListener?.remove()
        activeUserListener = null
        professionalBidsListener?.remove()
        professionalBidsListener = null
        firebaseService.firebaseSignOut()
        
        withContext(Dispatchers.IO) {
            try {
                // Completely purge local Room database to prevent caching or stale state issues for the next user session
                jobRequestDao.deleteAllJobs()
                userProfileDao.deleteAllUserProfiles()
                bidDao.deleteAllBids()
                chatMessageDao.deleteAllChatMessages()
                notificationDao.deleteAllNotifications()
            } catch (e: Exception) {
                Log.e("GalibuRepository", "Error clearing local database on logout: ${e.localizedMessage}")
            }
        }
        setCurrentUser(null)
    }

    suspend fun sendPasswordResetEmail(email: String) = withContext(Dispatchers.IO) {
        if (firebaseService.isFirebaseInitialized()) {
            firebaseService.firebaseSendPasswordResetEmail(email)
        }
    }

    suspend fun getDepartments() = firebaseService.fetchDepartments()
    suspend fun getCategories() = firebaseService.fetchCategories()
    suspend fun addCategory(name: String) = firebaseService.addCategory(name)
    fun isFirebaseInitialized() = firebaseService.isFirebaseInitialized()

    // --- Document Verification ---

    suspend fun submitVerificationDocuments(docType: String, docBytes: ByteArray) = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val docUrl = firebaseService.uploadIdentityDocument(user.id, docBytes)
        
        val updatedUser = user.copy(
            documentType = docType,
            documentPhotoSimulatedUrl = docUrl,
            verificationStatus = "PENDING"
        )
        userProfileDao.insertUser(updatedUser)
        firebaseService.syncUserProfileToCloud(updatedUser)
        setCurrentUser(updatedUser)
    }

    // --- Job Requests ---

    fun getAllProfessionals(): Flow<List<UserProfile>> = userProfileDao.getProfessionalsFlow()

    fun getAllJobs(): Flow<List<JobRequest>> = jobRequestDao.getAllJobsFlow()
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getOpenJobs(): Flow<List<JobRequest>> {
        val userIdFlow = _currentUser.map { it?.id.orEmpty() }.distinctUntilChanged()
        return userIdFlow.flatMapLatest { userId ->
            if (userId.isEmpty()) {
                jobRequestDao.getOpenJobsFlow()
            } else {
                combine(
                    jobRequestDao.getAllJobsFlow(),
                    bidDao.getJobIdsWithBidsFlow(userId)
                ) { allJobs, bidJobIds ->
                    allJobs.filter { job ->
                        val isOpen = job.status == "OPEN"
                        val isNegotiatingWithMe = job.status == "NEGOTIATING" && (job.id in bidJobIds || job.professionalId == userId)
                        val isAcceptedByMe = job.status == "ACCEPTED" && job.professionalId == userId
                        
                        isOpen || isNegotiatingWithMe || isAcceptedByMe
                    }
                }
            }
        }
    }
    fun getJobsByClient(clientId: String): Flow<List<JobRequest>> = jobRequestDao.getJobsByClientFlow(clientId)
    fun getJobsByProfessional(proId: String): Flow<List<JobRequest>> = jobRequestDao.getJobsByProfessionalFlow(proId)
    fun getJobById(jobId: String): Flow<JobRequest?> = jobRequestDao.getJobByIdFlow(jobId)

    suspend fun insertJobDirectly(job: JobRequest) = withContext(Dispatchers.IO) {
        jobRequestDao.insertJob(job)
        firebaseService.pushJobToCloud(job)
        if (!job.professionalId.isNullOrEmpty()) {
            val notification = AppNotification(
                userId = job.professionalId,
                title = "¡Nueva propuesta de contratación! 🛠️",
                body = "${job.clientName} te ha enviado una solicitud de servicio directo de ${job.category}.",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            notificationDao.insertNotification(notification)
        }
    }

    suspend fun createJobRequest(category: String, title: String, description: String, budget: Double, address: String, department: String = "Bogotá D.C.", municipality: String = "Bogotá") = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val jobId = "job_${UUID.randomUUID().hashCode()}"
        val job = JobRequest(
            id = jobId,
            category = category,
            title = title,
            description = description,
            budgetMin = budget,
            address = address,
            latitude = 4.6097 + (Math.random() - 0.5) * 0.05, // random Bogotá range coordinates
            longitude = -74.0817 + (Math.random() - 0.5) * 0.05,
            status = "OPEN",
            clientId = user.id,
            clientName = user.name,
            department = department,
            municipality = municipality,
            createdAt = System.currentTimeMillis()
        )
        jobRequestDao.insertJob(job)
        firebaseService.pushJobToCloud(job)
    }

    // --- Price Bids & Negotiations ---

    fun getBidsForJob(jobId: String): Flow<List<Bid>> = bidDao.getBidsForJobFlow(jobId)

    suspend fun submitBid(jobId: String, amount: Double, comment: String, durationHours: Int) = withContext(Dispatchers.IO) {
        val user = _currentUser.value ?: return@withContext
        val bidId = "bid_${UUID.randomUUID().hashCode()}"
        val bid = Bid(
            id = bidId,
            jobId = jobId,
            professionalId = user.id,
            professionalName = user.name,
            professionalRating = user.rating,
            professionalPhoto = if (!user.profilePhotoUrl.isNullOrEmpty()) user.profilePhotoUrl else "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop",
            amount = amount,
            comment = comment,
            durationHours = durationHours,
            status = "PENDING"
        )
        bidDao.insertBid(bid)
        firebaseService.pushBidToCloud(jobId, bid)

        // Update job status and reflect latest professional offer at job level
        val job = jobRequestDao.getJobByIdSync(jobId)
        if (job != null && job.status == "OPEN") {
            try {
                val cloudJob = firebaseService.getJobFromCloud(jobId)
                val finalJob = cloudJob ?: job
                val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
                val updatedJob = finalJob.copy(
                    status = "NEGOTIATING",
                    counterOfferAmount = amount,
                    counterOfferSender = "PROFESSIONAL",
                    address = finalAddress
                )
                jobRequestDao.insertJob(updatedJob)
                firebaseService.pushJobToCloud(updatedJob)
            } catch (e: Exception) {
                Log.e("GalibuRepository", "Error updating job status after bid submit: ${e.localizedMessage}")
            }
        }

        // Create alert for Client
        if (job != null) {
            val alert = AppNotification(
                userId = job.clientId,
                title = "¡Nueva oferta de servicio!",
                body = "${user.name} ha propuesto $$amount COP para tu solicitud de '${job.title}'."
            )
            notificationDao.insertNotification(alert)
        }
    }

    /**
     * Counter offer negotiation in real-time
     */
    suspend fun proposeCounterOffer(bidId: String, counterAmount: Double) = withContext(Dispatchers.IO) {
        val bid = bidDao.getBidByIdSync(bidId) ?: return@withContext
        val job = jobRequestDao.getJobByIdSync(bid.jobId) ?: return@withContext
        val user = _currentUser.value ?: return@withContext

        // Update bid indicating client counters
        val isUserClient = (user.id == job.clientId)
        val updatedBid = bid.copy(
            amount = counterAmount,
            status = if (isUserClient) "COUNTERED_BY_CLIENT" else "COUNTERED_BY_PROFESSIONAL",
            lastCounterOfferAmount = counterAmount
        )
        bidDao.insertBid(updatedBid)
        firebaseService.pushBidToCloud(job.id, updatedBid)

        val targetUserId = if (isUserClient) bid.professionalId else job.clientId
        
        // Notify respective counterparty
        val alert = AppNotification(
            userId = targetUserId,
            title = "Contraoferta propuesta 🔄",
            body = "${user.name} ofrece un precio negociado de $$counterAmount COP para '${job.title}'."
        )
        notificationDao.insertNotification(alert)
    }

    suspend fun acceptBid(bidId: String) = withContext(Dispatchers.IO) {
        val bid = bidDao.getBidByIdSync(bidId) ?: return@withContext
        acceptBidInternal(bidId, bid.professionalName, isBot = bid.professionalId.startsWith("bot_"))
    }

    private suspend fun acceptBidInternal(bidId: String, professionalName: String, isBot: Boolean) {
        val bid = bidDao.getBidByIdSync(bidId) ?: return
        val job = jobRequestDao.getJobByIdSync(bid.jobId) ?: return

        // Update accepted bid
        val updatedBid = bid.copy(status = "ACCEPTED")
        bidDao.insertBid(updatedBid)
        firebaseService.pushBidToCloud(job.id, updatedBid)

        // Clear other bids for this job
        bidDao.deleteBidsForJob(job.id)

        // Update JobRequest to accepted
        val cloudJob = firebaseService.getJobFromCloud(job.id)
        val finalJob = cloudJob ?: job
        val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val updatedJob = finalJob.copy(
            status = "ACCEPTED",
            professionalId = bid.professionalId,
            professionalName = professionalName,
            finalPrice = bid.amount,
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // Create notification for professional
        val alert = AppNotification(
            userId = bid.professionalId,
            title = "Oferta Aceptada 🎉",
            body = "¡Felicidades! ${job.clientName} ha aceptado tu presupuesto de $$${bid.amount} COP para '${job.title}'."
        )
        notificationDao.insertNotification(alert)

        // Send entry chat system message
        val welcomeMsg = ChatMessage(
            jobId = job.id,
            senderId = "system",
            senderName = "Sistema",
            text = "¡Presupuesto pactado en $$${bid.amount} COP! Chat seguro iniciado. Coordinen los detalles aquí."
        )
        chatMessageDao.insertMessage(welcomeMsg)
        firebaseService.sendChatMessageToCloud(welcomeMsg)
    }

    suspend fun rejectBid(bidId: String) = withContext(Dispatchers.IO) {
        val bid = bidDao.getBidByIdSync(bidId) ?: return@withContext
        val updatedBid = bid.copy(status = "REJECTED")
        bidDao.insertBid(updatedBid)
        firebaseService.pushBidToCloud(bid.jobId, updatedBid)

        // Notify pro
        val alert = AppNotification(
            userId = bid.professionalId,
            title = "Oferta Rechazada 😔",
            body = "Tu oferta de presupuesto de $$${bid.amount} COP para '${bid.id}' ha sido rechazada por el cliente."
        )
        notificationDao.insertNotification(alert)
    }

    suspend fun cancelJob(jobId: String, reason: String = "") = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        val currentUser = _currentUser.value ?: return@withContext
        
        // Only client can cancel their own jobs
        if (job.clientId != currentUser.id && job.professionalId != currentUser.id) {
            return@withContext
        }
        
        // Get job from cloud and merge
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val finalJob = cloudJob ?: job
        val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        
        // Update job status to CANCELLED
        val updatedJob = finalJob.copy(
            status = "CANCELLED",
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)
        
        // Notify all bidders if any
        val bids = firebaseService.getBidsForJob(jobId)
        bids.forEach { bid ->
            val alert = AppNotification(
                userId = bid.professionalId,
                title = "Solicitud Cancelada ❌",
                body = "El cliente ha cancelado la solicitud '${job.title}'${if (reason.isNotEmpty()) ": $reason" else ""}."
            )
            withContext(Dispatchers.IO) {
                notificationDao.insertNotification(alert)
            }
        }
        
        // Notify professional if job was ACCEPTED
        if (job.status == "ACCEPTED" && !job.professionalId.isNullOrEmpty()) {
            val isCanceller = job.professionalId == currentUser.id
            val targetUserId = if (isCanceller) job.clientId else job.professionalId!!
            val alert = AppNotification(
                userId = targetUserId,
                title = "Trabajo Cancelado ❌",
                body = "${currentUser.name} ha cancelado el trabajo '${job.title}'${if (reason.isNotEmpty()) ": $reason" else ""}."
            )
            notificationDao.insertNotification(alert)
        }
        
        // Delete all bids for this job
        bidDao.deleteBidsForJob(jobId)
        
        // Send system message in chat if chat exists
        if (job.status == "ACCEPTED") {
            val systemMsg = ChatMessage(
                jobId = jobId,
                senderId = "system",
                senderName = "Sistema",
                text = "⚠️ Este trabajo ha sido cancelado por ${currentUser.name}${if (reason.isNotEmpty()) ": $reason" else ""}."
            )
            chatMessageDao.insertMessage(systemMsg)
            firebaseService.sendChatMessageToCloud(systemMsg)
        }
    }

    // --- Chat and Messages ---

    fun getChatMessagesForJob(jobId: String): Flow<List<ChatMessage>> = chatMessageDao.getMessagesForJobFlow(jobId)

    fun getJobIdsWithBids(professionalId: String): Flow<List<String>> = bidDao.getJobIdsWithBidsFlow(professionalId)

    fun getBidsByProfessional(professionalId: String): Flow<List<Bid>> = bidDao.getBidsForProfessionalFlow(professionalId)

    suspend fun sendMessage(jobId: String, message: ChatMessage) = withContext(Dispatchers.IO) {
        try {
            firebaseService.sendChatMessageToCloud(message)
        } catch (e: Exception) {
            Log.e("GalibuRepository", "sendMessage failed for job $jobId", e)
        }
    }

    suspend fun sendChatMessage(jobId: String, text: String) = withContext(Dispatchers.IO) {
        val currentUserVal = _currentUser.value ?: return@withContext
        val msg = ChatMessage(
            jobId = jobId,
            senderId = currentUserVal.id,
            senderName = currentUserVal.name,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        sendMessage(jobId, msg)
    }

    suspend fun sendImageMessage(jobId: String, imageBytes: ByteArray) = withContext(Dispatchers.IO) {
        val currentUserVal = _currentUser.value ?: return@withContext
        val msgId = java.util.UUID.randomUUID().toString()
        val imageUrl = firebaseService.uploadChatImage(jobId, msgId, imageBytes)
        
        val msg = ChatMessage(
            id = msgId,
            jobId = jobId,
            senderId = currentUserVal.id,
            senderName = currentUserVal.name,
            text = imageUrl,
            type = "image",
            timestamp = System.currentTimeMillis()
        )
        sendMessage(jobId, msg)
    }

    suspend fun markAsRead(jobId: String) = withContext(Dispatchers.IO) {
        val currentUserId = _currentUser.value?.id ?: return@withContext
        firebaseService.markChatMessagesAsReadInCloud(jobId, currentUserId)
    }

    suspend fun deleteMessage(jobId: String, messageId: String) = withContext(Dispatchers.IO) {
        firebaseService.deleteChatMessageFromCloud(jobId, messageId)
    }

    suspend fun deleteMessage(messageId: String) = withContext(Dispatchers.IO) {
        Log.w("GalibuRepository", "deleteMessage(messageId) called but jobId is required for subcollection path jobs/jobId/chats/messageId")
    }

    suspend fun setTypingStatus(jobId: String, typing: Boolean) = withContext(Dispatchers.IO) {
        val currentUserId = _currentUser.value?.id ?: return@withContext
        firebaseService.setTypingStatusInCloud(jobId, currentUserId, typing)
    }

    fun listenTypingStatus(jobId: String): Flow<Map<String, Boolean>> = callbackFlow {
        val listener = firebaseService.listenToTypingStatus(jobId) { typingMap ->
            trySend(typingMap)
        }
        awaitClose {
            listener?.remove()
        }
    }

    // --- Payments and Completion ---

    /**
     * Set job request status to PENDING_RATING so both users must rate each other before closing.
     */
    suspend fun startJobCompletion(jobId: String) = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        if (job.status == "PENDING_RATING" || job.status == "COMPLETED") return@withContext

        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val finalJob = cloudJob ?: job
        val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val updatedJob = finalJob.copy(
            status = "PENDING_RATING",
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // System message in chat
        val systemMsg = ChatMessage(
            jobId = jobId,
            senderId = "system",
            senderName = "Sistema",
            text = "Servicio finalizado en campo. Se encuentra en estado 'Pendiente de Calificación' hasta que ambos usuarios (Cliente y Profesional) se califiquen mutuamente."
        )
        chatMessageDao.insertMessage(systemMsg)
        firebaseService.sendChatMessageToCloud(systemMsg)

        // Create alert notifications
        val alertClient = AppNotification(
            userId = job.clientId,
            title = "Calificación Pendiente ⚠️",
            body = "El servicio '${job.title}' requiere tu calificación. Califica al profesional para completar el cierre."
        )
        notificationDao.insertNotification(alertClient)

        if (job.professionalId != null) {
            val alertPro = AppNotification(
                userId = job.professionalId,
                title = "Calificación Pendiente ⚠️",
                body = "El servicio '${job.title}' requiere tu calificación. Califica al cliente para completar el cierre."
            )
            notificationDao.insertNotification(alertPro)
        }
    }

    suspend fun simulateJobPayment(jobId: String, paymentMethod: String) = withContext(Dispatchers.IO) {
        finalizeAndPayJob(jobId, paymentMethod)
    }

    suspend fun finalizeAndPayJob(jobId: String, paymentMethod: String = "Transferencia / Nequi") = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        val finalPrice = job.finalPrice ?: job.budgetMin

        // Deduct money from client and add to professional
        val client = userProfileDao.getUserByIdSync(job.clientId)
        val pro = if (job.professionalId != null) userProfileDao.getUserByIdSync(job.professionalId) else null

        if (client != null) {
            val updatedClient = client.copy(
                balance = (client.balance - finalPrice).coerceAtLeast(0.0),
                completedJobs = client.completedJobs + 1
            )
            userProfileDao.insertUser(updatedClient)
            if (_currentUser.value?.id == client.id) {
                setCurrentUser(updatedClient)
            }
        }

        if (pro != null) {
            val updatedPro = pro.copy(
                balance = pro.balance + finalPrice,
                completedJobs = pro.completedJobs + 1
            )
            userProfileDao.insertUser(updatedPro)
            if (_currentUser.value?.id == pro.id) {
                setCurrentUser(updatedPro)
            }
        }

        // Complete job request in DB
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val finalJob = cloudJob ?: job
        val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val completedJob = finalJob.copy(
            status = "COMPLETED",
            address = finalAddress
        )
        jobRequestDao.insertJob(completedJob)
        firebaseService.pushJobToCloud(completedJob)

        // System message in chat
        val systemMsg = ChatMessage(
            jobId = jobId,
            senderId = "system",
            senderName = "Sistema Pagos",
            text = "¡Ambas calificaciones completadas! Pago de $${String.format(Locale.US, "%,.0f", finalPrice)} COP liberado con éxito usando $paymentMethod. El servicio ha sido marcado como COMPLETADO."
        )
        chatMessageDao.insertMessage(systemMsg)
        firebaseService.sendChatMessageToCloud(systemMsg)

        // Create alert notifications
        val alertClient = AppNotification(
            userId = job.clientId,
            title = "Servicio Completado 🏆",
            body = "El pago a ${job.professionalName} ha sido liberado exitosamente. ¡Gracias por calificar!"
        )
        notificationDao.insertNotification(alertClient)

        if (job.professionalId != null) {
            val alertPro = AppNotification(
                userId = job.professionalId,
                title = "Pago recibido ($${String.format(Locale.US, "%,.0f", finalPrice)} COP) 💰",
                body = "¡Buen trabajo! El servicio se cerró y el pago fue liberado vía $paymentMethod."
            )
            notificationDao.insertNotification(alertPro)
        }
    }

    suspend fun submitServiceRating(jobId: String, rating: Float, review: String, isClientRating: Boolean) = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val finalJob = cloudJob ?: job
        val finalAddress = if (finalJob.address.isNotBlank()) finalJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        
        val updatedJob = if (isClientRating) {
            finalJob.copy(
                clientRatingOfPro = rating,
                clientReviewOfPro = review,
                completionRating = rating, // keep for backward compatibility
                completionReview = review, // keep for backward compatibility
                address = finalAddress
            )
        } else {
            finalJob.copy(
                proRatingOfClient = rating,
                proReviewOfClient = review,
                address = finalAddress
            )
        }
        
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // Re-calculate average ratings
        if (isClientRating) {
            val proId = job.professionalId ?: return@withContext
            val proProfile = userProfileDao.getUserByIdSync(proId)
            if (proProfile != null) {
                val currentRating = proProfile.rating
                val jobsCount = proProfile.completedJobs + 1
                val newAverage = ((currentRating * (jobsCount - 1)) + rating) / jobsCount
                val updatedPro = proProfile.copy(rating = newAverage)
                userProfileDao.insertUser(updatedPro)
            }
        } else {
            val clientId = job.clientId
            val clientProfile = userProfileDao.getUserByIdSync(clientId)
            if (clientProfile != null) {
                val currentRating = clientProfile.rating
                val jobsCount = clientProfile.completedJobs + 1
                val newAverage = ((currentRating * (jobsCount - 1)) + rating) / jobsCount
                val updatedClient = clientProfile.copy(rating = newAverage)
                userProfileDao.insertUser(updatedClient)
            }
        }

        // Check if BOTH ratings are now submitted:
        val freshJob = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        if (freshJob.clientRatingOfPro != null && freshJob.proRatingOfClient != null) {
            finalizeAndPayJob(jobId)
        }
    }

    // --- Push Notifications Center ---

    fun getNotificationsFlow(): Flow<List<AppNotification>> {
        val currentUserId = _currentUser.value?.id ?: ""
        return notificationDao.getNotificationsForUserFlow(currentUserId)
    }

    suspend fun markNotificationAsRead(id: Int) = withContext(Dispatchers.IO) {
        notificationDao.markAsRead(id)
    }

    suspend fun acceptDirectHire(jobId: String) = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val mergedJob = (cloudJob ?: job).copy(
            clientId = if ((cloudJob?.clientId ?: "").isNotBlank()) cloudJob!!.clientId else job.clientId,
            clientName = if ((cloudJob?.clientName ?: "").isNotBlank()) cloudJob!!.clientName else job.clientName,
            professionalId = cloudJob?.professionalId ?: job.professionalId,
            professionalName = cloudJob?.professionalName ?: job.professionalName
        )
        val finalAddress = if (mergedJob.address.isNotBlank()) mergedJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val finalPriceValue = mergedJob.counterOfferAmount ?: mergedJob.proHourlyRate ?: mergedJob.budgetMin
        val updatedJob = mergedJob.copy(
            status = "ACCEPTED",
            finalPrice = finalPriceValue,
            counterOfferSender = null,
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // Create alert/notification for Client
        val alert = AppNotification(
            userId = job.clientId,
            title = "Contratación Aceptada 🎉",
            body = "¡Buenas noticias! ${job.professionalName ?: "El profesional"} ha aceptado tu solicitud de contratación directa de ${job.category}."
        )
        notificationDao.insertNotification(alert)

        // System message in chat
        val welcomeMsg = ChatMessage(
            jobId = job.id,
            senderId = "system",
            senderName = "Sistema",
            text = "¡El profesional aceptó la solicitud de contratación directa! Chat seguro iniciado."
        )
        chatMessageDao.insertMessage(welcomeMsg)
        firebaseService.sendChatMessageToCloud(welcomeMsg)
    }

    suspend fun rejectDirectHire(jobId: String) = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val mergedJob = (cloudJob ?: job).copy(
            clientId = if ((cloudJob?.clientId ?: "").isNotBlank()) cloudJob!!.clientId else job.clientId,
            clientName = if ((cloudJob?.clientName ?: "").isNotBlank()) cloudJob!!.clientName else job.clientName,
            professionalId = cloudJob?.professionalId ?: job.professionalId,
            professionalName = cloudJob?.professionalName ?: job.professionalName
        )
        val finalAddress = if (mergedJob.address.isNotBlank()) mergedJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val updatedJob = mergedJob.copy(
            status = "CANCELLED",
            counterOfferSender = null,
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // Create alert/notification for Client
        val alert = AppNotification(
            userId = job.clientId,
            title = "Contratación Rechazada ❌",
            body = "${job.professionalName ?: "El profesional"} ha declinado tu solicitud de contratación directa de ${job.category}."
        )
        notificationDao.insertNotification(alert)
    }

    suspend fun cancelJobRequest(jobId: String) = withContext(Dispatchers.IO) {
        // Delete from local DB
        jobRequestDao.deleteJobById(jobId)
        // Delete from Firebase Firestore
        firebaseService.deleteJobFromCloud(jobId)
    }

    suspend fun submitCounterOfferDirectHire(jobId: String, amount: Double, isClient: Boolean) = withContext(Dispatchers.IO) {
        val job = jobRequestDao.getJobByIdSync(jobId) ?: return@withContext
        val cloudJob = firebaseService.getJobFromCloud(jobId)
        val mergedJob = (cloudJob ?: job).copy(
            clientId = if ((cloudJob?.clientId ?: "").isNotBlank()) cloudJob!!.clientId else job.clientId,
            clientName = if ((cloudJob?.clientName ?: "").isNotBlank()) cloudJob!!.clientName else job.clientName,
            professionalId = cloudJob?.professionalId ?: job.professionalId,
            professionalName = cloudJob?.professionalName ?: job.professionalName
        )
        val finalAddress = if (mergedJob.address.isNotBlank()) mergedJob.address else if (job.address.isNotBlank()) job.address else "Cl. 51 #62-25, Rionegro, Antioquia, Colombia"
        val updatedJob = mergedJob.copy(
            status = "NEGOTIATING",
            counterOfferAmount = amount,
            counterOfferSender = if (isClient) "CLIENT" else "PROFESSIONAL",
            address = finalAddress
        )
        jobRequestDao.insertJob(updatedJob)
        firebaseService.pushJobToCloud(updatedJob)

        // Notification title & body
        val titleMsg = if (isClient) "Nueva oferta del cliente 🛠️" else "Contraoferta recibida 🎯"
        val bodyMsg = if (isClient) {
            "${job.clientName} te ha enviado una nueva oferta de $${amount.toInt()}/h por el servicio de ${job.category}."
        } else {
            "${job.professionalName ?: "El profesional"} te ha enviado una contraoferta de $${amount.toInt()}/h por el servicio de ${job.category}."
        }

        val targetUserId = if (isClient) job.professionalId ?: "" else job.clientId
        if (targetUserId.isNotEmpty()) {
            val alert = AppNotification(
                userId = targetUserId,
                title = titleMsg,
                body = bodyMsg
            )
            notificationDao.insertNotification(alert)
        }

        // System message in chat
        val senderText = if (isClient) "El cliente" else "El profesional"
        val sysMsg = ChatMessage(
            jobId = job.id,
            senderId = "system",
            senderName = "Sistema",
            text = "¡$senderText propuso una nueva tarifa de $${amount.toInt()}/h!"
        )
        chatMessageDao.insertMessage(sysMsg)
        firebaseService.sendChatMessageToCloud(sysMsg)
    }

    private suspend fun processProfilePhotoToBase64(context: Context, photoUrl: String?, defaultPhoto: String): String {
        val targetUrl = if (photoUrl.isNullOrEmpty()) defaultPhoto else photoUrl
        
        if (targetUrl.startsWith("data:image/")) {
            return targetUrl
        }
        
        if (targetUrl.startsWith("http")) {
            val base64 = com.galibu.core.ui.utils.ImageUtils.downloadUrlToBase64(targetUrl)
            return base64 ?: targetUrl
        }
        
        if (targetUrl.startsWith("content://") || targetUrl.startsWith("file://")) {
            return try {
                val uri = targetUrl.toUri()
                val base64 = com.galibu.core.ui.utils.ImageUtils.uriToBase64(context, uri)
                base64 ?: targetUrl
            } catch (e: Exception) {
                targetUrl
            }
        }
        
        val base64 = com.galibu.core.ui.utils.ImageUtils.filePathToBase64(targetUrl)
        return base64 ?: targetUrl
    }
}


