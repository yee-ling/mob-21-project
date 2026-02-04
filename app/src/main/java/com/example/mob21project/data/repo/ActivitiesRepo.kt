package com.example.mob21project.data.repo

import com.example.mob21project.data.model.Activity
import com.example.mob21project.data.model.Booking
import com.example.mob21project.data.model.ClassDetails
import com.example.mob21project.data.model.ClassSession
import com.example.mob21project.data.model.FacilityDetails
import com.example.mob21project.data.model.User

interface ActivitiesRepo {
    // activity
    suspend fun addActivity(activity: Activity): String

    // class
    suspend fun getAllClassDetails(): List<ClassDetails>
    suspend fun getClassDetailsById(id: String): ClassDetails?
    suspend fun addClassDetails(classDetails: ClassDetails)

    // sessions
    suspend fun getActiveSessionsByClassId(classId: String): List<ClassSession>
    suspend fun getCancelledSessionsByClassId(classId: String): List<ClassSession>
    suspend fun addClassSession(classSession: ClassSession)
    suspend fun getClassSessionById(id: String): ClassSession?
    suspend fun cancelSession(id: String)

    // facility
    suspend fun addFacilityDetails(facilityDetails: FacilityDetails)
    suspend fun getAllFacilityDetails(): List<FacilityDetails>
    suspend fun getFacilityDetailsById(id: String): FacilityDetails?

    // bookings
    suspend fun addBooking(booking: Booking)
    suspend fun getFacilityBookingsByFacilityId(facilityId: String, date: Long): List<Booking>
    suspend fun getClassSessionBookingsBySessionId(sessionId: String, date: Long): List<Booking>
    suspend fun cancelBooking(id: String)

    // user
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(id: String): User?
    suspend fun addUser(user: User)
    suspend fun getConfirmedBookingsByCurrentUser(userId: String): List<Booking>
    suspend fun getCancelledBookingsByCurrentUser(userId: String): List<Booking>
    suspend fun isAdmin(id: String): Boolean

    companion object {
        private var instance: ActivitiesRepo? = null
        fun getInstance(): ActivitiesRepo {
            if (instance == null) {
                instance = ActivitiesRepoFireImpl()
            }
            return instance!!
        }
    }
}