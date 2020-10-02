package com.esaku.sekolahsiswa.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.LoginActivity
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.adapter.MapelAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelMapel
import com.esaku.sekolahsiswa.models.ModelProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_beranda.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*


class BerandaFragment : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var mycontext:Context
    lateinit var nama:String
    lateinit var kelas:String
    lateinit var foto:String
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"
    private lateinit var dataAdapter:MapelAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
        dataAdapter=MapelAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(R.layout.fragment_beranda, container, false)
        myview.recyclerview.layoutManager = GridLayoutManager(context, 2)
        myview.recyclerview.adapter = dataAdapter
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfile()
    }


    private fun initMapel(kelas: String?) {
        val apiservice= UtilsApi().getAPIService(mycontext)
        apiservice?.getMapel(kelas)?.enqueue(object : Callback<ResponseBody?> {
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
                                TypeToken<ArrayList<ModelMapel?>?>() {}.type
                            val dataMapel: ArrayList<ModelMapel> =
                                gson.fromJson(myobj.optString("data"), type)
                            dataAdapter.initData(dataMapel)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(mycontext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(mycontext, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(mycontext, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    activity?.finishAffinity()
                    Toast.makeText(mycontext, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(mycontext, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404 || response.code() == 405){
                    Toast.makeText(mycontext, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(mycontext, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun initProfile() {
        val apiservice= UtilsApi().getAPIService(mycontext)
        apiservice?.getProfile()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val gson = Gson()
                            val type: Type = object :
                                TypeToken<ArrayList<ModelProfile?>?>() {}.type
                            val datapengajuan: ArrayList<ModelProfile> =
                                gson.fromJson(obj.optString("user"), type)
                            for (i in 0 until datapengajuan.size) {
                                nama = datapengajuan[i].nama.toString()
                                kelas = datapengajuan[i].kelas.toString()
                                foto = datapengajuan[i].foto.toString()
                            }
                            myview.beranda_nama.text = nama
                            myview.beranda_kelas.text = kelas
                            preferences.saveKelas(kelas)
                            Glide.with(mycontext).load(link+foto).placeholder(R.drawable.ic_user).into(
                                myview.beranda_image_profile
                            )
                            initMapel(preferences.getKelas())
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
                    preferences.preferencesLogout()
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
