package com.galibu.client.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.galibu.core.data.model.*
import com.galibu.core.ui.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

class ClientViewModel(application: Application) : SharedViewModel(application) {

    fun createJobRequest(category: String, title: String, description: String, budget: Double, address: String, department: String = "Bogotá D.C.", municipality: String = "Bogotá") {
        viewModelScope.launch {
            try {
                showLoading("Publicando solicitud de servicio en Galibu...")
                kotlinx.coroutines.delay(1200)
                repository.createJobRequest(category, title, description, budget, address, department, municipality)
            } catch (e: Exception) {
                Log.e("ClientViewModel", "Falla crear solicitud: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Trabajo Creado Localmente 🛠️",
                    body = "Tu solicitud se guardó en el dispositivo, pero no se sincronizó a Firebase debido a: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun createJobRequest(
        category: String,
        title: String,
        description: String,
        budget: Double,
        address: String,
        department: String = "Bogotá D.C.",
        municipality: String = "Bogotá",
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                showLoading("Publicando solicitud...")
                kotlinx.coroutines.delay(1000)
                repository.createJobRequest(category, title, description, budget, address, department, municipality)
                hideLoading()
                onSuccess()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ClientViewModel", "Error creando solicitud: ${e.localizedMessage}")
            }
        }
    }

    fun startChatWithProfessional(proId: String, proName: String, proSpecialty: String, onStarted: (String) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Iniciando chat...")
                val existing = allJobs.value.find { it.professionalId == proId && it.status != "COMPLETED" }
                if (existing != null) {
                    hideLoading()
                    onStarted(existing.id)
                } else {
                    val user = currentUser.value ?: return@launch
                    val jobId = "chat_${proId}_${System.currentTimeMillis()}"
                    val newJob = JobRequest(
                        id = jobId,
                        category = proSpecialty,
                        title = "Consulta sobre ${proSpecialty}",
                        description = "Chat de consulta directa con el profesional ${proName}.",
                        budgetMin = 0.0,
                        address = user.address ?: "Ubicación del cliente",
                        status = "NEGOTIATING",
                        clientId = user.id,
                        clientName = user.name,
                        professionalId = proId,
                        professionalName = proName,
                        department = user.department ?: "Bogotá D.C.",
                        municipality = user.municipality ?: "Bogotá"
                    )
                    repository.insertJobDirectly(newJob)
                    kotlinx.coroutines.delay(500)
                    hideLoading()
                    onStarted(jobId)
                }
            } catch (e: Exception) {
                hideLoading()
                Log.e("ClientViewModel", "Error iniciando chat: ${e.localizedMessage}")
            }
        }
    }

    fun hireProfessional(proId: String, proName: String, proSpecialty: String, hourlyRate: Double?, proBaseHourlyRate: Double? = null, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                showLoading("Enviando solicitud de contratación...")
                val user = currentUser.value ?: return@launch
                val existing = allJobs.value.find {
                    it.professionalId == proId &&
                    it.clientId == user.id &&
                    (it.status == "NEGOTIATING" || it.status == "OPEN")
                }
                val jobId = existing?.id ?: "job_hire_${proId}_${System.currentTimeMillis()}"
                val rate = hourlyRate ?: 35000.0
                val proProfile = repository.getUserById(proId)
                val baseHourlyRate = proBaseHourlyRate ?: proProfile?.hourlyRate ?: 35000.0
                val newJob = JobRequest(
                    id = jobId,
                    category = proSpecialty,
                    title = "Contratación de ${proSpecialty}",
                    description = "Solicitud de servicio directo con el profesional ${proName}. Tarifa propuesta: $${rate}/hora.",
                    budgetMin = rate,
                    address = user.address ?: "Calle 100 # 15-22",
                    status = "NEGOTIATING",
                    clientId = user.id,
                    clientName = user.name,
                    professionalId = proId,
                    professionalName = proName,
                    department = user.department ?: "Bogotá D.C.",
                    municipality = user.municipality ?: "Bogotá",
                    proHourlyRate = baseHourlyRate,
                    counterOfferAmount = null,
                    counterOfferSender = null
                )
                repository.insertJobDirectly(newJob)
                kotlinx.coroutines.delay(1000)
                hideLoading()
                onResult(jobId)
            } catch (e: Exception) {
                hideLoading()
                Log.e("ClientViewModel", "Error al contratar profesional: ${e.localizedMessage}")
            }
        }
    }

    fun acceptBid(bidId: String) {
        viewModelScope.launch {
            try {
                showLoading("Aceptando cotización de servicio...")
                kotlinx.coroutines.delay(1200)
                repository.acceptBid(bidId)
            } catch (e: Exception) {
                Log.e("ClientViewModel", "Falla aceptar propuesta: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Propuesta Aceptada Localmente ⏳",
                    body = "Se aceptó la propuesta de forma offline. Falló la sincronización con Firebase: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun rejectBid(bidId: String) {
        viewModelScope.launch {
            try {
                showLoading("Rechazando cotización de servicio...")
                kotlinx.coroutines.delay(1000)
                repository.rejectBid(bidId)
            } catch (e: Exception) {
                Log.e("ClientViewModel", "Falla rechazar propuesta: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Propuesta Rechazada Localmente ⏳",
                    body = "Se rechazó la propuesta de forma offline. Falló la sincronización con Firebase: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun proposeCounterOffer(bidId: String, amount: Double) {
        viewModelScope.launch {
            try {
                showLoading("Proponiendo nueva tarifa de negociación...")
                kotlinx.coroutines.delay(1000)
                repository.proposeCounterOffer(bidId, amount)
            } catch (e: Exception) {
                Log.e("ClientViewModel", "Falla contraoferta: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Contraoferta Guardada Localmente 🔄",
                    body = "Tu contraoferta de $$amount COP se guardó de forma offline. Falló sincronización Firebase: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun cancelJob(jobId: String, reason: String = "", onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Cancelando solicitud...")
                kotlinx.coroutines.delay(800)
                repository.cancelJob(jobId, reason)
                onComplete()
            } catch (e: Exception) {
                Log.e("ClientViewModel", "Falla cancelar solicitud: ${e.localizedMessage}")
            } finally {
                hideLoading()
            }
        }
    }

    fun acceptDirectHire(jobId: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Aceptando contratación directa...")
                kotlinx.coroutines.delay(1000)
                repository.acceptDirectHire(jobId)
                hideLoading()
                onComplete()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ClientViewModel", "Falla aceptar contratación: ${e.localizedMessage}")
                showAlert("Error", "No se pudo aceptar la contratación: ${e.localizedMessage}")
            }
        }
    }

    fun submitCounterOfferDirectHire(jobId: String, amount: Double, isClient: Boolean = true, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Enviando contraoferta...")
                kotlinx.coroutines.delay(1000)
                repository.submitCounterOfferDirectHire(jobId, amount, isClient)
                hideLoading()
                onComplete()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ClientViewModel", "Falla contraoferta directa: ${e.localizedMessage}")
                showAlert("Error", "No se pudo enviar la contraoferta: ${e.localizedMessage}")
            }
        }
    }
}

