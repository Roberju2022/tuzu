package com.example.tuzu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tuzu.adapter.lugaresAdapter
import com.example.tuzu.databinding.ActivityLugaresListadoBinding

class LugaresListado : AppCompatActivity() {
    private lateinit var binding: ActivityLugaresListadoBinding

    private var listadoMutablelugares:MutableList<lugares> = lugaresProvider.LugaresList.toMutableList()

    private lateinit var adapter: lugaresAdapter
    private val llmanager = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLugaresListadoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.etFilter.addTextChangedListener {userfiler ->

            val filtrado = listadoMutablelugares.filter {

                lugares->lugares.Nombre.lowercase().contains(userfiler.toString().lowercase())
            }
            adapter.updatelugares(filtrado)

        }
        initRecyclerView()

    }
    private fun initRecyclerView() {
        adapter=lugaresAdapter(
            LugaresList = listadoMutablelugares,
            onClickListener = { superhero -> onItemSelected(superhero) },
            onClickDelete = { position -> onDeletedItem(position) }


        )
        binding.recyclerLugares.layoutManager = llmanager
        binding.recyclerLugares.adapter = adapter


    }

    private fun onDeletedItem(position: Int) {
        listadoMutablelugares.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun onItemSelected(lugares: lugares) {
        Toast.makeText(this, lugares.Nombre, Toast.LENGTH_SHORT).show()
    }


}