package com.esaku.sekolahsiswa

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.esaku.sekolahsiswa.adapter.DataKompetensiAdapter
import com.esaku.sekolahsiswa.adapter.MapelAdapter
import com.esaku.sekolahsiswa.apihelper.UtilsApi
import com.esaku.sekolahsiswa.models.ModelDataGuru
import com.esaku.sekolahsiswa.models.ModelDataKompetensi
import com.esaku.sekolahsiswa.models.ModelTahunAjaran
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.android.synthetic.main.fragment_beranda.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
class ViewImageActivity : AppCompatActivity() {
    var preferences  = Preferences()
    lateinit var foto:String
    lateinit var urutan:String
    lateinit var max:String
    var link="https://api.simkug.com/api/mobile-sekolah/storage/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        preferences.setPreferences(this)
        try {
            foto=intent.getStringExtra("foto")
            urutan=intent.getStringExtra("urutan")
            max=intent.getStringExtra("max")
            urutan_number.text = "Gambar $urutan dari $max"
            judul_tugas.text = foto
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

