package com.esaku.sekolahsiswa

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.saku.inventaris.models.ModelLogin
import kotlinx.android.synthetic.main.activity_login.*
import me.aflak.libraries.callback.FingerprintDialogCallback
import me.aflak.libraries.dialog.FingerprintDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(),FingerprintDialogCallback {
    private val permissionRequest = 100
    private val library=Library()
    var preferences  = Preferences()
    lateinit var idDevice:String
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        preferences.setPreferences(this)
        library.darkStatusBar(window,this)
        version_name.text = "v${BuildConfig.VERSION_NAME}"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(arePermissionsEnabled()){

            }else{
                requestMultiplePermissions()
            }
        }
        firebaseInstance()
            checkLogin()

        login_btnlogin.setOnClickListener {
            if(username.text.toString().trim()!=""||password.text.toString().trim()!=""){
                preferences.saveNoHp(username.text.toString())
                preferences.savePassword(password.text.toString())
                login(username.text.toString(),password.text.toString(),idDevice)
            }
        }



        btn_fingerprint.setOnClickListener {
            if(preferences.getFingerprintState()) {
                if (FingerprintDialog.isAvailable(this)) {
                    FingerprintDialog.initialize(this)
                        .title("Identifikasi Sidik Jari")
                        .message("Masuk menggunakan sidik jari.")
                        .callback(this)
                        .show()
                }
            }else{
                Toast.makeText(this@LoginActivity, "Anda harus login terlebih dahulu untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun firebaseInstance(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                idDevice = task.result?.token.toString()
                Log.e("FIREBASE",idDevice)

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

    }

    private fun requestMultiplePermissions(){
        val remainingPermissions: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
                } else {
                    return
                }
            ) {
                remainingPermissions.add(permission)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(remainingPermissions.toTypedArray(), permissionRequest)
        }
    }

    private fun arePermissionsEnabled(): Boolean {
        for (permission in permissions) {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
                } else {
                    return false
                }
            ) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            shouldShowRequestPermissionRationale(permissions[i]!!)
                        } else {
                            return
                        }
                    ) {
                        AlertDialog.Builder(this)
                            .setMessage("Aplikasi membutuhkan beberapa izin agar dapat berjalan dengan baik")
                            .setPositiveButton(
                                "Izinkan"
                            ) { _: DialogInterface?, _: Int -> requestMultiplePermissions() }
                            .setNegativeButton(
                                "Tidak"
                            ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                    return
                }
            }
        }
    }

    private fun login(nik: String?, password: String?, idDevice: String) {
        val utilsapi= UtilsApi().getAPIService(this)
        utilsapi?.login(nik,password,idDevice)?.enqueue(object : Callback<ModelLogin?> {
            override fun onResponse(
                call: Call<ModelLogin?>,
                modelLogin: Response<ModelLogin?>
            ) {
                if (modelLogin.isSuccessful) {
                    if (modelLogin.body() != null) {
                        preferences.saveToken(modelLogin.body()!!.token.toString())
                        preferences.saveTokenType(modelLogin.body()!!.tokenType.toString())
                        preferences.saveLogStatus(true)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(modelLogin.code() == 422) {
                    Toast.makeText(this@LoginActivity, "Username/Password masih kosong", Toast.LENGTH_SHORT).show()
                } else if(modelLogin.code() == 401){
                    Toast.makeText(this@LoginActivity, "Username/Password salah", Toast.LENGTH_SHORT).show()
                } else if(modelLogin.code() == 403){
                    Toast.makeText(this@LoginActivity, "Token Invalid", Toast.LENGTH_SHORT).show()
                } else if(modelLogin.code() == 404 || modelLogin.code() == 405){
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelLogin?>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkLogin(){
        if(preferences.getLogStatus()){
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                if(!intent.hasExtra("from_exit")&&preferences.getFingerprintState()){
                    if(FingerprintDialog.isAvailable(this)) {
                        FingerprintDialog.initialize(this)
                            .title("Identifikasi Sidik Jari")
                            .message("Masuk menggunakan sidik jari.")
                            .callback(this)
                            .show()
                    }
                }
            }, 500)
        }
    }

    override fun onAuthenticationSucceeded() {
        login(preferences.getNoHp(),preferences.getPassword(),idDevice)
//        Toast.makeText(this@LoginActivity, "Berhasil", Toast.LENGTH_SHORT).show()
//        TODO("Not yet implemented")
    }

    override fun onAuthenticationCancel() {
//        Toast.makeText(this@LoginActivity, "Gajadi", Toast.LENGTH_SHORT).show()
//        TODO("Not yet implemented")
    }
}