package com.example.tuzu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tuzu.R
import com.example.tuzu.lugares

class lugaresAdapter (

    private var LugaresList:List<lugares>,
    private val onClickListener: (lugares) -> Unit,
    private val onClickDelete:(Int) -> Unit
): RecyclerView.Adapter<lugaresViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): lugaresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return lugaresViewHolder(layoutInflater.inflate(R.layout.item_lugares, parent, false))
    }

    override fun onBindViewHolder(holder: lugaresViewHolder, position: Int) {
        val item = LugaresList[position]
        holder.render(item, onClickListener, onClickDelete)
    }

    override fun getItemCount(): Int = LugaresList.size

    fun updatelugares(LugaresList: List<lugares>){
        this.LugaresList = LugaresList
        notifyDataSetChanged()
    }


}