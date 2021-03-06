package com.esaku.sekolahsiswa.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.PdfViewerActivity
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.ViewImageActivity
import com.esaku.sekolahsiswa.models.FileDokItem
import kotlinx.android.synthetic.main.list_dokumen_informasi.view.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class DokumenDetailInformasiAdapter(
    private val context: Context,
    private val dataArray: MutableList<FileDokItem>
) : RecyclerView.Adapter<DokumenDetailInformasiAdapter.NamaKelompokViewHolder>() {
    val link="https://api.simkug.com/api/mobile-sekolah/storage/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaKelompokViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_dokumen_informasi,
            parent,
            false
        )
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        val data = dataArray[position]
        holder.name.text = data.nama

        if(data.nama?.substringAfter(".")=="jpg"||data.nama?.substringAfter(".")=="jpeg"||data.nama?.substringAfter(
                "."
            )=="png"){
            holder.icon.setImageResource(R.drawable.ic_image)
        }else if(data.nama?.substringAfter(".")=="pdf"){
            holder.icon.setImageResource(R.drawable.ic_pdf)
        }else if(data.nama?.substringAfter(".")=="zip"||data.nama?.substringAfter(".")=="rar"){
            holder.icon.setImageResource(R.drawable.ic_zip)
        }else if(data.nama?.substringAfter(".")=="xls"||data.nama?.substringAfter(".")=="xlsx"){
            holder.icon.setImageResource(R.drawable.ic_xls)
        }else if(data.nama?.substringAfter(".")=="doc"||data.nama?.substringAfter(".")=="docx"){
            holder.icon.setImageResource(R.drawable.ic_zip)
        }

        holder.buka.setOnClickListener {
            if(data.nama?.substringAfter(".")=="jpg"||data.nama?.substringAfter(".")=="jpeg"||data.nama?.substringAfter(".")=="png"){
                val intent = Intent(context, ViewImageActivity::class.java)
                intent.putExtra("urutan", (position + 1).toString())
                intent.putExtra("foto", data.url)
                intent.putExtra("nama", data.nama)
                intent.putExtra("max", (dataArray.size).toString())
                context.startActivity(intent)
            }else{
//                val file: File = File(
//                    context.filesDir,
//                    data.nama!!
//                )
                val intent = Intent(context, PdfViewerActivity::class.java)
                intent.putExtra("url", data.url)
                intent.putExtra("nama", data.nama)
                context.startActivity(intent)

//                val file = File(
//                    context.filesDir, data.nama!!
//                ) //name here is the name of any string you want to pass to the method
//                Toast.makeText(
//                            context,
//                    file.path,
//                            Toast.LENGTH_LONG
//                        ).show()
//                if(file.exists()){
//                    val testIntent = Intent("com.adobe.reader")
//                    testIntent.type = "application/pdf"
//                    testIntent.action = Intent.ACTION_VIEW
//                    val uri = Uri.fromFile(file)
//                    testIntent.setDataAndType(uri, "application/pdf")
//                    context.startActivity(testIntent)
//                                    Toast.makeText(
//                                        context,
//                                        "File exists",
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                }else{
//                    try {
//                        val request =
//                            DownloadManager.Request(Uri.parse(data.url))
//                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//                        request.setDescription(data.nama)
//                        request.setAllowedOverRoaming(true)
//                        request.setTitle(data.nama)
//                        request.setMimeType("pdf")
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
//                        request.setDestinationInExternalPublicDir(
//                            Environment.DIRECTORY_DOWNLOADS,
//                            data.nama
//                        )
//                        val manager =
//                            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
//
//
//                        manager!!.enqueue(request)
//                        Toast.makeText(
//                            context,
//                            "Download sedang berlangsung $link${data.url}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            context,
//                            "Mohon setujui perizinan aplikasi agar aplikasi dapat berjalan dengan baik.",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
            }


        }

//        try
//        {
//            url=URL(data.url)
//            ucon=url.openConnection()
//            ucon.connect()
//            holder.size.text = ucon.getHeaderField("content-length")
//        }
//        catch(final: IOException) {
//            final.printStackTrace()
//        }

    }


    fun getFileSizeOfUrl(url: String): Long {
        var urlConnection: URLConnection? = null
        try {
            val uri = URL(url)
            urlConnection = uri.openConnection()
            urlConnection!!.connect()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return urlConnection.contentLengthLong
            val contentLengthStr = urlConnection.getHeaderField("content-length")
            return if (contentLengthStr.isNullOrEmpty()) -1 else contentLengthStr.toLong()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        } finally {
            if (urlConnection is HttpURLConnection)
                urlConnection.disconnect()
        }
        return -1
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.dokumen_icon
        val name = itemView.dokumen_name
        val size = itemView.dokumen_size
        val buka = itemView.dokumen_open
//        val tgl = itemView.info_tgl
//        val namaimage = itemView.info_nama_image
//        val layoutFile = itemView.info_btn_image
//        val layout = itemView.layout_informasi
    }


}