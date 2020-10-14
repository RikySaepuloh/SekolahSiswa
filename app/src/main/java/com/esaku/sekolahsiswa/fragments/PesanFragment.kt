package com.esaku.sekolahsiswa.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.esaku.sekolahsiswa.LoginActivity
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.adapter.PageAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelInformasi
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_pesan.*
import kotlinx.android.synthetic.main.fragment_pesan.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Type

class PesanFragment : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var badgeDrawable: BadgeDrawable
    lateinit var mycontext:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(R.layout.fragment_pesan, container, false)

        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val pageAdapter =
            PageAdapter(
                activity?.supportFragmentManager,
                myview.tablayout.tabCount,
            )
        myview.viewpager.offscreenPageLimit = 2
        myview.viewpager.adapter = pageAdapter

        myview.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(myview.tablayout))

        badgeDrawable = myview.tablayout.getTabAt(0)!!.orCreateBadge
        badgeDrawable.horizontalOffset = -20
        badgeDrawable.backgroundColor = ContextCompat.getColor(mycontext,R.color.colorBlue)
        badgeDrawable.isVisible = true
        myview.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                myview.viewpager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

//        myview.tablayout.setupWithViewPager(myview.viewpager)
        initInfo()
    }

    private fun initInfo() {
        val apiservice= UtilsApi().getAPIService(context!!)
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
                            badgeDrawable.number=data.size

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
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    activity?.finishAffinity()
                    Toast.makeText(context, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404 || response.code() == 405){
                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
