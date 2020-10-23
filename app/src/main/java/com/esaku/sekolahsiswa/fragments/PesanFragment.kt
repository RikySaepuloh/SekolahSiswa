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

class PesanFragment(val params:String) : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var badgeDrawable: BadgeDrawable
    lateinit var mycontext:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
    }

    override fun onResume() {
        super.onResume()
        initInfo()
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


//        Toast.makeText(context,params,Toast.LENGTH_LONG).show()

        val pageAdapter =
            PageAdapter(
                childFragmentManager,
                myview.tablayout.tabCount,
            )
        myview.viewpager.offscreenPageLimit = 2
        myview.viewpager.adapter = pageAdapter

        myview.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(myview.tablayout))

        badgeDrawable = myview.tablayout.getTabAt(0)!!.orCreateBadge
        badgeDrawable.horizontalOffset = -20
        badgeDrawable.isVisible = false
        badgeDrawable.backgroundColor = ContextCompat.getColor(mycontext,R.color.colorBlue)
        myview.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                myview.viewpager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        if(params=="open_notifikasi"){
            myview.viewpager.currentItem=1
        }else{
            myview.viewpager.currentItem=0
        }
//        myview.tablayout.setupWithViewPager(myview.viewpager)
        initInfo()
    }

    private fun initInfo() {
        val apiservice= UtilsApi().getAPIService(context!!)
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
                                badgeDrawable.isVisible = false
                            }else{
                                badgeDrawable.isVisible = true
                                badgeDrawable.number=totalunread
                            }


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
