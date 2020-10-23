package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelDetailMatpel(

	@field:SerializedName("success")
	val success: Success? = null
)

data class DataGuruItem(

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("nama_matpel")
	val namaMatpel: String? = null,

	@field:SerializedName("singkatan")
	val singkatan: String? = null,

	@field:SerializedName("nama_guru")
	val namaGuru: String? = null,

	@field:SerializedName("kode_matpel")
	val kodeMatpel: String? = null
)

data class PelaksanaanItem(

	@field:SerializedName("file_dok")
	val fileDok: String? = null,

	@field:SerializedName("sts_kkm")
	val stsKkm: String? = null,

	@field:SerializedName("no_bukti")
	val noBukti: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("pelaksanaan")
	val pelaksanaan: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("kode_kd")
	val kodeKd: String? = null,

	@field:SerializedName("kode_jenis")
	val kodeJenis: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("kkm")
	val kkm: String? = null
)

data class Success(

	@field:SerializedName("data_kompetensi")
	val dataKompetensi: List<DataKompetensiItem?>? = null,

	@field:SerializedName("data_ta")
	val dataTa: List<DataTaItem?>? = null,

	@field:SerializedName("data_guru")
	val dataGuru: List<DataGuruItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataTaItem(

	@field:SerializedName("kode_ta")
	val kodeTa: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null
)

data class DataKompetensiItem(

	@field:SerializedName("nama_kd")
	val namaKd: String? = null,

	@field:SerializedName("pelaksanaan")
	val pelaksanaan: MutableList<PelaksanaanItem>,

	@field:SerializedName("kode_kd")
	val kodeKd: String? = null
)
