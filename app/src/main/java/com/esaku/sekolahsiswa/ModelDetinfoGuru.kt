package com.esaku.sekolahsiswa

import com.google.gson.annotations.SerializedName

data class ModelDetinfoGuru(


	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("nama_matpel")
	val namaMatpel: String? = null,

	@field:SerializedName("singkatan")
	val singkatan: String? = null,

	@field:SerializedName("nama_guru")
	val namaGuru: String? = null,

	@field:SerializedName("kode_matpel")
	val kodeMatpel: String? = null
)
