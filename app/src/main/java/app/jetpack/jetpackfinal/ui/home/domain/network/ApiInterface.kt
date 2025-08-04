package app.jetpack.jetpackfinal.ui.home.domain.network

import app.jetpack.jetpackfinal.ui.home.data.model.CreateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("createUser")
    suspend fun createUser(@Body request: CreateUserRequest): Response<ApiResponse>
}
interface SpotlightApiService{
    @GET("spotlight")
    suspend fun getSpotlight() : Response<ApiResponseSpotlight>
}
interface RecommendationApiService {
    @GET("recommendations")
    suspend fun getRecommendation(): Response<ApiResponseRecommedation>
}
