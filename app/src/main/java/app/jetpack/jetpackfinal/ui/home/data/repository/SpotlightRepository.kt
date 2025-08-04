package app.jetpack.jetpackfinal.ui.home.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.jetpack.jetpackfinal.ui.home.data.model.SpotlightData
import app.jetpack.jetpackfinal.ui.home.domain.network.ApiResponseSpotlight
import app.jetpack.jetpackfinal.ui.home.domain.network.SpotlightApiService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotlightRepository @Inject constructor(
    private val apiService: SpotlightApiService,
    private val dataStore: DataStore<Preferences>,
    private val database : FirebaseDatabase
) {

    suspend fun getSpotlightGeckoId(): Result<ApiResponseSpotlight> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSpotlight()
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    fun observeFirebaseData(
        spotlightList: List<String>,
        callback: (Result<List<SpotlightData>>) -> Unit
    ) {
        val coinDetailsRef = database.reference.child("coinDetails")

        // Create a list to store all fetched data
        val fetchedDataList = mutableListOf<SpotlightData>()
        var completedRequests = 0

        spotlightList.forEach { id ->
            coinDetailsRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val spotlightData = snapshot.getValue(SpotlightData::class.java)

                    synchronized(fetchedDataList) {
                        spotlightData?.let { fetchedDataList.add(it) }
                        completedRequests++

                        // When all requests are completed, return the full list
                        if (completedRequests == spotlightList.size) {
                            callback(Result.success(fetchedDataList))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(Result.failure(error.toException()))
                }
            })
        }
    }
}