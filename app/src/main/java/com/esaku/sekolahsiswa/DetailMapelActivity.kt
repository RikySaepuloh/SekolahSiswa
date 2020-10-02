package com.esaku.sekolahsiswa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.adapter.DataKompetensiAdapter
import com.esaku.sekolahsiswa.adapter.MapelAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelDataGuru
import com.esaku.sekolahsiswa.models.ModelDataKompetensi
import com.esaku.sekolahsiswa.models.ModelTahunAjaran
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_beranda.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class DetailMapelActivity : AppCompatActivity() {
    var preferences  = Preferences()
    lateinit var kodeMapel:String
    lateinit var namaMapel:String
    private lateinit var dataAdapter: DataKompetensiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        preferences.setPreferences(this)
        dataAdapter= DataKompetensiAdapter(this)
        kodeMapel=intent.getStringExtra("kode_mapel")
        namaMapel=intent.getStringExtra("nama_mapel")
        dataAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                if (dataAdapter.itemCount == 0) {
                    empty_view.visibility = View.VISIBLE
                    recyclerview.visibility = View.GONE
                } else {
                    empty_view.visibility = View.GONE
                    recyclerview.visibility = View.VISIBLE
                }

            }
        })
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = dataAdapter
        chip_group.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.chip_semua -> {
                    initDetailMapel(kodeMapel,"All",preferences.getKelas())
                }
                R.id.chip_ganjil ->{
                    initDetailMapel(kodeMapel,"1",preferences.getKelas())
                }
                R.id.chip_genap ->{
                    initDetailMapel(kodeMapel,"2",preferences.getKelas())
                }
            }
        }
        initDetailMapel(kodeMapel,"All",preferences.getKelas())
    }

    private fun initDetailMapel(kodeMapel: String, kodeSem: String,kodeKelas:String?) {
        val apiservice= UtilsApi().getAPIService(this@DetailMapelActivity)
        apiservice?.getMapelDetail(kodeMapel,kodeSem,kodeKelas)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val gson = Gson()
                            val myobj: JSONObject = obj.getJSONObject("success")
                            val type: Type = object :
                                TypeToken<ArrayList<ModelTahunAjaran?>?>() {}.type
                            val dataTahunAjaran: ArrayList<ModelTahunAjaran> =
                                gson.fromJson(myobj.optString("data_ta"), type)
                            val type2: Type = object :
                                TypeToken<ArrayList<ModelDataGuru?>?>() {}.type
                            val dataGuru: ArrayList<ModelDataGuru> =
                                gson.fromJson(myobj.optString("data_guru"), type2)
                            val type3: Type = object :
                                TypeToken<ArrayList<ModelDataKompetensi?>?>() {}.type
                            val dataKompetensi: ArrayList<ModelDataKompetensi> =
                                gson.fromJson(myobj.optString("data_kompetensi"), type3)
                            detail_tahun_ajaran.text = dataTahunAjaran[0].nama
                            detail_nama_guru.text = dataGuru[0].namaGuru
                            detail_nama_mapel.text = dataGuru[0].namaMapel
                            dataAdapter.initData(dataKompetensi)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(this@DetailMapelActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@DetailMapelActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@DetailMapelActivity, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    finishAffinity()
                    Toast.makeText(this@DetailMapelActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@DetailMapelActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404 || response.code() == 405){
                    Toast.makeText(this@DetailMapelActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@DetailMapelActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


}