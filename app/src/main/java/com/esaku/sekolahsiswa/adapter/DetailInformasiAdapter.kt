package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.models.ModelDetailInformasi
import kotlinx.android.synthetic.main.list_detail_informasi.view.*


class DetailInformasiAdapter(private val context: Context) : RecyclerView.Adapter<DetailInformasiAdapter.NamaKelompokViewHolder>() {
    val link="https://api.simkug.com/api/mobile-sekolah/storage/"
    private var dataArray= mutableListOf<ModelDetailInformasi>()
    private val dataTemporary= mutableListOf<ModelDetailInformasi>()
    private var dataAdapter= DokumenDetailInformasiAdapter(context)
    lateinit var kelas:String
    fun addData(data: MutableList<ModelDetailInformasi>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data: MutableList<ModelDetailInformasi>){
        dataArray.clear()
        dataTemporary.clear()
        dataArray.addAll(data)
        dataTemporary.addAll(data)
        notifyDataSetChanged()
    }

    fun initKelas(kelas: String){
        this.kelas=kelas
    }

    fun clearData(){
        dataArray.clear()
        notifyDataSetChanged()
    }

    fun subtractData(){
        dataArray.removeAt(dataArray.size - 1)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaKelompokViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_detail_informasi,
            parent,
            false
        )
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        holder.judul.text = dataArray[position].judul
        holder.tanggal.text = dataArray[position].tanggal
        if(dataArray[position].link=="-"||dataArray[position].link==null){
            holder.classroom.visibility = View.GONE
        }else{
            holder.classroom.setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dataArray[position].link))
                context.startActivity(browserIntent)
            }
        }

        holder.pesan.text = dataArray[position].pesan

        holder.recyclerview.apply {
            layoutManager = LinearLayoutManager(context,
                RecyclerView.VERTICAL, false)
            adapter = dataAdapter
        }
        dataAdapter.initData(dataArray[position].fileDok)



//        holder.recyclerview.adapter =
//        dataAdapter.initData(dataArray[position].fileDok)


//        holder.layout.setOnClickListener {
//            val intent = Intent(context, DetailInformasi::class.java)
//                intent.putExtra("nama", dataArray[position].nama)
//                intent.putExtra("nik", dataArray[position].nikUser)
//                intent.putExtra("nama_guru", dataArray[position].namaGuru)
//                intent.putExtra("kode_matpel", dataArray[position].kodeMatpel)
//                intent.putExtra("foto", dataArray[position].fileDok)
////                intent.putExtra("max",(dataArray.size).toString())
//                context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tanggal = itemView.detinfo_tanggal
        val judul = itemView.detinfo_judul
        val pesan = itemView.detinfo_pesan
        val classroom = itemView.detinfo_classroom
        val recyclerview = itemView.detinfo_recyclerview
//        val tgl = itemView.info_tgl
//        val namaimage = itemView.info_nama_image
//        val layoutFile = itemView.info_btn_image
//        val layout = itemView.layout_informasi
    }


}