package com.example.tuzu

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.tuzu.databinding.ActivityNuevoLugarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class nuevoLugar : AppCompatActivity() {
    private lateinit var binding:ActivityNuevoLugarBinding
    private val fileResult = 1
    private lateinit var auth: FirebaseAuth

    private var fileUri: Uri? = null

    private val database = Firebase.database
    private val myRef = database.getReference("Lugares")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNuevoLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title="Crear sitio"
        auth = Firebase.auth



        val bundle = intent.extras
        val dato_latitud = bundle?.getString("latitud")
        val dato_longitud = bundle?.getString("longitud")
        val dato_textodireccion = bundle?.getString("textodireccion")


        if(dato_latitud!=null){


          binding.txtUbicacionNuevositio.setText(dato_latitud+" /"+dato_longitud)


        }






        binding.btnCrearNuevositio.setOnClickListener {
            val nom = binding.txtNombresNuevositio.text.toString()
            val dir = binding.txtDireccionNuevositio.text.toString()
            val cel = binding.txtCelularNuevositio.text.toString()
            var key: String = myRef.push().key.toString()

            if(fileUri!=null){

                val user = auth.currentUser
                val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Lugares/"+user!!.uid)
                val fileName: StorageReference = folder.child("img"+key)

                fileName.putFile(fileUri!!).addOnSuccessListener {
                    fileName.downloadUrl.addOnSuccessListener { uri ->

                        val f =uri.toString()
                        createsitio(nom,dir,cel,f,key)


                        //Toast.makeText(this, "$f", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Log.i("TAG", "file upload error")
                }

            }





        }


        binding.posterImageView.setOnClickListener{

          fileUpload()


        }

        binding.txtUbicacionNuevositio.setOnClickListener{

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Grant permission call", Toast.LENGTH_SHORT).show()
                permisos()

            }else{

            val intent = Intent(this, mapa_nuevo_lugar::class.java)
            this.startActivity(intent)}


        }


    }


    private fun createsitio(nombre: String, direccion: String, celular:String,photo:String,key:String) {
                           val map = hashMapOf(
                        "nombre" to nombre,
                        "direccion" to direccion,
                        "celular" to celular,
                          "photo" to photo,
                               "key" to key
                        )

                    val db = Firebase.firestore

                    db.collection("prueba").document(key.toString()).set(map).addOnSuccessListener {
                        //infoUser()
                        Toast.makeText(this, "Sitio creado con exito", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar la informacion",
                                Toast.LENGTH_SHORT
                            ).show()
                        }



                    //reload()
      var imageView=binding.posterImageView
        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        val user = auth.currentUser
        //var key: String = myRef.push().key.toString()

        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Lugares/"+user!!.uid)
        val fileName: StorageReference = folder.child("img$key")


        var uploadTask = fileName.putBytes(data)
        uploadTask.addOnFailureListener {

        }.addOnSuccessListener { taskSnapshot ->

            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }




    }

    private fun fileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, fileResult)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data
                
                val d = data.dataString
                Toast.makeText(this, "$uri", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "aja cargamos foto", Toast.LENGTH_SHORT).show()


        binding.posterImageView.setImageURI(uri)


               //uri?.let { imageUpload(it) }

            }
        }
    }





    private fun imageUpload(mUri: Uri) {

        val user = auth.currentUser
        val key: String = myRef.push().key.toString()
        val folder: StorageReference = FirebaseStorage.getInstance("$key").reference.child("Lugares")
        val fileName: StorageReference = folder.child("img$key")



        fileName.putFile(mUri).addOnSuccessListener {


            val imageView = binding.posterImageView
            Glide.with(this )
                .load(mUri.toString())
                .into(imageView)




        }.addOnFailureListener {
            Log.i("TAG", "file upload error")
        }



    }

    private fun fileUpload() {
        previewImage.launch("image/*")
    }

    private val previewImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        fileUri = uri
        binding.posterImageView.setImageURI(uri)
    }


    fun permisos() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)


    }



}