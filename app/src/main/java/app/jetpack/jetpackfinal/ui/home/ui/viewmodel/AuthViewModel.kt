package app.jetpack.jetpackfinal.ui.home.ui.viewmodel

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jetpack.jetpackfinal.ui.home.data.repository.UserRepository
import app.jetpack.jetpackfinal.ui.home.domain.di.PreferencesKeys
import app.jetpack.jetpackfinal.utils.toCustomString
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.sol4k.Keypair
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
//            getAuthToken()
//            getSavedToken()
//            createWallet()
        }
    }


    suspend fun createWallet(email : String){
        val generatedKeypair = Keypair.generate()

        val secretKeyByteArray = generatedKeypair.secret
        val secretKey = secretKeyByteArray.toCustomString()
        val publicKeyByteArray = generatedKeypair.publicKey
        val publicKey = publicKeyByteArray.toString()

        saveTokenLocally(secretKey, publicKey, email)
    }


    suspend fun saveTokenLocally(privateKey: String, publicKey: String, authToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PRIVATE_KEY] = privateKey
            preferences[PreferencesKeys.PUBLIC_KEY] = publicKey
            preferences[PreferencesKeys.AUTH_TOKEN] = authToken
        }
    }


    fun handleCompleteSignInFlow(idToken: String) {
        Log.e("Id Token", "$idToken")
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // 1. Firebase Sign In
                val firebaseResult = repository.signInWithGoogle(idToken).getOrThrow()

                // 4. Create Wallet
                firebaseResult.email?.let { createWallet(it) }

                val walletResponse = repository.createUser().getOrThrow()

                // Success state with all data
                _uiState.value = AuthUiState.Success(
                    AuthSuccessData(
                        firebaseUser = firebaseResult,
                        //walletData = walletResponse
                    )
                )

            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Sign in failed")
            }
        }
    }

    fun getAuthToken(): Flow<String?> = dataStore.data
        .map { it[PreferencesKeys.AUTH_TOKEN] }
        .catch { emit(null) }
}











data class AuthSuccessData(
    val firebaseUser: FirebaseUser,
)

sealed class AuthUiState {
    object Initial : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val data: AuthSuccessData) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}