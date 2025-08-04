package app.jetpack.jetpackfinal.ui.home.domain.network

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("id_token") val idToken: String
)