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
            @Field("password") pass: String?,
            @Field("id_device") id_device: String?

    ): Call<ModelLogin?>?

//    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @PUT("update-status-read")
    fun updateRead(
        @Field("no_pesan") no_bukti: String?
//        @Field("password") pass: String?,
//        @Field("id_device") id_device: String?
    ): Call<ResponseBody>

    @GET("profile-siswa")
    fun getProfile(
    ): Call<ResponseBody?>?

    @GET("mata-pelajaran-detail")
    fun getMapelDetail(
        @Query("kode_matpel") kode_matpel: String?,
        @Query("kode_sem") kode_sem: String?,
        @Query("kode_kelas") kode_kelas: String?
    ): Call<ResponseBody?>?

    @GET("info")
    fun getInfo(
    ): Call<ResponseBody?>?

    @GET("info2")
    fun getInfo2(
    ): Call<ResponseBody?>?

    @GET("info2-detail")
    fun getInfoDetail(
        @Query("kode_matpel") kode_matpel: String?,
        @Query("nik_guru") nik_guru: String?,
        ): Call<ResponseBody?>?

    @GET("notif")
    fun getNotif(
    ): Call<ResponseBody?>?

    @GET("mata-pelajaran")
    fun getMapel(
        @Query("kode_kelas") kode_kelas: String?
    ): Call<ResponseBody?>?

}