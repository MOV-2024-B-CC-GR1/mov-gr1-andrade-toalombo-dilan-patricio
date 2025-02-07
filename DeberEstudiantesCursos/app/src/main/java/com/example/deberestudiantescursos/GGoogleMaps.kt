package com.example.deberestudiantescursos

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GGoogleMaps : AppCompatActivity() {

    // Mapa de Google que será inicializado en la actividad
    private lateinit var mapa: GoogleMap

    // Variable para gestionar permisos de ubicación
    var permisos = false
    var nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    var nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)

        // Ajusta los márgenes del diseño para adaptarse a la barra del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar el botón de aceptar para cerrar la actividad
        val btnAceptar = findViewById<Button>(R.id.btnAceptar)
        btnAceptar.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        // Solicitar permisos de ubicación si no están concedidos
        solicitarPermisos()

        // Inicializar la lógica del mapa
        inicializarLogicaMapa()
    }

    // Comprueba si el usuario ha concedido los permisos necesarios
    fun tengoPermisos(): Boolean {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        val tienePermisos = permisoFine == PackageManager.PERMISSION_GRANTED &&
                permisoCoarse == PackageManager.PERMISSION_GRANTED
        permisos = tienePermisos
        return permisos
    }

    // Si los permisos no han sido concedidos, solicitarlos al usuario
    fun solicitarPermisos() {
        if (!tengoPermisos()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }

    // Inicializa el mapa y configura sus parámetros
    fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            with(googleMap) {
                mapa = googleMap
                establecerConfiguracionMapa() // Configurar opciones del mapa
                moverEstudiante() // Ubicar un marcador en la posición del estudiante
            }
        }
    }

    // Ubica un marcador en la posición del estudiante en el mapa
    fun moverEstudiante() {
        val estudiante = LatLng(36.00082606856848, 139.0830028147945)
        val titulo = "Estudiante"
        val marcadorEstudiante = anadirMarcador(estudiante, titulo)
        marcadorEstudiante.tag = titulo // Asigna un tag al marcador
        moverCamaraConZoom(estudiante) // Ajusta la cámara para enfocar el marcador
    }

    @SuppressLint("MissingPermission")
    fun establecerConfiguracionMapa() {
        with(mapa) {
            if (tengoPermisos()) {
                mapa.isMyLocationEnabled = true // Habilita la ubicación del usuario en el mapa
                uiSettings.isMyLocationButtonEnabled = true // Habilita el botón de ubicación
            }
            uiSettings.isZoomControlsEnabled = true // Habilita los controles de zoom
        }
    }

    // Mueve la cámara del mapa a una ubicación específica con zoom
    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 17f) {
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    // Añade un marcador en el mapa con una ubicación y un título
    fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }
}
