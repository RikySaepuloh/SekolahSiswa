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
import com.esaku.sekolahsiswa.models.ModelDataKompetensi
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_kompetensi.view.*
import kotlinx.android.synthetic.main.list_mapel.view.*

class DataKompetensiAdapter(private val context: Context) : RecyclerView.Adapter<DataKompetensiAdapter.NamaKelompokViewHolder>() {
    private var dataArray= mutableListOf<ModelDataKompetensi>()
    private val dataTemporary= mutableListOf<ModelDataKompetensi>()
    lateinit var kelas:String
    fun addData(data:MutableList<ModelDataKompetensi>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:MutableList<ModelDataKompetensi>){
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_kompetensi, parent, false)
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        if(dataArray[position].fileDok!="-"){
            holder.file.setOnClickListener {
            val intent = Intent(context,ViewImageActivity::class.java)
            intent.putExtra("urutan",(position+1).toString())
            intent.putExtra("foto",dataArray[position].fileDok)
            intent.putExtra("max",(dataArray.size+1).toString())
            context.startActivity(intent)
            }
        }else{
            holder.file.visibility=View.GONE
        }
        holder.tgl.text = dataArray[position].tglInput+" • "+dataArray[position].minggu
        try {
            holder.nilai.text = dataArray[position].nilai?.substringBefore(".")
        } catch (e: Exception) {
            e.printStackTrace()
            holder.nilai.text = dataArray[position].nilai
        }
        holder.dasar.text = dataArray[position].namaKd
        holder.pelaksanaan.text = dataArray[position].pelaksanaan

    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tgl: TextView = itemView.kompetensi_tgl
        val dasar: TextView = itemView.kompetensi_dasar
        val pelaksanaan: TextView = itemView.kompetensi_pelaksanaan
        val nilai: TextView = itemView.kompetensi_nilai
        val file: MaterialButton = itemView.kompetensi_file
        val layoutFile: MaterialCardView = itemView.layout_file
    }


}