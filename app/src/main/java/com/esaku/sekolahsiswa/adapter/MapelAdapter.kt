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
            "pai" -> {
                holder.iconMapel.setImageResource(R.drawable.pai)
            }
            "pkr" -> {
                holder.iconMapel.setImageResource(R.drawable.pka)
            }
            "pka" -> {
                holder.iconMapel.setImageResource(R.drawable.pka)
            }
            "pah" -> {
                holder.iconMapel.setImageResource(R.drawable.pah)
            }
            "pab" -> {
                holder.iconMapel.setImageResource(R.drawable.pab)
            }
            "pkn" -> {
                holder.iconMapel.setImageResource(R.drawable.pkn)
            }
            "mtk" -> {
                holder.iconMapel.setImageResource(R.drawable.mtk)
            }
            "bin" -> {
                holder.iconMapel.setImageResource(R.drawable.bin)
            }
            "ipa" -> {
                holder.iconMapel.setImageResource(R.drawable.ipa)
            }
            "ips" -> {
                holder.iconMapel.setImageResource(R.drawable.ips)
            }
            "sbk" -> {
                holder.iconMapel.setImageResource(R.drawable.sbk)
            }
            "pjk" -> {
                holder.iconMapel.setImageResource(R.drawable.pjk)
            }
            "plh" -> {
                holder.iconMapel.setImageResource(R.drawable.plh)
            }
            "bsu" -> {
                holder.iconMapel.setImageResource(R.drawable.bsu)
            }
            "big" -> {
                holder.iconMapel.setImageResource(R.drawable.big)
            }
            "tik" -> {
                holder.iconMapel.setImageResource(R.drawable.tik)
            }
            "pkt" -> {
                holder.iconMapel.setImageResource(R.drawable.pkr)
            }
            "bma" -> {
                holder.iconMapel.setImageResource(R.drawable.bma)
            }
            "bic" -> {
                holder.iconMapel.setImageResource(R.drawable.bic)
            }
            else -> {

            }
        }

        holder.namaMapel.text = "\n${dataArray[position].singkatan}"
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