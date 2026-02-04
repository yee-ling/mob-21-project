package com.example.mob21project.data.repo

import com.example.mob21project.data.model.Activity
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.model.BookingStatus
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.ClassSession
import com.example.mob21project.data.model.ClassSessionStatus
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.model.Role
import com.example.mob21project.data.model.User
import com.example.mob21project.service.AuthService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ActivitiesRepoFireImpl: ActivitiesRepo {
    val db = FirebaseFirestore.getInstance()
    val authService = AuthService.getInstance()

    private val activitiesRef = db.collection("activities")
    private val usersRef = db.collection("users")
    private val classDetailsRef = db.collection("classDetails")
    private val classSessionsRef = db.collection("classSessions")
    private val facilityDetailsRef = db.collection("facilityDetails")
    private val bookingsRef = db.collection("bookings")

    // activity
    override suspend fun addActivity(activity: Activity): String {
        val docRef = activitiesRef.document() // generate ID
        val newActivity = activity.copy(id = docRef.id)
        docRef.set(newActivity).await()
        return docRef.id
    }

    // class
    override suspend fun addClassDetails(classDetails: ClassDetails) {
        val docRef = classDetailsRef.document()
        val newClass = classDetails.copy(id = docRef.id)
        docRef.set(newClass).await()
    }
    override suspend fun getAllClassDetails(): List<ClassDetails> {
        return try {
            val snapshot = classDetailsRef.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(ClassDetails::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    override suspend fun getClassDetailsById(id: String): ClassDetails? {
        val snapshot = classDetailsRef.document(id).get().await()
        return snapshot.toObject(ClassDetails::class.java)
            ?.copy(id = snapshot.id)
    }

    // sessions
    override suspend fun getActiveSessionsByClassId(classId: String): List<ClassSession> {
        val snapshot = classSessionsRef
            .whereEqualTo("classId", classId)
            .whereEqualTo("status", ClassSessionStatus.ACTIVE.name)
            .get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(ClassSession::class.java)
                ?.copy(id = it.id)
        }
    }
    override suspend fun getCancelledSessionsByClassId(classId: String): List<ClassSession> {
        val snapshot = classSessionsRef
            .whereEqualTo("classId", classId)
            .whereEqualTo("status", ClassSessionStatus.CANCELLED.name)
            .get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(ClassSession::class.java)
                ?.copy(id = it.id)
        }
    }
    override suspend fun addClassSession(classSession: ClassSession) {
        val docRef = classSessionsRef.document()
        val newSession = classSession.copy(id = docRef.id)
        docRef.set(newSession).await()
    }

    override suspend fun getClassSessionById(id: String): ClassSession? {
        val snapshot = classSessionsRef.document(id).get().await()
        return snapshot.toObject(ClassSession::class.java)
            ?.copy(id = snapshot.id)
    }

    override suspend fun cancelSession(id: String) {
        try {
            val updates = mutableMapOf<String, Any>(
                "status" to ClassSessionStatus.CANCELLED.name,
                "updatedAt" to System.currentTimeMillis(),
                "cancelledAt" to System.currentTimeMillis()
            )
            classSessionsRef.document(id)
                .update(updates)
                .await()
        } catch (e: kotlin.Exception) {
            e.printStackTrace()
        }
    }

    // facility
    override suspend fun addFacilityDetails(facilityDetails: FacilityDetails) {
        val docRef = facilityDetailsRef.document()
        val newFacility = facilityDetails.copy(id = docRef.id)
        docRef.set(newFacility).await()
    }
    override suspend fun getAllFacilityDetails(): List<FacilityDetails> {
        return try {
            val snapshot = facilityDetailsRef.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(FacilityDetails::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    override suspend fun getFacilityDetailsById(id: String): FacilityDetails? {
        val snapshot = facilityDetailsRef.document(id).get().await()
        if(!snapshot.exists()) return null

        return snapshot.toObject(FacilityDetails::class.java)
            ?.copy(id = snapshot.id)
    }

    override suspend fun addBooking(booking: Booking) {
        require(booking.isValid()) {"Booking must have either facilityId or sessionId"}
        val docRef = bookingsRef.document()
        val newBooking = booking.copy(id = docRef.id)
        docRef.set(newBooking).await()
    }

    // bookings
    override suspend fun getFacilityBookingsByFacilityId(facilityId: String, date: Long): List<Booking> {
        return try {
            val snapshot = bookingsRef
                .whereEqualTo("facilityId", facilityId)
                .whereEqualTo("date", date)
                .whereEqualTo("status", BookingStatus.CONFIRMED)
                .get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Booking::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getClassSessionBookingsBySessionId(sessionId: String, date: Long): List<Booking> {
        return try {
            val snapshot = bookingsRef
                .whereEqualTo("sessionId", sessionId)
                .whereEqualTo("date", date)
                .whereEqualTo("status", BookingStatus.CONFIRMED)
                .get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Booking::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun cancelBooking(id: String) {
        try {
            val updates = mutableMapOf<String, Any>(
                "status" to BookingStatus.CANCELLED.name,
                "updatedAt" to System.currentTimeMillis(),
                "cancelledAt" to System.currentTimeMillis()
            )
            bookingsRef.document(id)
                .update(updates)
                .await()
        } catch (e: kotlin.Exception) {
            e.printStackTrace()
        }
    }

    // user
    override suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = usersRef.get().await()
            snapshot.documents.mapNotNull {
                it.toObject(User::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getUserById(id: String): User? {
        val snapshot = usersRef.document(id).get().await()
        return snapshot.toObject(User::class.java)?.copy(id = snapshot.id)
    }
    override suspend fun addUser(user: User) {
        usersRef.document(user.id).set(user).await()
    }
    override suspend fun getConfirmedBookingsByCurrentUser(userId: String): List<Booking> {
        return try {
            val snapshot = bookingsRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", BookingStatus.CONFIRMED)
                .get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Booking::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    override suspend fun getCancelledBookingsByCurrentUser(userId: String): List<Booking> {
        return try {
            val snapshot = bookingsRef
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", BookingStatus.CANCELLED)
                .get().await()
            snapshot.documents.mapNotNull {
                it.toObject(Booking::class.java)
                    ?.copy(id = it.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    override suspend fun isAdmin(id: String): Boolean {
        return getUserById(id)?.role == Role.ADMIN
    }
}