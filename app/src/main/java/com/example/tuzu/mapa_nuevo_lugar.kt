package com.example.tuzu

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tuzu.databinding.ActivityMapaNuevoLugarBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception


class mapa_nuevo_lugar : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapaNuevoLugarBinding

    private lateinit var fusedLocation: FusedLocationProviderClient
    private var ultimoMarcador : Marker? = null

    private var latitud =""
    private var longitud =""
    private var textolugar =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMapaNuevoLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.title="Seleccion ubicacion mapa"
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        binding.btnSeleccionMapa.setOnClickListener {


            ///
            if(latitud.isNotEmpty()||longitud.isNotEmpty()) {

                val intent = Intent(this, nuevoLugar::class.java)
                val bundle = Bundle()
                bundle.putString("latitud", "${latitud}")
                bundle.putString("longitud", "${longitud}")
                bundle.putString("textodireccion", "${textolugar}")
                intent.putExtras(bundle)
                startActivity(intent)
            }else{

                Toast.makeText(this, "Error seleccione un lugar en el mapa", Toast.LENGTH_SHORT).show()
            }
            //

        }

    }
    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, android.R.color.holo_red_dark)
        BitmapHelper.vectorToBitmap(this, R.drawable.add_foto, color)
    }


    override fun onMapReady(googleMap: GoogleMap) {


//permisos ubicacion
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Grant permission call", Toast.LENGTH_SHORT).show()
            permisos()

        }


            //permisos ubicacion
            mMap = googleMap
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isCompassEnabled = true


            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val ubicacion = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(ubicacion, 15f)
                    )
                }
            }

            mMap.setOnMapClickListener {
                val markerOptions = MarkerOptions().position(it)
                markerOptions.icon(bicycleIcon)

                val nombreUbicacion = obtenerDireccion(it)

                markerOptions.title(nombreUbicacion)



                if (ultimoMarcador != null)
                    ultimoMarcador!!.remove()
                ultimoMarcador = mMap.addMarker(markerOptions)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(it))


            }


    }

    fun obtenerDireccion(latLng: LatLng) : String {
        val geocoder = Geocoder(this)
        val direcciones : List<Address>?
        val primeraDireccion : Address
        var textoDireccion = ""

        try {



            direcciones = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1)
            if (direcciones != null && direcciones.isNotEmpty()) {
                primeraDireccion = direcciones[0]

                // Si la dirección tiene varias líneas
                if (primeraDireccion.maxAddressLineIndex > 0) {
                    for (i in 0..primeraDireccion.maxAddressLineIndex) {
                        textoDireccion += primeraDireccion.getAddressLine(i) + "\n"
                    }
                }
                // Si hay principal y secundario
                else {
                    textoDireccion += primeraDireccion.thoroughfare + ", " +
                            primeraDireccion.subThoroughfare + "\n"
                }

              latitud=latLng.latitude.toString()
               longitud=latLng.longitude.toString()
                textolugar=textoDireccion

            }
        } catch (e : Exception) {
            textoDireccion = "Dirección no encontrada"
        }

        return textoDireccion
    }

    fun permisos() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)



    }
}