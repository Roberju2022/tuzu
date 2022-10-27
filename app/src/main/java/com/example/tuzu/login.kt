package com.example.tuzu

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

           val jj =binding.txtcontrasena.error

            when {
                mPassword.isEmpty() || mEmail.isEmpty() -> {
                    Toast.makeText(this, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT).show()
                }
                else -> {


                    if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                        Toast.makeText(this, "$jj +Ingrese un email valido.",
                            Toast.LENGTH_SHORT).show()
                    }   else {
                        Toast.makeText(this, "Pasamos a validar con base de datos", Toast.LENGTH_SHORT).show()
                    }


                }
            }



        }



    }
}