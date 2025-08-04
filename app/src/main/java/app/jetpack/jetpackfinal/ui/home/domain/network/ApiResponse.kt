package app.jetpack.jetpackfinal.ui.home.domain.network

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val error: ApiError? = null
)
data class ApiError(
    val code: Int,
    val message: String
)

data class ApiResponseSpotlight(
    val success : Boolean,
    val data : List<String>
)
data class ApiResponseRecommedation(
    val success : Boolean,
    val data : List<String>
)

data class EmptyResponse(
    val success : Boolean,
    val message : String
)