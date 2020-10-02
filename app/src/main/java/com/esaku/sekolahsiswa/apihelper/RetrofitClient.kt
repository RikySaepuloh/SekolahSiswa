package com.esaku.sekolahsiswa.apihelper

import android.content.Context
import com.esaku.sekolahsiswa.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitClient {
    var retrofit: Retrofit? = null
    var preferences  = Preferences()

    fun getClient(baseUrl: String?,context: Context): Retrofit? {
        preferences.setPreferences(context)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val intercept: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "bearer "+ preferences.getToken()
                    )
                    .build()
                return chain.proceed(newRequest)
            }
        }
        val client =
            OkHttpClient.Builder().addInterceptor(intercept).addInterceptor(interceptor)
                .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        return retrofit
    }

}