package com.galibu.core.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.galibu.core.data.local.AppDatabase
import com.galibu.core.data.firebase.FirebaseService
import com.galibu.core.data.model.*
import com.galibu.core.data.repository.GalibuRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class SharedViewModel(application: Application) : AndroidViewModel(application) {

    protected val database = AppDatabase.getDatabase(application)
    val firebaseService = FirebaseService()
    
    val repository = GalibuRepository(
        userProfileDao = database.userProfileDao(),
        jobRequestDao = database.jobRequestDao(),
        bidDao = database.bidDao(),
        chatMessageDao = database.chatMessageDao(),
        notificationDao = database.appNotificationDao(),
        firebaseService = firebaseService
    )

    val currentUser: StateFlow<UserProfile?> = repository.currentUser

    private val _activeRole = MutableStateFlow<String?>(null)
    val activeRole: StateFlow<String?> = _activeRole.asStateFlow()

    val activeBannerNotification = MutableStateFlow<AppNotification?>(null)

    private val _isGlobalLoading = MutableStateFlow(false)
    val isGlobalLoading = _isGlobalLoading.asStateFlow()

    private val _globalLoadingMessage = MutableStateFlow("Cargando...")
    val globalLoadingMessage = _globalLoadingMessage.asStateFlow()

    fun showLoading(message: String = "Sincronizando con Firebase...") {
        _globalLoadingMessage.value = message
        _isGlobalLoading.value = true
    }

    fun hideLoading() {
        _isGlobalLoading.value = false
    }

    data class GlobalAlert(val title: String, val message: String, val isError: Boolean = false)
    private val _globalAlert = MutableStateFlow<GlobalAlert?>(null)
    val globalAlert = _globalAlert.asStateFlow()

    fun showAlert(title: String, message: String, isError: Boolean = false) {
        _globalAlert.value = GlobalAlert(title, message, isError)
    }

    fun dismissAlert() {
        _globalAlert.value = null
    }

    data class PortfolioItem(val title: String, val desc: String, val imageUrl: String)
    
    val portfolios = mapOf(
        "pro_123" to listOf(
            PortfolioItem("Remodelación Baño Principal", "Instalación completa de griferías premium, tuberías PVC reforzadas de alta presión y acabados de cerámica.", "https://images.unsplash.com/photo-1584622650111-993a426fbf0a?w=400"),
            PortfolioItem("Certificado Especialista Sena", "Acreditación oficial en fontanería, instalaciones de agua domiciliarias y soldadura galvánica de cobre.", "https://images.unsplash.com/photo-1589330694653-ded6df03f754?w=400"),
            PortfolioItem("Reparación Trampa de Grasas", "Limpieza técnica e instalación de filtros herméticos en restaurante céntrico de Bogotá.", "https://images.unsplash.com/photo-1542013936693-8848e5740a7a?w=400")
        ),
        "bot_luis_ferruccio" to listOf(
            PortfolioItem("Reparación Grifo Monocontrol", "Desensamble y corrección de fuga por empaque dañado en grifería de cocina de lujo.", "https://images.unsplash.com/photo-1504148455328-c376907d081c?w=400"),
            PortfolioItem("Instalación Calentador a Gas", "Montaje seguro de calentador de paso de 5.5 litros con ducto de evacuación homologado.", "https://images.unsplash.com/photo-1621905252507-b354bc25edac?w=400")
        ),
        "bot_gerson_martnez" to listOf(
            PortfolioItem("Tableros Eléctricos Bifásicos", "Actualización completa de tacos de energía e interruptores de sobrecarga residencial.", "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=400"),
            PortfolioItem("Certificación Retie 2026", "Acreditación de normas de seguridad colombianas para instalaciones eléctricas.", "https://images.unsplash.com/photo-1510519138101-570d1dca3d66?w=400")
        ),
        "bot_gerson_martínez" to listOf(
            PortfolioItem("Tableros Eléctricos Bifásicos", "Actualización completa de tacos de energía e interruptores de sobrecarga residencial.", "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?w=400"),
            PortfolioItem("Certificación Retie 2026", "Acreditación de normas de seguridad colombianas para instalaciones eléctricas.", "https://images.unsplash.com/photo-1510519138101-570d1dca3d66?w=400")
        )
    )

    fun getPortfolio(proId: String): List<PortfolioItem> {
        val cleanProId = proId.trim().lowercase()
        return portfolios[cleanProId] ?: portfolios[proId] ?: listOf(
            PortfolioItem("Trabajo General Galibu", "Instalaciones, reparaciones y asesorías inmediatas de calidad.", "https://images.unsplash.com/photo-1621905252507-b354bc25edac?w=400")
        )
    }

    private val _firestoreDepartments = MutableStateFlow<List<FirebaseDepartment>>(emptyList())
    val firestoreDepartments: StateFlow<List<FirebaseDepartment>> = _firestoreDepartments.asStateFlow()

    private val _firestoreCategories = MutableStateFlow<List<FirebaseCategory>>(emptyList())
    val firestoreCategories: StateFlow<List<FirebaseCategory>> = _firestoreCategories.asStateFlow()

    init {
        viewModelScope.launch {
            var firstCollect = true
            repository.getNotificationsFlow().collect { list ->
                if (firstCollect) {
                    firstCollect = false
                    return@collect
                }
                if (list.isNotEmpty()) {
                    val latest = list.maxByOrNull { it.timestamp }
                    if (latest != null && System.currentTimeMillis() - latest.timestamp < 15000) {
                        activeBannerNotification.value = latest
                    }
                }
            }
        }

        viewModelScope.launch {
            currentUser.collect { user ->
                if (user != null) {
                    if (user.role == "CLIENT") {
                        _activeRole.value = "CLIENT"
                    } else if (user.role == "PROFESSIONAL") {
                        _activeRole.value = "PROFESSIONAL"
                    } else if (user.role == "BOTH" && _activeRole.value == null) {
                        _activeRole.value = "CLIENT"
                    }
                } else {
                    _activeRole.value = null
                }
            }
        }

        viewModelScope.launch {
            try {
                if (repository.isFirebaseInitialized()) {
                    _firestoreDepartments.value = repository.getDepartments()
                    _firestoreCategories.value = repository.getCategories()
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error loading dynamic firestore config: ${e.localizedMessage}")
            }
        }
    }

    fun addCategory(name: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.addCategory(name)
            if (success) {
                try {
                    _firestoreCategories.value = repository.getCategories()
                } catch (e: Exception) {
                    Log.e("SharedViewModel", "Error reloading categories: ${e.localizedMessage}")
                }
            }
            onResult(success)
        }
    }

    val allJobs: StateFlow<List<JobRequest>> = repository.getAllJobs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val professionals: StateFlow<List<UserProfile>> = repository.getAllProfessionals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val openJobs: StateFlow<List<JobRequest>> = repository.getOpenJobs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val jobIdsWithBids: StateFlow<List<String>> = currentUser
        .flatMapLatest { user ->
            if (user != null && (user.role == "PROFESSIONAL" || user.role == "BOTH")) {
                repository.getJobIdsWithBids(user.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val proBids: StateFlow<List<Bid>> = currentUser
        .flatMapLatest { user ->
            if (user != null && (user.role == "PROFESSIONAL" || user.role == "BOTH")) {
                repository.getBidsByProfessional(user.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<AppNotification>> = repository.getNotificationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedJobId = MutableStateFlow<String?>(null)
    val selectedJobId = _selectedJobId.asStateFlow()

    private val _typingStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val typingStatus = _typingStatus.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val isPartnerTyping: StateFlow<Boolean> = typingStatus
        .map { map ->
            val currentUserId = currentUser.value?.id ?: ""
            map.filterKeys { it != currentUserId }.values.any { it }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    protected var activeBidsListener: com.google.firebase.firestore.ListenerRegistration? = null
    protected var activeChatsListener: com.google.firebase.firestore.ListenerRegistration? = null

    override fun onCleared() {
        super.onCleared()
        activeBidsListener?.remove()
        activeChatsListener?.remove()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedJob: StateFlow<JobRequest?> = _selectedJobId
        .flatMapLatest { id ->
            if (id != null) repository.getJobById(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedJobBids: StateFlow<List<Bid>> = _selectedJobId
        .flatMapLatest { id ->
            if (id != null) repository.getBidsForJob(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedJobMessages: StateFlow<List<ChatMessage>> = _selectedJobId
        .flatMapLatest { id ->
            if (id != null) repository.getChatMessagesForJob(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getUserByEmail(email: String): UserProfile? {
        return repository.getUserByEmail(email)
    }

    suspend fun authenticateGoogleCredential(idToken: String): String? {
        return firebaseService.authenticateGoogleCredential(idToken)
    }

    // --- Authentication Actions ---

    fun loginWithGoogle(
        idToken: String? = null,
        email: String,
        name: String,
        avatarUrl: String?,
        role: String,
        customPhone: String? = null,
        customDepartment: String? = null,
        customMunicipality: String? = null,
        customAddress: String? = null,
        customCategory: String? = null,
        workPhotos: List<String> = emptyList(),
        customHourlyRate: Double? = null,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                showLoading("Iniciando sesión segura con Google...")
                var firebaseUid: String? = null
                if (!idToken.isNullOrEmpty()) {
                    try {
                        firebaseUid = firebaseService.authenticateGoogleCredential(idToken)
                    } catch (e: Exception) {
                        Log.e("SharedViewModel", "Falla autenticación con ID Token de Google: ${e.localizedMessage}")
                    }
                }
                val success = repository.loginWithGoogle(
                    firebaseUid = firebaseUid,
                    email = email,
                    name = name,
                    avatarUrl = avatarUrl,
                    role = role,
                    customPhone = customPhone,
                    customDepartment = customDepartment,
                    customMunicipality = customMunicipality,
                    customAddress = customAddress,
                    customCategory = customCategory,
                    customWorkPhotos = workPhotos,
                    customHourlyRate = customHourlyRate,
                    context = getApplication<Application>()
                )
                if (success) {
                    _activeRole.value = role
                }
                onResult(success)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla inicio de sesión con Google: ${e.localizedMessage}")
                onResult(false)
            } finally {
                hideLoading()
            }
        }
    }

    fun login(email: String, role: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Iniciando sesión segura...")
                kotlinx.coroutines.delay(1000)
                val errorMsg = repository.login(email, "", role)
                val success = (errorMsg == null)
                if (success) {
                    _activeRole.value = role
                }
                onResult(success)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla inicio de sesión: ${e.localizedMessage}")
                onResult(false)
                val warning = AppNotification(
                    userId = "",
                    title = "Error de Sesión 🔐",
                    body = "No se pudo verificar la sesión de forma remota. Falló comunicación con Firebase Auth/Firestore."
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun login(email: String, password: String, role: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Verificando credenciales en Firebase...")
                kotlinx.coroutines.delay(1000)
                val errorMsg = repository.login(email, password, role)
                if (errorMsg == null) {
                    _activeRole.value = role
                }
                onResult(errorMsg)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla inicio de sesión con contraseña: ${e.localizedMessage}")
                onResult(e.localizedMessage ?: "Error desconocido durante el inicio de sesión.")
                val warning = AppNotification(
                    userId = "",
                    title = "Error de Sesión 🔐",
                    body = "No se pudo conectar a Firebase Auth: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun sendPasswordResetEmail(email: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Enviando correo de recuperación...")
                kotlinx.coroutines.delay(1000)
                if (repository.isFirebaseInitialized()) {
                    repository.sendPasswordResetEmail(email)
                    onResult(null)
                } else {
                    onResult(null)
                    val notification = AppNotification(
                        userId = "",
                        title = "Correo de Recuperación (Simulado) ✉️",
                        body = "Se ha enviado un enlace para restablecer la contraseña de $email satisfactoriamente."
                    )
                    database.appNotificationDao().insertNotification(notification)
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla al enviar correo de recuperación: ${e.localizedMessage}")
                onResult(e.localizedMessage ?: "No se pudo enviar el correo de recuperación")
            } finally {
                hideLoading()
            }
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String = "",
        role: String,
        category: String?,
        department: String? = "Bogotá D.C.",
        municipality: String? = "Bogotá",
        profilePhotoUrl: String? = null,
        address: String? = "",
        workPhotos: List<String> = emptyList(),
        hourlyRate: Double? = null,
        acceptedTerms: Boolean = false,
        googleIdToken: String? = null,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            showLoading("Creando nueva cuenta de usuario...")
            kotlinx.coroutines.delay(1200)
            try {
                val existing = repository.getUserByEmail(email.trim().lowercase())
                if (existing != null) {
                    if ((existing.role == "CLIENT" && role == "PROFESSIONAL") || 
                        (existing.role == "PROFESSIONAL" && role == "CLIENT")) {
                        var workPhotosCsv: String? = existing.workPhotos
                        if (workPhotos.isNotEmpty()) {
                            val context = getApplication<Application>()
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
                                            val url = firebaseService.uploadWorkPhoto(existing.id, "upg_work_$index", bytes)
                                            uploadedWorkPhotos.add(url)
                                        }
                                    } catch (e: Exception) {
                                        Log.e("SharedViewModel", "Error uploading upgrade work photo $index: ${e.localizedMessage}")
                                    }
                                } else {
                                    uploadedWorkPhotos.add(uriStr)
                                }
                            }
                            if (uploadedWorkPhotos.isNotEmpty()) {
                                workPhotosCsv = uploadedWorkPhotos.joinToString("|")
                            }
                        }

                        val upgraded = existing.copy(
                            role = "BOTH",
                            password = if (password.isNotBlank()) password else existing.password,
                            workCategory = category ?: existing.workCategory,
                            department = department ?: existing.department,
                            municipality = municipality ?: existing.municipality,
                            profilePhotoUrl = profilePhotoUrl ?: existing.profilePhotoUrl,
                            phone = if (phone.isNotBlank() && phone != "+57 300 000 0000") phone else existing.phone,
                            address = if (!address.isNullOrBlank()) address else existing.address,
                            workPhotos = workPhotosCsv,
                            hourlyRate = hourlyRate ?: existing.hourlyRate,
                            acceptedTerms = acceptedTerms
                        )
                        repository.updateUserDirectly(upgraded)
                        onResult(null)
                        return@launch
                    } else {
                        onResult("Este correo ya está registrado. Por favor, inicia sesión.")
                        return@launch
                    }
                }
                
                repository.registerUser(
                    getApplication<Application>(), 
                    name, email, phone, password, role, category, 
                    department, municipality, profilePhotoUrl, address,
                    workPhotos, hourlyRate, acceptedTerms, googleIdToken
                )
                onResult(null)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla de registro: ${e.localizedMessage}")
                val rawMsg = e.localizedMessage ?: ""
                val friendlyMessage = if (rawMsg.contains("already in use", ignoreCase = true) || 
                                        rawMsg.contains("already exists", ignoreCase = true)) {
                    "Este correo ya está registrado. Por favor, inicia sesión."
                } else {
                    rawMsg.ifBlank { "Error de comunicación con Firebase" }
                }
                onResult(friendlyMessage)
            } finally {
                hideLoading()
            }
        }
    }

    fun updateProfile(name: String, phone: String, workCategory: String?, profilePhotoUrl: String?, department: String? = "Bogotá D.C.", municipality: String? = "Bogotá", address: String? = null, hourlyRate: Double? = null) {
        viewModelScope.launch {
            try {
                showLoading("Actualizando información de tu perfil...")
                kotlinx.coroutines.delay(1000)
                
                var finalPhotoUrl = profilePhotoUrl
                val currentUserId = currentUser.value?.id ?: "unknown"
                if (!profilePhotoUrl.isNullOrEmpty() && !profilePhotoUrl.startsWith("http") && !profilePhotoUrl.startsWith("data:image")) {
                    try {
                        val bytes = if (profilePhotoUrl.startsWith("content://") || profilePhotoUrl.startsWith("file://")) {
                            val uri = profilePhotoUrl.toUri()
                            getApplication<Application>().contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        } else {
                            val file = java.io.File(profilePhotoUrl)
                            if (file.exists()) file.readBytes() else null
                        }
                        if (bytes != null) {
                            val uploadedUrl = firebaseService.uploadProfilePhoto(currentUserId, bytes)
                            finalPhotoUrl = uploadedUrl
                        }
                    } catch (e: Exception) {
                        Log.e("SharedViewModel", "Error reading/uploading chosen image: ${e.localizedMessage}")
                    }
                }
                
                repository.updateProfile(name, phone, workCategory, finalPhotoUrl, department, municipality, address, hourlyRate)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla actualización de perfil: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Perfil Actualizado Localmente ⏳",
                    body = "Tu perfil se guardó de forma local en el dispositivo. Falló sincronización con Firebase Firestore: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun toggleUserRole() {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val targetRole = if (user.role == "CLIENT") {
                "PROFESSIONAL"
            } else if (user.role == "PROFESSIONAL") {
                "CLIENT"
            } else {
                if (_activeRole.value == "CLIENT") "PROFESSIONAL" else "CLIENT"
            }
            if (user.role == "BOTH") {
                _activeRole.value = targetRole
                showLoading("Cambiando vista de rol...")
                kotlinx.coroutines.delay(800)
                hideLoading()
                return@launch
            }
            val updatedProfile = user.copy(role = targetRole)
            showLoading("Cambiando vista de rol...")
            kotlinx.coroutines.delay(800)
            repository.updateUserDirectly(updatedProfile)
            _activeRole.value = targetRole
            hideLoading()
        }
    }

    fun updateUserDirectly(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateUserDirectly(profile)
        }
    }

    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                showLoading("Actualizando contraseña segura...")
                kotlinx.coroutines.delay(1000)
                repository.changePassword(newPassword)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla cambio de contraseña: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Contraseña guardada localmente ⏳",
                    body = "Contraseña cambiada localmente. Falló sincronización remota: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                showLoading("Eliminando tu cuenta de forma segura...")
                kotlinx.coroutines.delay(1500)
                repository.deleteAccount()
                _selectedJobId.value = null
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla eliminación de cuenta: ${e.localizedMessage}")
            } finally {
                hideLoading()
            }
        }
    }

    fun cancelJobRequest(jobId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Cancelando solicitud...")
                repository.cancelJobRequest(jobId)
                onResult(true)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla cancelando solicitud: ${e.localizedMessage}")
                onResult(false)
            } finally {
                hideLoading()
            }
        }
    }

    fun rechargeBalance(amount: Double) {
        viewModelScope.launch {
            try {
                showLoading("Procesando recarga segura PSE/Tarjeta...")
                kotlinx.coroutines.delay(1200)
                repository.rechargeBalance(amount)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla recarga: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Recarga de saldo guardada localmente 💰",
                    body = "Se recargaron $$amount COP de forma offline en este dispositivo. Falló sincronización en la nube: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                showLoading("Cerrando tu sesión de forma segura...")
                kotlinx.coroutines.delay(1000)
                repository.logout()
                _selectedJobId.value = null
                _activeRole.value = null
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla al cerrar sesión: ${e.localizedMessage}")
            } finally {
                hideLoading()
            }
        }
    }

    // --- Job Selection ---

    fun selectJob(jobId: String?) {
        activeBidsListener?.remove()
        activeBidsListener = null
        activeChatsListener?.remove()
        activeChatsListener = null

        _selectedJobId.value = jobId

        if (jobId != null) {
            activeBidsListener = repository.syncBidsForJob(jobId)
            activeChatsListener = repository.syncChatsForJob(jobId)
        }
    }

    // --- Chat ---

    val messages: StateFlow<List<ChatMessage>> = selectedJobMessages

    private var typingJob: kotlinx.coroutines.Job? = null

    fun startListening(jobId: String) {
        selectJob(jobId)
        typingJob?.cancel()
        typingJob = viewModelScope.launch {
            repository.listenTypingStatus(jobId).collect {
                _typingStatus.value = it
            }
        }
        viewModelScope.launch {
            repository.markAsRead(jobId)
        }
    }

    fun stopListening() {
        val jobId = _selectedJobId.value
        if (jobId != null) {
            viewModelScope.launch {
                repository.setTypingStatus(jobId, false)
            }
        }
        typingJob?.cancel()
        typingJob = null
        _typingStatus.value = emptyMap()
        selectJob(null)
    }

    fun setTyping(typing: Boolean) {
        val jobId = _selectedJobId.value ?: return
        viewModelScope.launch {
            repository.setTypingStatus(jobId, typing)
        }
    }

    fun sendChatMessage(text: String) {
        sendMessage(text)
    }

    fun sendMessage(text: String) {
        val jobId = _selectedJobId.value ?: return
        if (text.trim().isEmpty()) return
        viewModelScope.launch {
            try {
                repository.sendChatMessage(jobId, text)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla enviar mensaje: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Mensaje guardado localmente 💬",
                    body = "Tu mensaje se envió localmente. Falló sincronización con Firestore: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            }
        }
    }

    fun sendMessage(message: ChatMessage) {
        val jobId = _selectedJobId.value ?: return
        viewModelScope.launch {
            try {
                repository.sendMessage(jobId, message)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla enviar mensaje: ${e.localizedMessage}")
            }
        }
    }

    fun sendImageMessage(imageBytes: ByteArray) {
        val jobId = _selectedJobId.value ?: return
        viewModelScope.launch {
            try {
                showLoading("Enviando imagen...")
                repository.sendImageMessage(jobId, imageBytes)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla enviar imagen: ${e.localizedMessage}")
            } finally {
                hideLoading()
            }
        }
    }

    // --- Payments ---

    fun payAndCompleteJob(jobId: String, paymentMethod: String, onResult: () -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Iniciando cierre y calificaciones...")
                kotlinx.coroutines.delay(1000)
                repository.startJobCompletion(jobId)
                onResult()
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla iniciar cierre servicio: ${e.localizedMessage}")
                onResult()
            } finally {
                hideLoading()
            }
        }
    }

    fun submitRating(jobId: String, rating: Float, review: String, isClientRating: Boolean) {
        viewModelScope.launch {
            try {
                repository.submitServiceRating(jobId, rating, review, isClientRating)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla calificar servicio: ${e.localizedMessage}")
            }
        }
    }

    // --- Notifications ---

    fun markNotificationAsRead(id: Int) {
        viewModelScope.launch {
            try {
                repository.markNotificationAsRead(id)
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Falla marcar notificación: ${e.localizedMessage}")
            }
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            try {
                val currentNotifications = notifications.value
                currentNotifications.forEach { notif ->
                    if (!notif.isRead) {
                        repository.markNotificationAsRead(notif.id)
                    }
                }
            } catch (e: Exception) {
                Log.e("SharedViewModel", "Error marking all notifications read: ${e.localizedMessage}")
            }
        }
    }

    // --- Firebase Sync Controls ---

    val isCloudSyncEnabled: StateFlow<Boolean> = firebaseService.isCloudSyncEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun toggleCloudSync(enabled: Boolean) {
        firebaseService.toggleCloudSync(enabled)
    }

    fun triggerSimulatedFcmPush(title: String, body: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val notification = AppNotification(
                userId = user.id,
                title = title,
                body = body,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            database.appNotificationDao().insertNotification(notification)
        }
    }

    fun rechargeBalanceManually(amount: Double) {
        viewModelScope.launch {
            try {
                showLoading("Procesando recarga...")
                kotlinx.coroutines.delay(1000)
                repository.rechargeBalance(amount)
                hideLoading()
            } catch (e: Exception) {
                hideLoading()
                Log.e("SharedViewModel", "Error en recarga: ${e.localizedMessage}")
            }
        }
    }
}

