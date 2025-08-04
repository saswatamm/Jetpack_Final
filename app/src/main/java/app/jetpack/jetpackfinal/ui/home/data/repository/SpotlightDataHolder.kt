package app.jetpack.jetpackfinal.ui.home.data.repository

import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotlightDataHolder @Inject constructor() {
    private val _spotlightData = MutableStateFlow<List<SpotlightData>>(emptyList())
    val spotlightData: StateFlow<List<SpotlightData>> = _spotlightData.asStateFlow()

    fun updateSpotlightData(data: List<SpotlightData>) {
        _spotlightData.value = data
    }
}