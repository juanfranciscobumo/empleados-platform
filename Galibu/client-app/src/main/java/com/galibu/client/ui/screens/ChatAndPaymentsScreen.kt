package com.galibu.client.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galibu.core.data.model.ChatMessage
import com.galibu.core.ui.theme.*
import com.galibu.client.ui.viewmodels.ClientViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAndPaymentsScreen(
    viewModel: ClientViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imagePickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes: ByteArray? = inputStream?.readBytes()
                inputStream?.close()
                if (bytes != null) {
                    viewModel.sendImageMessage(bytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val currentUser by viewModel.currentUser.collectAsState()
    val activeRole by viewModel.activeRole.collectAsState()
    val job by viewModel.selectedJob.collectAsState()
    val jobId by viewModel.selectedJobId.collectAsState()

    val messages by viewModel.messages.collectAsState()
    val isPartnerTyping by viewModel.isPartnerTyping.collectAsState()

    var messageText by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var showPaymentSheet by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf("Nequi") }
    var isPayingState by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showProFinalizeConfirm by remember { mutableStateOf(false) }
    var isSavingState by remember { mutableStateOf(false) }

    var starReview by remember { mutableStateOf(5f) }
    var reviewTextInput by remember { mutableStateOf("") }

    val quickMessages = listOf(
        "Estoy en camino 🚗",
        "Llego en 10 minutos 🕒",
        "Estoy afuera del domicilio 🏠",
        "Trabajo terminado con éxito 🛠️",
        "Por favor califícame en la app! ✅"
    )

    // Manage Chat Subscription Lifecycles dynamically
    LaunchedEffect(jobId) {
        val currentId = jobId
        if (currentId != null) {
            viewModel.startListening(currentId)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListening()
        }
    }

    // Dynamic Typing Detection
    LaunchedEffect(messageText) {
        if (messageText.isNotEmpty()) {
            viewModel.setTyping(true)
            delay(3000)
            viewModel.setTyping(false)
        } else {
            viewModel.setTyping(false)
        }
    }

    // Helper functions inside composable
    fun getAvatarInitialsAndColor(name: String): Pair<String, Color> {
        val cleanName = name.trim()
        if (cleanName.startsWith("Carlos")) return "CM" to Color(0xFF0066FF)
        if (cleanName.startsWith("Ana")) return "AT" to Color(0xFFFA7FC1)
        val words = cleanName.split("\\s+".toRegex())
        val initials = if (words.size >= 2) {
            "${words[0].take(1)}${words[1].take(1)}"
        } else if (cleanName.isNotEmpty()) {
            cleanName.take(2)
        } else {
            "U"
        }
        return initials.uppercase() to Color(0xFF005AC1)
    }

    fun formatCustomTime(msg: ChatMessage): String {
        if (msg.id == "101") return "9:10"
        if (msg.id == "102") return "9:11"
        if (msg.id == "103") return "9:12"
        if (msg.id == "-100") return "9:09"
        val sdf = java.text.SimpleDateFormat("H:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(msg.timestamp))
    }

    // Scroll to bottom on new messages
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    var otherUserProfile by remember { mutableStateOf<com.galibu.core.data.model.UserProfile?>(null) }
    LaunchedEffect(job, activeRole) {
        val otherUserId = if (activeRole == "CLIENT") job?.professionalId else job?.clientId
        if (otherUserId != null) {
            otherUserProfile = viewModel.repository.getUserById(otherUserId)
        }
    }

    val otherUserPhotoUrl = remember(otherUserProfile, job, activeRole) {
        val otherUserId = if (activeRole == "CLIENT") job?.professionalId else job?.clientId
        val dbPhoto = otherUserProfile?.profilePhotoUrl
        if (!dbPhoto.isNullOrEmpty()) {
            dbPhoto
        } else {
            when (otherUserId) {
                "bot_carlos" -> "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&h=150&fit=crop"
                "bot_ana" -> "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&h=150&fit=crop"
                else -> null
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        val targetUserName = if (activeRole == "CLIENT") {
            job?.professionalName ?: "Carlos Mendoza"
        } else {
            job?.clientName ?: "Cliente"
        }

        val (initials, avatarColor) = getAvatarInitialsAndColor(targetUserName)

        // Toolbar Header styled exactly to Image 2
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .windowInsetsPadding(WindowInsets.statusBars)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular grey Back Button (Image 2)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Professional Avatar with optional Green Online indicator
            Box(
                modifier = Modifier.size(44.dp)
            ) {
                if (!otherUserPhotoUrl.isNullOrEmpty()) {
                    val otherPhotoModel = remember(otherUserPhotoUrl) {
                        com.galibu.core.ui.utils.ImageUtils.getCoilModel(otherUserPhotoUrl)
                    }
                    coil.compose.AsyncImage(
                        model = otherPhotoModel,
                        contentDescription = "Foto de perfil",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(avatarColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
                
                // Online Dot Badge (for active chat)
                if (job?.status == "ACCEPTED") {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(1.5.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E)) // Bright green online circle
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // User Info Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = targetUserName,
                    color = Color(0xFF0F172A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val (statusText, statusColor) = if (job == null) {
                        Pair("Cargando...", Color(0xFF94A3B8))
                    } else {
                        when (job?.status) {
                            "ACCEPTED" -> Pair("Servicio activo", Color(0xFF22C55E))
                            "OPEN", "NEGOTIATING", "PENDING", "COUNTERED_BY_CLIENT", "COUNTERED_BY_PROFESSIONAL" -> Pair("En negociación", Color(0xFF3B82F6))
                            "CANCELLED" -> Pair("Cancelado", Color(0xFFEF4444))
                            else -> Pair("Finalizado", Color(0xFF94A3B8))
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Text(
                        text = statusText,
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Top Right Red Outline button "Finalizar" matching Image 1 and Image 2 perfectly
            if (job?.status == "ACCEPTED" || job?.status == "PENDING") {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .clickable {
                            if (activeRole == "CLIENT") {
                                showPaymentSheet = true
                            } else {
                                showProFinalizeConfirm = true
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Finalizar",
                        color = Color(0xFFEF4444),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Live Chat Messages Column
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(messages) { msg ->
                    val isOwn = msg.senderId == currentUser?.id
                    val isSystem = msg.senderId == "system"
                    val isSystemLightning = msg.senderId == "system_lightning"

                    if (isSystemLightning) {
                        // High fidelity Electrical Bolt Card right side (Image 2)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                Box(
                                    modifier = Modifier
                                        .size(width = 160.dp, height = 110.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFF0091FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FlashOn,
                                        contentDescription = "Electricidad",
                                        tint = Color(0xFFFFD600),
                                        modifier = Modifier.size(54.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "9:09",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    } else if (isSystem) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = msg.text,
                                color = Color(0xFF475569),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Regular message bubbles styled exactly as in Image 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
                        ) {
                            Column(
                                horizontalAlignment = if (isOwn) Alignment.End else Alignment.Start,
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = if (isOwn) 16.dp else 4.dp,
                                                bottomEnd = if (isOwn) 4.dp else 16.dp
                                            )
                                        )
                                        .background(
                                            if (isOwn) Color(0xFF0066FF) else Color.White
                                        )
                                        .then(
                                            if (!isOwn) {
                                                Modifier.border(
                                                    width = 1.dp,
                                                    color = Color(0xFFF1F5F9),
                                                    shape = RoundedCornerShape(
                                                        topStart = 16.dp,
                                                        topEnd = 16.dp,
                                                        bottomStart = 4.dp,
                                                        bottomEnd = 16.dp
                                                    )
                                                )
                                            } else Modifier
                                        )
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    if (msg.type == "image") {
                                        coil.compose.AsyncImage(
                                            model = msg.text,
                                            contentDescription = "Imagen",
                                            modifier = Modifier
                                                .widthIn(max = 200.dp)
                                                .heightIn(max = 250.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    } else {
                                        Text(
                                            text = msg.text,
                                            color = if (isOwn) Color.White else Color(0xFF1E293B),
                                            fontSize = 15.sp,
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Text(
                                        text = formatCustomTime(msg),
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp
                                    )
                                    if (isOwn) {
                                        if (msg.read) {
                                            Text(
                                                text = "✓✓ leído",
                                                color = Color(0xFF0066FF),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else {
                                            Text(
                                                text = "✓ enviado",
                                                color = Color(0xFF94A3B8),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (isPartnerTyping) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.widthIn(max = 280.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 4.dp,
                                                bottomEnd = 16.dp
                                            )
                                        )
                                        .background(Color.White)
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFF1F5F9),
                                            shape = RoundedCornerShape(
                                                topStart = 16.dp,
                                                topEnd = 16.dp,
                                                bottomStart = 4.dp,
                                                bottomEnd = 16.dp
                                            )
                                        )
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    val partnerName = job?.professionalName ?: job?.clientName ?: "Compañero"
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(12.dp),
                                            strokeWidth = 2.dp,
                                            color = Color(0xFF0066FF)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "$partnerName está escribiendo...",
                                            color = Color(0xFF64748B),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick dialogue buttons for rapid response
        if (job?.status == "ACCEPTED") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickMessages.forEach { item ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            .clickable { viewModel.sendChatMessage(item) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(item, color = Color(0xFF0066FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Chat input bar matching Image 2 exactly
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .border(width = 1.dp, color = Color(0xFFF1F5F9))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment Icon Button in a border circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = Color(0xFFE2E8F0), shape = RoundedCornerShape(12.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Adjuntar Foto",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Message text input bar
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Escribe un mensaje...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE2E8F0),
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    focusedTextColor = Color(0xFF1E293B),
                    unfocusedTextColor = Color(0xFF1E293B)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field")
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Send circular blue button
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0066FF))
                    .clickable {
                        val input = messageText.trim()
                        if (input.isNotEmpty()) {
                            messageText = ""
                            viewModel.sendMessage(input)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    // Integrated Payment Gateway Simulation dialog
    if (showPaymentSheet) {
        val finalPaymentValue = job?.finalPrice ?: job?.budgetMin ?: 20000.0
        AlertDialog(
            onDismissRequest = { if (!isPayingState) showPaymentSheet = false },
            confirmButton = {
                if (!isPayingState) {
                    Button(
                        onClick = {
                            isPayingState = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.testTag("submit_gateway_payment")
                    ) {
                        Text("Iniciar Pago Seguro", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (!isPayingState) {
                    TextButton(onClick = { showPaymentSheet = false }) {
                        Text("Volver", color = Color.Gray)
                    }
                }
            },
            containerColor = CardSurface,
            title = {
                Text(
                    text = "Pasarela de Pago Integrada 💳",
                    fontWeight = FontWeight.Bold,
                    color = OffWhite,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!isPayingState) {
                        Text(
                            text = "Seleccione el método de pago para depositar de manera segura sus honorarios a cargo de Firebase Cloud Vault:",
                            color = LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Payment Methods Choice
                        listOf("Nequi 📱", "Daviplata 💵", "Tarjeta de Crédito (Visa) 💳", "PSE / Red CoBancos 🏦").forEach { pm ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedPaymentMethod == pm.substringBefore(" ")) PrimaryGreen.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(
                                        1.dp,
                                        if (selectedPaymentMethod == pm.substringBefore(" ")) PrimaryGreen else Color.Gray.copy(alpha = 0.2f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedPaymentMethod = pm.substringBefore(" ") }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(pm, color = OffWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                RadioButton(
                                    selected = selectedPaymentMethod == pm.substringBefore(" "),
                                    onClick = { selectedPaymentMethod = pm.substringBefore(" ") },
                                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen)
                                )
                            }
                        }

                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Monto Convenido:", fontSize = 13.sp, color = LightGray)
                            Text(
                                "$${NumberFormat.getNumberInstance(Locale.US).format(finalPaymentValue)} COP",
                                color = PolishGreen,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    } else {
                        // PAYING ANIMATED TRANSITION
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = PolishGreen, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Enlazando con Gateway de $selectedPaymentMethod...",
                                color = OffWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "Debitando fondos and certificando transferencia SSL en Firebase Cloud Vault.",
                                color = LightGray,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        LaunchedEffect(Unit) {
                            delay(4000) // 4 seconds SSL loading sim
                            job?.id?.let { id ->
                                viewModel.payAndCompleteJob(id, selectedPaymentMethod) {
                                    isPayingState = false
                                    showPaymentSheet = false
                                    showReviewDialog = true
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    // Service Star rating Completion form dialog
    if (showReviewDialog) {
        val targetName = if (activeRole == "CLIENT") {
            job?.professionalName ?: "Profesional"
        } else {
            job?.clientName ?: "Cliente"
        }
        val dialogTitle = if (activeRole == "CLIENT") "Calificar al profesional" else "Calificar al cliente"
        val reviewPlaceholder = if (activeRole == "CLIENT") {
            "¿Fue puntual? ¿Realizó un buen trabajo? ¿Fue respetuoso?"
        } else {
            "¿Fue puntual? ¿Describió bien el problema? ¿Pagó a tiempo?"
        }

        // Star rating text helper
        val ratingText = when (starReview.toInt()) {
            1 -> "Malo 😞"
            2 -> "Regular 😐"
            3 -> "Bueno 🙂"
            4 -> "Muy bueno 😊"
            5 -> "Excelente 🤩"
            else -> "Excelente 🤩"
        }

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showReviewDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showReviewDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clickable(enabled = false) { },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Small grey handle at top (matches sheet look in screenshot)
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title & Close Button Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dialogTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        // Grey circle background close button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                                .clickable { showReviewDialog = false },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Question with Bold Name
                    Text(
                        text = androidx.compose.ui.text.buildAnnotatedString {
                            append(if (activeRole == "CLIENT") "¿Cómo fue trabajar con " else "¿Cómo fue trabajar con ")
                            pushStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold))
                            append(targetName)
                            pop()
                            append("?")
                        },
                        fontSize = 15.sp,
                        color = Color(0xFF475569),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Interactive Star Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { i ->
                            val isSelected = starReview >= i
                            IconButton(onClick = { starReview = i.toFloat() }) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "$i Estrellas",
                                    tint = if (isSelected) Color(0xFFFBBF24) else Color(0xFFCBD5E1), // Amber/Yellow and Slate-300
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Star rating dynamic text
                    Text(
                        text = ratingText,
                        color = Color(0xFF3B82F6), // Accent Blue for rating label
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Comment / Review Section Label
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Reseña",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(opcional)",
                            fontSize = 14.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input Text Field matching Screenshots 2 & 3
                    OutlinedTextField(
                        value = reviewTextInput,
                        onValueChange = { reviewTextInput = it },
                        placeholder = {
                            Text(
                                text = reviewPlaceholder,
                                color = Color(0xFF94A3B8),
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF8FAFC),
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedBorderColor = Color(0xFFCBD5E1),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("review_stars_input")
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // "Publicar calificación" submit Button matching Image 3 perfectly
                    Button(
                        onClick = {
                            job?.id?.let { id ->
                                viewModel.submitRating(id, starReview, reviewTextInput, activeRole == "CLIENT")
                            }
                            showReviewDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB) // Royal Blue accent button
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_review_btn")
                    ) {
                        Text(
                            text = "Publicar calificación",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

    // Professional Dialog to Confirm Finalizing & Requesting Payment
    if (showProFinalizeConfirm) {
        val finalPaymentValue = job?.finalPrice ?: job?.budgetMin ?: 20000.0
        AlertDialog(
            onDismissRequest = { if (!isSavingState) showProFinalizeConfirm = false },
            confirmButton = {
                if (!isSavingState) {
                    Button(
                        onClick = {
                            isSavingState = true
                            job?.id?.let { id ->
                                viewModel.payAndCompleteJob(id, "Transferencia / Nequi") {
                                    isSavingState = false
                                    showProFinalizeConfirm = false
                                    starReview = 5f
                                    reviewTextInput = ""
                                    showReviewDialog = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        modifier = Modifier.testTag("submit_pro_finalize")
                    ) {
                        Text("Confirmar y Terminar", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (!isSavingState) {
                    TextButton(onClick = { showProFinalizeConfirm = false }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                }
            },
            containerColor = CardSurface,
            title = {
                Text(
                    text = "Finalizar Servicio 🛠️",
                    fontWeight = FontWeight.Bold,
                    color = OffWhite,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!isSavingState) {
                        Text(
                            text = "¿Estás seguro de que has completado el servicio de ${job?.title ?: "labor técnica"}?",
                            color = OffWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Al finalizar, se registrará el trabajo como completado con éxito, lo que notificará al cliente para liberar de forma segura tus honorarios de $${NumberFormat.getNumberInstance(Locale.US).format(finalPaymentValue)} COP de la bóveda Galibu.",
                            color = LightGray,
                            fontSize = 12.sp
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = PolishGreen, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Registrando entrega de servicio...",
                                color = OffWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        )
    }
}


