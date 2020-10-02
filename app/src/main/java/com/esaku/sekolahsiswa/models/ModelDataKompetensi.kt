package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelDataKompetensi(
	@field:SerializedName("nama_kd")
	val namaKd: String? = null,

	@field:SerializedName("tgl_input")
	val tglInput: String? = null,

	@field:SerializedName("file_dok")
	val fileDok: String? = null,

	@field:SerializedName("no_bukti")
	val noBukti: String? = null,

	@field:SerializedName("pelaksanaan")
	val pelaksanaan: String? = null,

	@field:SerializedName("kode_kd")
	val kodeKd: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("minggu")
	val minggu: String? = null,

	@field:SerializedName("periode")
	val periode: String? = null
)
