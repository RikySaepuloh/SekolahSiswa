package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelNotifikasi(


	@field:SerializedName("file_dok")
	val fileDok: String? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("no_bukti")
	val noBukti: String? = null,

	@field:SerializedName("sts_read_mob")
	val stsReadMob: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null
)