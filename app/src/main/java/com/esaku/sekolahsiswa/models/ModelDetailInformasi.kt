package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelDetailInformasi(

	@field:SerializedName("file_dok")
	val fileDok: MutableList<FileDokItem>,

	@field:SerializedName("tgl_input")
	val tglInput: String? = null,

	@field:SerializedName("no_bukti")
	val noBukti: String? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("ref3")
	val ref3: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("ref2")
	val ref2: String? = null,

	@field:SerializedName("ref1")
	val ref1: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null
)

data class FileDokItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
