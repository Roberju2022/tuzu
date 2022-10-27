package com.example.tuzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.tuzu.databinding.ActivityLoginBinding
import java.util.regex.Pattern


class login : AppCompatActivity() {
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
                        Toast.makeText(this, "Pasamos a validar con base de datos", Toast.LENGTH_SHORT).show()
                    }


                }
            }



        }


        binding.signUpTextView.setOnClickListener{

            val register= Intent(this,Registro::class.java)
            startActivity(register)

        }



    }
}