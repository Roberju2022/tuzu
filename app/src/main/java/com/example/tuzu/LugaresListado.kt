package com.example.tuzu

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tuzu.adapter.lugaresAdapter
import com.example.tuzu.databinding.ActivityLugaresListadoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class LugaresListado : AppCompatActivity() {
    private lateinit var binding: ActivityLugaresListadoBinding
    private val database = Firebase.database
    private val myRef = database.getReference("Lugares")


    //private var listadoMutablelugares:MutableList<lugares> = lugaresProvider.LugaresList.toMutableList()
    private var listadoMutablelugares:MutableList<lugares> = ArrayList()

    private lateinit var adapter: lugaresAdapter
    private val llmanager = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLugaresListadoBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.floatingActionButton.setOnClickListener{
            val intent= Intent(this,nuevoLugar::class.java)
            startActivity(intent)


        }

        /*
        val db = Firebase.firestore
        val docRef = db.collection("prueba").document("CBdBC8USP4bcQiEyK9GD")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    Toast.makeText(this, "${document.data}", Toast.LENGTH_SHORT).show()


                    val mlugar =
                        lugares(document.getString("Nombre").toString(),document.getString("Direccion").toString(),document.getString("Photo").toString())

                    //listadoMutablelugares.add(mlugar)
                    //mlugar.let { listadoMutablelugares.add(it) }

                    listadoMutablelugares.add(index = 1, mlugar)
                    adapter.notifyItemInserted(1)
                    //llmanager.scrollToPositionWithOffset(3, 10)


                } else {
                    Log.d(TAG, "No such document")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        */


        //aja
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
                Log.d(TAG, "Error getting documents: ", exception)
            }

        //aja


       //listadoMutablelugares.addAll(lugaresProvider.LugaresList.toMutableList())
        binding.etFilter.addTextChangedListener {userfiler ->

            val filtrado = listadoMutablelugares.filter {

                lugares->lugares.Nombre.lowercase().contains(userfiler.toString().lowercase())
            }
            adapter.updatelugares(filtrado)

        }
        initRecyclerView()
        createSuperHero()

        deleteSwipe(binding.recyclerLugares)

    }

    //CREAR
    private fun createSuperHero() {
        val lugarnuevo = lugares(
            "Incognito",
            "AristiDevsCorporation",
            "https://pbs.twimg.com/profile_images/1037281659727634432/5x2XVPwB_400x400.jpg"
        )

        listadoMutablelugares.add( lugarnuevo)
        adapter.notifyItemInserted(3)
       // llmanager.scrollToPositionWithOffset(3, 10)
    }
    //



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

    private fun deleteSwipe(recyclerView: RecyclerView){

        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                //val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("game/img"+listVideoGames[viewHolder.adapterPosition].key)
               // imageFirebaseStorage.delete()

                //listadoMutablelugares[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }

                listadoMutablelugares.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }




}