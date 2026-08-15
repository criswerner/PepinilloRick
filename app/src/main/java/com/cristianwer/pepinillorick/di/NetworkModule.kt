package com.cristianwer.pepinillorick.di

import android.content.Context
import com.cristianwer.platform.network.provider.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun provideNetworkClient(@ApplicationContext context: Context): NetworkClient {
        return NetworkClient.Builder(BASE_URL)
            .setDebug(true)
            .enableCache(context)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(networkClient: NetworkClient): Retrofit {
        return networkClient.retrofit
    }
}
