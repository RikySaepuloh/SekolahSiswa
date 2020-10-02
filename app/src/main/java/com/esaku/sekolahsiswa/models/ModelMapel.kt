package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

class ModelMapel(

    @SerializedName("kode_matpel")
    val kodeMapel: String? = null,

    @SerializedName("nama")
    val namaMapel: String? = null

)