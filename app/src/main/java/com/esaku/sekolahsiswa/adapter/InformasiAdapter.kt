package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.DetailMapelActivity
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.ViewImageActivity
import com.esaku.sekolahsiswa.models.ModelInformasi
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_informasi.view.*
import kotlinx.android.synthetic.main.list_mapel.view.*

class InformasiAdapter(private val context: Context) : RecyclerView.Adapter<InformasiAdapter.NamaKelompokViewHolder>() {
    private var dataArray= mutableListOf<ModelInformasi>()
    private val dataTemporary= mutableListOf<ModelInformasi>()
    lateinit var kelas:String
    fun addData(data:MutableList<ModelInformasi>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:MutableList<ModelInformasi>){
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
        holder.guru.text = dataArray[position].nama
        holder.desc.text = dataArray[position].judul
        holder.tgl.text = dataArray[position].tanggal
        holder.namaimage.text = dataArray[position].fileDok
        if(dataArray[position].fileDok!="-"){
            holder.layoutFile.setOnClickListener {
                val intent = Intent(context,ViewImageActivity::class.java)
                intent.putExtra("urutan",(position+1).toString())
                intent.putExtra("foto",dataArray[position].fileDok)
                intent.putExtra("max",(dataArray.size).toString())
                context.startActivity(intent)
            }
        }else{
            holder.layoutFile.visibility=View.GONE
        }
    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val guru: TextView = itemView.info_guru
        val desc: TextView = itemView.info_desc
        val tgl: TextView = itemView.info_tgl
        val namaimage: TextView = itemView.info_nama_image
        val layoutFile: MaterialCardView = itemView.info_btn_image
    }


}