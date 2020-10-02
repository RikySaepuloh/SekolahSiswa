package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

class ModelTahunAjaran(

    @SerializedName("kode_ta")
    val kodeTA: String? = null,

    @SerializedName("nama")
    val nama: String? = null

)