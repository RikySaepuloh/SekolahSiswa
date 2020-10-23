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

    override fun onResume() {
        super.onResume()
        initInfo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        .replace(R.id.frame_layout, PesanFragment("open_informasi")).commit()
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
        when {
            intent.hasExtra("open_notifikasi") -> {
                bottom_navigation.selectedItemId = R.id.menu_pesan
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.frame_layout, PesanFragment("open_notifikasi")).commit()
            }
            intent.hasExtra("open_informasi") -> {
                bottom_navigation.selectedItemId = R.id.menu_pesan
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.frame_layout, PesanFragment("open_informasi")).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.frame_layout, BerandaFragment()).commit()
            }
        }
        initInfo()
// An icon only badge will be displayed unless a number is set:
    }

    private fun initInfo() {
        val apiservice= UtilsApi().getAPIService(this@MainActivity)
        apiservice?.getInfo2()?.enqueue(object : Callback<ResponseBody?> {
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

                            var totalunread=0
                            for (i in 0 until data.size){
                                if(data[i].stsReadMob=="0"){
                                    totalunread+=1
                                }
                            }
                            if(totalunread==0){
                                badge.isVisible = false
                            }else{
                                badge.isVisible = true
                                badge.number=totalunread
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
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