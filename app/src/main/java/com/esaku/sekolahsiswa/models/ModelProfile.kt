package com.esaku.sekolahsiswa.models

import com.google.gson.annotations.SerializedName

data class ModelProfile(

    @field:SerializedName("status_login")
    val statusLogin: String? = null,

    @field:SerializedName("path_view")
    val pathView: String? = null,

    @field:SerializedName("jk")
    val jk: Any? = null,

    @field:SerializedName("kode_lokasi")
    val kodeLokasi: String? = null,

    @field:SerializedName("kode_klp_menu")
    val kodeKlpMenu: String? = null,

    @field:SerializedName("no_hp")
    val noHp: String? = null,

    @field:SerializedName("jabatan")
    val jabatan: String? = null,

    @field:SerializedName("agama")
    val agama: Any? = null,

    @field:SerializedName("kode_menu")
    val kodeMenu: String? = null,

    @field:SerializedName("tmp_lahir")
    val tmpLahir: Any? = null,

    @field:SerializedName("kode_pp")
    val kodePp: String? = null,

    @field:SerializedName("nik")
    val nik: String? = null,

    @field:SerializedName("nik2")
    val nik2: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("nis")
    val nis: String? = null,

    @field:SerializedName("nama_pp")
    val namaPp: String? = null,

    @field:SerializedName("logo")
    val logo: String? = null,

    @field:SerializedName("email")
    val email: Any? = null,

    @field:SerializedName("pass")
    val pass: String? = null,

    @field:SerializedName("nmlok")
    val nmlok: String? = null,

    @field:SerializedName("tgl_lahir")
    val tglLahir: String? = null,

    @field:SerializedName("kode_kelas")
    val kodeKelas: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("foto")
    val foto: String? = null,

    @field:SerializedName("background")
    val background: String? = null,

    @field:SerializedName("status_admin")
    val statusAdmin: String? = null
)
