package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esaku.sekolahsiswa.*
import com.esaku.sekolahsiswa.models.ModelNotifikasi
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_notifikasi.view.*

class NotifikasiAdapter(private val context: Context) : RecyclerView.Adapter<NotifikasiAdapter.NamaKelompokViewHolder>() {
    private var dataArray= mutableListOf<ModelNotifikasi>()
    private val dataTemporary= mutableListOf<ModelNotifikasi>()
    val link="https://api.simkug.com/api/mobile-sekolah/storage/"
    lateinit var kelas:String
    fun addData(data:MutableList<ModelNotifikasi>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data:MutableList<ModelNotifikasi>){
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_notifikasi, parent, false)
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        holder.pesan.text = dataArray[position].pesan
        holder.tgl.text = "Pemberitahuan - "+dataArray[position].tanggal
        if(dataArray[position].stsReadMob=="1"){
            holder.pesan.typeface = Typeface.DEFAULT
            holder.layoutNotif.setBackgroundColor(ContextCompat.getColor(context,R.color.colorWhite))
        }else{
            holder.pesan.typeface = Typeface.DEFAULT_BOLD
            holder.layoutNotif.setBackgroundColor(ContextCompat.getColor(context,R.color.colorLightBlue))
        }

        if(dataArray[position].fileDok=="-"||dataArray[position].fileDok==null){
            holder.layoutFile.visibility=View.GONE
        }else{
            Glide.with(context).load(link+dataArray[position].fileDok).error(R.drawable.ic_broken_image).into(holder.image)
        }

        holder.layoutNotif.setOnClickListener {
            holder.layoutNotif.setBackgroundColor(ContextCompat.getColor(context,R.color.colorWhite))
            holder.pesan.typeface = Typeface.DEFAULT
            val mydata = Intent("notifikasi_trigger")
            mydata.putExtra("file_dok", dataArray[position].fileDok)
            mydata.putExtra("isi_notif", dataArray[position].pesan)
            mydata.putExtra("tgl_notif", dataArray[position].tanggal)
            mydata.putExtra("no_bukti", dataArray[position].noBukti)
            LocalBroadcastManager.getInstance(context).sendBroadcast(mydata)
        }
    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tgl = itemView.notif_tgl
        val pesan = itemView.notif_pesan
        val image = itemView.file_image
        val layoutFile = itemView.layout_image
        val layoutNotif = itemView.layout_notifikasi
    }


}