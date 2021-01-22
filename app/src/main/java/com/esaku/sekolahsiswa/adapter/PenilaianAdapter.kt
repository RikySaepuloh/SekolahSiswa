package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.BottomSheetPenilaian
import com.esaku.sekolahsiswa.DetailMapelActivity
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.ViewImageActivity
import com.esaku.sekolahsiswa.models.PelaksanaanItem
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_penilaian.view.*

class PenilaianAdapter(private val context: Context,private val dataArray: MutableList<PelaksanaanItem>,private val kodeKD:String,private val dasar:String) : RecyclerView.Adapter<PenilaianAdapter.NamaKelompokViewHolder>() {
//    lateinit var kodeKD: String
//    lateinit var dasar:String
//    lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaKelompokViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_penilaian, parent, false)
//        this.context=parent.context
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
//        if(dataArray[position].fileDok!="-"){
//            holder.file.setOnClickListener {
//            val intent = Intent(context,ViewImageActivity::class.java)
//            intent.putExtra("urutan",(position+1).toString())
//            intent.putExtra("foto",dataArray[position].fileDok)
//            intent.putExtra("max",(dataArray.size).toString())
//            context.startActivity(intent)
//            }
//        }else{
//            holder.file.visibility=View.GONE
//        }

        holder.judul.text = dataArray[position].pelaksanaan
        holder.desc.text = dataArray[position].keterangan
        holder.nilai.text = dataArray[position].nilai?.substringBefore(".")+"/100"
        holder.progress.progress = dataArray[position].nilai!!.substringBefore(".").toInt()
        try {
            val kkm=dataArray[position].kkm?.substringBefore(".")?.toInt()
            if (dataArray[position].nilai?.substringBefore(".")?.toInt()!! >= kkm!!){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    holder.progress.progressTintList = ContextCompat.getColorStateList(context,R.color.colorBlue)
                    holder.nilai.setTextColor(ContextCompat.getColor(context,R.color.colorBlue))
                }
            }else{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    holder.progress.progressTintList = ContextCompat.getColorStateList(context,R.color.colorRed)
                    holder.nilai.setTextColor(ContextCompat.getColor(context,R.color.colorRed))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if(dataArray[position].fileDok=="-"||dataArray[position].fileDok==null){
            holder.nilaiBerkas.visibility=View.INVISIBLE
            holder.tgl.text = dataArray[position].tgl
        }else{
            holder.tgl.text = dataArray[position].tgl+" ||"
            holder.nilaiBerkas.visibility=View.VISIBLE
        }

        holder.layout.setOnClickListener {
            val bs=BottomSheetPenilaian()
            val mydata = Intent("penilaian_trigger")
            mydata.putExtra("file_dok", dataArray[position].fileDok)
            mydata.putExtra("judul_ph", dataArray[position].pelaksanaan)
            mydata.putExtra("deskripsi", dataArray[position].deskripsi)
            mydata.putExtra("keterangan", dataArray[position].keterangan)
            mydata.putExtra("kkm", dataArray[position].kkm?.substringBefore("."))
            mydata.putExtra("nilai", dataArray[position].nilai?.substringBefore("."))
            mydata.putExtra("judul_kd", "Kompetensi Dasar $kodeKD")
            mydata.putExtra("desc_kd", dasar)
            LocalBroadcastManager.getInstance(context).sendBroadcast(mydata)
        }
//        holder.dasar.text = "Kompetensi Dasar "+dataArray[position].namaKd
//        try {
//            holder.nilai.text = dataArray[position].nilai?.substringBefore(".")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            holder.nilai.text = dataArray[position].nilai
//        }
//        holder.dasar.text = dataArray[position].namaKd
//        holder.pelaksanaan.text = dataArray[position].pelaksanaan
//        holder.judul.text = "Kompetensi Dasar "+dataArray[position].kodeKd
//        holder.setIsRecyclable(false)
    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul = itemView.nilai_judul
        val desc = itemView.nilai_desc
        val nilai = itemView.nilai_nilai
        val nilaiBerkas = itemView.nilai_berkas
        val progress = itemView.nilai_progress
        val selengkapnya = itemView.nilai_selengkapnya
        val layout = itemView.layout_penilaian
        val tgl = itemView.nilai_tgl
//        val nilai = itemView.kompetensi_nilai
//        val file = itemView.kompetensi_file
//        val layoutFile = itemView.layout_file
    }


}