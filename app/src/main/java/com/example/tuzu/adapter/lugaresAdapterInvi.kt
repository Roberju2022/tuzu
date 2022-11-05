package com.example.tuzu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tuzu.R
import com.example.tuzu.lugares

class lugaresAdapterInvi (

    private var LugaresList:List<lugares>,
    private val onClickListener: (lugares) -> Unit,
    private val onClickDelete:(Int) -> Unit

): RecyclerView.Adapter<lugaresViewHolderInvi>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): lugaresViewHolderInvi {
        val layoutInflater = LayoutInflater.from(parent.context)
        return lugaresViewHolderInvi(layoutInflater.inflate(R.layout.item_lugares_invi, parent, false))
    }

    override fun onBindViewHolder(holder: lugaresViewHolderInvi, position: Int) {
        val item = LugaresList[position]
        holder.render(item, onClickListener, onClickDelete)
    }

    override fun getItemCount(): Int = LugaresList.size

    fun updatelugares(LugaresList: List<lugares>){
        this.LugaresList = LugaresList
        notifyDataSetChanged()
    }




}