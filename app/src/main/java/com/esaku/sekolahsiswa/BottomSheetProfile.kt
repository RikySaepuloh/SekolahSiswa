package com.esaku.sekolahsiswa

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.bottom_sheet_profile.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetProfile(
    var nama: String,
    var kelas: String,
    var nis: String,
    var jk: String,
    var tmpLahir: String,
    var email: String,
    var agama: String,
    var tglLahir: String
) : BottomSheetDialogFragment() {
    var preferences = Preferences()
    lateinit var ctx: Context
    var link = "https://api.simkug.com/api/mobile-sekolah/storage/"
    val agamaList = arrayListOf("Buddha", "Hindu", "Islam", "Katolik", "Konghucu", "Kristen")
    lateinit var agamaChange: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet_profile, container, false)
//        preferences.setPreferences(context!!)

//        myview.kirim.setOnClickListener { sendApprove() }
//        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        try {
//            val date: Date = SimpleDateFormat("dd/MM/yyyy").parse(tglLahir)
//            tglLahir = SimpleDateFormat("yyyy-MM-dd").format(date)
//        } catch (e: Exception) {
////            e.printStackTrace()
//        }

        bsp_nis.setText(nis)
        bsp_nama.setText(nama)
        bsp_kelas.setText(kelas)
        bsp_tmp_lahir.setText(tmpLahir)
        bsp_tanggal_Lahir.setText(tglLahir)
        bsp_email.setText(email)
        agamaChange=agama
        spinner.setItems(agamaList)
        for (i in 0 until agamaList.size) {
            if (agamaList[i] == agama) {
                spinner.selectedIndex = i
            }
        }
        if (jk == "Laki-Laki") {
            rb_lk.isChecked = true
        } else if (jk == "Perempuan") {
            rb_pr.isChecked = true
        }
        rg_jk.setOnCheckedChangeListener { _, checkedId ->
            jk = if (checkedId==R.id.rb_lk){
                "Laki-Laki"
            }else{
                "Perempuan"
            }
        }
        spinner.setOnItemSelectedListener { _, _, _, item ->
            agamaChange = item.toString()
        }

        btn_simpan.setOnClickListener {
            MaterialAlertDialogBuilder(context!!)
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin merubah data?")
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.cancel()
                    // Respond to negative button press
                }
                .setPositiveButton("Ya") { dialog, which ->
                    if (bsp_tmp_lahir.text.toString()==""||bsp_tanggal_Lahir.text.toString()==""||bsp_email.text.toString()==""){
                        Toast.makeText(context,"Kolom tidak boleh kosong!",Toast.LENGTH_LONG).show()
                    }else{
                        val date: Date = SimpleDateFormat("dd/MM/yyyy").parse(bsp_tanggal_Lahir.text.toString())
                        tglLahir = SimpleDateFormat("yyyy-MM-dd").format(date)
                        // Respond to positive button press
//                    Toast.makeText(context, "Berhasil diubah", Toast.LENGTH_SHORT).show()
//                    this.dialog?.dismiss()
                        updateProfile(this.dialog,bsp_tmp_lahir.text.toString(),
                            tglLahir,
                            agamaChange,
                            jk,
                            bsp_email.text.toString())
                    }
                }
                .show()
        }


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this@BottomSheetProfile.dialog as BottomSheetDialog).behavior.setState(
                    BottomSheetBehavior.STATE_EXPANDED
                )
            }
        }
    }

    //
////    fun sendApprove(tanggal : String?, modul : String?,  status : String?,no_aju : String?, keterangan : String?) {
//    fun sendApprove() {
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


//    private fun updateProfile(idDevice:String,nik:String) {
//
//        val apiservice = UtilsApi().getAPIService(context!!)
//        apiservice?.updateProfile()?.enqueue(object : Callback<ResponseBody?> {
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
////
////
//////        try {
//////            val instance = FCMInstance
//////            instance.setContext(context!!)
//////            val response = instance.api.sendNotif("notification")
//////            if(response.isSuccessful){
//////                Log.d(TAG,"Response: ${Gson().toJson(response)}")
//////            }else{
//////                Log.d(TAG,response.errorBody().toString())
//////            }
//////        } catch (e: Exception) {
//////            Log.d(TAG,e.toString())
//////        }
////    }
//
//}

    fun updateProfile(dialog: Dialog?,tmpLahir: String,tglLahir: String,agama: String,jk: String,email: String) {
        val apiservice = UtilsApi().getAPIService(context!!)
        apiservice?.updateProfile(
            tmpLahir,tglLahir,agama,jk,email
        )?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            Toast.makeText(context, obj.optString("message"), Toast.LENGTH_SHORT)
                                .show()
                            dialog?.dismiss()
//                            initProfile()
                        } catch (e: Exception) {

                        }
                    } else {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if (response.code() == 422) {
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 401) {
                    Toast.makeText(
                        context,
                        "Sesi telah berakhir, silahkan login kembali",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() == 403) {
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
