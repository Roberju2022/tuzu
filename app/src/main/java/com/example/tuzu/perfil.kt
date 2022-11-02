package com.example.tuzu

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tuzu.databinding.FragmentPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import java.time.LocalDateTime
import java.util.*

class perfil : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val fileResult = 1
    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

      binding.btnActualizar.setOnClickListener{
          var nombres=binding.EditexNombres.text.toString()
          var apellidos=binding.editexApellidos.text.toString()
          var celular=binding.editexCelular.text.toString()
          var fechanacimiento=binding.editexFechanacimiento.text.toString()
          actualizar(nombres,apellidos,celular,fechanacimiento)
          cargar()


      }

      binding.profileImageView.setOnClickListener{
          fileManager()

      }



        binding.editexFechanacimiento.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear: Int = c.get(Calendar.YEAR)

            val mMonth: Int = c.get(Calendar.MONTH)

            val mDay: Int = c.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(
                activity as Context,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.editexFechanacimiento.setText(
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    )
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()

        }


        cargar()



    }

    private fun cargar() {
        val user = auth.currentUser
        val uid = user!!.uid
        val db = Firebase.firestore

        if (user != null){
            binding.txtEmailPerfil.text = user.email
            db.collection("users").document(uid).get().addOnSuccessListener {
                binding.txtNombresPerfil.text = (it.get("nombres") as String?)
                binding.EditexNombres.setText(it.get("nombres") as String?)
                binding.txtApellidosPerfil.text = (it.get("apellidos") as String?)
                binding.editexApellidos.setText(it.get("apellidos") as String?)
                binding.editexCelular.setText(it.get("celular") as String?)
                binding.editexFechanacimiento.setText(it.get("fecha nacimiento") as String?)

            }
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.add_foto)
                .into(binding.profileImageView)
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.add_foto)
                .into(binding.imgFondoPerfil)
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
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val uri = data.data
                Toast.makeText(activity, "aja cargamos foto", Toast.LENGTH_SHORT).show()

                uri?.let { imageUpload(it) }

            }
        }
    }


    private fun imageUpload(mUri: Uri) {

        val user = auth.currentUser
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Users")
        val fileName: StorageReference = folder.child("img"+user!!.uid)


        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->

                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(uri.toString())
                }

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Se realizaron los cambios correctamente.",
                                Toast.LENGTH_SHORT).show()
                            cargar()
                        }
                    }
            }
        }.addOnFailureListener {
            Log.i("TAG", "file upload error")
        }
    }

    private fun actualizar(nombres:String,apellidos:String,celular:String,fechanacimiento:String){
        val user = auth.currentUser
        val uid = user!!.uid
        val db = Firebase.firestore
        //db.collection("users").document(uid).get().addOnSuccessListener
        val washingtonRef = db.collection("users").document(uid)


        washingtonRef
            .update("nombres", nombres,"apellidos",apellidos,"celular",celular,"fecha nacimiento",fechanacimiento)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")  }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

    }


}