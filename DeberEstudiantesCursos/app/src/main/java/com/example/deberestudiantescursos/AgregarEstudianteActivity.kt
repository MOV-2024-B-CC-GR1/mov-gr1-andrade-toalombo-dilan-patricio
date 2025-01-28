package com.example.deberestudiantescursos

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Actividad para agregar o editar estudiantes
class AgregarEstudianteActivity : AppCompatActivity() {

    // Variables para almacenar la información del estudiante y los elementos de la interfaz
    private var estudianteId: Int? = null // ID del estudiante para edición (nulo si se crea uno nuevo)
    private lateinit var etNombre: EditText // Campo de texto para el nombre
    private lateinit var etEdad: EditText // Campo de texto para la edad
    private lateinit var etPromedio: EditText // Campo de texto para el promedio
    private lateinit var btnGuardar: Button // Botón para guardar los cambios
    private lateinit var dbHelper: DatabaseHelper // Helper para manejar la base de datos SQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita un diseño que se adapta a dispositivos con bordes redondeados o áreas seguras
        setContentView(R.layout.activity_agregar_estudiante)

        // Inicializar las vistas referenciándolas desde el layout
        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        etPromedio = findViewById(R.id.etPromedio)
        btnGuardar = findViewById(R.id.btnGuardarEstudiante)

        dbHelper = DatabaseHelper(this) // Instancia de la base de datos

        // Verificar si se recibe un ID para edición
        estudianteId = intent.getIntExtra("ESTUDIANTE_ID", -1).takeIf { it != -1 }

        if (estudianteId != null) {
            // Si hay un ID, estamos en modo edición y se cargan los datos del estudiante
            cargarDatosEstudiante(estudianteId!!)
        }

        // Configurar el evento para el botón "Guardar"
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString() // Obtener el texto del campo "Nombre"
            val edad = etEdad.text.toString().toIntOrNull() // Convertir el texto de "Edad" a entero
            val promedio = etPromedio.text.toString().toDoubleOrNull() // Convertir el texto de "Promedio" a decimal

            if (nombre.isNotEmpty() && edad != null && promedio != null) {
                if (estudianteId != null) {
                    // Si hay un ID, actualizamos el estudiante
                    actualizarEstudiante(estudianteId!!)
                } else {
                    // Si no hay ID, se crea un nuevo estudiante
                    agregarEstudiante(nombre, edad, promedio)
                }
            } else {
                // Mostrar un mensaje si algún campo está vacío
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Manejar los insets (bordes seguros) del dispositivo para una mejor experiencia visual
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Cargar los datos de un estudiante existente
    private fun cargarDatosEstudiante(id: Int) {
        val db = dbHelper.readableDatabase // Base de datos en modo lectura
        val cursor = db.rawQuery("SELECT * FROM Estudiante WHERE id = ?", arrayOf(id.toString()))

        if (cursor.moveToFirst()) { // Verificar si hay datos disponibles
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")) // Nombre del estudiante
            val edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad")) // Edad del estudiante
            val promedio = cursor.getDouble(cursor.getColumnIndexOrThrow("promedio")) // Promedio del estudiante

            // Mostrar los datos en los campos de texto
            etNombre.setText(nombre)
            etEdad.setText(edad.toString())
            etPromedio.setText(promedio.toString())
        }
        cursor.close() // Cerrar el cursor para liberar recursos
    }

    // Actualizar los datos de un estudiante existente en la base de datos
    private fun actualizarEstudiante(id: Int) {
        val db = dbHelper.writableDatabase // Base de datos en modo escritura

        val nombre = etNombre.text.toString() // Nombre actualizado
        val edad = etEdad.text.toString().toInt() // Edad actualizada
        val promedio = etPromedio.text.toString().toDouble() // Promedio actualizado

        // Crear un objeto ContentValues para guardar los datos
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("edad", edad)
            put("promedio", promedio)
        }

        // Ejecutar la actualización y verificar cuántas filas se actualizaron
        val filasActualizadas = db.update("Estudiante", valores, "id = ?", arrayOf(id.toString()))

        if (filasActualizadas > 0) {
            Toast.makeText(this, "Estudiante actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad y regresar al MainActivity
        } else {
            Toast.makeText(this, "Error al actualizar el estudiante", Toast.LENGTH_SHORT).show()
        }
    }

    // Agregar un nuevo estudiante a la base de datos
    private fun agregarEstudiante(nombre: String, edad: Int, promedio: Double) {
        val db = dbHelper.writableDatabase // Base de datos en modo escritura

        // Crear un objeto ContentValues para guardar los datos
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("edad", edad)
            put("promedio", promedio)
        }

        // Insertar el nuevo estudiante en la tabla
        val resultado = db.insert("Estudiante", null, valores)
        if (resultado != -1L) {
            Toast.makeText(this, "Estudiante agregado exitosamente", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad y regresar al MainActivity
        } else {
            Toast.makeText(this, "Error al agregar el estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}
