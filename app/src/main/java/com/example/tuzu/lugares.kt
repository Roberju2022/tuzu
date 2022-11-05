package com.example.tuzu

import com.google.firebase.database.Exclude


data class lugares(
    val Nombre: String,
    val Direccion: String,
    val photo: String,
    @Exclude val key: String? = null
)