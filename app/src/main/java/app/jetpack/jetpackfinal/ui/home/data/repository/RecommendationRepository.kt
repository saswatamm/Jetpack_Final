package app.jetpack.jetpackfinal.ui.home.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.jetpack.jetpackfinal.ui.home.data.model.RecommendationData
import app.jetpack.jetpackfinal.ui.home.domain.network.ApiResponseRecommedation
import app.jetpack.jetpackfinal.ui.home.domain.network.RecommendationApiService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val apiService: RecommendationApiService,
    private val dataStore: DataStore<Preferences>,
    private val database : FirebaseDatabase
) {

    suspend fun getRecommendationGeckoId(): Result<ApiResponseRecommedation> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecommendation()
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
        recommendationList: List<String>,
        callback: (Result<List<RecommendationData>>) -> Unit
    ) {
        val coinDetailsRef = database.reference.child("coinDetails")

        val fetchedDataList = mutableListOf<RecommendationData>()
        var completedRequests = 0

        recommendationList.forEach { id ->
            coinDetailsRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val recommendationData = snapshot.getValue(RecommendationData::class.java)

                    synchronized(fetchedDataList) {
                        recommendationData?.let { fetchedDataList.add(it) }
                        completedRequests++

                        if (completedRequests == recommendationList.size) {
                            // Ensure all items are returned
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