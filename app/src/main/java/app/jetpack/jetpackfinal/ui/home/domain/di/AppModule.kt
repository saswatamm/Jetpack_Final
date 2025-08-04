package app.jetpack.jetpackfinal.ui.home.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder().callTimeout(300, TimeUnit.SECONDS).retryOnConnectionFailure(true).connectTimeout(300, TimeUnit.SECONDS).build()
    }
}