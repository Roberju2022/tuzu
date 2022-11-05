package com.example.tuzu.adapter

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tuzu.databinding.ItemLugaresBinding
import com.example.tuzu.login
import com.example.tuzu.lugares

class lugaresViewHolder (view: View):RecyclerView.ViewHolder(view){

    val binding = ItemLugaresBinding.bind(view)
    fun render(
        lugaresModel: lugares,
        onClickListener: (lugares) -> Unit,
        onClickDelete: (Int) -> Unit

    ) {
        binding.tvname.text = lugaresModel.Nombre
        binding.tvdireccion.text=lugaresModel.Direccion

        Glide.with(binding.tvimagen.context).load(lugaresModel.photo).into(binding.tvimagen)
        binding.tvimagen.setOnClickListener { onClickListener(lugaresModel) }



        itemView.setOnClickListener { Toast.makeText(itemView.context,"Nombre "+binding.tvdireccion.text, Toast.LENGTH_SHORT).show()}
        binding.btnDelete.setOnClickListener {  onClickDelete(adapterPosition) }

        itemView.setOnLongClickListener{ v ->
            val intent = Intent(v.context, login::class.java).apply {
                putExtra("key", lugaresModel.key)
            }
            v.context.startActivity(intent)
            true
        }


    }

}