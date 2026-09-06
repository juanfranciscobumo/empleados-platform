package com.galibu.client.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.galibu.client.R
import com.galibu.core.data.model.AppNotification
import com.galibu.core.data.model.Bid
import com.galibu.core.data.model.ChatMessage
import com.galibu.core.data.model.JobRequest
import com.galibu.core.data.model.UserProfile
import com.galibu.core.ui.theme.PolishBg
import com.galibu.core.ui.theme.PolishBlue
import com.galibu.core.ui.theme.PolishBorder
import com.galibu.core.ui.theme.PolishCard
import com.galibu.core.ui.theme.PolishDark
import com.galibu.core.ui.theme.PolishGreen
import com.galibu.core.ui.theme.PolishGrayText
import com.galibu.core.ui.theme.PolishIce
import com.galibu.core.ui.theme.PolishNavy
import com.galibu.core.ui.theme.StatusAccepted
import com.galibu.core.ui.theme.StatusCancelled
import com.galibu.core.ui.theme.StatusCompleted
import com.galibu.core.ui.theme.StatusNegotiating
import com.galibu.core.ui.theme.StatusOpen
import com.galibu.client.ui.viewmodels.ClientViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class ProfessionalModel(
    val id: String,
    val name: String,
    val ratePerHour: String,
    val tags: List<String>,
    val description: String,
    val photoUrl: String?,
    val rating: Float,
    val serviceCount: Int,
    val location: String,
    val isOnline: Boolean = false,
    val verified: Boolean = true,
    val workCategory: String? = null,
    val municipality: String? = null,
    val department: String? = null,
    val address: String? = null,
    val hourlyRate: Double? = null,
    val memberSince: String = ""
)

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}

private fun getGreeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when {
        hour in 0..11 -> "Buenos días"
        hour in 12..17 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}

fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Ahora"
        diff < TimeUnit.HOURS.toMillis(1) -> "${diff / TimeUnit.MINUTES.toMillis(1)} min"
        diff < TimeUnit.DAYS.toMillis(1) -> "${diff / TimeUnit.HOURS.toMillis(1)}h"
        diff < TimeUnit.DAYS.toMillis(7) -> "${diff / TimeUnit.DAYS.toMillis(1)}d"
        else -> {
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}

private fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "plomería", "plomeria" -> Color(0xFF2563EB)    // Blue-600
        "electricidad" -> Color(0xFF059669)              // Emerald-600
        "carpintería", "carpinteria" -> Color(0xFFD97706) // Amber-600
        "pintura" -> Color(0xFFDB2777)                   // Pink-600
        "albañilería", "albanileria" -> Color(0xFF4F46E5) // Indigo-600
        "jardinería", "jardineria" -> Color(0xFF16A34A)  // Green-600
        "cerrajería", "cerrajeria" -> Color(0xFF64748B)  // Slate-500
        "fumigación", "fumigacion" -> Color(0xFF7C3AED)  // Violet-600
        "mecánica", "mecanica" -> Color(0xFFDC2626)      // Red-600
        "limpieza" -> Color(0xFF0891B2)                  // Cyan-600
        "tecnología", "tecnologia" -> Color(0xFF2563EB)  // Blue-600
        else -> PolishBlue
    }
}

private fun getCategoryIcon(category: String): String {
    return when (category.lowercase()) {
        "plomería", "plomeria" -> "🔧"
        "electricidad" -> "⚡"
        "carpintería", "carpinteria" -> "🪚"
        "pintura" -> "🎨"
        "albañilería", "albanileria" -> "🧱"
        "jardinería", "jardineria" -> "🌿"
        "cerrajería", "cerrajeria" -> "🔑"
        "fumigación", "fumigacion" -> "🐛"
        "mecánica", "mecanica" -> "🔩"
        "limpieza" -> "🧹"
        "tecnología", "tecnologia" -> "💻"
        else -> "📋"
    }
}

private fun getCategoryEmoji(category: String): String {
    return when (category.lowercase()) {
        "plomería", "plomeria" -> "\uD83D\uDCA7"
        "electricidad" -> "⚡"
        "carpintería", "carpinteria" -> "\uD83D\uDD28"
        "pintura" -> "\uD83C\uDFA8"
        "albañilería", "albanileria" -> "\uD83E\uDDF1"
        "jardinería", "jardineria" -> "\uD83C\uDF3F"
        "cerrajería", "cerrajeria" -> "\uD83D\uDD11"
        "fumigación", "fumigacion" -> "\uD83D\uDD12"
        "mecánica", "mecanica" -> "\uD83D\uDD29"
        "limpieza" -> "\uD83E\uDDF9"
        "tecnología", "tecnologia" -> "\uD83D\uDCBB"
        else -> "\uD83D\uDCCB"
    }
}

private fun getStatusColor(status: String): Color {
    return when (status.uppercase()) {
        "OPEN", "ABIERTO" -> StatusOpen
        "NEGOTIATING", "NEGOCIANDO" -> StatusNegotiating
        "ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO" -> StatusAccepted
        "COMPLETED", "COMPLETADO" -> StatusCompleted
        "CANCELLED", "CANCELADO" -> StatusCancelled
        "PENDING_RATING", "PENDIENTE_CALIFICACION" -> Color(0xFFFF9800)
        "PENDING", "PENDIENTE" -> Color(0xFFFF9800)
        else -> PolishGrayText
    }
}

private fun getStatusText(status: String): String {
    return when (status.uppercase()) {
        "OPEN", "ABIERTO" -> "Abierto"
        "NEGOTIATING", "NEGOCIANDO" -> "Negociando"
        "ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO" -> "Trabajo en curso"
        "COMPLETED", "COMPLETADO" -> "Completado"
        "CANCELLED", "CANCELADO" -> "Cancelado"
        "PENDING_RATING", "PENDIENTE_CALIFICACION" -> "Por calificar"
        "PENDING", "PENDIENTE" -> "Pendiente"
        else -> status
    }
}

private fun formatCurrency(amount: Double): String {
    val formatter = java.text.NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount)
}

private fun getRatingDistribution(reviews: List<Pair<Float, String>>): Map<Int, Int> {
    val distribution = mutableMapOf<Int, Int>()
    for (i in 1..5) {
        distribution[i] = reviews.count { it.first.toInt() == i }
    }
    return distribution
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ClientDashboardScreen(
    viewModel: ClientViewModel,
    onNavigateToJobBids: (String) -> Unit,
    onNavigateToChat: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser by viewModel.currentUser.collectAsState()
    val professionals by viewModel.professionals.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val openJobs by viewModel.openJobs.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val firestoreCatList by viewModel.firestoreCategories.collectAsState()
    val firestoreDeptList by viewModel.firestoreDepartments.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showProfessionalDetail by remember { mutableStateOf(false) }
    var selectedProfessional by remember { mutableStateOf<ProfessionalModel?>(null) }
    var showJobPostForm by remember { mutableStateOf(false) }
    var showRatingDialog by remember { mutableStateOf(false) }
    var ratingJobId by remember { mutableStateOf("") }
    var showCancelJobDialog by remember { mutableStateOf(false) }
    var cancelJobId by remember { mutableStateOf("") }
    var showNotificationsPanel by remember { mutableStateOf(false) }
    var showEditProfile by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showRechargeDialog by remember { mutableStateOf(false) }
    var isCloudSyncEnabled by remember { mutableStateOf(false) }
    var showSettingsMenu by remember { mutableStateOf(false) }

    val categories = remember(firestoreCatList) {
        if (firestoreCatList.isNotEmpty()) {
            firestoreCatList.map { it.name }
        } else {
            listOf(
                "Plomería", "Electricidad", "Carpintería", "Pintura",
                "Albañilería", "Jardinería", "Cerrajería", "Fumigación"
            )
        }
    }

    val departmentsAndMunicipalities = remember(firestoreDeptList) {
        if (firestoreDeptList.isNotEmpty()) {
            firestoreDeptList.map { it.name to it.municipalities }
        } else {
            listOf(
                "Antioquia" to listOf("Medellín", "Envigado", "Bello", "Rionegro"),
                "Bogotá D.C." to listOf("Bogotá"),
                "Valle del Cauca" to listOf("Cali", "Palmira", "Yumbo", "Jamundí"),
                "Atlántico" to listOf("Barranquilla", "Soledad", "Puerto Colombia"),
                "Santander" to listOf("Bucaramanga", "Floridablanca", "Girón")
            )
        }
    }


    val professionalModels = remember(professionals) {
        professionals.filter { it.role == "PROFESSIONAL" || it.role == "professional" }.map { profile ->
            ProfessionalModel(
                id = profile.id,
                name = profile.name,
                ratePerHour = profile.hourlyRate?.let { formatCurrency(it) } ?: "A convenir",
                tags = listOfNotNull(
                    profile.workCategory,
                    profile.department
                ),
                description = "Especialista en ${profile.workCategory ?: "servicios generales"}",
                photoUrl = profile.profilePhotoUrl,
                rating = profile.rating?.toFloat() ?: 0f,
                serviceCount = profile.completedJobs ?: 0,
                location = "${profile.municipality ?: ""}, ${profile.department ?: ""}".trim().trimEnd(','),
                isOnline = false,
                verified = profile.isVerified ?: false,
                workCategory = profile.workCategory,
                municipality = profile.municipality,
                department = profile.department,
                address = profile.address,
                hourlyRate = profile.hourlyRate,
                memberSince = ""
            )
        }
    }

    val filteredProfessionals = remember(professionalModels, selectedCategory, searchQuery) {
        professionalModels.filter { pro ->
            val matchesCategory = selectedCategory == null || pro.workCategory?.lowercase() == selectedCategory?.lowercase()
            val matchesSearch = searchQuery.isEmpty() || pro.name.contains(searchQuery, ignoreCase = true) ||
                    pro.tags.any { it.contains(searchQuery, ignoreCase = true) } ||
                    pro.location.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val clientJobs = remember(allJobs, currentUser) {
        allJobs.filter { it.clientId == currentUser?.id }
    }

    val acceptedJobs = clientJobs.filter {
        it.status.uppercase() in listOf("ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO")
    }

    val pendingRatingJobs = clientJobs.filter {
        it.status.uppercase() in listOf("PENDING_RATING", "PENDIENTE_CALIFICACION")
    }

    Scaffold(
        topBar = { },
        bottomBar = {
            NavigationBar(selectedTab) { selectedTab = it }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = PolishBg
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> InicioTab(
                    currentUser = currentUser,
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    clientJobs = clientJobs,
                    onJobClick = { job ->
                        viewModel.selectJob(job.id)
                        onNavigateToJobBids(job.id)
                    },
                    onNotificationsClick = { onNavigateToNotifications() },
                    unreadNotifications = notifications.count { !it.isRead },
                    showSettingsMenu = showSettingsMenu,
                    onSettingsMenuDismiss = { showSettingsMenu = false },
                    onSettingsClick = { showSettingsMenu = true },
                    isCloudSyncEnabled = isCloudSyncEnabled,
                    onSyncToggle = {
                        showSettingsMenu = false
                        isCloudSyncEnabled = !isCloudSyncEnabled
                    },
                    onChangePassword = {
                        showSettingsMenu = false
                        showChangePassword = true
                    },
                    onDeleteAccount = {
                        showSettingsMenu = false
                        showDeleteAccountDialog = true
                    },
                    onLogout = {
                        showSettingsMenu = false
                        viewModel.logout()
                    },
                    onPostJob = { showJobPostForm = true }
                )
                1 -> SolicitudesTab(
                    currentUser = currentUser,
                    clientJobs = clientJobs,
                    onJobClick = { job ->
                        viewModel.selectJob(job.id)
                        onNavigateToJobBids(job.id)
                    },
                    onPostJob = { showJobPostForm = true },
                    onNotificationsClick = { onNavigateToNotifications() }
                )
                2 -> ProfesionalesTab(
                    professionals = filteredProfessionals,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    categories = categories,
                    onProfessionalClick = { pro ->
                        selectedProfessional = pro
                        showProfessionalDetail = true
                    }
                )
                3 -> BilleteraTab(
                    currentUser = currentUser,
                    onRecharge = { showRechargeDialog = true }
                )
                4 -> PerfilTab(
                    currentUser = currentUser,
                    onEditProfile = { showEditProfile = true },
                    onChangePassword = { showChangePassword = true },
                    onDeleteAccount = { showDeleteAccountDialog = true },
                    onLogout = { viewModel.logout() },
                    onRecharge = { showRechargeDialog = true }
                )
            }
        }
    }

    if (showProfessionalDetail && selectedProfessional != null) {
        ProfessionalDetailSheet(
            professional = selectedProfessional!!,
            onDismiss = { showProfessionalDetail = false },
            onHire = { pro ->
                viewModel.createJobRequest(
                    category = pro.workCategory ?: "General",
                    title = "Servicio de ${pro.workCategory ?: "General"}",
                    description = "Solicitud de servicio para ${pro.workCategory ?: "servicios generales"}",
                    budget = pro.hourlyRate ?: 50000.0,
                    address = currentUser?.address ?: "",
                    department = currentUser?.department ?: "",
                    municipality = currentUser?.municipality ?: "",
                    onSuccess = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Solicitud creada exitosamente")
                        }
                        showProfessionalDetail = false
                    }
                )
            },
            onChat = { pro ->
                viewModel.startChatWithProfessional(
                    proId = pro.id,
                    proName = pro.name,
                    proSpecialty = pro.workCategory ?: "",
                    onStarted = { chatId ->
                        onNavigateToChat(chatId)
                    }
                )
                showProfessionalDetail = false
            }
        )
    }

    if (showJobPostForm) {
        JobPostForm(
            currentUser = currentUser,
            categories = categories,
            departmentsAndMunicipalities = departmentsAndMunicipalities,
            onDismiss = { showJobPostForm = false },
            onSubmit = { category, title, description, budget, address, department, municipality ->
                viewModel.createJobRequest(
                    category = category,
                    title = title,
                    description = description,
                    budget = budget,
                    address = address,
                    department = department,
                    municipality = municipality,
                    onSuccess = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Solicitud creada exitosamente")
                        }
                        showJobPostForm = false
                    }
                )
            }
        )
    }

    if (showRatingDialog && ratingJobId.isNotEmpty()) {
        RatingDialog(
            jobId = ratingJobId,
            onDismiss = {
                showRatingDialog = false
                ratingJobId = ""
            },
            onSubmit = { rating, review ->
                viewModel.submitRating(
                    jobId = ratingJobId,
                    rating = rating.toFloat(),
                    review = review,
                    isClientRating = true
                )
                showRatingDialog = false
                ratingJobId = ""
                scope.launch {
                    snackbarHostState.showSnackbar("Calificación publicada")
                }
            }
        )
    }

    if (showCancelJobDialog && cancelJobId.isNotEmpty()) {
        CancelJobDialog(
            onDismiss = {
                showCancelJobDialog = false
                cancelJobId = ""
            },
            onConfirm = { reason ->
                viewModel.cancelJob(
                    jobId = cancelJobId,
                    reason = reason,
                    onComplete = {
                        showCancelJobDialog = false
                        cancelJobId = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Trabajo cancelado")
                        }
                    }
                )
            }
        )
    }

    if (showEditProfile) {
        EditProfileDialog(
            currentUser = currentUser,
            onDismiss = { showEditProfile = false },
            onSave = { name, phone, workCategory, department, municipality, address, hourlyRate ->
                viewModel.updateProfile(
                    name = name,
                    phone = phone,
                    workCategory = workCategory,
                    profilePhotoUrl = null,
                    department = department,
                    municipality = municipality,
                    address = address,
                    hourlyRate = hourlyRate
                )
                showEditProfile = false
                scope.launch {
                    snackbarHostState.showSnackbar("Perfil actualizado")
                }
            }
        )
    }

    if (showChangePassword) {
        ChangePasswordDialog(
            onDismiss = { showChangePassword = false },
            onSubmit = { newPassword ->
                viewModel.changePassword(newPassword)
                showChangePassword = false
                scope.launch {
                    snackbarHostState.showSnackbar("Contraseña actualizada")
                }
            }
        )
    }

    if (showDeleteAccountDialog) {
        DeleteAccountDialog(
            onDismiss = { showDeleteAccountDialog = false },
            onConfirm = {
                viewModel.deleteAccount()
                showDeleteAccountDialog = false
            }
        )
    }

    if (showRechargeDialog) {
        RechargeDialog(
            onDismiss = { showRechargeDialog = false },
            onSubmit = { amount ->
                viewModel.rechargeBalance(amount)
                showRechargeDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Recarga exitosa")
                }
            }
        )
    }
}

@Composable
fun NavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        Triple("Inicio", Icons.Default.Home, "home"),
        Triple("Solicitudes", Icons.Default.Description, "requests"),
        Triple("Profesionales", Icons.Default.Store, "professionals"),
        Triple("Billetera", Icons.Default.Payment, "wallet"),
        Triple("Perfil", Icons.Default.Person, "profile")
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, PolishBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, (title, icon, _) ->
                val isSelected = selectedTab == index
                val color = if (isSelected) PolishBlue else PolishGrayText
                val weight = if (isSelected) FontWeight.Bold else FontWeight.Medium

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp, 3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isSelected) PolishBlue else Color.Transparent)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        color = color,
                        fontSize = 11.sp,
                        fontWeight = weight
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SolicitudesTab(
    currentUser: UserProfile?,
    clientJobs: List<JobRequest>,
    onJobClick: (JobRequest) -> Unit,
    onPostJob: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val greeting = remember { getGreeting() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = greeting,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Text(
                    text = currentUser?.name ?: "Usuario",
                    fontSize = 18.sp,
                    color = PolishGrayText
                )
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = PolishBlue,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis solicitudes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Text(
                text = "${clientJobs.size} total",
                fontSize = 14.sp,
                color = PolishGrayText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (clientJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes solicitudes aún",
                        color = PolishDark,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Publica tu primer trabajo y recibe cotizaciones de profesionales",
                        color = PolishGrayText,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onPostJob,
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publicar trabajo")
                    }
                }
            }
        } else {
            clientJobs.forEach { job ->
                JobRequestCard(
                    job = job,
                    onClick = { onJobClick(job) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun InicioTab(
    currentUser: UserProfile?,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    clientJobs: List<JobRequest>,
    onJobClick: (JobRequest) -> Unit,
    onNotificationsClick: () -> Unit,
    unreadNotifications: Int = 0,
    showSettingsMenu: Boolean = false,
    onSettingsMenuDismiss: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    isCloudSyncEnabled: Boolean = true,
    onSyncToggle: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPostJob: () -> Unit = {}
) {
    val greeting = remember { getGreeting() }
    val firstName = currentUser?.name?.split(" ")?.firstOrNull() ?: "Usuario"

    val totalJobs = clientJobs.size
    val inProgressJobs = clientJobs.count {
        it.status.uppercase() in listOf("ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO")
    }
    val urgentJobs = clientJobs.count {
        it.status.uppercase() in listOf("URGENT", "URGENTE")
    }
    val completedJobs = clientJobs.count {
        it.status.uppercase() in listOf("COMPLETED", "COMPLETADO")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "\u00A1Hola, $firstName! \uD83D\uDC4B",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = PolishGrayText
                    )
                    Text(
                        text = "Solicitudes",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PolishDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestiona las solicitudes de servicio\nde tus clientes.",
                        fontSize = 14.sp,
                        color = PolishGrayText,
                        lineHeight = 20.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF1F5F9))
                                .clickable { onNotificationsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = PolishDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (unreadNotifications > 0) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935))
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (unreadNotifications > 9) "9+" else "$unreadNotifications",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Box {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF16A34A))
                                .clickable { onSettingsClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.AccountBalanceWallet,
                                    contentDescription = "Billetera",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "$0",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = showSettingsMenu,
                            onDismissRequest = onSettingsMenuDismiss
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sincronizar datos") },
                                onClick = onSyncToggle,
                                leadingIcon = {
                                    Icon(
                                        if (isCloudSyncEnabled) Icons.Default.CloudQueue else Icons.Default.CloudOff,
                                        contentDescription = null
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cambiar contrase\u00F1a") },
                                onClick = onChangePassword,
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar cuenta") },
                                onClick = onDeleteAccount,
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE53935))
                                }
                            )
                            HorizontalDivider(color = PolishBorder, thickness = 1.dp)
                            DropdownMenuItem(
                                text = { Text("Cerrar sesi\u00F3n") },
                                onClick = onLogout,
                                leadingIcon = {
                                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFE53935))
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPostJob,
                colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nueva solicitud")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeStatCard(
                    count = totalJobs,
                    label = "Solicitudes\ntotales",
                    icon = Icons.Default.Description,
                    iconBg = Color(0xFFEDE9FE),
                    iconTint = Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    count = inProgressJobs,
                    label = "En progreso",
                    icon = Icons.Default.CheckCircle,
                    iconBg = Color(0xFFDCFCE7),
                    iconTint = Color(0xFF16A34A),
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    count = urgentJobs,
                    label = "Urgente",
                    icon = Icons.Default.AccessTime,
                    iconBg = Color(0xFFFFF7ED),
                    iconTint = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    count = completedJobs,
                    label = "Completadas",
                    icon = Icons.Default.CheckCircleOutline,
                    iconBg = Color(0xFFEFF6FF),
                    iconTint = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis solicitudes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, PolishBorder, RoundedCornerShape(8.dp))
                        .clickable { }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Todas", fontSize = 13.sp, color = PolishDark)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (clientJobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No tienes solicitudes", color = PolishDark, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Publica tu primer trabajo", color = PolishGrayText, fontSize = 14.sp)
                }
            }
        } else {
            clientJobs.forEach { job ->
                HomeJobRequestCard(
                    job = job,
                    onClick = { onJobClick(job) }
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun HomeStatCard(
    count: Int,
    label: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 108.dp)
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$count",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PolishDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = PolishGrayText,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
fun HomeJobRequestCard(
    job: JobRequest,
    onClick: () -> Unit
) {
    val catColor = getCategoryColor(job.category)
    val catIcon = getCategoryIcon(job.category).let { if (it == "\uD83D\uDCCB") "\uD83D\uDCC1" else it }
    val isUrgent = job.status.uppercase() in listOf("URGENT", "URGENTE")
    val statusLabel = if (isUrgent) "Urgente" else "Normal"
    val statusBg = if (isUrgent) Color(0xFFFFF1F2) else Color(0xFFDCFCE7)
    val statusTint = if (isUrgent) Color(0xFFE53935) else Color(0xFF16A34A)
    val timeAgo = formatTimestamp(job.createdAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(catColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = catIcon, fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = statusLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = statusTint
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = PolishGrayText,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Hace $timeAgo", fontSize = 11.sp, color = PolishGrayText)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = job.title.ifEmpty { job.category },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Cliente: ${job.clientName.ifEmpty { "Sin nombre" }}",
                    fontSize = 12.sp,
                    color = PolishGrayText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "4.${(0..9).random()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PolishDark
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = job.address.ifEmpty { "${job.municipality ?: ""}, ${job.department ?: ""}" },
                        fontSize = 11.sp,
                        color = PolishGrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun QuickServiceCard(
    category: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = getCategoryIcon(category)
    val color = getCategoryColor(category)

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, PolishBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = category,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = PolishDark,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ProfessionalRowCard(
    professional: ProfessionalModel,
    onClick: () -> Unit
) {
    val tagColor = getCategoryColor(professional.workCategory ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .border(1.dp, PolishBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    if (professional.photoUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(professional.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = professional.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .border(2.dp, PolishBorder, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PolishIce)
                                .border(2.dp, PolishBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = PolishBlue,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    if (professional.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                                .border(2.dp, Color.White, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = professional.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (professional.verified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Verificado",
                                tint = PolishBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (professional.tags.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            professional.tags.filterNotNull().take(2).forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = tagColor.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = tag,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        color = tagColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = professional.description,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", professional.rating),
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Medium
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(16.dp)
                        .background(Color(0xFFE2E8F0))
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = professional.location.ifEmpty { "N/A" },
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(16.dp)
                        .background(Color(0xFFE2E8F0))
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${professional.serviceCount} servicios",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Composable
fun JobRequestCard(
    job: JobRequest,
    onClick: () -> Unit
) {
    val statusColor = getStatusColor(job.status)
    val statusText = getStatusText(job.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.title ?: job.category,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (job.description != null) {
                Text(
                    text = job.description,
                    fontSize = 14.sp,
                    color = PolishGrayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (job.finalPrice != null || job.budgetMin != null) {
                    Text(
                        text = formatCurrency(job.finalPrice ?: job.budgetMin!!),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                }
                if (job.professionalName != null) {
                    Text(
                        text = "con ${job.professionalName}",
                        fontSize = 13.sp,
                        color = PolishGrayText
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfesionalesTab(
    professionals: List<ProfessionalModel>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit,
    categories: List<String>,
    onProfessionalClick: (ProfessionalModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Profesionales",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Buscar por nombre, especialidad...")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = PolishGrayText
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Limpiar",
                            tint = PolishGrayText
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PolishBlue,
                unfocusedBorderColor = PolishBorder,
                focusedContainerColor = PolishCard,
                unfocusedContainerColor = PolishCard
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Todas") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PolishBlue,
                    selectedLabelColor = Color.White
                )
            )
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category) null else category)
                    },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getCategoryColor(category),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${professionals.size} profesionales encontrados",
            fontSize = 14.sp,
            color = PolishGrayText
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (professionals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No se encontraron profesionales",
                        color = PolishGrayText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Intenta con otra búsqueda o categoría",
                        color = PolishGrayText,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(professionals) { pro ->
                    ProfessionalRowCard(
                        professional = pro,
                        onClick = { onProfessionalClick(pro) }
                    )
                }
            }
        }
    }
}

@Composable
fun BilleteraTab(
    currentUser: UserProfile?,
    onRecharge: () -> Unit
) {
    val balance = currentUser?.balance ?: 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Billetera",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PolishBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Saldo disponible",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatCurrency(balance),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRecharge,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = PolishBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recargar",
                        color = PolishBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Transacciones recientes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Receipt,
                    contentDescription = null,
                    tint = PolishGrayText,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No hay transacciones recientes",
                    color = PolishGrayText,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun PerfilTab(
    currentUser: UserProfile?,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    onLogout: () -> Unit,
    onRecharge: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mi perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PolishCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentUser?.profilePhotoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentUser.profilePhotoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, PolishBlue, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(PolishIce)
                            .border(3.dp, PolishBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = PolishBlue,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentUser?.name ?: "Usuario",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )

                if (currentUser?.isVerified == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = PolishBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Verificado",
                            fontSize = 14.sp,
                            color = PolishBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentUser?.email ?: "",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )

                if (currentUser?.phone != null) {
                    Text(
                        text = currentUser.phone,
                        fontSize = 14.sp,
                        color = PolishGrayText
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentUser?.completedJobs ?: 0}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishBlue
                        )
                        Text(
                            text = "Servicios",
                            fontSize = 12.sp,
                            color = PolishGrayText
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", currentUser?.rating ?: 0f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishBlue
                        )
                        Text(
                            text = "Calificación",
                            fontSize = 12.sp,
                            color = PolishGrayText
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatCurrency(currentUser?.balance ?: 0.0),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishBlue
                        )
                        Text(
                            text = "Saldo",
                            fontSize = 12.sp,
                            color = PolishGrayText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            icon = Icons.Default.Edit,
            title = "Editar perfil",
            onClick = onEditProfile
        )

        ProfileMenuItem(
            icon = Icons.Default.Payment,
            title = "Recargar saldo",
            onClick = onRecharge
        )

        ProfileMenuItem(
            icon = Icons.Default.Badge,
            title = "Cambiar contraseña",
            onClick = onChangePassword
        )

        ProfileMenuItem(
            icon = Icons.Default.Delete,
            title = "Eliminar cuenta",
            onClick = onDeleteAccount,
            tint = Color(0xFFE53935)
        )

        ProfileMenuItem(
            icon = Icons.Default.Logout,
            title = "Cerrar sesión",
            onClick = onLogout,
            tint = Color(0xFFE53935)
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    tint: Color = PolishDark
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                color = tint,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = PolishGrayText,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalDetailSheet(
    professional: ProfessionalModel,
    onDismiss: () -> Unit,
    onHire: (ProfessionalModel) -> Unit,
    onChat: (ProfessionalModel) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PolishCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 140.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PolishBorder)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    if (professional.photoUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(professional.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = professional.name,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(3.dp, PolishBlue, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(PolishIce)
                                .border(3.dp, PolishBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = PolishBlue,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }
                    if (professional.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(PolishGreen)
                                .border(3.dp, PolishCard, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = professional.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    if (professional.verified) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verificado",
                            tint = PolishBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = professional.location.ifEmpty { "Ubicación no disponible" },
                        fontSize = 14.sp,
                        color = PolishGrayText
                    )
                }

                if (professional.memberSince.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Miembro desde ${professional.memberSince}",
                        fontSize = 13.sp,
                        color = PolishGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", professional.rating),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishDark
                        )
                    }
                    Text(
                        text = "Calificación",
                        fontSize = 12.sp,
                        color = PolishGrayText
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${professional.serviceCount}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    Text(
                        text = "Servicios",
                        fontSize = 12.sp,
                        color = PolishGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Tarifas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tarifa base:",
                        fontSize = 14.sp,
                        color = PolishGrayText
                    )
                    Text(
                        text = professional.ratePerHour,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Negociación:",
                        fontSize = 14.sp,
                        color = PolishGrayText
                    )
                    Text(
                        text = "Disponible",
                        fontSize = 14.sp,
                        color = PolishGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Detalle de la solicitud",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (professional.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        professional.tags.filterNotNull().forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = getCategoryColor(professional.workCategory ?: "").copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = tag,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 13.sp,
                                    color = getCategoryColor(professional.workCategory ?: ""),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = professional.description,
                    fontSize = 14.sp,
                    color = PolishGrayText,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Reseñas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(12.dp))

                val sampleReviews = listOf(
                    Pair(5f, "Excelente trabajo, muy profesional y puntual."),
                    Pair(4f, "Buen servicio, quedé satisfecho con el resultado."),
                    Pair(5f, "Muy recomendable, volveré a contratarlo.")
                )

                val distribution = getRatingDistribution(sampleReviews)
                val totalReviews = sampleReviews.size

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", professional.rating),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = PolishDark
                        )
                        Row {
                            repeat(5) { index ->
                                Icon(
                                    if (index < professional.rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = "$totalReviews reseñas",
                            fontSize = 13.sp,
                            color = PolishGrayText
                        )
                    }

                    Column(
                        modifier = Modifier.width(180.dp)
                    ) {
                        for (i in 5 downTo 1) {
                            val count = distribution[i] ?: 0
                            val fraction = if (totalReviews > 0) count.toFloat() / totalReviews else 0f
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$i",
                                    fontSize = 12.sp,
                                    color = PolishGrayText,
                                    modifier = Modifier.width(12.dp)
                                )
                                LinearProgressIndicator(
                                    progress = { fraction },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFFFFC107),
                                    trackColor = PolishBorder
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$count",
                                    fontSize = 12.sp,
                                    color = PolishGrayText,
                                    modifier = Modifier.width(20.dp),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                sampleReviews.forEach { (rating, review) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                Icon(
                                    if (index < rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cliente",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = PolishDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = review,
                            fontSize = 13.sp,
                            color = PolishGrayText
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onHire(professional) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aceptar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { onChat(professional) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PolishBlue)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PolishBlue)
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chat",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AddressFieldWithGps(
    address: String,
    onAddressChange: (String) -> Unit,
    department: String,
    municipality: String,
    placeholder: String = "Dirección del servicio"
) {
    val context = LocalContext.current
    var isFetchingLocation by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            isFetchingLocation = true
            getCurrentGpsLocation(context, department, municipality) { result ->
                onAddressChange(result)
                isFetchingLocation = false
            }
        }
    }

    Column {
        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = PolishGrayText)
            },
            trailingIcon = {
                IconButton(onClick = {
                    val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    if (hasFine || hasCoarse) {
                        isFetchingLocation = true
                        getCurrentGpsLocation(context, department, municipality) { result ->
                            onAddressChange(result)
                            isFetchingLocation = false
                        }
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        )
                    }
                }) {
                    if (isFetchingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = PolishBlue
                        )
                    } else {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = "Usar mi ubicación (GPS)",
                            tint = PolishBlue
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PolishBlue,
                unfocusedBorderColor = PolishBorder
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Escribe la dirección manualmente o toca el ícono para usar tu ubicación GPS.",
            fontSize = 11.sp,
            color = PolishGrayText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JobPostForm(
    currentUser: UserProfile?,
    categories: List<String>,
    departmentsAndMunicipalities: List<Pair<String, List<String>>>,
    onDismiss: () -> Unit,
    onSubmit: (category: String, title: String, description: String, budget: Double, address: String, department: String, municipality: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var department by remember { mutableStateOf(currentUser?.department ?: "") }
    var municipality by remember { mutableStateOf(currentUser?.municipality ?: "") }
    var expandedCategory by remember { mutableStateOf(false) }
    var showDeptDropdown by remember { mutableStateOf(false) }
    var showMuniDropdown by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val categoryPrices = mapOf(
        "Plomería" to "50,000 - 150,000 COP/hora",
        "Electricidad" to "60,000 - 180,000 COP/hora",
        "Carpintería" to "45,000 - 130,000 COP/hora",
        "Pintura" to "40,000 - 120,000 COP/hora",
        "Albañilería" to "50,000 - 160,000 COP/hora",
        "Jardinería" to "35,000 - 100,000 COP/hora",
        "Cerrajería" to "60,000 - 150,000 COP/hora",
        "Fumigación" to "70,000 - 200,000 COP/hora"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            shape = RoundedCornerShape(16.dp),
            color = PolishCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Publicar solicitud",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = PolishGrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Categoría *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        placeholder = { Text("Seleccionar categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PolishBlue,
                            unfocusedBorderColor = PolishBorder
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(getCategoryIcon(category), fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(category)
                                    }
                                },
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                if (selectedCategory.isNotEmpty() && categoryPrices.containsKey(selectedCategory)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PolishIce
                    ) {
                        Text(
                            text = "Rango de precios: ${categoryPrices[selectedCategory]}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            fontSize = 13.sp,
                            color = PolishBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Describe el problema o servicio que necesitas...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Dirección",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                AddressFieldWithGps(
                    address = address,
                    onAddressChange = { address = it },
                    department = department,
                    municipality = municipality
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Departamento",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = department,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDeptDropdown = true },
                                placeholder = { Text("Seleccionar") },
                                trailingIcon = {
                                    IconButton(onClick = { showDeptDropdown = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar departamentos", tint = PolishBlue)
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PolishBlue,
                                    unfocusedBorderColor = PolishBorder
                                )
                            )
                            DropdownMenu(
                                expanded = showDeptDropdown,
                                onDismissRequest = { showDeptDropdown = false }
                            ) {
                                departmentsAndMunicipalities.forEach { (dept, munis) ->
                                    DropdownMenuItem(
                                        text = { Text(dept) },
                                        onClick = {
                                            department = dept
                                            municipality = munis.firstOrNull() ?: ""
                                            showDeptDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Municipio",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val currentMunis = remember(department, departmentsAndMunicipalities) {
                            departmentsAndMunicipalities.firstOrNull { it.first == department }?.second ?: emptyList()
                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = municipality,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showMuniDropdown = true },
                                placeholder = { Text("Seleccionar") },
                                trailingIcon = {
                                    IconButton(onClick = { showMuniDropdown = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Mostrar municipios", tint = PolishBlue)
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PolishBlue,
                                    unfocusedBorderColor = PolishBorder
                                )
                            )
                            DropdownMenu(
                                expanded = showMuniDropdown,
                                onDismissRequest = { showMuniDropdown = false }
                            ) {
                                currentMunis.forEach { muni ->
                                    DropdownMenuItem(
                                        text = { Text(muni) },
                                        onClick = {
                                            municipality = muni
                                            showMuniDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cancelar")
                    }

                    Button(
                        onClick = {
                            if (!isSubmitting) {
                                isSubmitting = true
                                onSubmit(
                                    selectedCategory,
                                    selectedCategory,
                                    description,
                                    0.0,
                                    address,
                                    department,
                                    municipality
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedCategory.isNotEmpty() && !isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Publicar",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RatingDialog(
    jobId: String,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, review: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var review by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Calificar servicio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Cómo fue tu experiencia?",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { index ->
                        IconButton(onClick = { rating = index + 1 }) {
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Estrella ${index + 1}",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = review,
                    onValueChange = { review = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Escribe tu reseña (opcional)") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, review) },
                enabled = rating > 0,
                colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Publicar calificación",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = PolishGrayText
                )
            }
        }
    )
}

@Composable
fun CancelJobDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Cancelar trabajo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Estás seguro de que deseas cancelar este trabajo?",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Razón (opcional)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Motivo de la cancelación...") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = PolishBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(reason) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Sí, cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "No, mantener",
                    color = PolishGrayText
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentUser: UserProfile?,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, workCategory: String, department: String, municipality: String, address: String, hourlyRate: Double?) -> Unit
) {
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var workCategory by remember { mutableStateOf(currentUser?.workCategory ?: "") }
    var department by remember { mutableStateOf(currentUser?.department ?: "") }
    var municipality by remember { mutableStateOf(currentUser?.municipality ?: "") }
    var address by remember { mutableStateOf(currentUser?.address ?: "") }
    var hourlyRate by remember { mutableStateOf(currentUser?.hourlyRate?.toString() ?: "") }

    val categories = listOf(
        "Plomería", "Electricidad", "Carpintería", "Pintura",
        "Albañilería", "Jardinería", "Cerrajería", "Fumigación"
    )
    var expandedCategory by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            shape = RoundedCornerShape(16.dp),
            color = PolishCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Editar perfil",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = PolishGrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Nombre",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Teléfono",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = PolishGrayText
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Categoría de trabajo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = workCategory,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = true,
                        placeholder = { Text("Seleccionar categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PolishBlue,
                            unfocusedBorderColor = PolishBorder
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(getCategoryIcon(category), fontSize = 18.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(category)
                                    }
                                },
                                onClick = {
                                    workCategory = category
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tarifa por hora (COP)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = hourlyRate,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            hourlyRate = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej: 50000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Departamento",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Municipio",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = municipality,
                    onValueChange = { municipality = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Dirección",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                AddressFieldWithGps(
                    address = address,
                    onAddressChange = { address = it },
                    department = department,
                    municipality = municipality
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cancelar")
                    }

                    Button(
                        onClick = {
                            val rateValue = hourlyRate.toDoubleOrNull()
                            onSave(
                                name,
                                phone,
                                workCategory,
                                department,
                                municipality,
                                address,
                                rateValue
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = name.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Guardar",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Cambiar contraseña",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Nueva contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Mínimo 8 caracteres") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.Face else Icons.Default.Badge,
                                contentDescription = if (showPassword) "Ocultar" else "Mostrar",
                                tint = PolishGrayText
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmar contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Repite la contraseña") },
                    visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                if (showConfirmPassword) Icons.Default.Face else Icons.Default.Badge,
                                contentDescription = if (showConfirmPassword) "Ocultar" else "Mostrar",
                                tint = PolishGrayText
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )

                if (newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Las contraseñas no coinciden",
                        fontSize = 13.sp,
                        color = Color(0xFFE53935)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(newPassword) },
                enabled = newPassword.length >= 8 && newPassword == confirmPassword,
                colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Actualizar",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = PolishGrayText
                )
            }
        }
    )
}

@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var confirmText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Eliminar cuenta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Esta acción es permanente y no se puede deshacer. Se eliminarán todos tus datos, incluyendo perfil, trabajos y calificaciones.",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Escribe \"ELIMINAR\" para confirmar:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmText,
                    onValueChange = { confirmText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("ELIMINAR") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFE53935),
                        unfocusedBorderColor = PolishBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmText == "ELIMINAR",
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Eliminar cuenta",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = PolishGrayText
                )
            }
        }
    )
}

@Composable
fun RechargeDialog(
    onDismiss: () -> Unit,
    onSubmit: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    val presetAmounts = listOf(10000.0, 25000.0, 50000.0, 100000.0, 200000.0, 500000.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Recargar saldo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Selecciona un monto rápido o ingresa uno personalizado",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetAmounts.forEach { preset ->
                        FilterChip(
                            selected = amount == preset.toLong().toString(),
                            onClick = { amount = preset.toLong().toString() },
                            label = { Text(formatCurrency(preset)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PolishBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            amount = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Monto personalizado (COP)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Money,
                            contentDescription = null,
                            tint = PolishGrayText
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    if (amountValue > 0) {
                        onSubmit(amountValue)
                    }
                },
                enabled = (amount.toDoubleOrNull() ?: 0.0) > 0,
                colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Recargar",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = PolishGrayText
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotificationsPanel(
    notifications: List<AppNotification>,
    onDismiss: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PolishCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 140.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PolishBorder)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notificaciones",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = PolishGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = PolishGrayText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay notificaciones",
                            color = PolishGrayText,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = { onNotificationClick(notification) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: AppNotification,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) PolishCard else PolishIce
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PolishBlue)
                        .padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.body,
                    fontSize = 13.sp,
                    color = PolishGrayText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTimestamp(notification.timestamp),
                    fontSize = 11.sp,
                    color = PolishGrayText
                )
            }
        }
    }
}

@Composable
fun JobDetailCard(
    job: JobRequest,
    onChat: () -> Unit,
    onBids: () -> Unit,
    onCancel: () -> Unit,
    onRate: () -> Unit,
    onPay: () -> Unit
) {
    val statusColor = getStatusColor(job.status)
    val statusText = getStatusText(job.status)
    val categoryColor = getCategoryColor(job.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryColor.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getCategoryIcon(job.category),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = job.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = categoryColor
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = job.title ?: job.category,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (job.description != null && job.description.isNotEmpty()) {
                Text(
                    text = job.description,
                    fontSize = 14.sp,
                    color = PolishGrayText,
                    lineHeight = 20.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = PolishBorder, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Presupuesto",
                        fontSize = 12.sp,
                        color = PolishGrayText
                    )
                    Text(
                        text = formatCurrency(job.finalPrice ?: job.budgetMin ?: 0.0),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                }
                if (job.professionalName != null) {
                    val proName = job.professionalName ?: ""
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Profesional",
                            fontSize = 12.sp,
                            color = PolishGrayText
                        )
                        Text(
                            text = proName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = PolishDark
                        )
                    }
                }
            }

            if (job.address != null && job.address.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = job.address,
                        fontSize = 13.sp,
                        color = PolishGrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (job.department != null || job.municipality != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = listOfNotNull(job.municipality, job.department).joinToString(", "),
                        fontSize = 13.sp,
                        color = PolishGrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (job.status.uppercase()) {
                "OPEN", "ABIERTO" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBids,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Receipt,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Ver ofertas")
                        }
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Cancelar")
                        }
                    }
                }
                "ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onChat,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Chat")
                        }
                        Button(
                            onClick = onPay,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Payment,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pagar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                "PENDING_RATING", "PENDIENTE_CALIFICACION" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onChat,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Chat")
                        }
                        Button(
                            onClick = onRate,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Calificar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                "COMPLETED", "COMPLETADO" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (job.clientRatingOfPro == null) {
                            Button(
                                onClick = onRate,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Calificar profesional",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Trabajo completado",
                                    fontSize = 14.sp,
                                    color = PolishGreen,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    repeat(5) { index ->
                                        Icon(
                                            if (index < (job.clientRatingOfPro ?: 0f).toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = null,
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                "CANCELLED", "CANCELADO" -> {
                    Text(
                        text = "Trabajo cancelado",
                        fontSize = 14.sp,
                        color = Color(0xFFE53935),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (job.status.uppercase() == "CANCELLED" && job.counterOfferAmount != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Razón: ${job.completionReview ?: "No especificada"}",
                            fontSize = 13.sp,
                            color = PolishGrayText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                "NEGOTIATING", "NEGOCIANDO" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onChat,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Chat")
                        }
                    }
                    if (job.counterOfferAmount != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFF3E0)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Money,
                                    contentDescription = null,
                                    tint = Color(0xFFFF9800),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Contraoferta de ${job.counterOfferSender ?: "el profesional"}",
                                        fontSize = 13.sp,
                                        color = Color(0xFFE65100),
                                        fontWeight = FontWeight.Medium
                                    )
                                    val coAmount = job.counterOfferAmount ?: 0.0
                                    Text(
                                        text = formatCurrency(coAmount),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun ActiveJobsSection(
    jobs: List<JobRequest>,
    onJobClick: (JobRequest) -> Unit,
    onChatClick: (String) -> Unit,
    onCancelClick: (String) -> Unit,
    onRateClick: (String) -> Unit,
    onPayClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trabajos activos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PolishBlue.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${jobs.size}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Work,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No hay trabajos activos",
                        color = PolishGrayText,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            jobs.forEach { job ->
                JobDetailCard(
                    job = job,
                    onChat = {
                        job.professionalId?.let { onChatClick(job.id) }
                    },
                    onBids = { onJobClick(job) },
                    onCancel = { onCancelClick(job.id) },
                    onRate = { onRateClick(job.id) },
                    onPay = { onPayClick(job.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PendingRatingSection(
    jobs: List<JobRequest>,
    onRateClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Por calificar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFF9800).copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${jobs.size}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todos tus trabajos están calificados",
                    color = PolishGrayText,
                    fontSize = 14.sp
                )
            }
        } else {
            jobs.forEach { job ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRateClick(job.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = job.title ?: job.category,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PolishDark
                            )
                            Text(
                                text = "Califica a ${job.professionalName ?: "el profesional"}",
                                fontSize = 13.sp,
                                color = PolishGrayText
                            )
                        }
                        Icon(
                            Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CompletedJobsSection(
    jobs: List<JobRequest>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Trabajos completados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PolishGreen.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "${jobs.size}",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Aún no has completado trabajos",
                        color = PolishGrayText,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            jobs.take(5).forEach { job ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PolishCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = job.title ?: job.category,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PolishDark
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = String.format("%.1f", job.clientRatingOfPro ?: 0f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PolishDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "con ${job.professionalName ?: "Profesional"}",
                            fontSize = 13.sp,
                            color = PolishGrayText
                        )
                        val fp = job.finalPrice
                        if (fp != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatCurrency(fp),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = PolishGreen
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ChatScreen(
    jobId: String,
    messages: List<ChatMessage>,
    currentUserId: String?,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val listState = rememberScrollState()

    LaunchedEffect(messages.size) {
        listState.animateScrollTo(listState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PolishBg)
    ) {
        Surface(
            color = PolishCard,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = PolishDark
                    )
                }
                Text(
                    text = "Chat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(listState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            messages.forEach { message ->
                val isCurrentUser = message.senderId == currentUserId
                ChatBubble(
                    message = message,
                    isCurrentUser = isCurrentUser
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = null,
                            tint = PolishGrayText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Inicia la conversación",
                            color = PolishGrayText,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        Surface(
            color = PolishCard,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe un mensaje...") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder,
                        focusedContainerColor = PolishBg,
                        unfocusedContainerColor = PolishBg
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onSendMessage(messageText)
                            messageText = ""
                        }
                    },
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = if (messageText.isNotBlank()) PolishBlue else PolishGrayText
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    isCurrentUser: Boolean
) {
    val bubbleColor = if (isCurrentUser) PolishBlue else PolishCard
    val textColor = if (isCurrentUser) Color.White else PolishDark
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            color = bubbleColor,
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                if (!isCurrentUser) {
                    Text(
                        text = message.senderName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = textColor,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatTimestamp(message.timestamp),
                    fontSize = 10.sp,
                    color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else PolishGrayText,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CounterOfferDialog(
    currentAmount: Double?,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onCounterOffer: (Double) -> Unit
) {
    var counterAmount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Contraoferta",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (currentAmount != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PolishIce
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Money,
                                contentDescription = null,
                                tint = PolishBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Monto propuesto",
                                    fontSize = 12.sp,
                                    color = PolishGrayText
                                )
                                Text(
                                    text = formatCurrency(currentAmount),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PolishBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tu contraoferta",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = counterAmount,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            counterAmount = newValue
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Monto en COP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PolishBlue,
                        unfocusedBorderColor = PolishBorder
                    )
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val amount = counterAmount.toDoubleOrNull()
                        if (amount != null && amount > 0) {
                            onCounterOffer(amount)
                        }
                    },
                    enabled = (counterAmount.toDoubleOrNull() ?: 0.0) > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Enviar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                    )
                ) {
                    Text(text = "Rechazar")
                }
                OutlinedButton(
                    onClick = onAccept,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PolishGreen),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PolishGreen)
                    )
                ) {
                    Text(text = "Aceptar")
                }
            }
        }
    )
}

@Composable
fun BidCard(
    bid: Bid,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onCounterOffer: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (bid.professionalPhoto != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(bid.professionalPhoto)
                            .crossfade(true)
                            .build(),
                        contentDescription = bid.professionalName,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, PolishBorder, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(PolishIce)
                            .border(2.dp, PolishBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = PolishBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bid.professionalName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", bid.professionalRating),
                            fontSize = 13.sp,
                            color = PolishGrayText
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatCurrency(bid.amount),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                    if (bid.durationHours != null) {
                        Text(
                            text = "${bid.durationHours}h estimadas",
                            fontSize = 12.sp,
                            color = PolishGrayText
                        )
                    }
                }
            }

            if (bid.comment != null && bid.comment.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = bid.comment,
                    fontSize = 13.sp,
                    color = PolishGrayText,
                    lineHeight = 18.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            val lcoAmount = bid.lastCounterOfferAmount
            if (lcoAmount != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFF3E0)
                ) {
                    Text(
                        text = "Contraoferta: ${formatCurrency(lcoAmount)}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (bid.status.uppercase()) {
                "PENDING", "PENDIENTE" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE53935))
                            )
                        ) {
                            Text(text = "Rechazar", fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = onCounterOffer,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF9800)),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFF9800))
                            )
                        ) {
                            Text(text = "Contraoferta", fontSize = 13.sp)
                        }
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Aceptar",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
                "ACCEPTED", "ACEPTADO" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onChat,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Chatear", fontSize = 13.sp)
                        }
                    }
                }
                "REJECTED", "RECHAZADA" -> {
                    Text(
                        text = "Oferta rechazada",
                        fontSize = 13.sp,
                        color = Color(0xFFE53935),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalPortfolioSheet(
    professionalName: String,
    portfolioItems: List<Pair<String, String>>,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PolishCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 140.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PolishBorder)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Portafolio",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                    Text(
                        text = professionalName,
                        fontSize = 14.sp,
                        color = PolishGrayText
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = PolishGrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (portfolioItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = PolishGrayText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No hay trabajos en el portafolio",
                            color = PolishGrayText,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    items(portfolioItems) { (title, description) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = PolishBg)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PolishDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = description,
                                    fontSize = 13.sp,
                                    color = PolishGrayText,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun JobHistorySection(
    jobs: List<JobRequest>,
    onJobClick: (JobRequest) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Historial de trabajos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (jobs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Receipt,
                        contentDescription = null,
                        tint = PolishGrayText,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sin historial de trabajos",
                        color = PolishGrayText,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            jobs.forEach { job ->
                JobHistoryItem(
                    job = job,
                    onClick = { onJobClick(job) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun JobHistoryItem(
    job: JobRequest,
    onClick: () -> Unit
) {
    val statusColor = getStatusColor(job.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getCategoryColor(job.category).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getCategoryIcon(job.category),
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = job.title ?: job.category,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PolishDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = job.professionalName ?: "Sin asignar",
                    fontSize = 13.sp,
                    color = PolishGrayText
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = getStatusText(job.status),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
                val fp2 = job.finalPrice
                if (fp2 != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCurrency(fp2),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishDark
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentDialog(
    jobId: String,
    amount: Double?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var paymentMethod by remember { mutableStateOf("card") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PolishCard,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Confirmar pago",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (amount != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PolishIce
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total a pagar",
                                fontSize = 14.sp,
                                color = PolishGrayText
                            )
                            Text(
                                text = formatCurrency(amount),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = PolishBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Método de pago",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.height(12.dp))

                PaymentMethodOption(
                    title = "Tarjeta de crédito/débito",
                    icon = Icons.Default.Payment,
                    selected = paymentMethod == "card",
                    onClick = { paymentMethod = "card" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentMethodOption(
                    title = "PSE",
                    icon = Icons.Default.Store,
                    selected = paymentMethod == "pse",
                    onClick = { paymentMethod = "pse" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentMethodOption(
                    title = "Efectivo",
                    icon = Icons.Default.Money,
                    selected = paymentMethod == "cash",
                    onClick = { paymentMethod = "cash" }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentMethodOption(
                    title = "Saldo en billetera",
                    icon = Icons.Default.Payment,
                    selected = paymentMethod == "wallet",
                    onClick = { paymentMethod = "wallet" }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(paymentMethod) },
                colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Pagar ahora",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = PolishGrayText
                )
            }
        }
    )
}

@Composable
fun PaymentMethodOption(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = if (selected) PolishBlue.copy(alpha = 0.08f) else Color.Transparent,
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, PolishBlue) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) PolishBlue else PolishGrayText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = if (selected) PolishBlue else PolishDark,
                modifier = Modifier.weight(1f)
            )
            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = PolishBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PolishGrayText.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = PolishDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = PolishGrayText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
            if (actionText != null && onAction != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onAction,
                    colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = actionText,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color = PolishBlue
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = PolishGrayText
            )
        }
    }
}

@Composable
fun LoadingOverlay(message: String = "Cargando...") {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PolishCard)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = PolishBlue,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = PolishDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ErrorSnackBar(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE53935),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun SuccessSnackBar(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = PolishGreen,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun ProfessionalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Buscar profesionales...")
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = PolishGrayText
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Limpiar",
                            tint = PolishGrayText
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PolishBlue,
                unfocusedBorderColor = PolishBorder,
                focusedContainerColor = PolishCard,
                unfocusedContainerColor = PolishCard
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PolishCard)
        ) {
            Icon(
                Icons.Default.FilterList,
                contentDescription = "Filtros",
                tint = PolishBlue
            )
        }
    }
}

@Composable
fun CategoryFilterSection(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categorías",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = PolishDark
            )
            if (selectedCategory != null) {
                TextButton(onClick = { onCategorySelected(null) }) {
                    Text(
                        text = "Limpiar",
                        fontSize = 13.sp,
                        color = PolishBlue
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("Todas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PolishBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category) null else category)
                    },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getCategoryColor(category),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun ProfessionalGridCard(
    professional: ProfessionalModel,
    onClick: () -> Unit
) {
    val tagColor = getCategoryColor(professional.workCategory ?: "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PolishCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                if (professional.photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(professional.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = professional.name,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(2.dp, PolishBorder, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PolishIce)
                            .border(2.dp, PolishBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = PolishBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                if (professional.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(PolishGreen)
                            .border(2.dp, PolishCard, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = professional.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (professional.workCategory != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = tagColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = professional.workCategory,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        color = tagColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", professional.rating),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${professional.serviceCount})",
                    fontSize = 12.sp,
                    color = PolishGrayText
                )
            }
        }
    }
}

@Composable
fun WeatherLocationHeader(
    location: String?
) {
    if (location != null && location.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = PolishBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = location,
                fontSize = 13.sp,
                color = PolishGrayText
            )
        }
    }
}

@Composable
fun JobStatsOverview(jobs: List<JobRequest>) {
    val activeCount = jobs.count {
        it.status.uppercase() in listOf("ACCEPTED", "ACEPTADO", "IN_PROGRESS", "EN_PROGRESO", "NEGOTIATING", "NEGOCIANDO")
    }
    val completedCount = jobs.count {
        it.status.uppercase() in listOf("COMPLETED", "COMPLETADO")
    }
    val pendingCount = jobs.count {
        it.status.uppercase() in listOf("OPEN", "ABIERTO", "PENDING", "PENDIENTE")
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            label = "Activos",
            value = "$activeCount",
            icon = Icons.Default.Work,
            color = PolishBlue
        )
        StatCard(
            label = "Completados",
            value = "$completedCount",
            icon = Icons.Default.Check,
            color = PolishGreen
        )
        StatCard(
            label = "Pendientes",
            value = "$pendingCount",
            icon = Icons.Default.Receipt,
            color = Color(0xFFFF9800)
        )
    }
}

@Composable
fun QuickActionsRow(
    onPostJob: () -> Unit,
    onSearchPro: () -> Unit,
    onWallet: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onPostJob() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PolishBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Publicar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onSearchPro() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = PolishGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Buscar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .clickable { onWallet() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Payment,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Billetera",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun JobRequestFormStep(
    step: Int,
    totalSteps: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (index <= step) PolishBlue else PolishBorder
                    )
            )
            if (index < totalSteps - 1) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(2.dp)
                        .background(
                            if (index < step) PolishBlue else PolishBorder
                        )
                )
            }
        }
    }
}

@Composable
fun ImageCaptureSection(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    capturedImages: List<Uri>,
    onRemoveImage: (Uri) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Fotos del problema",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = PolishDark
        )
        Text(
            text = "Opcional - Ayuda al profesional a entender mejor",
            fontSize = 12.sp,
            color = PolishGrayText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCameraClick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Cámara", fontSize = 13.sp)
            }
            OutlinedButton(
                onClick = onGalleryClick,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Galería", fontSize = 13.sp)
            }
        }

        if (capturedImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(capturedImages) { uri ->
                    Box(modifier = Modifier.size(80.dp)) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Imagen capturada",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { onRemoveImage(uri) },
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd)
                                .background(
                                    Color.Black.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Eliminar",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnlineStatusDot(isOnline: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(if (isOnline) PolishGreen else PolishBorder)
            .border(2.dp, PolishCard, CircleShape)
    )
}

@Composable
fun CategoryTag(
    category: String,
    modifier: Modifier = Modifier
) {
    val color = getCategoryColor(category)
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getCategoryIcon(category),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = category,
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RatingStars(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Int = 16
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}

@Composable
fun ReviewItem(
    rating: Float,
    text: String,
    reviewerName: String,
    timestamp: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingStars(rating = rating, starSize = 14)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = reviewerName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = PolishDark
                )
            }
            Text(
                text = timestamp,
                fontSize = 11.sp,
                color = PolishGrayText
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = PolishGrayText,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun PriceRangeDisplay(
    minPrice: Double,
    maxPrice: Double
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = PolishIce
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Money,
                contentDescription = null,
                tint = PolishBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${formatCurrency(minPrice)} - ${formatCurrency(maxPrice)}",
                fontSize = 13.sp,
                color = PolishBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    count: Int? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            if (count != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PolishBlue.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "$count",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PolishBlue
                    )
                }
            }
        }
        if (actionText != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionText,
                    fontSize = 14.sp,
                    color = PolishBlue
                )
            }
        }
    }
}

@Composable
fun ProfessionalContactBar(
    onCallClick: () -> Unit,
    onChatClick: () -> Unit,
    onHireClick: () -> Unit
) {
    Surface(
        color = PolishCard,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCallClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Llamar", fontSize = 13.sp)
            }

            OutlinedButton(
                onClick = onChatClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    Icons.Default.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Chat", fontSize = 13.sp)
            }

            Button(
                onClick = onHireClick,
                modifier = Modifier.weight(1.2f),
                colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Contratar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ProfessionalAboutSection(
    description: String,
    specialties: List<String>,
    memberSince: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Sobre mí",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = PolishGrayText,
            lineHeight = 20.sp
        )

        if (specialties.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Especialidades",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PolishDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                specialties.forEach { specialty ->
                    CategoryTag(category = specialty)
                }
            }
        }

        if (memberSince.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Store,
                    contentDescription = null,
                    tint = PolishGrayText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Miembro desde $memberSince",
                    fontSize = 13.sp,
                    color = PolishGrayText
                )
            }
        }
    }
}

@Composable
fun ProfessionalStatsRow(
    rating: Float,
    reviewCount: Int,
    completedJobs: Int,
    location: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", rating),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
            }
            Text(
                text = "$reviewCount reseñas",
                fontSize = 12.sp,
                color = PolishGrayText
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(40.dp)
                .background(PolishBorder)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$completedJobs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
            Text(
                text = "Trabajos",
                fontSize = 12.sp,
                color = PolishGrayText
            )
        }

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(40.dp)
                .background(PolishBorder)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = PolishGrayText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location,
                    fontSize = 13.sp,
                    color = PolishDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "Ubicación",
                fontSize = 12.sp,
                color = PolishGrayText
            )
        }
    }
}

@Composable
fun ProfessionalPricingSection(
    hourlyRate: Double?,
    negotiable: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tarifas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Money,
                    contentDescription = null,
                    tint = PolishBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tarifa por hora",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )
            }
            Text(
                text = if (hourlyRate != null) formatCurrency(hourlyRate) else "A convenir",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PolishBlue
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Balance,
                    contentDescription = null,
                    tint = PolishGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Negociación",
                    fontSize = 14.sp,
                    color = PolishGrayText
                )
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (negotiable) PolishGreen.copy(alpha = 0.1f) else Color(0xFFFFF3E0)
            ) {
                Text(
                    text = if (negotiable) "Disponible" else "No disponible",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (negotiable) PolishGreen else Color(0xFFE65100)
                )
            }
        }
    }
}

@Composable
fun ProfessionalReviewsSection(
    reviews: List<Pair<Float, String>>,
    totalRating: Float,
    totalReviews: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Reseñas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = PolishDark
        )
        Spacer(modifier = Modifier.height(12.dp))

        val distribution = getRatingDistribution(reviews)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.1f", totalRating),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = PolishDark
                )
                RatingStars(rating = totalRating, starSize = 18)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$totalReviews reseñas",
                    fontSize = 13.sp,
                    color = PolishGrayText
                )
            }

            Column(
                modifier = Modifier.width(180.dp)
            ) {
                for (i in 5 downTo 1) {
                    val count = distribution[i] ?: 0
                    val fraction = if (totalReviews > 0) count.toFloat() / totalReviews else 0f
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$i",
                            fontSize = 12.sp,
                            color = PolishGrayText,
                            modifier = Modifier.width(12.dp)
                        )
                        LinearProgressIndicator(
                            progress = { fraction },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFFFFC107),
                            trackColor = PolishBorder
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$count",
                            fontSize = 12.sp,
                            color = PolishGrayText,
                            modifier = Modifier.width(20.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val sampleReviews = listOf(
            Pair(5f, "Excelente trabajo, muy profesional y puntual. Lo recomiendo ampliamente."),
            Pair(4f, "Buen servicio, quedé satisfecho con el resultado. Volveré a contratarlo."),
            Pair(5f, "Muy recomendable, resolvería el problema rápidamente y con buena calidad.")
        )

        sampleReviews.forEach { (rating, text) ->
            ReviewItem(
                rating = rating,
                text = text,
                reviewerName = "Cliente verificado",
                timestamp = "Hace 2 días"
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfessionalBottomActions(
    onHire: () -> Unit,
    onChat: () -> Unit,
    onViewPortfolio: () -> Unit
) {
    Surface(
        color = PolishCard,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onViewPortfolio,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Portafolio", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onChat,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Chatear", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onHire,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PolishGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contratar ahora",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
