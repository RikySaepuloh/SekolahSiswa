package com.esaku.sekolahsiswa

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_confirm.view.*


class BottomSheetPenilaian : BottomSheetDialogFragment() {
    lateinit var myview : View
    var preferences  = Preferences()
    lateinit var fileDok:String
    lateinit var judulPH:String
    lateinit var judulKD:String
    lateinit var keteranganPH:String
    lateinit var keteranganKD:String
    lateinit var kodeKD:String
    lateinit var kkm:String
    lateinit var nilai:String
    lateinit var ctx:Context
    lateinit var nilaiMessage:String
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.bottom_sheet_confirm, container, false)
        preferences.setPreferences(context!!)

//        myview.kirim.setOnClickListener { sendApprove() }
        return myview
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        dialog?.setOnShowListener {
//
//
//            val d = dialog as BottomSheetDialog
//            val bottomSheet: FrameLayout? =
//                d.findViewById(com.google.android.material.R.id.design_bottom_sheet)
//            val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//
////            val bottomSheetInternal = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//
////            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
////            BottomSheetBehavior.from(bottomSheetInternal)
////                .setState(BottomSheetBehavior.STATE_EXPANDED)
//        }
//        return super.onCreateDialog(savedInstanceState)
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this@BottomSheetPenilaian.dialog as BottomSheetDialog).behavior.setState(
                    BottomSheetBehavior.STATE_EXPANDED
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myview.bsp_judul_ph.text = judulPH
        myview.bsp_judul2_ph.text = judulPH
        myview.bsp_judul_kd.text = judulKD
        myview.bsp_desc_kompetensi.text = keteranganKD
        myview.bsp_desc_ph.text = keteranganPH
        myview.bsp_nilai.text = "$nilai/100"
        myview.bsp_nilai_desc.text = nilaiMessage
        myview.bsp_progress.progress = nilai.toInt()
        if (nilai.toInt() >= kkm.toInt()){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                myview.bsp_progress.progressTintList = ContextCompat.getColorStateList(
                    ctx,
                    R.color.colorBlue
                )
                myview.bsp_nilai.setTextColor(ContextCompat.getColor(ctx, R.color.colorBlue))
            }
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                myview.bsp_progress.progressTintList = ContextCompat.getColorStateList(
                    ctx,
                    R.color.colorRed
                )
                myview.bsp_nilai.setTextColor(ContextCompat.getColor(ctx, R.color.colorRed))
            }
        }
        if(!this::fileDok.isInitialized||fileDok=="-"){
            myview.bsp_image.visibility=View.GONE
        }else{
            Glide.with(context!!).load(link + fileDok).error(R.drawable.ic_broken_image).into(myview.bsp_image)
        }
        myview.bsp_image.setOnClickListener {
            val intent=Intent(context, ViewPImageActivity::class.java)
            intent.putExtra("foto", fileDok)
            startActivity(intent)
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