package com.example.tuzu

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.tuzu.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)





        binding.btningresar.setOnClickListener{
            Toast.makeText(this, "Ingreso", Toast.LENGTH_SHORT).show()
            val login= Intent(this,login::class.java)
            startActivity(login)

        }
        binding.btnregistro.setOnClickListener{

            Toast.makeText(this, "Registro", Toast.LENGTH_SHORT).show()
            val register= Intent(this,Registro::class.java)
            startActivity(register)

        }
        binding.btnsomos.setOnClickListener{
            Toast.makeText(this, "Somos", Toast.LENGTH_SHORT).show()

            val prueba= Intent(this,LugaresListado::class.java)
            startActivity(prueba)

        }

        binding.btnListInvi.setOnClickListener{
            Toast.makeText(this, "Lista Invitado", Toast.LENGTH_SHORT).show()

            val intent= Intent(this,listadoLugaresInvitado::class.java)
            startActivity(intent)

        }



    }
}