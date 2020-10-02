package com.esaku.sekolahsiswa.apihelper

import android.content.Context

class UtilsApi {
    private val BASE_URL_API = "https://api.simkug.com/api/mobile-sekolah/"

    fun getAPIService(context:Context): BaseApiService? {
        return RetrofitClient().getClient(BASE_URL_API,context)?.create(BaseApiService::class.java)
    }
}