package com.esaku.sekolahsiswa.apihelper

import com.saku.inventaris.models.ModelLogin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface BaseApiService {

    @FormUrlEncoded
    @POST("login-siswa")
    fun
            login(
            @Field("nik") nik: String?,
            @Field("password") pass: String?): Call<ModelLogin?>?

    @GET("profile-siswa")
    fun getProfile(
    ): Call<ResponseBody?>?

    @GET("mata-pelajaran-detail")
    fun getMapelDetail(
        @Query("kode_matpel") kode_matpel: String?,
        @Query("kode_sem") kode_sem: String?,
        @Query("kode_kelas") kode_kelas: String?
    ): Call<ResponseBody?>?

    @GET("mata-pelajaran")
    fun getMapel(
        @Query("kode_kelas") kode_kelas: String?
    ): Call<ResponseBody?>?

}