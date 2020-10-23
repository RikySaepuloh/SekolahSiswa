package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelInformasi2(

	@field:SerializedName("file_dok")
	val fileDok: String? = null,

	@field:SerializedName("no_bukti")
	val noBukti: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("sts_read_mob")
	val stsReadMob: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("nama_guru")
	val namaGuru: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("tipe")
	val tipe: String? = null,

	@field:SerializedName("nik_user")
	val nikUser: String? = null,

	@field:SerializedName("kode_matpel")
	val kodeMatpel: String? = null
)
