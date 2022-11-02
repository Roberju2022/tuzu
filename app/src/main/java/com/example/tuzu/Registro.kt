package com.example.tuzu

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.graphics.red
import com.example.tuzu.databinding.ActivityLoginBinding
import com.example.tuzu.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Pattern

class Registro : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth:FirebaseAuth
    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        binding.btnRegresar.setOnClickListener{

            val home= Intent(this,MainActivity::class.java)
            startActivity(home)
        }

        binding.txtFechaNacimiento.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR)

            val mMonth: Int = c.get(Calendar.MONTH)

            val mDay: Int = c.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.txtFechaNacimiento.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()

        }

        binding.btnRegistrar.setOnClickListener{

            val mNombres = binding.txtNombres.text.toString()
            val mApellidos = binding.txtApellidos.text.toString()
            val mEmail = binding.txtEmail.text.toString()
            val mTelefono = binding.txtTelefono.text.toString()
            val mPassword = binding.txtContrasena.text.toString()
            val mFechanacimiento = binding.txtFechaNacimiento.text.toString()
            val mRepeatPassword = binding.txtVeriContrasena.text.toString()

            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[-@#$%^&+=])" +     // Al menos 1 carácter especial
                    ".{6,}" +                // Al menos 4 caracteres
                    "$")

            val letras = Pattern.compile("^"+"(?=.*[qwertyuiopasdfghjklñzxcvbnmQWERTYUIOPASDFGHJKLÑZXCVBNM])"+".{1,}"+"$")

            if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                //binding.txtEmail.setBackgroundColor(Color.RED)
                Toast.makeText(this, "Ingrese un email valido.",
                    Toast.LENGTH_SHORT).show()
                binding.txtEmail.error="aja y tu que"
            } else if (mNombres.isEmpty() || !letras.matcher(mNombres).matches()){
                Toast.makeText(this, "Los nombres no deben tener caracteres numericos.",
                    Toast.LENGTH_SHORT).show()
            }else if (mApellidos.isEmpty() || !letras.matcher(mApellidos).matches()){
                Toast.makeText(this, "Los apellidos no deben tener caracteres numericos.",
                    Toast.LENGTH_SHORT).show()
            }else if (mTelefono.isEmpty() || mTelefono.length<10){
                Toast.makeText(this, "El numero celular esde minimo 10 caracteres.",
                    Toast.LENGTH_SHORT).show()
            }else if (mFechanacimiento.isEmpty()){
                Toast.makeText(this, "El campo fecha es obligatorio.",
                    Toast.LENGTH_SHORT).show()
            }else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(this, "La contraseña es debil.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword){
                Toast.makeText(this, "La contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show()
            } else {


                createAccount(mEmail,mPassword,mNombres, mApellidos,mFechanacimiento,mTelefono)

            }

        }





    }

    private fun createAccount(email: String, password: String, nombres:String, apellidos:String,fecha_nacimiento:String,celular:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //val intent = Intent(this, CheckEmailActivity::class.java)
                    //this.startActivity(intent)
                    val user = auth.currentUser

                    val uid = user!!.uid

                    val map = hashMapOf(
                        "nombres" to nombres,
                        "apellidos" to apellidos,
                        "fecha nacimiento" to fecha_nacimiento,
                        "celular" to celular,

                    )

                    val db = Firebase.firestore

                    db.collection("users").document(uid).set(map).addOnSuccessListener {
                        //infoUser()
                        Toast.makeText(this, "Usuario Registrado", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Fallo al guardar la informacion",
                                Toast.LENGTH_SHORT
                            ).show()
                        }



                    reload()
                } else {
                    Toast.makeText(this, "No se pudo crear la cuenta. Vuelva a intertarlo",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reload() {
        val intent = Intent(this, principal::class.java)
        this.startActivity(intent)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){

            reload()

        }
    }

}