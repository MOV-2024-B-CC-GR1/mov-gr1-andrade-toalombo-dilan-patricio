package com.example.deberestudiantescursos

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Clase para la actividad de agregar o editar un curso
class AgregarCursoActivity : AppCompatActivity() {

    // Variables para almacenar el ID del estudiante y del curso
    private var estudianteId: Int = -1
    private var cursoId: Int = -1
    private lateinit var dbHelper: DatabaseHelper // Instancia del helper para manejar la base de datos
    private lateinit var tvEstudianteInfo: TextView // TextView para mostrar información del estudiante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_curso)

        // Inicializamos la base de datos y obtenemos referencias a los elementos del layout
        dbHelper = DatabaseHelper(this)
        tvEstudianteInfo = findViewById(R.id.tvEstudianteInfo)

        // Referencias a los campos de texto y botón en el layout
        val etNombre = findViewById<EditText>(R.id.etNombreCurso) // Campo para el nombre del curso
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionCurso) // Campo para la descripción del curso
        val etDuracion = findViewById<EditText>(R.id.etDuracionHorasCurso) // Campo para la duración del curso
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCurso) // Botón para guardar

        // Obtenemos los IDs del estudiante y del curso de los datos enviados a través del intent
        estudianteId = intent.getIntExtra("ESTUDIANTE_ID", -1)
        cursoId = intent.getIntExtra("CURSO_ID", -1)

        // Verificamos si el estudiante existe
        if (estudianteId == -1) {
            Toast.makeText(this, "Error: no se encontró el estudiante.", Toast.LENGTH_SHORT).show()
            finish() // Cierra la actividad si no se encontró el estudiante
            return
        }

        // Muestra información del estudiante
        mostrarInformacionEstudiante()

        // Si estamos editando un curso, cargamos sus datos
        if (cursoId != -1) {
            cargarDatosCurso(cursoId, etNombre, etDescripcion, etDuracion)
        }

        // Configuramos el evento del botón para guardar los datos
        btnGuardar.setOnClickListener {
            // Obtenemos los valores de los campos de texto
            val nombre = etNombre.text.toString()
            val descripcion = etDescripcion.text.toString()
            val duracionHoras = etDuracion.text.toString().toIntOrNull() // Convierte la duración a entero (maneja errores)

            // Validamos que los campos no estén vacíos
            if (nombre.isNotEmpty() && descripcion.isNotEmpty() && duracionHoras != null) {
                val db = dbHelper.writableDatabase // Base de datos en modo escritura
                val valores = ContentValues().apply {
                    put("nombre", nombre)
                    put("descripcion", descripcion) // Guardamos la descripción del curso
                    put("duracionHoras", duracionHoras)
                    put("estudianteId", estudianteId) // Asociamos el curso al estudiante
                }

                if (cursoId == -1) {
                    // Si el cursoId es -1, significa que estamos creando un nuevo curso
                    val resultado = db.insert("Curso", null, valores) // Insertamos en la tabla Curso
                    if (resultado != -1L) {
                        Toast.makeText(this, "Curso agregado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Finalizamos la actividad
                    } else {
                        Toast.makeText(this, "Error al guardar el curso.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si el cursoId no es -1, significa que estamos editando un curso existente
                    val resultado = db.update("Curso", valores, "id = ?", arrayOf(cursoId.toString()))
                    if (resultado > 0) {
                        Toast.makeText(this, "Curso editado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Finalizamos la actividad
                    } else {
                        Toast.makeText(this, "Error al editar el curso.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Si los campos están vacíos, mostramos un mensaje
                Toast.makeText(this, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para mostrar la información del estudiante
    private fun mostrarInformacionEstudiante() {
        val db = dbHelper.readableDatabase // Base de datos en modo lectura
        val cursor = db.rawQuery("SELECT nombre, edad FROM Estudiante WHERE id = ?", arrayOf(estudianteId.toString()))
        if (cursor.moveToFirst()) { // Verificamos si hay resultados
            val nombre = cursor.getString(0) // Obtenemos el nombre del estudiante
            val edad = cursor.getInt(1) // Obtenemos la edad del estudiante
            tvEstudianteInfo.text = "Estudiante: $nombre, Edad: $edad años" // Mostramos los datos en el TextView
        }
        cursor.close() // Cerramos el cursor para liberar recursos
    }

    // Función para cargar los datos de un curso en los campos de texto
    private fun cargarDatosCurso(cursoId: Int, etNombre: EditText, etDescripcion: EditText, etDuracion: EditText) {
        val db = dbHelper.readableDatabase // Base de datos en modo lectura
        val cursor = db.rawQuery(
            "SELECT nombre, descripcion, duracionHoras FROM Curso WHERE id = ?",
            arrayOf(cursoId.toString())
        )

        if (cursor.moveToFirst()) { // Verificamos si hay resultados
            etNombre.setText(cursor.getString(0)) // Cargamos el nombre del curso
            etDescripcion.setText(cursor.getString(1)) // Cargamos la descripción del curso
            etDuracion.setText(cursor.getInt(2).toString()) // Cargamos la duración del curso
        }
        cursor.close() // Cerramos el cursor para liberar recursos
    }
}
