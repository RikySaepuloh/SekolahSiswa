package com.esaku.sekolahsiswa

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.adapter.DetailInformasiAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelDetailInformasi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.back
import kotlinx.android.synthetic.main.activity_detail.empty_view
import kotlinx.android.synthetic.main.activity_detail.recyclerview
import kotlinx.android.synthetic.main.activity_detail_informasi.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import kotlin.collections.ArrayList

class DetailInformasiActivity : AppCompatActivity() {
    var preferences  = Preferences()
    lateinit var kodeMapel:String
    lateinit var nama:String
    lateinit var namaGuru:String
    lateinit var nik:String
    lateinit var foto:String
    lateinit var noBukti:String
    private lateinit var dataAdapter: DetailInformasiAdapter
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"
    val library=Library()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_informasi)
        preferences.setPreferences(this)
        library.darkStatusBar(window,this)
//        val hey = getFileSizeOfUrl("https://api.simkug.com/api/mobile-sekolah/storage/5f8956551c9bb_jerry-wang-KV9F7Ypl2N0-unsplash.jpg")
//        Toast.makeText(this,hey.toString(),Toast.LENGTH_LONG).show()

        dataAdapter= DetailInformasiAdapter(this)
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
        val bundle = intent.extras
        if (bundle != null) {
//            var string = "Bundle{"
//            for (key in bundle.keySet()) {
//                string += " " + key + " => " + bundle[key] + ";"
//            }
//            string += " }Bundle"
//
//            Log.d(ContentValues.TAG, "BUNDLE: ${bundle.keySet()}")
//            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
            if(bundle.getString("key")!=null){
                var key: Map<String, Any> = HashMap()
                key = Gson().fromJson(bundle.getString("key"), key.javaClass)
                kodeMapel= key["kode_matpel"].toString()
                noBukti= key["no_bukti"].toString()
                nik= key["nik"].toString()
                initDetailMapel(kodeMapel,nik)
                sendStsRead(noBukti)
            }
        }
        if(intent.hasExtra("kode_matpel")){
            kodeMapel=intent.getStringExtra("kode_matpel")
//            nama=intent.getStringExtra("nama")
//            namaGuru=intent.getStringExtra("nama_guru")
            nik=intent.getStringExtra("nik")
            noBukti=intent.getStringExtra("no_bukti")
            initDetailMapel(kodeMapel,nik)
            sendStsRead(noBukti)
//            foto=intent.getStringExtra("foto")
//            Glide.with(this).load(link+foto).error(R.drawable.ic_user).into(detinfo_image)
//            detinfo_matpel.text = nama
//            detinfo_guru.text = namaGuru
        }
//        try {
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


        back.setOnClickListener {
            super.onBackPressed()
        }

    }

    fun getFileSizeOfUrl(url: String): Long {
        var urlConnection: URLConnection? = null
        try {
            val uri = URL(url)
            urlConnection = uri.openConnection()
            urlConnection!!.connect()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return urlConnection.contentLengthLong
            val contentLengthStr = urlConnection.getHeaderField("content-length")
            return if (contentLengthStr.isNullOrEmpty()) -1 else contentLengthStr.toLong()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        } finally {
            if (urlConnection is HttpURLConnection)
                urlConnection.disconnect()
        }
        return -1
    }

    private fun sendStsRead(noBukti:String) {
        val apiservice= UtilsApi().getAPIService(this)
        apiservice?.updateRead(noBukti)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
            }
        })
    }


    private fun initDetailMapel(kodeMapel: String,nikGuru:String) {
        val apiservice= UtilsApi().getAPIService(this@DetailInformasiActivity)
        apiservice?.getInfoDetail(kodeMapel,nikGuru)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val type: Type = object :
                                TypeToken<ArrayList<ModelDetailInformasi?>?>() {}.type
                            val dataTahunAjaran: ArrayList<ModelDetailInformasi> =
                                Gson().fromJson(obj.optString("data"), type)

                            val type2: Type = object :
                                TypeToken<ArrayList<ModelDetinfoGuru?>?>() {}.type
                            val dataGuru: ArrayList<ModelDetinfoGuru> =
                                Gson().fromJson(obj.optString("data_guru"), type2)

                            detinfo_matpel.text = dataGuru[0].namaMatpel
                            detinfo_guru.text = dataGuru[0].namaGuru
//                            detinfo_guru.text = obj.optString("nama_guru")
//                            detail_nama_guru.text = dataGuru[0].namaGuru
//                            detail_nama_mapel.text = dataGuru[0].namaMapel
                            dataTahunAjaran.reverse()
                            dataAdapter.initData(dataTahunAjaran)
                            recyclerview.scrollToPosition(dataTahunAjaran.size-1)
                            Glide.with(this@DetailInformasiActivity).load(link+dataGuru[0].foto).error(R.drawable.ic_user).into(detinfo_image)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(this@DetailInformasiActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@DetailInformasiActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@DetailInformasiActivity, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    finishAffinity()
                    Toast.makeText(this@DetailInformasiActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@DetailInformasiActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404 || response.code() == 405){
                    Toast.makeText(this@DetailInformasiActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@DetailInformasiActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


}