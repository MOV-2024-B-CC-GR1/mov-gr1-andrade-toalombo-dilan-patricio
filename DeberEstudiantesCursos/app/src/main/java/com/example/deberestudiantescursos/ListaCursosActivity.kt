package com.example.deberestudiantescursos

// Importaciones necesarias para trabajar con vistas, base de datos y navegación
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ListaCursosActivity : AppCompatActivity() {

    // Declaración de variables para manejar la base de datos, la lista de cursos, y otros componentes
    private lateinit var dbHelper: DatabaseHelper // Clase que gestiona la base de datos
    private lateinit var listView: ListView // ListView para mostrar los cursos
    private lateinit var cursos: MutableList<Pair<Int, String>> // Lista mutable de cursos con ID y nombre
    private lateinit var estudianteInfoTextView: TextView // TextView para mostrar información del estudiante
    private var estudianteId: Int = -1 // ID del estudiante asociado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_cursos) // Asocia el layout con la actividad

        // Inicialización de las vistas y del helper de base de datos
        dbHelper = DatabaseHelper(this)
        listView = findViewById(R.id.listaCursos)
        estudianteInfoTextView = findViewById(R.id.estudianteInfoTextView)
        val btnAgregarCurso = findViewById<Button>(R.id.btnAgregarCurso)

        // Obtener el ID del estudiante enviado desde otra actividad
        estudianteId = intent.getIntExtra("ESTUDIANTE_ID", -1)

        if (estudianteId == -1) {
            // Si no se encuentra el estudiante, muestra un mensaje de error y cierra la actividad
            Toast.makeText(this, "Error: no se encontró el estudiante.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Muestra información del estudiante en un TextView
        mostrarInformacionEstudiante()

        // Carga los cursos asociados al estudiante en el ListView
        cargarCursos(estudianteId)

        // Configura el botón para agregar un nuevo curso
        btnAgregarCurso.setOnClickListener {
            val intent = Intent(this, AgregarCursoActivity::class.java)
            intent.putExtra("ESTUDIANTE_ID", estudianteId) // Pasa el ID del estudiante
            startActivity(intent)
        }

        // Configura un evento para mantener presionado un curso en la lista (long click)
        listView.setOnItemLongClickListener { _, _, position, _ ->
            mostrarOpciones(cursos[position].first, cursos[position].second) // Abre un menú de opciones
            true
        }
    }

    // Método para mostrar información del estudiante
    private fun mostrarInformacionEstudiante() {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT nombre, edad FROM Estudiante WHERE id = ?", arrayOf(estudianteId.toString()))
        if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val edad = cursor.getInt(1)
            estudianteInfoTextView.text = "Estudiante: $nombre, Edad: $edad años" // Formatea la información
        }
        cursor.close()
    }

    // Método para cargar los cursos asociados al estudiante
    private fun cargarCursos(estudianteId: Int) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, descripcion, duracionHoras FROM Curso WHERE estudianteId  = ?",
            arrayOf(estudianteId.toString())
        )

        cursos = mutableListOf() // Reinicia la lista de cursos
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0) // ID del curso
                val nombre = cursor.getString(1) // Nombre del curso
                val duracionHoras = cursor.getInt(3) // Duración del curso
                cursos.add(id to "$nombre - ${duracionHoras}h") // Añade el curso a la lista con su formato
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Configura un adaptador para mostrar los cursos en el ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cursos.map { it.second })
        listView.adapter = adapter
    }

    // Método para mostrar un menú de opciones (Editar o Eliminar) al mantener presionado un curso
    private fun mostrarOpciones(cursoId: Int, cursoInfo: String) {
        val opciones = arrayOf("Editar", "Eliminar")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Opciones para $cursoInfo")
        builder.setItems(opciones) { _, which ->
            when (which) {
                0 -> editarCurso(cursoId) // Llama al método para editar el curso
                1 -> eliminarCurso(cursoId) // Llama al método para eliminar el curso
            }
        }
        builder.show()
    }

    // Método para editar un curso
    private fun editarCurso(cursoId: Int) {
        val intent = Intent(this, AgregarCursoActivity::class.java)
        intent.putExtra("CURSO_ID", cursoId) // Pasa el ID del curso para edición
        intent.putExtra("ESTUDIANTE_ID", estudianteId)
        startActivity(intent)
    }

    // Método para eliminar un curso
    private fun eliminarCurso(cursoId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Curso")
        builder.setMessage("¿Estás seguro de que quieres eliminar este curso?")
        builder.setPositiveButton("Eliminar") { _, _ ->
            val db = dbHelper.writableDatabase
            db.execSQL("DELETE FROM Curso WHERE id = ?", arrayOf(cursoId)) // Elimina el curso de la base de datos
            cargarCursos(estudianteId) // Recarga la lista de cursos
            Toast.makeText(this, "Curso eliminado correctamente", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    // Método que se ejecuta cuando la actividad vuelve a ser visible
    override fun onResume() {
        super.onResume()
        cargarCursos(estudianteId) // Recarga los cursos para reflejar cambios
    }
}
