package com.esaku.sekolahsiswa

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.saku.inventaris.models.ModelLogin
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val permissionRequest = 100
    private val library=Library()
    var preferences  = Preferences()
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
        version_name.text = "v"+BuildConfig.VERSION_NAME
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(arePermissionsEnabled()){

            }else{
                requestMultiplePermissions()
            }
        }

        checkLogin()
        login_btnlogin.setOnClickListener {
            if(username.text.toString().trim()!=""||password.text.toString().trim()!=""){
                login(username.text.toString(),password.text.toString())
            }
        }
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

    private fun login(nik: String?, password : String?) {
        val utilsapi= UtilsApi().getAPIService(this)
        utilsapi?.login(nik,password)?.enqueue(object : Callback<ModelLogin?> {
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
        }
    }
}