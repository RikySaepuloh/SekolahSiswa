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
import com.esaku.sekolahsiswa.models.ModelMapel
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_mapel.view.*

class MapelAdapter(private val context: Context) : RecyclerView.Adapter<MapelAdapter.NamaKelompokViewHolder>() {
    private var dataArray= mutableListOf<ModelMapel>()
    private val dataTemporary= mutableListOf<ModelMapel>()
    lateinit var kelas:String
    fun addData(data:MutableList<ModelMapel>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:MutableList<ModelMapel>){
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_mapel, parent, false)
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        when (dataArray[position].kodeMapel?.toLowerCase()) {
            "bind" -> {
                holder.iconMapel.setImageResource(R.drawable.bahasa_indonesia)
            }
            "bsdp" -> {
                holder.iconMapel.setImageResource(R.drawable.bsdp)
            }
            "pkn" -> {
                holder.iconMapel.setImageResource(R.drawable.pkn)
            }
            "pjok" -> {
                holder.iconMapel.setImageResource(R.drawable.pjok)
            }
            "ipa" -> {
                holder.iconMapel.setImageResource(R.drawable.ipa)
            }
            "ips" -> {
                holder.iconMapel.setImageResource(R.drawable.ips)
            }
            "pa" -> {
                holder.iconMapel.setImageResource(R.drawable.pendidikan_agama)
            }
            else -> {

            }
        }

        holder.namaMapel.text = "\n${dataArray[position].namaMapel}"
        holder.layout.setOnClickListener {
            val intent = Intent(context,DetailMapelActivity::class.java)
            intent.putExtra("kode_mapel",dataArray[position].kodeMapel)
            intent.putExtra("nama_mapel",dataArray[position].namaMapel)
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMapel: TextView = itemView.nama_mapel
        val iconMapel: ImageView = itemView.icon_mapel
        val layout: CardView = itemView.mapel_layout
    }


}