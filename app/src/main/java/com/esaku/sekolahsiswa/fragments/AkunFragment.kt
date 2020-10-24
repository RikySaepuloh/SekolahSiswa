package com.esaku.sekolahsiswa.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.LoginActivity
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_akun.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.ArrayList

class AkunFragment : Fragment() {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var nama:String
    lateinit var kelas:String
    lateinit var foto:String
    lateinit var nis:String
    lateinit var tglLahir:String
    lateinit var username:String
    lateinit var id:String
    lateinit var mycontext:Context
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myview = inflater.inflate(R.layout.fragment_akun, container, false)
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfile()
        myview.btn_keluar.setOnClickListener {
            preferences.preferencesLogout()
            val intent=Intent(context,LoginActivity::class.java)
            startActivity(intent)
            activity?.finishAffinity()
        }
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
                            for(i in 0 until datapengajuan.size){
                                nama=datapengajuan[i].nama.toString()
                                nis=datapengajuan[i].nis.toString()
                                kelas=datapengajuan[i].kelas.toString()
                                tglLahir=datapengajuan[i].tglLahir.toString()
                                username=datapengajuan[i].nik.toString()
                                foto=datapengajuan[i].foto.toString()
//                                id=datapengajuan[i].nik2.toString()
                            }
                            myview.akun_nama.text = nama
//                            myview.akun_id.text = id
                            myview.akun_nis.text = nis
                            myview.akun_kelas.text = kelas
                            myview.akun_tgl_lahir.text = tglLahir
                            myview.akun_username.text = username
                            Glide.with(mycontext).load(link+foto).placeholder(R.drawable.ic_user).into(myview.akun_image_profile)
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
