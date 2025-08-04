package app.jetpack.jetpackfinal.ui.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val spotlightDataHolder: SpotlightDataHolder
) : ViewModel(){
    val spotlightData = spotlightDataHolder.spotlightData
    fun getCoinDetails(coinId: String): StateFlow<SpotlightData?> =
        spotlightDataHolder.spotlightData
            .map { list ->
                list.find { it.staticData.id == coinId }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = null
            )




}