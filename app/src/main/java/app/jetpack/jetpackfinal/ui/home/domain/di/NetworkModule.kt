package app.jetpack.jetpackfinal.ui.home.domain.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.jetpack.jetpackfinal.ui.home.domain.network.AuthApiService
import app.jetpack.jetpackfinal.ui.home.domain.network.RecommendationApiService
import app.jetpack.jetpackfinal.ui.home.domain.network.SpotlightApiService
import app.jetpack.jetpackfinal.utils.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase = FirebaseDatabase.getInstance()


    @Provides
    @Singleton
    fun provideApiService(): AuthApiService {
        return Retrofit.Builder()
            .baseUrl("https://createuser-5m3ta2ltgq-uc.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotlightService(): SpotlightApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotlightApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecommendationService() : RecommendationApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecommendationApiService::class.java)
    }



    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir, "user_prefs.preferences_pb") }
        )

}