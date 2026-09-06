package com.galibu.client.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galibu.core.data.model.AppNotification
import com.galibu.core.data.model.JobRequest
import com.galibu.core.ui.theme.*
import com.galibu.client.ui.viewmodels.ClientViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationsAndHistoryScreen(
    viewModel: ClientViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var selectedTab by remember { mutableStateOf("NOTIFICA") } // "NOTIFICA" or "HISTORIAL"

    val completedJobs = remember(allJobs, currentUser) {
        allJobs.filter {
            it.status == "COMPLETED" && (it.clientId == currentUser?.id || it.professionalId == currentUser?.id)
        }
    }

    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkGrayBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Toggle tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .background(CardSurface, RoundedCornerShape(12.dp))
                .border(1.dp, PolishBorder, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Button(
                onClick = { selectedTab = "NOTIFICA" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "NOTIFICA") PrimaryGreen else Color.Transparent,
                    contentColor = if (selectedTab == "NOTIFICA") Color.White else LightGray
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("notif_tab_btn")
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Push Alerts (${notifications.filter { !it.isRead }.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { selectedTab = "HISTORIAL" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "HISTORIAL") PrimaryGreen else Color.Transparent,
                    contentColor = if (selectedTab == "HISTORIAL") Color.White else LightGray
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .testTag("history_tab_btn")
            ) {
                Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Historial (${completedJobs.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (selectedTab == "NOTIFICA") {
            // Push Notification feed center
            Text(
                text = "Centro de Notificaciones Push 📣",
                color = OffWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (notifications.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = LightGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No hay notificaciones", color = OffWhite, fontWeight = FontWeight.Bold)
                        Text("Aquí se grabarán tus alertas push de presupuestos, verificaciones y pagos en tiempo real.", color = LightGray, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                notifications.forEach { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isRead) CardSurface.copy(alpha = 0.6f) else CardSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .border(1.dp, PolishBorder, RoundedCornerShape(12.dp))
                            .clickable { viewModel.markNotificationAsRead(item.id) }
                            .testTag("notif_item_${item.id}")
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (item.isRead) Color.Transparent else PolishGreen)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        fontWeight = if (item.isRead) FontWeight.Normal else FontWeight.Black,
                                        color = if (item.isRead) LightGray else OffWhite,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = dateFormatter.format(Date(item.timestamp)),
                                        fontSize = 9.sp,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.body,
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

        } else {
            // Completed services historical log
            Text(
                text = "Historial completo de Servicios",
                color = OffWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (completedJobs.isEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.HistoryToggleOff, contentDescription = null, tint = LightGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No hay registros completados", color = OffWhite, fontWeight = FontWeight.Bold)
                        Text("Los servicios pagados y calificados con éxito formarán parte de tu expediente en el histórico.", color = LightGray, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                completedJobs.forEach { job ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .border(1.dp, PolishBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(PrimaryGreen.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (job.category) {
                                                "Plomero" -> Icons.Default.WaterDrop
                                                "Electricista" -> Icons.Default.ElectricBolt
                                                "Mesero" -> Icons.Default.Restaurant
                                                else -> Icons.Default.DeliveryDining
                                            },
                                            contentDescription = null,
                                            tint = SecondaryLime,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(job.category, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Text(
                                    text = "$${NumberFormat.getNumberInstance(Locale.US).format(job.finalPrice ?: job.budgetMin)} COP",
                                    color = SecondaryLime,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = job.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OffWhite
                            )

                            Text(
                                text = "Profesional contratado: ${job.professionalName ?: "N/AP"}",
                                fontSize = 12.sp,
                                color = LightGray,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Text(
                                text = "Cliente: ${job.clientName}",
                                fontSize = 12.sp,
                                color = LightGray
                            )

                            // Star review results
                            if (job.completionRating != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PolishBg)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row {
                                        (1..5).forEach { i ->
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (i <= (job.completionRating ?: 5.0f)) PolishGreen else Color.LightGray.copy(alpha = 0.5f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "“${job.completionReview ?: "Excelente servicio"}”",
                                            fontSize = 11.sp,
                                            color = LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


