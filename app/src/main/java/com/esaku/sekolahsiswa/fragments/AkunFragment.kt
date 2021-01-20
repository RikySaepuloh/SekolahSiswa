package com.esaku.sekolahsiswa.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.BottomSheetProfile
import com.esaku.sekolahsiswa.LoginActivity
import com.esaku.sekolahsiswa.Preferences
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelProfile
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_akun.view.*
import kotlinx.android.synthetic.main.popup_image.*
import me.aflak.libraries.callback.FingerprintDialogCallback
import me.aflak.libraries.dialog.FingerprintDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.reflect.Type
import java.util.*

class AkunFragment : Fragment(),FingerprintDialogCallback {
    var preferences  = Preferences()
    private lateinit var myview : View
    lateinit var nama:String
    lateinit var kelas:String
    lateinit var foto:String
    lateinit var nis:String
    lateinit var jk:String
    lateinit var tmpLahir:String
    lateinit var email:String
    lateinit var agama:String
    lateinit var tglLahir:String
    lateinit var username:String
    lateinit var id:String
    lateinit var mycontext:Context
    private lateinit var dialog : Dialog
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"



    override fun onAttach(context: Context) {
        super.onAttach(context)
        preferences.setPreferences(context)
        mycontext=context
        dialog = Dialog(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myview = inflater.inflate(R.layout.fragment_akun, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
        }else{
            myview.fingerprint_trigger.visibility= View.GONE
        }
        return myview
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfile()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_image)
        myview.akun_image_profile.setOnClickListener {
            Glide.with(context!!).load(link + foto).into(dialog.image)
            dialog.show()
        }

        myview.btn_ubah_data.setOnClickListener {
            val bsp=BottomSheetProfile(nama,kelas,nis, jk, tmpLahir, email, agama, tglLahir)
            bsp.show(childFragmentManager,"Ubah Data")
        }

        myview.btn_keluar.setOnClickListener {
            val noHp=preferences.getNoHp()
            val password=preferences.getPassword()
            val fstate=preferences.getFingerprintState()
            preferences.preferencesLogout()
            preferences.savePassword(password)
            preferences.saveNoHp(noHp)
            preferences.saveFingerprintState(fstate)
            val intent=Intent(context, LoginActivity::class.java)
            intent.putExtra("from_exit", true)
            startActivity(intent)
            activity?.finishAffinity()
        }
        myview.akun_btn_ubah.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        myview.fingerprint_toggle.isChecked = preferences.getFingerprintState()
        myview.fingerprint_trigger.setOnClickListener {
            AlertDialog.Builder(context!!)
                .setTitle("Perhatian!")
                .setMessage(
                    "Kami tidak menjamin keamanan data dan akun anda ketika anda mengaktifkan fitur ini.\nSeluruh kejadian yang disebabkan oleh fitur ini berada diluar tanggung jawab kami.\n" +
                            "Gunakan sesuai dengan kebutuhan anda."
                )
                .setPositiveButton(
                    "Nyalakan Fitur"
                ) { _: DialogInterface?, _: Int ->
                    if (FingerprintDialog.isAvailable(context)) {
                        FingerprintDialog.initialize(context)
                            .title("Identifikasi Sidik Jari")
                            .message("Sesuaikan sidik jari.")
                            .callback(this)
                            .show()
                    }
                }
                .setNegativeButton(
                    "Matikan Fitur"
                ) { dialog: DialogInterface, _: Int ->
                    preferences.saveFingerprintState(false)
                    myview.fingerprint_toggle.isChecked = false
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                myview.akun_image_profile.setImageURI(fileUri)
                val file: File = ImagePicker.getFile(data)!!
                val fileReqBody: RequestBody =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val imagedata: MultipartBody.Part =
                    MultipartBody.Part.createFormData("foto", file.name, fileReqBody)
                ubahDataWithImage(imagedata)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }

    fun ubahDataWithImage(foto: MultipartBody.Part) {
        val apiservice = UtilsApi().getAPIService(context!!)
        apiservice?.ubahProfileWithImage(foto)?.enqueue(object : Callback<ResponseBody?> {
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
                            initProfile()
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
                                nis = datapengajuan[i].nis.toString()
                                kelas = datapengajuan[i].kodeKelas.toString()
                                tglLahir = datapengajuan[i].tglLahir.toString()
                                jk = datapengajuan[i].jk.toString()
                                tmpLahir = datapengajuan[i].tmpLahir.toString()
                                email = datapengajuan[i].email.toString()
                                agama = datapengajuan[i].agama.toString()
                                username = datapengajuan[i].nik2.toString()
                                foto = datapengajuan[i].foto.toString()
//                                id=datapengajuan[i].nik2.toString()
                            }
                            myview.akun_nama.text = nama
//                            myview.akun_id.text = id
                            myview.akun_nis.text = nis
                            myview.akun_kelas.text = kelas
                            myview.akun_tgl_lahir.text = tglLahir
                            myview.akun_username.text = username
                            myview.akun_tmpt_lahir.text= tmpLahir
                            myview.akun_agama.text= agama
                            myview.akun_email.text= email
                            myview.akun_jk.text= jk
                            Glide.with(mycontext).load(link + foto).placeholder(R.drawable.ic_user)
                                .into(
                                    myview.akun_image_profile
                                )
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
                    val noHp = preferences.getNoHp()
                    val password = preferences.getPassword()
                    val fstate = preferences.getFingerprintState()
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

    override fun onAuthenticationSucceeded() {
        preferences.saveFingerprintState(true)
        myview.fingerprint_toggle.isChecked = true
    }

    override fun onAuthenticationCancel() {
    }

}
