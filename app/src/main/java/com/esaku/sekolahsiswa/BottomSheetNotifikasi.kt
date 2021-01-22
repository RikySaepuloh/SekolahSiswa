package com.esaku.sekolahsiswa

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_notifikasi.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetNotifikasi : BottomSheetDialogFragment() {
    lateinit var myview : View
    var preferences  = Preferences()
    lateinit var fileDok:String
    lateinit var judulNotif:String
    lateinit var isiNotif:String
    lateinit var noBukti:String
    lateinit var tglNotif:String
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.bottom_sheet_notifikasi, container, false)
        preferences.setPreferences(context!!)

        myview.bsn_isi.text = isiNotif
        myview.bsn_judul.text = tglNotif
        if(!this::fileDok.isInitialized||fileDok=="-"){
            myview.bsn_image.visibility=View.GONE
        }else{
            Glide.with(context!!).load(link+fileDok).error(R.drawable.ic_broken_image).into(myview.bsn_image)
        }
        sendStsRead(noBukti)
//        myview.kirim.setOnClickListener { sendApprove() }
        return myview
    }

    private fun sendStsRead(noBukti:String) {
        val apiservice= UtilsApi().getAPIService(context!!)
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
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
            }
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this@BottomSheetNotifikasi.dialog as BottomSheetDialog).behavior.setState(
                    BottomSheetBehavior.STATE_EXPANDED
                )
            }
        }
    }
//
////    fun sendApprove(tanggal : String?, modul : String?,  status : String?,no_aju : String?, keterangan : String?) {
//    fun sendApprove() {
//    val tanggal: String =
//        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//
//    val apiservice= UtilsApi().getAPIService(context!!)
//        apiservice?.approval(noAju,status,keterangan,noUrut,tanggal)?.enqueue(object :
//            Callback<ResponseBody?> {
//            override fun onResponse(
//                call: Call<ResponseBody?>,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        val obj = JSONObject(response.body()!!.string())
//                        val myobj = obj.getJSONObject("success")
//                        val idDevice = myobj.optString("id_device_app")
//                        val nik = myobj.optString("nik_app_next")
//                        sendNotification(idDevice,nik)
//                        Toast.makeText(context, myobj.optString("message"), Toast.LENGTH_SHORT).show()
//                        val intent = Intent(context, MainActivity::class.java)
//                        intent.putExtra("history",true)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        startActivity(intent)
//                    }else{
//                        //                        if (data.length() > 0) {
//                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                    }
//                } else if(response.code() == 422) {
//                    Toast.makeText(context, "Keterangan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 401){
//                    Toast.makeText(context, "Format Keterangan Salah", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 403){
//                    Toast.makeText(context, "Token Invalid", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 404){
//                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//
//    private fun sendNotification(idDevice:String,nik:String) {
//
//        val title  = "Justifikasi Pengadaan"
//        val message  = "Pengajuan Justifikasi Pengadaan $noAju menunggu approval anda."
//
//        val apiservice = UtilsApi().getAPIService(context!!)
//        apiservice?.sendNotif(idDevice,title,message,nik)?.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(
//                call: Call<ResponseBody?>,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        try {
//                            val obj = JSONObject(response.body()!!.string())
//                            Log.e("Response:", obj.toString())
////                                if(obj.optString("status").isNotEmpty()&&obj.optString("status")=="false"){
////                                    Toast.makeText(context,"Terjadi kesalahan notif",Toast.LENGTH_SHORT).show()
////                                }
//
//                        } catch (e: Exception) {
//
//                        }
//                    }else{
//                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                    }
//                } else if(response.code() == 422) {
//                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 401){
//                    val intent = Intent(context, LoginActivity::class.java)
//                    startActivity(intent)
//                    preferences.preferencesLogout()
//                    activity?.finish()
//                    Toast.makeText(context, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 403){
//                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 404){
//                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//
////        try {
////            val instance = FCMInstance
////            instance.setContext(context!!)
////            val response = instance.api.sendNotif("notification")
////            if(response.isSuccessful){
////                Log.d(TAG,"Response: ${Gson().toJson(response)}")
////            }else{
////                Log.d(TAG,response.errorBody().toString())
////            }
////        } catch (e: Exception) {
////            Log.d(TAG,e.toString())
////        }
//    }

}