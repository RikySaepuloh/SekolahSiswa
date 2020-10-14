package com.esaku.sekolahsiswa

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.fragments.AkunFragment
import com.esaku.sekolahsiswa.fragments.BerandaFragment
import com.esaku.sekolahsiswa.fragments.PesanFragment
import com.esaku.sekolahsiswa.models.ModelInformasi
import com.google.android.material.badge.BadgeDrawable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    lateinit var badge:BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.frame_layout, BerandaFragment()).commit()
        bottom_navigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_beranda -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, BerandaFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_pesan -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, PesanFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_profile -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.frame_layout, AkunFragment()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
        badge = bottom_navigation.getOrCreateBadge(R.id.menu_pesan)
        badge.isVisible = true
        initInfo()
// An icon only badge will be displayed unless a number is set:
    }

    private fun initInfo() {
        val apiservice= UtilsApi().getAPIService(this@MainActivity)
        apiservice?.getInfo()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val gson = Gson()
//                            val myobj: JSONObject = obj.getJSONObject("success")
                            val type: Type = object :
                                TypeToken<ArrayList<ModelInformasi?>?>() {}.type
                            val data: ArrayList<ModelInformasi> =
                                gson.fromJson(obj.optString("data"), type)
                            badge.number=data.size

//                            val type2: Type = object :
//                                TypeToken<ArrayList<ModelDataGuru?>?>() {}.type
//                            val dataGuru: ArrayList<ModelDataGuru> =
//                                gson.fromJson(myobj.optString("data_guru"), type2)
//                            val type3: Type = object :
//                                TypeToken<ArrayList<ModelDataKompetensi?>?>() {}.type
//                            val dataKompetensi: ArrayList<ModelDataKompetensi> =
//                                gson.fromJson(myobj.optString("data_kompetensi"), type3)
//                            detail_tahun_ajaran.text = dataTahunAjaran[0].nama
//                            detail_nama_guru.text = dataGuru[0].namaGuru
//                            detail_nama_mapel.text = dataGuru[0].namaMapel
//                            dataAdapter.initData(data)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(this@MainActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@MainActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
//                    preferences.preferencesLogout()
                    finishAffinity()
//                    Toast.makeText(this@MainActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@MainActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404 || response.code() == 405){
                    Toast.makeText(this@MainActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Yakin ingin keluar?")
        builder.setCancelable(true)
        builder.setPositiveButton("Ya") { _, _ -> super.onBackPressed() }
        builder.setNegativeButton("Tidak") { _, _ ->

        }
        val dialog = builder.create()
        dialog.show()
    }
}