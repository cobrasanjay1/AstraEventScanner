package com.astra.eventscanner.data.api

import android.content.Context
import com.astra.eventscanner.BuildConfig
import com.astra.eventscanner.data.session.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var apiService: ApiService? = null

    fun getApiService(context: Context): ApiService {
        if (apiService == null) {
            val sessionManager = SessionManager(context)
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor(sessionManager))
                .build()

            val rawUrl = BuildConfig.API_BASE_URL.trim()
            val baseUrl = when {
                rawUrl.isNotBlank() && (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) -> {
                    if (rawUrl.endsWith("/")) rawUrl else "$rawUrl/"
                }
                else -> "https://astra-events.onrender.com/"
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
}
