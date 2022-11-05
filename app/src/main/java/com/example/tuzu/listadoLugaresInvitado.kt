package com.example.tuzu

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tuzu.adapter.lugaresAdapter
import com.example.tuzu.adapter.lugaresAdapterInvi
import com.example.tuzu.databinding.ActivityListadoLugaresInvitadoBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class listadoLugaresInvitado : AppCompatActivity() {
    private lateinit var binding:ActivityListadoLugaresInvitadoBinding

    private var listadoMutablelugares:MutableList<lugares> = ArrayList()
    private lateinit var adapter: lugaresAdapterInvi
    private val llmanager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListadoLugaresInvitadoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ///
        val db = Firebase.firestore

        db.collection("prueba")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //Toast.makeText(this, "${document.id} => ${document.data}", Toast.LENGTH_SHORT).show()

                    val mlugar =
                        lugares(document.getString("nombre").toString(),document.getString("direccion").toString(),document.getString("photo").toString(),document.getString("key").toString())

                    listadoMutablelugares.add(mlugar)
                    adapter.notifyItemInserted(1)
                    // Log.d(TAG, "${document.id} => ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }

        //aja


        //listadoMutablelugares.addAll(lugaresProvider.LugaresList.toMutableList())
        binding.etFilterInvi.addTextChangedListener {userfiler ->

            val filtrado = listadoMutablelugares.filter {

                    lugares->lugares.Nombre.lowercase().contains(userfiler.toString().lowercase())
            }
            adapter.updatelugares(filtrado)

        }

        //

        this.title="Lugares Turisticos"

        binding.btnflotantebackInvi.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)

        }



    }


    public override fun onPause() {
         super.onPause()
        //Toast.makeText(this, "Estoy en pausa", Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter= lugaresAdapterInvi(
            LugaresList = listadoMutablelugares,
            onClickListener = { lugares -> onItemSelected(lugares) },
            onClickDelete = { position -> onDeletedItem(position) }


        )
        binding.recyclerLugaresInvi.layoutManager = llmanager
        binding.recyclerLugaresInvi.adapter = adapter


    }

    private fun onDeletedItem(position: Int) {
        listadoMutablelugares.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun onItemSelected(lugares: lugares) {



        val intent= Intent(this,Detalle_lugar::class.java)
        val bundle=Bundle()
        bundle.putString("key",lugares.key)
        intent.putExtras(bundle)




        startActivity(intent)
    }
}