package com.example.tuzu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tuzu.databinding.ActivityLoginBinding
import java.util.regex.Pattern



class login : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnIngresar.setOnClickListener {
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.txtcontrasena.text.toString()





            when {
                mPassword.isEmpty() || mEmail.isEmpty() -> {
                    Toast.makeText(this, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT).show()
                }
                else -> {




                    if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                        Toast.makeText(this, "Ingrese un email valido.",
                            Toast.LENGTH_SHORT).show()
                    }else if (mPassword.length<8){binding.txtcontrasena.error="La contraseña es de minimo 8 caracteres"}

                    else {
                        val principal = Intent(this,principal::class.java)
                        startActivity(principal)
                        Toast.makeText(this, "Pasamos a validar con base de datos", Toast.LENGTH_SHORT).show()
                    }


                }
            }



        }


        binding.signUpTextView.setOnClickListener{

            val register= Intent(this,Registro::class.java)
            startActivity(register)

        }

        binding.btnFoto.setOnClickListener{

            Toast.makeText(this, "foto", Toast.LENGTH_SHORT).show()
            dispatchTakePictureIntent()

        }






    }






    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView5.setImageBitmap(imageBitmap)
        }
    }






}