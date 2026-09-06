package com.galibu.professional

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.zIndex
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galibu.professional.ui.screens.*
import com.galibu.professional.ui.viewmodels.ProfessionalViewModel
import com.galibu.core.ui.theme.*
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val viewModel = ViewModelProvider(this)[ProfessionalViewModel::class.java]

        setContent {
            GalibuTheme {
                val currentUser by viewModel.currentUser.collectAsState()
                val activeRole by viewModel.activeRole.collectAsState()
                val activeNotification by viewModel.activeBannerNotification.collectAsState()
                val isGlobalLoading by viewModel.isGlobalLoading.collectAsState()
                val globalLoadingMessage by viewModel.globalLoadingMessage.collectAsState()
                val globalAlert by viewModel.globalAlert.collectAsState()

                var currentRoute by rememberSaveable { mutableStateOf("AUTH") }
                var selectedTab by rememberSaveable { mutableStateOf("HOME") }

                LaunchedEffect(activeNotification) {
                    if (activeNotification != null) {
                        kotlinx.coroutines.delay(5000)
                        viewModel.activeBannerNotification.value = null
                    }
                }

                LaunchedEffect(currentUser) {
                    if (currentUser == null) {
                        currentRoute = "AUTH"
                    } else if (currentRoute == "AUTH") {
                        currentRoute = "PROFESSIONAL_DASHBOARD"
                    }
                }

                var showSplash by rememberSaveable { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showSplash = false
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (showSplash) Color.White else DarkGrayBg
                ) {
                    if (showSplash) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_app_logo_1781899012982),
                                    contentDescription = "Logo Galibu",
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Galibu", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PolishBlue)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Profesionales", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                            ) { innerPadding ->
                                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                                    when (currentRoute) {
                                        "AUTH" -> AuthScreen(
                                            viewModel = viewModel,
                                            onAuthSuccess = { currentRoute = "PROFESSIONAL_DASHBOARD" }
                                        )
                                        "PROFESSIONAL_DASHBOARD" -> ProfessionalDashboardScreen(
                                            viewModel = viewModel,
                                            onNavigateToVerification = { currentRoute = "PROFESSIONAL_VERIFICATION" },
                                            onNavigateToChat = { jobId ->
                                                viewModel.selectJob(jobId)
                                                currentRoute = "CHAT"
                                            },
                                            onNavigateToNegotiation = { jobId ->
                                                viewModel.selectJob(jobId)
                                                currentRoute = "NEGOTIATION"
                                            }
                                        )
                                        "PROFESSIONAL_VERIFICATION" -> ProfessionalVerificationScreen(
                                            viewModel = viewModel,
                                            modifier = Modifier.padding(bottom = 0.dp),
                                            onBack = { currentRoute = "PROFESSIONAL_DASHBOARD" }
                                        )
                                        "NEGOTIATION" -> PriceNegotiationScreen(
                                            viewModel = viewModel,
                                            onBidAccepted = { jobId ->
                                                viewModel.selectJob(jobId)
                                                currentRoute = "CHAT"
                                            },
                                            onBack = { currentRoute = "PROFESSIONAL_DASHBOARD" }
                                        )
                                        "CHAT" -> ChatAndPaymentsScreen(
                                            viewModel = viewModel,
                                            onBack = { currentRoute = "PROFESSIONAL_DASHBOARD" }
                                        )
                                        "NOTIFICATIONS_HISTORY" -> NotificationsAndHistoryScreen(viewModel = viewModel)
                                        "PROFILE" -> ProfileScreen(
                                            viewModel = viewModel,
                                            onNavigateToNotifications = {
                                                selectedTab = "ALERTS"
                                                currentRoute = "NOTIFICATIONS_HISTORY"
                                            }
                                        )
                                    }
                                }
                            }

                            // Global Loading Overlay
                            androidx.compose.animation.AnimatedVisibility(
                                visible = isGlobalLoading,
                                enter = androidx.compose.animation.fadeIn(),
                                exit = androidx.compose.animation.fadeOut()
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color(0x990F172A)).clickable(enabled = true, onClick = {}).zIndex(99999f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                        modifier = Modifier.width(280.dp).padding(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator(color = PolishBlue, strokeWidth = 3.dp, modifier = Modifier.size(48.dp))
                                            Spacer(modifier = Modifier.height(20.dp))
                                            Text(text = globalLoadingMessage ?: "PROCESANDO...", color = PolishNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                            }

                            // Global Alert Dialog
                            globalAlert?.let { alert ->
                                AlertDialog(
                                    onDismissRequest = { viewModel.dismissAlert() },
                                    containerColor = Color.White,
                                    title = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (alert.isError) Icons.Default.Warning else Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (alert.isError) Color.Red else Color(0xFF059669),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = alert.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PolishNavy)
                                        }
                                    },
                                    text = { Text(text = alert.message, fontSize = 14.sp, color = PolishGrayText) },
                                    confirmButton = {
                                        Button(onClick = { viewModel.dismissAlert() }, colors = ButtonDefaults.buttonColors(containerColor = PolishBlue)) {
                                            Text("Entendido", color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

