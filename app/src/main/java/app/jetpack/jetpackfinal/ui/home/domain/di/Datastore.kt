package app.jetpack.jetpackfinal.ui.home.domain.di

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val AUTH_TOKEN = stringPreferencesKey("google_id")
    val PRIVATE_KEY = stringPreferencesKey("private_key")
    val PUBLIC_KEY = stringPreferencesKey("public_key")
}
