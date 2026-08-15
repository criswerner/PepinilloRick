package com.cristianwer.platform.network.provider

import android.content.Context
import com.cristianwer.platform.network.config.NetworkConfig.CACHE_SIZE_BYTES
import com.cristianwer.platform.network.config.NetworkConfig.CONNECT_TIMEOUT_SECONDS
import com.cristianwer.platform.network.config.NetworkConfig.READ_TIMEOUT_SECONDS
import com.cristianwer.platform.network.config.NetworkConfig.WRITE_TIMEOUT_SECONDS
import com.cristianwer.platform.network.interceptors.LoggingInterceptorFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class NetworkClient private constructor(
    val retrofit: Retrofit
) {

    /**
     * Helper reificado para Kotlin: permite llamar `client.createService<UserApiService>()`
     */
    inline fun <reified T> createService(): T = retrofit.create(T::class.java)

    /**
     * Mantiene la compatibilidad con el estilo Java
     */
    fun <T> createService(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    class Builder(private val baseUrl: String) {
        private var connectTimeout = CONNECT_TIMEOUT_SECONDS
        private var readTimeout = READ_TIMEOUT_SECONDS
        private var writeTimeout = WRITE_TIMEOUT_SECONDS
        private var isDebug = false
        private var tokenProvider: (() -> String?)? = null
        private var cacheDir: File? = null
        private var cacheSizeBytes = CACHE_SIZE_BYTES
        private val interceptors = mutableListOf<Interceptor>()

        fun setConnectTimeout(seconds: Long) = apply { this.connectTimeout = seconds }
        fun setReadTimeout(seconds: Long) = apply { this.readTimeout = seconds }
        fun setWriteTimeout(seconds: Long) = apply { this.writeTimeout = seconds }
        fun setDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
        fun setTokenProvider(provider: () -> String?) = apply { this.tokenProvider = provider }

        fun enableCache(context: Context, sizeInBytes: Long = CACHE_SIZE_BYTES) = apply {
            this.cacheDir = context.applicationContext.cacheDir
            this.cacheSizeBytes = sizeInBytes
        }

        fun addInterceptor(interceptor: Interceptor) = apply {
            this.interceptors.add(interceptor)
        }

        fun build(): NetworkClient {
            val okHttpBuilder = OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)

            // 1. Configuración de Caché
            cacheDir?.let { dir ->
                val httpCacheDirectory = File(dir, "http-cache")
                okHttpBuilder.cache(Cache(httpCacheDirectory, cacheSizeBytes))
            }

            // 2. Interceptor de Autenticación (si existe tokenProvider)
            tokenProvider?.let { provider ->
                okHttpBuilder.addInterceptor { chain ->
                    val original = chain.request()
                    val token = provider()

                    val requestBuilder = original.newBuilder()
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.header("Authorization", "Bearer $token")
                    }
                    chain.proceed(requestBuilder.build())
                }
            }

            // 3. Interceptores personalizados del usuario
            interceptors.forEach { okHttpBuilder.addInterceptor(it) }

            // 4. Interceptor de Logging (SIEMPRE al final para registrar la request completa)
            okHttpBuilder.addInterceptor(LoggingInterceptorFactory.create(isDebug))

            val client = okHttpBuilder.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return NetworkClient(retrofit)
        }
    }
}