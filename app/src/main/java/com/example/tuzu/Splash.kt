package com.example.tuzu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.tuzu.databinding.ActivitySplashBinding

import android.os.Handler

class Splash : AppCompatActivity() {



    private lateinit var binding:ActivitySplashBinding
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

       val barra = binding.progressBar

        var barStatus=0
        var cargando =binding.txtCargando

        var i=0

        Thread(Runnable {

            while (i < 100) {
                i += 1

                handler.post(Runnable {
                    barra.progress = i

                    cargando!!.text = "Cargando " + i.toString() + "/" + barra.max
                })
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            val home= Intent(this,MainActivity::class.java)
            startActivity(home)


        }).start()



    }
}