package com.esaku.sekolahsiswa

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.adapter.DataKompetensiAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.DataKompetensiItem
import com.esaku.sekolahsiswa.models.ModelDataGuru
import com.esaku.sekolahsiswa.models.ModelTahunAjaran
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class DetailMapelActivity : AppCompatActivity() {
    var preferences  = Preferences()
    lateinit var kodeMapel:String
    private lateinit var dataAdapter: DataKompetensiAdapter

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.hasExtra("file_dok")){
//                Toast.makeText(this@DetailMapelActivity,intent.getStringExtra("kode_kd"),Toast.LENGTH_LONG).show()
                val bs = BottomSheetPenilaian()
                bs.fileDok=intent.getStringExtra("file_dok")
                bs.judulPH=intent.getStringExtra("judul_ph")
                bs.nilai=intent.getStringExtra("nilai")
                bs.judulKD=intent.getStringExtra("judul_kd")
                bs.keteranganKD=intent.getStringExtra("desc_kd")
                bs.nilaiMessage=intent.getStringExtra("keterangan")
                bs.keteranganPH=intent.getStringExtra("deskripsi")
                bs.kkm=intent.getStringExtra("kkm")
//                bs.noAju=noAju
//                bs.status=status
//                bs.noUrut=noUrut
//                bs.keterangan=input_keterangan.text.toString()
                bs.show(supportFragmentManager, "BottomSheetConfirm")
//                periode = intent.getStringExtra("filter")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("penilaian_trigger"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mMessageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        preferences.setPreferences(this)
        dataAdapter= DataKompetensiAdapter(this)
        val bundle = intent.extras
        if (bundle != null) {
            if(bundle.getString("key")!=null){
                var key: Map<String, Any> = HashMap()
                key = Gson().fromJson(bundle.getString("key"), key.javaClass)
                kodeMapel= key["kode_matpel"].toString()
            }

//            var string = "Bundle{"
//            for (key in bundle.keySet()) {
//                string += " " + key + " => " + bundle[key] + ";"
//            }
//            string += " }Bundle"
//
//            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
//            Log.d(ContentValues.TAG, "BUNDLE: ${bundle.keySet()}")
            //bundle must contain all info sent in "data" field of the notification
        }

        if(intent.hasExtra("kode_mapel")){
            kodeMapel=intent.getStringExtra("kode_mapel")
////            kodeMapel= intent.extras?.get("kode_mapel") as String
        }
        back.setOnClickListener {
            super.onBackPressed()
        }
        when (kodeMapel.toLowerCase()) {

            "pai" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pai)
            }
            "pkr" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pka)
            }
            "pka" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pka)
            }
            "pah" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pah)
            }
            "pab" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pab)
            }
            "pkn" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pkn)
            }
            "mtk" -> {
                ilustrasi_mapel.setImageResource(R.drawable.mtk)
            }
            "bin" -> {
                ilustrasi_mapel.setImageResource(R.drawable.bin)
            }
            "ipa" -> {
                ilustrasi_mapel.setImageResource(R.drawable.ipa)
            }
            "ips" -> {
                ilustrasi_mapel.setImageResource(R.drawable.ips)
            }
            "sbk" -> {
                ilustrasi_mapel.setImageResource(R.drawable.sbk)
            }
            "pjk" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pjk)
            }
            "plh" -> {
                ilustrasi_mapel.setImageResource(R.drawable.plh)
            }
            "bsu" -> {
                ilustrasi_mapel.setImageResource(R.drawable.bsu)
            }
            "big" -> {
                ilustrasi_mapel.setImageResource(R.drawable.big)
            }
            "tik" -> {
                ilustrasi_mapel.setImageResource(R.drawable.tik)
            }
            "pkt" -> {
                ilustrasi_mapel.setImageResource(R.drawable.pkr)
            }
            "bma" -> {
                ilustrasi_mapel.setImageResource(R.drawable.bma)
            }
            "bic" -> {
                ilustrasi_mapel.setImageResource(R.drawable.bic)
            }

        }
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
                    initDetailMapel(kodeMapel, "All", preferences.getKelas())
                }
                R.id.chip_ganjil -> {
                    initDetailMapel(kodeMapel, "1", preferences.getKelas())
                }
                R.id.chip_genap -> {
                    initDetailMapel(kodeMapel, "2", preferences.getKelas())
                }
            }
        }
        initDetailMapel(kodeMapel, "All", preferences.getKelas())
    }

    private fun initDetailMapel(kodeMapel: String, kodeSem: String, kodeKelas: String?) {
        val apiservice= UtilsApi().getAPIService(this@DetailMapelActivity)
        apiservice?.getMapelDetail(kodeMapel, kodeSem, kodeKelas)?.enqueue(object :
            Callback<ResponseBody?> {
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
                                TypeToken<ArrayList<DataKompetensiItem?>?>() {}.type
                            val dataKompetensi: ArrayList<DataKompetensiItem> =
                                gson.fromJson(myobj.optString("data_kompetensi"), type3)
                            dataKompetensi.reverse()
                            detail_tahun_ajaran.text = dataTahunAjaran[0].nama
                            detail_nama_guru.text = dataGuru[0].namaGuru
                            detail_nama_mapel.text = dataGuru[0].namaMapel
                            dataAdapter.initData(dataKompetensi)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailMapelActivity,
                            "Terjadi kesalahan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() == 422) {
                    Toast.makeText(
                        this@DetailMapelActivity,
                        "Terjadi kesalahan",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() == 401) {
                    val intent = Intent(this@DetailMapelActivity, LoginActivity::class.java)
                    startActivity(intent)
                    val noHp=preferences.getNoHp()
                    val password=preferences.getPassword()
                    val fstate=preferences.getFingerprintState()
                    preferences.preferencesLogout()
                    preferences.savePassword(password)
                    preferences.saveNoHp(noHp)
                    preferences.saveFingerprintState(fstate)
                    finishAffinity()
                    Toast.makeText(
                        this@DetailMapelActivity,
                        "Sesi telah berakhir, silahkan login kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() == 403) {
                    Toast.makeText(this@DetailMapelActivity, "Unauthorized", Toast.LENGTH_SHORT)
                        .show()
                } else if (response.code() == 404 || response.code() == 405) {
                    Toast.makeText(
                        this@DetailMapelActivity,
                        "Terjadi kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@DetailMapelActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}