package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

class ModelDataGuru(

    @SerializedName("nik")
    val nik: String? = null,
    @SerializedName("kode_matpel")
    val kodeMapel: String? = null,
    @SerializedName("nama_matpel")
    val namaMapel: String? = null,
    @SerializedName("nama_guru")
    val namaGuru: String? = null

)