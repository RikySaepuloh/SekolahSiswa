package com.esaku.sekolahsiswa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esaku.sekolahsiswa.R
import com.esaku.sekolahsiswa.models.DataKompetensiItem
import kotlinx.android.synthetic.main.list_matpel_detail.view.*

class DataKompetensiAdapter(private val context: Context) : RecyclerView.Adapter<DataKompetensiAdapter.NamaKelompokViewHolder>() {
    private var dataArray= mutableListOf<DataKompetensiItem>()
    private val dataTemporary= mutableListOf<DataKompetensiItem>()
//    private var dataAdapter= PenilaianAdapter(context)
    private val viewPool = RecyclerView.RecycledViewPool()

    fun addData(data: MutableList<DataKompetensiItem>){
        dataArray.addAll(data)
        notifyDataSetChanged()
    }
    fun initData(data: MutableList<DataKompetensiItem>){
        dataArray.clear()
        dataTemporary.clear()
        dataArray.addAll(data)
        dataTemporary.addAll(data)
        notifyDataSetChanged()
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
            R.layout.list_matpel_detail,
            parent,
            false
        )
        return NamaKelompokViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
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
        val childLayoutManager = LinearLayoutManager(             holder.recylerview.context, RecyclerView.VERTICAL, false)
        holder.judul.text = "Kompetensi Dasar "+dataArray[position].kodeKd
        holder.dasar.text = dataArray[position].namaKd
        dataArray[position].pelaksanaan.reverse()
        holder.recylerview.apply {
            layoutManager = childLayoutManager
            adapter = PenilaianAdapter(context,dataArray[position].pelaksanaan,
                dataArray[position].kodeKd!!,
                dataArray[position].namaKd!!)
            setRecycledViewPool(viewPool)
        }




//        try {
//            holder.nilai.text = dataArray[position].nilai?.substringBefore(".")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            holder.nilai.text = dataArray[position].nilai
//        }
//        holder.dasar.text = dataArray[position].namaKd
//        holder.pelaksanaan.text = dataArray[position].pelaksanaan
//        holder.judul.text = "Kompetensi Dasar "+dataArray[position].kodeKd

    }



    override fun getItemCount(): Int {
        return dataArray.size
    }

    class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val judul = itemView.kompetensi_judul
        val dasar = itemView.kompetensi_dasar
        val recylerview = itemView.kompetensi_recyclerview

//        val nilai = itemView.kompetensi_nilai
//        val file = itemView.kompetensi_file
//        val layoutFile = itemView.layout_file
    }


}