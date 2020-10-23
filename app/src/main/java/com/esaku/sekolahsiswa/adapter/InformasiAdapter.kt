package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.DetailInformasiActivity
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.models.ModelInformasi2
import kotlinx.android.synthetic.main.list_informasi.view.*

class InformasiAdapter(private val context: Context) : RecyclerView.Adapter<InformasiAdapter.NamaKelompokViewHolder>() {
    val link="https://api.simkug.com/api/mobile-sekolah/storage/"
    private var dataArray= mutableListOf<ModelInformasi2>()
    private val dataTemporary= mutableListOf<ModelInformasi2>()
    lateinit var kelas:String
    fun addData(data:MutableList<ModelInformasi2>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:MutableList<ModelInformasi2>){
        dataArray.clear()
        dataTemporary.clear()
        dataArray.addAll(data)
        dataTemporary.addAll(data)
        notifyDataSetChanged()
    }

    fun initKelas(kelas:String){
        this.kelas=kelas
    }

    fun clearData(){
        dataArray.clear()
        notifyDataSetChanged()
    }

    fun subtractData(){
        dataArray.removeAt(dataArray.size-1)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaKelompokViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_informasi, parent, false)
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        if(dataArray[position].stsReadMob=="1"){
            holder.guru.typeface = Typeface.DEFAULT
            holder.desc.typeface = Typeface.DEFAULT
        }else{
            holder.guru.typeface = Typeface.DEFAULT_BOLD
            holder.desc.typeface = Typeface.DEFAULT_BOLD
        }
        holder.guru.text = dataArray[position].nama
        holder.desc.text = dataArray[position].judul
        holder.tgl.text = dataArray[position].tanggal
        Glide.with(context).load(link+dataArray[position].fileDok).error(R.drawable.ic_user).into(holder.image)
        holder.layout.setOnClickListener {
            holder.guru.typeface = Typeface.DEFAULT
            holder.desc.typeface = Typeface.DEFAULT
            val intent = Intent(context,DetailInformasiActivity::class.java)
                intent.putExtra("no_bukti",dataArray[position].noBukti)
                intent.putExtra("nama",dataArray[position].nama)
                intent.putExtra("nik",dataArray[position].nikUser)
                intent.putExtra("nama_guru",dataArray[position].namaGuru)
                intent.putExtra("kode_matpel",dataArray[position].kodeMatpel)
                intent.putExtra("foto",dataArray[position].fileDok)
//                intent.putExtra("max",(dataArray.size).toString())
                context.startActivity(intent)
        }
//        holder.namaimage.text = dataArray[position].fileDok
//        if(dataArray[position].fileDok=="-" || dataArray[position].fileDok==null){
//            holder.layoutFile.visibility=View.GONE
//        }else{
//            holder.layoutFile.setOnClickListener {
//                val intent = Intent(context,ViewImageActivity::class.java)
//                intent.putExtra("urutan",(position+1).toString())
//                intent.putExtra("foto",dataArray[position].fileDok)
//                intent.putExtra("max",(dataArray.size).toString())
//                context.startActivity(intent)
//            }
//        }
    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val guru = itemView.info_guru
        val image = itemView.info_image
        val desc = itemView.info_desc
        val tgl = itemView.info_tgl
//        val namaimage = itemView.info_nama_image
//        val layoutFile = itemView.info_btn_image
        val layout = itemView.layout_informasi
    }


}