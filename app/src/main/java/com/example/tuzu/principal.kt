package com.example.tuzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tuzu.databinding.ActivityPrincipalBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class principal : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding:ActivityPrincipalBinding



    override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
        binding=ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth= Firebase.auth




        binding.bottomNavigation.setOnNavigationItemSelectedListener  {
            when(it.itemId) {
                R.id.item_menu_perfil -> {
                    // Respond to navigation item 1 click
                    Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show()

                    openFragment(perfil())
                    this.title="Mi perfil"


                    true
                }
                R.id.item_menu_home -> {
                    // Respond to navigation item 1 click
                    Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()

                    openFragment(inicio())
                    this.title="Inicio"

                    true
                }
                R.id.item_menu_salir-> {
                    // Respond to navigation item 2 click


                    //
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Cerrar sesion")
                        .setMessage("Esta seguro que desea salir?")
                        .setNegativeButton("Cancelar") { view, _ ->

                            view.dismiss()
                        }
                        .setPositiveButton("Salir") { view, _ ->
                            signOut()
                            view.dismiss()
                        }
                        .setCancelable(false)
                        .create()

                    dialog.show()
                    //
                    true
                }
                else -> false
            }
        }



    }



    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, login::class.java)
        this.startActivity(intent)
    }


    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}