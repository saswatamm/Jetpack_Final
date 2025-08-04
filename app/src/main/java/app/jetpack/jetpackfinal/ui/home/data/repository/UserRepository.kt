package app.jetpack.jetpackfinal.ui.home.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.jetpack.jetpackfinal.ui.home.data.model.CreateUserRequest
import app.jetpack.jetpackfinal.ui.home.domain.di.PreferencesKeys
import app.jetpack.jetpackfinal.ui.home.domain.network.ApiResponse
import app.jetpack.jetpackfinal.ui.home.domain.network.AuthApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>

) {

    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun createUser(): Result<ApiResponse> =
        withContext(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUser
                    ?: return@withContext Result.failure(Exception("User not authenticated"))
                val publicKey = dataStore.data.first()[PreferencesKeys.PUBLIC_KEY]
//                    ?: return@withContext Result.failure(Exception("Auth token not found"))


                val response = apiService.createUser(
                    CreateUserRequest(
                        email = currentUser.email!!,
                        walletPubKey = publicKey!!
                    )
                )

                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}