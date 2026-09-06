package com.galibu.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "", // "CLIENT" or "PROFESSIONAL"
    val isVerified: Boolean = true,
    val rating: Float = 5.0f,
    val completedJobs: Int = 0,
    val balance: Double = 0.0,
    val documentType: String? = null,
    val documentPhotoSimulatedUrl: String? = null,
    val workCategory: String? = null,
    val verificationStatus: String = "VERIFIED",
    val password: String = "",
    val profilePhotoUrl: String? = null,
    val department: String? = "Bogotá D.C.",
    val municipality: String? = "Bogotá",
    val address: String? = "",
    val workPhotos: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val hourlyRate: Double? = null,
    val acceptedTerms: Boolean = false
) : Serializable

@Entity(tableName = "job_requests")
data class JobRequest(
    @PrimaryKey val id: String = "",
    val category: String = "",
    val title: String = "",
    val description: String = "",
    val budgetMin: Double = 0.0,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "",
    val clientId: String = "",
    val clientName: String = "",
    val professionalId: String? = null,
    val professionalName: String? = null,
    val finalPrice: Double? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completionRating: Float? = null,
    val completionReview: String? = null,
    val clientRatingOfPro: Float? = null,
    val clientReviewOfPro: String? = null,
    val proRatingOfClient: Float? = null,
    val proReviewOfClient: String? = null,
    val department: String? = "Bogotá D.C.",
    val municipality: String? = "Bogotá",
    val proHourlyRate: Double? = null,
    val counterOfferAmount: Double? = null,
    val counterOfferSender: String? = null
) : Serializable

@Entity(tableName = "bids")
data class Bid(
    @PrimaryKey val id: String = "",
    val jobId: String = "",
    val professionalId: String = "",
    val professionalName: String = "",
    val professionalRating: Float = 5.0f,
    val professionalPhoto: String = "",
    val amount: Double = 0.0,
    val comment: String = "",
    val durationHours: Int = 1,
    val status: String = "",
    val lastCounterOfferAmount: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val jobId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "text",
    val read: Boolean = false
) : Serializable

@Entity(tableName = "app_notifications")
data class AppNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String = "",
    val title: String = "",
    val body: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
) : Serializable

data class FirebaseDepartment(
    val name: String = "",
    val municipalities: List<String> = emptyList()
) : Serializable

data class FirebaseCategory(
    val name: String = ""
) : Serializable

