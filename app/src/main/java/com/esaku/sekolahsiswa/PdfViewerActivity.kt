package com.esaku.sekolahsiswa

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import es.voghdev.pdfviewpager.library.util.FileUtil
import kotlinx.android.synthetic.main.activity_pdf_viewer.*


open class PdfViewerActivity : AppCompatActivity(), DownloadFile.Listener {
    lateinit var adapter: PDFPagerAdapter
    lateinit var remotePDFViewPager: RemotePDFViewPager
    lateinit var root: LinearLayout
    lateinit var btnDownload: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pdf_viewer)
        val url = intent.getStringExtra("url")
        val nama = intent.getStringExtra("nama")
        setContentView(R.layout.activity_pdf_viewer);

        root = findViewById(R.id.remote_pdf_root)
        btnDownload = findViewById(R.id.btn_download)

        setDownloadButtonListener(url,nama)
        remotePDFViewPager = RemotePDFViewPager(this, url, this)
    }

    private fun setDownloadButtonListener(url: String?,nama:String) {
        val ctx: Context = this
        val listener: DownloadFile.Listener = this
        btnDownload.setOnClickListener {
            remotePDFViewPager = RemotePDFViewPager(ctx, url, listener)
            remotePDFViewPager.id = R.id.pdf_page_container
            hideDownloadButton()
            if (url != null) {
                downloadFile(url,nama)
            }
        }
    }

    fun updateLayout() {
        root.removeAllViewsInLayout()
        root.addView(
            btnDownload,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        root.addView(
            remotePDFViewPager,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun showDownloadButton() {
        btnDownload.visibility = View.VISIBLE
    }

    fun hideDownloadButton() {
        btnDownload.visibility = View.INVISIBLE
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
//        adapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
//        remotePDFViewPager.adapter = adapter
//        setContentView(remotePDFViewPager)
        adapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
        remotePDFViewPager.adapter = adapter
        updateLayout()
        showDownloadButton()
    }

    override fun onFailure(e: Exception?) {
//        TODO("Not yet implemented")
    }

    override fun onProgressUpdate(progress: Int, total: Int) {
//        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.close()
    }

    fun downloadFile(url: String,nama:String){
                            try {
                        val request = DownloadManager.Request(Uri.parse(url))
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                        request.setDescription(nama)
                        request.setAllowedOverRoaming(true)
                        request.setTitle(nama)
                        request.setMimeType("pdf")
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            nama
                        )
                        val manager =
                            this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?


                        manager!!.enqueue(request)
                        Toast.makeText(
                            this,
                            "Download sedang berlangsung $url}",
                            Toast.LENGTH_LONG
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            "Mohon setujui perizinan aplikasi agar aplikasi dapat berjalan dengan baik.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
    }

}