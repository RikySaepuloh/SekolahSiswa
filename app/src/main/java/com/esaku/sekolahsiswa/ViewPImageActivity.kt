package com.esaku.sekolahsiswa

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_view_image.*

class ViewPImageActivity : AppCompatActivity() {
    var preferences  = Preferences()
    lateinit var foto:String
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        preferences.setPreferences(this)
        back.setOnClickListener {
            super.onBackPressed()
        }
        try {
            foto=intent.getStringExtra("foto")!!
            Glide.with(this).load(link+foto).placeholder(R.drawable.ic_broken_image).into(image)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,"Terjadi kesalahan",Toast.LENGTH_LONG).show()
        }


        image.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Unduh Berkas")
                .setMessage(foto)
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, which ->
                    try {
                        val request =
                            DownloadManager.Request(Uri.parse(link + foto))
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        request.setDescription(foto)
                        request.setAllowedOverRoaming(true)
                        request.setTitle(foto)
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            foto
                        )
                        val manager =
                            this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                        manager!!.enqueue(request)
                        Toast.makeText(
                            this,
                            "Download sedang berlangsung $link$foto",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Mohon setujui perizinan aplikasi agar aplikasi dapat berjalan dengan baik.", Toast.LENGTH_LONG).show()
                    }
                }
                .show()
        }
    }
}

