package com.example.mob21project.service

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.mob21project.data.model.AuthUser
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

@Singleton
class AuthService @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _authUser = MutableStateFlow<AuthUser?>(null)
    val authUser = _authUser.asStateFlow()

    init {
        firebaseAuth.currentUser?.let { updateUser(it) }
    }

    private fun updateUser(firebaseUser: FirebaseUser) {
        _authUser.update {
            AuthUser(
                uid = firebaseUser.uid,
                displayName = firebaseUser.displayName ?: "Unknown",
                email = firebaseUser.email ?: "Unknown",
                photoUrl = firebaseUser.photoUrl.toString()
            )
        }
    }

    suspend fun updateAuthProfilePhoto(photoUrl: String) {
        val user = firebaseAuth.currentUser ?: return

        val request = userProfileChangeRequest {
            photoUri = photoUrl.toUri()
        }

        user.updateProfile(request).await()

        // keep StateFlow in sync
        updateUser(user)
    }

    // Email
    suspend fun signUpWithEmail(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let { updateUser(it) }
        return result.user
    }

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser? {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        result.user?.let { updateUser(it) }
        return result.user
    }

    // Google
    suspend fun signInWithGoogle(context:Context): Boolean {
        return try {
            val token = getGoogleCredential(context)
            val credential = GoogleAuthProvider.getCredential(token,null)
            val result = firebaseAuth.signInWithCredential(credential).await()

            result.user?.let { updateUser(it)}
            result.user != null
        } catch (e:GetCredentialCancellationException){
            Log.d("AuthService","Sign-in was cancelled",e)
            false
        } catch (e: GetCredentialException){
            Log.d("AuthService","Google Sign-in failed",e)
            false
        } catch (e:Exception){
            Log.d("AuthService","Sign-in failed",e)
            false
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _authUser.value = null
    }

    private suspend fun getGoogleCredential(context: Context): String? {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("309538088888-kmgvbnie387s55spmmbpnd30njt9jko6.apps.googleusercontent.com")
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            result.credential.data.getString(
                "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun getCurrentUser(): AuthUser? {
        return authUser.value
    }
    fun getCurrentUid(): String? = firebaseAuth.currentUser?.uid
    companion object {
        private var instance: AuthService?=null
        fun getInstance(): AuthService{
            if(instance==null) {
                instance = AuthService()
            }
            return instance!!
        }
    }
}