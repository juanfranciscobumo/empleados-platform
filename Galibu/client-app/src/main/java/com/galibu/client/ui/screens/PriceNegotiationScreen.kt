package com.galibu.client.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.galibu.core.data.model.Bid
import com.galibu.core.data.model.JobRequest
import com.galibu.core.ui.theme.*
import com.galibu.client.ui.viewmodels.ClientViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceNegotiationScreen(
    viewModel: ClientViewModel,
    onBidAccepted: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedJob by viewModel.selectedJob.collectAsState()
    val bids by viewModel.selectedJobBids.collectAsState()
    val professionals by viewModel.professionals.collectAsState()

    var showCounterDialog by remember { mutableStateOf(false) }
    var targetingBidForCounter by remember { mutableStateOf<Bid?>(null) }
    var counterAmountInput by remember { mutableStateOf("") }
    var showDirectCounterDialog by remember { mutableStateOf(false) }
    var directCounterAmountInput by remember { mutableStateOf("") }
    var showClientRatingSheet by remember { mutableStateOf(false) }
    var clientRatingStars by remember { mutableStateOf(0) }
    var clientRatingComment by remember { mutableStateOf("") }

    var showPortfolioDialog by remember { mutableStateOf(false) }
    var portfolioProId by remember { mutableStateOf<String?>(null) }
    var portfolioProName by remember { mutableStateOf("") }
    var showProfileDetailForBid by remember { mutableStateOf<Bid?>(null) }
    val hasAssignedProfessional = selectedJob?.professionalId?.isNotEmpty() == true
    val isDirectNegotiation =
        selectedJob?.let { it.status == "NEGOTIATING" && !it.professionalId.isNullOrEmpty() }
            ?: false


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back toolbar (Clean, dark-colored text on light background)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp)).size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = PolishDark
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ofertas de Profesionales",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PolishDark
            )
        }
        selectedJob?.let { job ->
            val professionalUser = professionals.find { it.id == job.professionalId }
            val baseRateVal = job.proHourlyRate ?: professionalUser?.hourlyRate ?: job.budgetMin
            val proposedRateValue = when (job.counterOfferSender) {
                "CLIENT" -> job.counterOfferAmount ?: job.budgetMin
                "PROFESSIONAL" -> job.counterOfferAmount ?: baseRateVal
                else -> baseRateVal
            }
            val isWaitingForPro =
                job.counterOfferSender == null || job.counterOfferSender == "CLIENT"
            Card(
                colors = CardDefaults.cardColors(containerColor = PolishBlue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.EmojiObjects,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.size(140.dp).align(Alignment.BottomEnd)
                            .offset(x = 20.dp, y = 20.dp)
                    )
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.background(
                                    Color(0xFF004CB0),
                                    RoundedCornerShape(16.dp)
                                ).padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = (job.category ?: "Servicio").uppercase(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = job.description,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = job.address,
                                color = Color.White,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (isDirectNegotiation) {
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.28f),
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isWaitingForPro) "0 ofertantes · Esperando ofertas de profesionales cercanos." else "Contraoferta recibida · Revisa y decide.",
                                    color = Color.White.copy(alpha = 0.92f),
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.cancelJobRequest(job.id) { success ->
                                            if (success) {
                                                viewModel.showAlert(
                                                    "Aviso",
                                                    "Solicitud cancelada exitosamente"
                                                )
                                            } else {
                                                viewModel.showAlert(
                                                    "Aviso",
                                                    "Error al cancelar la solicitud"
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFEE2E2),
                                        contentColor = Color(0xFFDC2626)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 8.dp,
                                        vertical = 6.dp
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        "Cancelar",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }

                                if (!isWaitingForPro) {
                                    OutlinedButton(
                                        onClick = {
                                            directCounterAmountInput =
                                                com.galibu.core.ui.utils.PriceUtils.formatThousands(
                                                    proposedRateValue.toInt().toString()
                                                )
                                            showDirectCounterDialog = true
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                        border = BorderStroke(
                                            1.dp,
                                            Color.White.copy(alpha = 0.55f)
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(
                                            horizontal = 8.dp,
                                            vertical = 6.dp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            "Contraofertar",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.acceptDirectHire(job.id) {
                                                viewModel.showAlert(
                                                    "Aviso",
                                                    "¡Contratación directa aceptada exitosamente!"
                                                )
                                                onBidAccepted(job.id)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(
                                                0xFF1D4ED8
                                            )
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(
                                            horizontal = 8.dp,
                                            vertical = 6.dp
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                "Aceptar",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                            Text(
                                                "$${
                                                    NumberFormat.getNumberInstance(Locale.US)
                                                        .format(proposedRateValue)
                                                }/h",
                                                fontSize = 9.sp,
                                                color = Color.White.copy(alpha = 0.9f),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.selectJob(job.id)
                                        onBidAccepted(job.id)
                                    },
                                    modifier = Modifier
                                        .size(44.dp)
                                        .border(1.dp, Color.White.copy(alpha = 0.55f), CircleShape)
                                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubbleOutline,
                                        contentDescription = "Conversación",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (hasAssignedProfessional) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            val detailStatusLabel = when (job.status) {
                                "ACCEPTED" -> "Trabajo en curso"
                                "PENDING_RATING" -> "Por calificar"
                                "COMPLETED" -> "Completado"
                                else -> if (isWaitingForPro) "Propuesta enviada" else "Contraoferta recibida"
                            }
                            val detailStatusColor = when (job.status) {
                                "ACCEPTED" -> Color(0xFF2563EB)
                                "PENDING_RATING" -> Color(0xFFD97706)
                                "COMPLETED" -> Color(0xFF475569)
                                else -> Color(0xFFEA580C)
                            }
                            val detailStatusBg = when (job.status) {
                                "ACCEPTED" -> Color(0xFFEFF6FF)
                                "PENDING_RATING" -> Color(0xFFFFFBEB)
                                "COMPLETED" -> Color(0xFFF1F5F9)
                                else -> Color(0xFFFFF7ED)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(detailStatusBg)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = detailStatusLabel,
                                    color = detailStatusColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Mi precio (cliente)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "$${
                                        NumberFormat.getNumberInstance(Locale.US)
                                            .format(job.budgetMin)
                                    }/h",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2563EB)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = job.title,
                            color = Color(0xFF1E293B),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${job.category} · Hace ${
                                ((System.currentTimeMillis() - job.createdAt) / 60000).coerceAtLeast(
                                    1
                                )
                            } min", color = Color(0xFF64748B), fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val proName = job.professionalName ?: "Profesional"
                            val initials = proName.split(" ").filter { it.isNotEmpty() }.take(2)
                                .joinToString("") { it.first().uppercase() }
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    initials,
                                    color = Color(0xFF2563EB),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = proName,
                                    color = Color(0xFF1E293B),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB000),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "4.8 (142 reseñas)",
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Tarifa del profesional",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "$${
                                        NumberFormat.getNumberInstance(Locale.US)
                                            .format(baseRateVal)
                                    }/h",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (job.status == "NEGOTIATING") {
                                        if (job.counterOfferSender == "CLIENT") "Tú propusiste" else "Profesional propone"
                                    } else {
                                        "Tarifa acordada"
                                    },
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = "$${
                                        NumberFormat.getNumberInstance(Locale.US)
                                            .format(proposedRateValue)
                                    }/h",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEA580C)
                                )
                            }
                        }

                        if (job.status == "ACCEPTED") {
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.selectJob(job.id)
                                        onBidAccepted(job.id)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(44.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Chatear", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                Button(
                                    onClick = {
                                        viewModel.payAndCompleteJob(job.id, "Transferencia / Nequi") {
                                            viewModel.showAlert("Aviso", "Servicio finalizado. Ahora puedes calificar al profesional.")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Finalizar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                            
                            // Botón Cancelar para trabajos ACCEPTED
                            Spacer(modifier = Modifier.height(8.dp))
                            var showCancelDialog by remember { mutableStateOf(false) }
                            var cancelReason by remember { mutableStateOf("") }
                            
                            OutlinedButton(
                                onClick = { showCancelDialog = true },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color(0xFFDC2626)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFDC2626)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Cancelar trabajo", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            }
                            
                            if (showCancelDialog) {
                                AlertDialog(
                                    onDismissRequest = { showCancelDialog = false },
                                    title = { 
                                        Text(
                                            "¿Cancelar trabajo aceptado?", 
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        ) 
                                    },
                                    text = {
                                        Column {
                                            Text(
                                                "Esta acción no se puede deshacer. El trabajo será cancelado y se notificará al profesional asignado.",
                                                fontSize = 14.sp,
                                                color = Color(0xFF64748B)
                                            )
                                            Spacer(Modifier.height(16.dp))
                                            OutlinedTextField(
                                                value = cancelReason,
                                                onValueChange = { cancelReason = it },
                                                label = { Text("Razón (opcional)", fontSize = 13.sp) },
                                                placeholder = { Text("Ej: Ya no necesito el servicio", fontSize = 12.sp) },
                                                modifier = Modifier.fillMaxWidth(),
                                                maxLines = 3,
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = PolishBlue,
                                                    focusedLabelColor = PolishBlue
                                                )
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                viewModel.cancelJob(job.id, cancelReason) {
                                                    showCancelDialog = false
                                                    cancelReason = ""
                                                    viewModel.showAlert("Aviso", "Trabajo cancelado exitosamente")
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFDC2626)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Sí, cancelar", fontWeight = FontWeight.Bold)
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { 
                                                showCancelDialog = false
                                                cancelReason = ""
                                            }
                                        ) {
                                            Text("No, volver", color = Color(0xFF64748B))
                                        }
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }

                        if (job.status == "PENDING_RATING" && job.clientRatingOfPro == null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { showClientRatingSheet = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFFFBEB),
                                    contentColor = Color(0xFFD97706)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.StarOutline,
                                    contentDescription = null,
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Calificar al profesional", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        if (!isDirectNegotiation) {
            // Bids Stream List Title (matches "Ofertas de profesionales" reference design)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).background(Color(0xFFE0E7FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = PolishBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Ofertas de profesionales",
                    color = Color(0xFF1E293B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E7FF))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${bids.size}",
                        color = PolishBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                text = "Los profesionales han enviado sus propuestas.",
                color = Color(0xFF64748B),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 46.dp, bottom = 12.dp)
            )

            if (bids.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = PolishCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PolishBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Esperando ofertas",
                            color = Color(0xFF1E293B),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Profesionales cercanos están revisando tu solicitud. Recibirás contraofertas de precios en un momento.",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                bids.forEachIndexed { index, bid ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .border(1.dp, PolishBorder, RoundedCornerShape(16.dp))
                            .testTag("bid_card_${bid.id}")
                            .clickable { showProfileDetailForBid = bid }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                val docModel = remember(bid.professionalPhoto) {
                                    if (bid.professionalPhoto.isNullOrEmpty()) {
                                        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop"
                                    } else if (bid.professionalPhoto.startsWith("data:image/")) {
                                        try {
                                            val base64Data =
                                                bid.professionalPhoto.substringAfter("base64,")
                                            val decodedBytes = android.util.Base64.decode(
                                                base64Data,
                                                android.util.Base64.DEFAULT
                                            )
                                            android.graphics.BitmapFactory.decodeByteArray(
                                                decodedBytes,
                                                0,
                                                decodedBytes.size
                                            )
                                        } catch (e: Exception) {
                                            bid.professionalPhoto
                                        }
                                    } else {
                                        bid.professionalPhoto
                                    }
                                }
                                AsyncImage(
                                    model = docModel,
                                    contentDescription = bid.professionalName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(52.dp).clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = bid.professionalName,
                                        fontWeight = FontWeight.Bold,
                                        color = PolishDark,
                                        fontSize = 15.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = String.format(Locale.US, "%.1f", bid.professionalRating),
                                            color = PolishDark,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFE0E7FF))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = PolishBlue,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Profesional verificado",
                                            color = PolishBlue,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Hace ${formatTimestamp(bid.createdAt)}",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            softWrap = false
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = bid.comment,
                                        color = Color(0xFF64748B),
                                        fontSize = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "$${
                                            NumberFormat.getNumberInstance(Locale.US)
                                                .format(bid.amount)
                                        }",
                                        fontWeight = FontWeight.Black,
                                        color = PolishDark,
                                        fontSize = 20.sp,
                                        modifier = Modifier.testTag("bid_price_${bid.id}")
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "COP",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B),
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                                if (index == 0) {
                                    Button(
                                        onClick = { showProfileDetailForBid = bid },
                                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text("Ver oferta", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { showProfileDetailForBid = bid },
                                        border = BorderStroke(1.dp, PolishBlue),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PolishBlue),
                                        shape = RoundedCornerShape(20.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text("Ver oferta", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Status tags for counters
                            if (bid.status == "COUNTERED_BY_CLIENT") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF2196F3).copy(alpha = 0.1f))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Les hiciste una contraoferta. Esperando respuesta...",
                                        color = Color(0xFF2196F3),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (bid.status == "COUNTERED_BY_PROFESSIONAL") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PolishGreen.copy(alpha = 0.15f))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "¡El profesional contraofertó! Revisa su nueva propuesta.",
                                        color = PolishGreen,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF2FF))
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).background(Color(0xFFC7D2FE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Consejo",
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Revisa los perfiles, calificaciones y comentarios antes de aceptar una oferta.",
                        color = Color(0xFF475569),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
        if (showProfileDetailForBid != null) {
            val currentBid = showProfileDetailForBid!!
            ProfessionalProfileDetailSheet(
                bid = currentBid,
                viewModel = viewModel,
                onDismiss = { showProfileDetailForBid = null },
                onAccept = {
                    viewModel.acceptBid(currentBid.id)
                    viewModel.showAlert(
                        "Aviso",
                        "¡Cotización aceptada!"
                    ); onBidAccepted(currentBid.jobId)
                    showProfileDetailForBid = null
                },
                onReject = {
                    viewModel.rejectBid(currentBid.id)
                    viewModel.showAlert("Aviso", "Oferta rechazada")
                    showProfileDetailForBid = null
                },
                onCounter = {
                    targetingBidForCounter = currentBid
                    counterAmountInput = com.galibu.core.ui.utils.PriceUtils.formatThousands(
                        currentBid.amount.toInt().toString()
                    )
                    showCounterDialog = true
                    showProfileDetailForBid = null
                }
            )
        }

        if (showDirectCounterDialog) {
            ModalBottomSheet(
                onDismissRequest = { showDirectCounterDialog = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    Text(
                        text = "Negociar Tarifa",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Ingresa tu nueva propuesta por hora para el profesional.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = com.galibu.core.ui.utils.PriceUtils.formatThousands(
                            directCounterAmountInput
                        ),
                        onValueChange = { input ->
                            val clean = input.filter { it.isDigit() }
                            if (clean.length <= 9) directCounterAmountInput = clean
                        },
                        label = { Text("Nueva tarifa por hora (COP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PolishBlue,
                            focusedLabelColor = PolishBlue
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDirectCounterDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                val amount = com.galibu.core.ui.utils.PriceUtils.cleanPrice(
                                    directCounterAmountInput
                                ).toDoubleOrNull() ?: 0.0
                                if (amount > 0.0) {
                                    selectedJob?.let { activeJob ->
                                        viewModel.submitCounterOfferDirectHire(
                                            activeJob.id,
                                            amount,
                                            isClient = true
                                        ) {
                                            viewModel.showAlert(
                                                "Aviso",
                                                "¡Contraoferta enviada exitosamente!"
                                            )
                                        }
                                    }
                                }
                                showDirectCounterDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.4f)
                        ) {
                            Text(
                                "Proponer contraoferta",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (showClientRatingSheet && selectedJob != null) {
            ModalBottomSheet(
                onDismissRequest = { showClientRatingSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color.White
            ) {
                val activeJob = selectedJob ?: return@ModalBottomSheet
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    Text("Calificar al profesional", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PolishDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Comparte tu experiencia con ${activeJob.professionalName ?: "el profesional"}.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { clientRatingStars = star }) {
                                Icon(
                                    imageVector = if (star <= clientRatingStars) Icons.Default.Star else Icons.Default.StarOutline,
                                    contentDescription = null,
                                    tint = if (star <= clientRatingStars) Color(0xFFF59E0B) else Color(0xFFCBD5E1)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = clientRatingComment,
                        onValueChange = { clientRatingComment = it },
                        label = { Text("Comentario (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (clientRatingStars <= 0) {
                                viewModel.showAlert("Aviso", "Selecciona una calificación de 1 a 5 estrellas.")
                                return@Button
                            }
                            viewModel.submitRating(
                                activeJob.id,
                                clientRatingStars.toFloat(),
                                clientRatingComment.trim(),
                                isClientRating = true
                            )
                            showClientRatingSheet = false
                            clientRatingStars = 0
                            clientRatingComment = ""
                            viewModel.showAlert("Aviso", "¡Calificación enviada exitosamente!")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text("Enviar calificación", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // Modal Counter Offer picker (Redesigned as an gorgeous, premium Bottom Sheet)
        if (showCounterDialog) {
            ModalBottomSheet(
                onDismissRequest = { showCounterDialog = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Negociar Tarifa 🔄",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        IconButton(onClick = { showCounterDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color(0xFF64748B)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingresa un valor negociado inferior o superior. El profesional decidirá de forma inmediata.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Oferta del profesional:",
                            fontSize = 14.sp,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = "$${
                                NumberFormat.getNumberInstance(Locale.US)
                                    .format(targetingBidForCounter?.amount ?: 0.0)
                            } \nCOP",
                            color = Color(0xFF0F172A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Increments Quick adjusters
                    Text(
                        text = "Ajuste rápido:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(-10000, -5000, 5000, 10000).forEach { adjustment ->
                            OutlinedButton(
                                onClick = {
                                    val current = com.galibu.core.ui.utils.PriceUtils.cleanPrice(
                                        counterAmountInput
                                    ).toDoubleOrNull() ?: 15000.0
                                    val newVal = (current + adjustment).coerceAtLeast(10000.0)
                                    counterAmountInput =
                                        com.galibu.core.ui.utils.PriceUtils.formatThousands(
                                            newVal.toInt().toString()
                                        )
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = if (adjustment > 0) Color(0xFF059669) else Color(
                                        0xFFDC2626
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (adjustment > 0) Color(0xFF059669) else Color(
                                        0xFFDC2626
                                    )
                                ),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${if (adjustment > 0) "+$" else "-$"}${
                                        Math.abs(
                                            adjustment
                                        ) / 1000
                                    }.000",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = counterAmountInput,
                        onValueChange = { input ->
                            val clean = input.filter { it.isDigit() }
                            if (clean.length <= 9) {
                                counterAmountInput =
                                    com.galibu.core.ui.utils.PriceUtils.formatThousands(clean)
                            }
                        },
                        label = { Text("Tu Tarifa de Contraoferta (COP)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PolishBlue,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedTextColor = Color(0xFF1E293B),
                            unfocusedTextColor = Color(0xFF1E293B),
                            focusedLabelColor = PolishBlue,
                            unfocusedLabelColor = Color(0xFF64748B)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("counter_amount_input")
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // RateAssist Box (Clean, Slate-50 background)
                    val targetAmount =
                        com.galibu.core.ui.utils.PriceUtils.cleanPrice(counterAmountInput)
                            .toDoubleOrNull() ?: 20000.0
                    val categoryName = selectedJob?.category ?: "Servicios"
                    val isCompetitive = when {
                        targetAmount < 18000.0 -> "BAJA ⚠️ (Es probable que ignoren esta oferta)"
                        targetAmount in 18000.0..35000.0 -> "COMPETITIVA ✅ (Velocidad de respuesta óptima)"
                        else -> "PREMIUM ⭐ (Atracción inmediata de especialistas estrella)"
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "RateAssist (IA Analista)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Categoría: $categoryName | Oferta: $isCompetitive",
                                fontSize = 11.sp,
                                color = if (targetAmount < 18000.0) Color(0xFFD97706) else Color(
                                    0xFF059669
                                ),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "El promedio local en Bogotá para $categoryName oscila entre $20.000 y $35.000 COP dependiendo de la complejidad exacta.",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                lineHeight = 15.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Actions: Proponer and Cancelar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showCounterDialog = false },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(
                                    0xFF64748B
                                )
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Cancelar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Button(
                            onClick = {
                                val rawVal =
                                    com.galibu.core.ui.utils.PriceUtils.cleanPrice(counterAmountInput)
                                        .toDoubleOrNull() ?: 20000.0
                                val counterVal = if (rawVal < 1000.0) rawVal * 1000.0 else rawVal
                                targetingBidForCounter?.let { bid ->
                                    viewModel.proposeCounterOffer(bid.id, counterVal)
                                }
                                viewModel.showAlert(
                                    "Aviso",
                                    "¡Tu contraoferta ha sido enviada al profesional!"
                                ); showCounterDialog = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PolishBlue),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("submit_counter_offer")
                        ) {
                            Text(
                                text = "Proponer Contraoferta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (showPortfolioDialog) {
            val gallery = viewModel.getPortfolio(portfolioProId ?: "pro_125")
            AlertDialog(
                onDismissRequest = { showPortfolioDialog = false },
                confirmButton = {
                    Button(
                        onClick = { showPortfolioDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = PolishBlue)
                    ) {
                        Text("Cerrar", fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = PolishGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Portafolio de $portfolioProName 📂",
                            fontWeight = FontWeight.Bold,
                            color = PolishDark,
                            fontSize = 16.sp
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Trabajos y certificaciones verificadas:",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )

                        if (gallery.isEmpty()) {
                            Text(
                                text = "No se cargaron muestras de fotos previas.",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        gallery.forEach { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, PolishBorder, RoundedCornerShape(12.dp))
                            ) {
                                Column {
                                    AsyncImage(
                                        model = item.imageUrl,
                                        contentDescription = item.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                    )
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = PolishDark
                                        )
                                        Text(
                                            text = item.desc,
                                            fontSize = 10.sp,
                                            color = Color(0xFF64748B),
                                            lineHeight = 12.sp,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}


