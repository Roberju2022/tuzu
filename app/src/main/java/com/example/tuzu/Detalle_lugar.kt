package com.example.tuzu

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.tuzu.databinding.ActivityDetalleLugarBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.jar.Manifest

class Detalle_lugar : AppCompatActivity() {
    private lateinit var binding:ActivityDetalleLugarBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetalleLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.title="Detalle lugar"

        val bundle = intent.extras
        val dato = bundle?.getString("key")

        Toast.makeText(this, "$dato", Toast.LENGTH_SHORT).show()


        //
        val db =Firebase.firestore
        val docRef = db.collection("prueba").document(dato.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {



                    val imageView = binding.imgDetallelugar
                    Glide.with(this )
                        .load(document.getString("photo"))
                        .into(imageView)

                    binding.txtNombrelugarDetalle.text=document.getString("nombre")
                    binding.txtDirecciolugarDetalle.text=document.getString("direccion")
                    binding.txtCelularlugarDetalle.text=document.getString("celular")
                    val celular =document.getString("celular")

                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")


                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        //



        binding.btnflotantebackdetalleLugar.setOnClickListener{
            val intent= Intent(this,listadoLugaresInvitado::class.java)
            startActivity(intent)

        }

        binding.btnflotantellamar.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Grant permission call", Toast.LENGTH_SHORT).show()
                permisos()
            }else{ makePhoneCall(binding.txtCelularlugarDetalle.text.toString())

            }
        }

    }

    private fun permisos() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),1)
    }


    fun makePhoneCall(number: String) : Boolean {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}