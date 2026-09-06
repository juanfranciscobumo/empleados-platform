package com.galibu.professional.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.galibu.core.data.model.*
import com.galibu.core.ui.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

class ProfessionalViewModel(application: Application) : SharedViewModel(application) {

    fun submitBid(jobId: String, amount: Double, comment: String, durationHours: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Enviando tu propuesta de servicio...")
                kotlinx.coroutines.delay(1000)
                repository.submitBid(jobId, amount, comment, durationHours)
            } catch (e: Exception) {
                Log.e("ProfessionalViewModel", "Falla enviar propuesta: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Oferta Guardada Localmente ⏳",
                    body = "Tu propuesta de $$amount COP se guardó localmente. Error de sincronización Firebase: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                onComplete()
                hideLoading()
            }
        }
    }

    fun submitVerification(docType: String, docBytes: ByteArray) {
        viewModelScope.launch {
            try {
                showLoading("Enviando y encriptando documentos de verificación...")
                repository.submitVerificationDocuments(docType, docBytes)
                
                viewModelScope.launch {
                    kotlinx.coroutines.delay(10000)
                    val user = repository.currentUser.value
                    if (user != null && user.verificationStatus == "PENDING") {
                        val verifiedUser = user.copy(verificationStatus = "VERIFIED")
                        repository.updateUserDirectly(verifiedUser)
                        
                        val successNotif = AppNotification(
                            userId = user.id,
                            title = "Identidad Verificada Exitosamente 🎉",
                            body = "El pipeline inteligente de Firebase procesó tu ID ($docType). Tu cuenta cuenta ahora con sello verificado y puedes ofertar."
                        )
                        database.appNotificationDao().insertNotification(successNotif)
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfessionalViewModel", "Falla verificación de documentos: ${e.localizedMessage}")
                val user = currentUser.value
                val warning = AppNotification(
                    userId = user?.id ?: "",
                    title = "Documentos Guardados Localmente ⏳",
                    body = "Tus documentos se guardaron para verificación local. Falló la subida remota a Firebase: ${e.localizedMessage}"
                )
                database.appNotificationDao().insertNotification(warning)
            } finally {
                hideLoading()
            }
        }
    }

    fun acceptDirectHire(jobId: String, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Aceptando contratación...")
                kotlinx.coroutines.delay(1000)
                repository.acceptDirectHire(jobId)
                hideLoading()
                onResult()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ProfessionalViewModel", "Error al aceptar contratación: ${e.localizedMessage}")
            }
        }
    }

    fun rejectDirectHire(jobId: String, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Rechazando contratación...")
                kotlinx.coroutines.delay(1000)
                repository.rejectDirectHire(jobId)
                hideLoading()
                onResult()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ProfessionalViewModel", "Error al rechazar contratación: ${e.localizedMessage}")
            }
        }
    }

    fun submitCounterOfferDirectHire(jobId: String, amount: Double, isClient: Boolean, onResult: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                showLoading("Enviando contraoferta...")
                kotlinx.coroutines.delay(1000)
                repository.submitCounterOfferDirectHire(jobId, amount, isClient)
                hideLoading()
                onResult()
            } catch (e: Exception) {
                hideLoading()
                Log.e("ProfessionalViewModel", "Error al enviar contraoferta directa: ${e.localizedMessage}")
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
                Log.e("ProfessionalViewModel", "Falla cancelar solicitud: ${e.localizedMessage}")
            } finally {
                hideLoading()
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
                Log.e("ProfessionalViewModel", "Falla aceptar propuesta: ${e.localizedMessage}")
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
                Log.e("ProfessionalViewModel", "Falla rechazar propuesta: ${e.localizedMessage}")
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
                Log.e("ProfessionalViewModel", "Falla contraoferta: ${e.localizedMessage}")
            } finally {
                hideLoading()
            }
        }
    }
}

