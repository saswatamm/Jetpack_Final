package app.jetpack.jetpackfinal.ui.home.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jetpack.jetpackfinal.ui.home.data.model.RecommendationData
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.data.repository.RecommendationRepository
import app.jetpack.jetpackfinal.ui.home.data.repository.SpotlightDataHolder
import app.jetpack.jetpackfinal.ui.home.data.repository.SpotlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val spotlightRepository: SpotlightRepository,
    private val recommendationRepository: RecommendationRepository,
    private val spotlightDataHolder: SpotlightDataHolder
) : ViewModel() {

    // StateFlows for UI states
    private val _spotlightData = MutableStateFlow<SpotlightUiState>(SpotlightUiState.Initial)
    val spotlightData: StateFlow<SpotlightUiState> = _spotlightData.asStateFlow()

    private val _spotlightData1 = MutableStateFlow<List<SpotlightData>>(emptyList())
    val spotlightData1: StateFlow<List<SpotlightData>> = _spotlightData1.asStateFlow()

    private val _recommendationData = MutableStateFlow<RecommendationUiState>(RecommendationUiState.Initial)
    val recommendationData: StateFlow<RecommendationUiState> = _recommendationData.asStateFlow()

    // Caches
    private val spotlightCache = MutableStateFlow<List<SpotlightData>>(emptyList())
    private val recommendationCache = MutableStateFlow<List<RecommendationData>>(emptyList())

    // Coroutine exception handler
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        Log.e("HomeViewModel", "Coroutine exception: ", exception)
        viewModelScope.launch {
            handleError(exception)
        }
    }

    private val debounceJob = Job()
    private val debounceScope = CoroutineScope(Dispatchers.Default + debounceJob + errorHandler)

    init {
        viewModelScope.launch(errorHandler) {
            supervisorScope {
                // Launch both data fetches in parallel
                launch { fetchSpotlightData() }
                launch { fetchRecommendationData() }
            }
        }
    }

    private suspend fun fetchSpotlightData() {
        _spotlightData.value = SpotlightUiState.Loading

        // Use cache while fetching new data if available
        spotlightCache.value.takeIf { it.isNotEmpty() }?.let {
            _spotlightData.value = SpotlightUiState.Success(SpotlightSuccessData(it))
        }

        try {
            withContext(Dispatchers.IO) {
                val geckoIdResult = spotlightRepository.getSpotlightGeckoId()

                when {
                    geckoIdResult.isSuccess -> {
                        val geckoIds = geckoIdResult.getOrNull()?.data ?: emptyList()

                        withTimeout(10000) { // 10 seconds timeout
                            spotlightRepository.observeFirebaseData(geckoIds) { result ->
                                result.fold(
                                    onSuccess = { data ->
                                        viewModelScope.launch {
                                            _spotlightData.value = SpotlightUiState.Success(SpotlightSuccessData(data))
                                            _spotlightData1.value = data
                                            spotlightCache.value = data
                                            spotlightDataHolder.updateSpotlightData(data)
                                        }
                                    },
                                    onFailure = { error ->
                                        Log.e("SpotlightData", "Error fetching data", error)
                                        _spotlightData.value = SpotlightUiState.Error(
                                            error.message ?: "Firebase data fetch failed"
                                        )
                                    }
                                )
                            }
                        }
                    }
                    else -> {
                        val error = geckoIdResult.exceptionOrNull()
                        Log.e("SpotlightData", "Error fetching GeckoId", error)
                        _spotlightData.value = SpotlightUiState.Error(
                            error?.message ?: "GeckoId fetch failed"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SpotlightData", "Unexpected error", e)
            _spotlightData.value = SpotlightUiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    private suspend fun fetchRecommendationData() {
        _recommendationData.value = RecommendationUiState.Loading

        // Use cache while fetching new data if available
        recommendationCache.value.takeIf { it.isNotEmpty() }?.let {
            _recommendationData.value = RecommendationUiState.Success(RecommendationSuccessData(it))
        }

        try {
            withContext(Dispatchers.IO) {
                val geckoIdResult = recommendationRepository.getRecommendationGeckoId()

                when {
                    geckoIdResult.isSuccess -> {
                        val geckoIds = geckoIdResult.getOrNull()?.data ?: emptyList()

                        withTimeout(10000) { // 10 seconds timeout
                            recommendationRepository.observeFirebaseData(geckoIds) { result ->
                                result.fold(
                                    onSuccess = { data ->
                                        viewModelScope.launch {
                                            _recommendationData.value = RecommendationUiState.Success(
                                                RecommendationSuccessData(data)
                                            )
                                            recommendationCache.value = data
                                        }
                                    },
                                    onFailure = { error ->
                                        Log.e("RecommendationData", "Error fetching data", error)
                                        _recommendationData.value = RecommendationUiState.Error(
                                            error.message ?: "Firebase data fetch failed"
                                        )
                                    }
                                )
                            }
                        }
                    }
                    else -> {
                        val error = geckoIdResult.exceptionOrNull()
                        Log.e("RecommendationData", "Error fetching GeckoId", error)
                        _recommendationData.value = RecommendationUiState.Error(
                            error?.message ?: "GeckoId fetch failed"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RecommendationData", "Unexpected error", e)
            _recommendationData.value = RecommendationUiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    fun refreshData() {
        viewModelScope.launch(errorHandler) {
            supervisorScope {
                launch { fetchSpotlightData() }
                launch { fetchRecommendationData() }
            }
        }
    }

    fun removeCard(cardId: String) {
        _spotlightData1.update { currentList ->
            val removedCard = currentList.find { it.staticData.id == cardId }
            currentList
                .filterNot { it.staticData.id == cardId }
                .let {
                    if (removedCard != null) it + removedCard
                    else it
                }
        }
    }

    private suspend fun handleError(error: Throwable) {
        Log.e("HomeViewModel", "Error handled: ${error.message}", error)
        when (error) {
            is java.net.UnknownHostException -> {
                // Handle network connectivity issues
                _spotlightData.value = SpotlightUiState.Error("No internet connection")
                _recommendationData.value = RecommendationUiState.Error("No internet connection")
            }
            is java.net.SocketTimeoutException -> {
                // Handle timeout issues
                _spotlightData.value = SpotlightUiState.Error("Connection timed out")
                _recommendationData.value = RecommendationUiState.Error("Connection timed out")
            }
            else -> {
                // Handle other errors
                _spotlightData.value = SpotlightUiState.Error("An unexpected error occurred")
                _recommendationData.value = RecommendationUiState.Error("An unexpected error occurred")
            }
        }
    }

    override fun onCleared() {
        debounceJob.cancel()
        super.onCleared()
    }
}

sealed class SpotlightUiState {
    object Initial : SpotlightUiState()
    object Loading : SpotlightUiState()
    data class Success(val data: SpotlightSuccessData) : SpotlightUiState()
    data class Error(val message: String) : SpotlightUiState()
}

sealed class RecommendationUiState {
    object Initial : RecommendationUiState()
    object Loading : RecommendationUiState()
    data class Success(val data: RecommendationSuccessData) : RecommendationUiState()
    data class Error(val message: String) : RecommendationUiState()
}

data class RecommendationSuccessData(
    val recommendationData: List<RecommendationData>
)

data class SpotlightSuccessData(
    val spotlightData: List<SpotlightData>
)