package com.esaku.sekolahsiswa.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.esaku.sekolahsiswa.BottomSheetNotifikasi
import com.esaku.sekolahsiswa.LoginActivity
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.adapter.NotifikasiAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelNotifikasi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_notifikasi.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class NotifikasiFragment : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    private lateinit var dataAdapter: NotifikasiAdapter
    lateinit var mycontext:Context
    var mHandler= Handler()
    private lateinit var mRunnable:Runnable

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.hasExtra("isi_notif")){
                val bs = BottomSheetNotifikasi()
                bs.fileDok=intent.getStringExtra("file_dok")
                bs.isiNotif=intent.getStringExtra("isi_notif")
                bs.tglNotif=intent.getStringExtra("tgl_notif")
                bs.noBukti=intent.getStringExtra("no_bukti")
                bs.show(childFragmentManager, "BottomSheetNotifikasi")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(mycontext)
            .registerReceiver(mMessageReceiver, IntentFilter("notifikasi_trigger"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(mycontext)
            .unregisterReceiver(mMessageReceiver)
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
        dataAdapter= NotifikasiAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(R.layout.fragment_notifikasi, container, false)
        myview.recyclerview.layoutManager = LinearLayoutManager(context)
        myview.recyclerview.adapter = dataAdapter
        mRunnable = Runnable {
            // Do something here
            initNotif()
            // Schedule the task to repeat after 1 second
            mHandler.postDelayed(
                mRunnable, // Runnable
                3000 // Delay in milliseconds
            )
        }

        // Schedule the task to repeat after 1 second
        mHandler.postDelayed(
            mRunnable, // Runnable
            3000 // Delay in milliseconds
        )
        initNotif()
        return myview
    }

    private fun initNotif() {
        val apiservice= UtilsApi().getAPIService(context!!)
        apiservice?.getNotif()?.enqueue(object : Callback<ResponseBody?> {
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
                                TypeToken<ArrayList<ModelNotifikasi?>?>() {}.type
                            val data: ArrayList<ModelNotifikasi> =
                                gson.fromJson(obj.optString("data"), type)
                            if(data.size==0||data==null){
                                myview.empty_view.visibility=View.VISIBLE
                            }
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
                            dataAdapter.initData(data)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if (response.code() == 422) {
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 401) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    val noHp=preferences.getNoHp()
                    val password=preferences.getPassword()
                    val fstate=preferences.getFingerprintState()
                    preferences.preferencesLogout()
                    preferences.savePassword(password)
                    preferences.saveNoHp(noHp)
                    preferences.saveFingerprintState(fstate)
                    activity?.finishAffinity()
                    Toast.makeText(
                        context,
                        "Sesi telah berakhir, silahkan login kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 404 || response.code() == 405) {
                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
