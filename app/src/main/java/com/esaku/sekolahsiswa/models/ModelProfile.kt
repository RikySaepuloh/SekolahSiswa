package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

class ModelProfile(

    @SerializedName("path_view")
    val pathView: String? = null,

    @SerializedName("kode_klp_menu")
    val kodeKlpMenu: String? = null,

    @SerializedName("kode_lokasi")
    val kodeLokasi: String? = null,

    @SerializedName("kode_kelas")
    val kelas: String? = null,

    @SerializedName("jabatan")
    val jabatan: String? = null,

    @SerializedName("tgl_lahir")
    val tglLahir: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nmlok")
    val nmlok: String? = null,

    @SerializedName("kode_pp")
    val kodePp: String? = null,

    @SerializedName("nis")
    val nis: String? = null,

    @SerializedName("nik")
    val nik: String? = null,

    @SerializedName("nik2")
    val nik2: String? = null,

    @SerializedName("kode_lokkonsol")
    val kodeLokkonsol: String? = null,

    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("foto")
    val foto: Any? = null,

    @SerializedName("kode_bidang")
    val kodeBidang: String? = null,

    @SerializedName("nama_pp")
    val namaPp: String? = null,

    @SerializedName("logo")
    val logo: String? = null,

    @SerializedName("no_telp")
    val noTelp: String? = null,

    @SerializedName("status_admin")
    val statusAdmin: String? = null,

    @SerializedName("klp_akses")
    val klpAkses: String? = null)