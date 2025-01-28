package com.example.deberestudiantescursos

// Importaciones necesarias para el funcionamiento de la actividad.
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Clase principal de la actividad, que extiende AppCompatActivity.
class MainActivity : AppCompatActivity() {

    // Declaración de variables que se inicializarán más adelante.
    private lateinit var dbHelper: DatabaseHelper // Ayuda para manejar la base de datos.
    private lateinit var listaEstudiantes: ListView // Vista que muestra la lista de estudiantes.
    private lateinit var estudiantes: MutableList<Estudiante> // Lista de estudiantes obtenidos de la BD.
    private lateinit var adapter: ArrayAdapter<String> // Adaptador para manejar los datos en la lista.

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el diseño de borde a borde para la actividad.
        setContentView(R.layout.activity_main) // Define el archivo de diseño XML asociado.

        // Ajusta los márgenes de la vista principal para que se adapte a las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa el helper de la base de datos.
        dbHelper = DatabaseHelper(this)
        // Encuentra la lista de estudiantes definida en el archivo XML.
        listaEstudiantes = findViewById(R.id.listaEstudiantes)

        // Carga la lista de estudiantes desde la base de datos.
        estudiantes = obtenerEstudiantes()

        // Crea un adaptador para mostrar nombres y detalles de los estudiantes en la lista.
        val nombres = estudiantes.map { "${it.nombre} - ${it.edad} años (${it.promedio})" }.toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombres)
        listaEstudiantes.adapter = adapter

        // Configura el botón para agregar un nuevo estudiante.
        findViewById<Button>(R.id.btnCrearEstudiante).setOnClickListener {
            val intent = Intent(this, AgregarEstudianteActivity::class.java)
            startActivity(intent) // Abre la actividad para agregar un estudiante.
        }

        // Configura un listener de clic largo para cada elemento de la lista.
        listaEstudiantes.setOnItemLongClickListener { _, _, position, _ ->
            mostrarOpcionesCRUD(estudiantes[position]) // Muestra un menú de opciones CRUD.
            true
        }
    }

    // Método para obtener estudiantes desde la base de datos.
    private fun obtenerEstudiantes(): MutableList<Estudiante> {
        val lista = mutableListOf<Estudiante>() // Lista mutable para almacenar estudiantes.
        val db = dbHelper.readableDatabase // Obtén la base de datos en modo lectura.
        val cursor = db.rawQuery("SELECT * FROM Estudiante", null) // Consulta para obtener todos los estudiantes.

        // Recorre los resultados del cursor y crea objetos Estudiante.
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Estudiante(
                        id = cursor.getInt(0), // ID del estudiante.
                        nombre = cursor.getString(1), // Nombre.
                        edad = cursor.getInt(2), // Edad.
                        promedio = cursor.getDouble(3) // Promedio.
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close() // Cierra el cursor.
        return lista
    }

    // Muestra un cuadro de diálogo con opciones CRUD para el estudiante seleccionado.
    private fun mostrarOpcionesCRUD(estudiante: Estudiante) {
        val opciones = arrayOf("Editar", "Eliminar", "Ver Cursos")
        AlertDialog.Builder(this)
            .setTitle("Opciones para ${estudiante.nombre}")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> editarEstudiante(estudiante) // Opción para editar.
                    1 -> eliminarEstudiante(estudiante) // Opción para eliminar.
                    2 -> verCursos(estudiante) // Opción para ver los cursos del estudiante.
                }
            }
            .show()
    }

    // Inicia la actividad para editar un estudiante.
    private fun editarEstudiante(estudiante: Estudiante) {
        val intent = Intent(this, AgregarEstudianteActivity::class.java)
        intent.putExtra("ESTUDIANTE_ID", estudiante.id) // Pasa el ID del estudiante a editar.
        startActivity(intent)
    }

    // Elimina un estudiante de la base de datos y actualiza la lista.
    private fun eliminarEstudiante(estudiante: Estudiante) {
        val db = dbHelper.writableDatabase // Obtén la base de datos en modo escritura.
        db.delete("Estudiante", "id = ?", arrayOf(estudiante.id.toString())) // Borra el estudiante por ID.
        estudiantes.remove(estudiante) // Elimina al estudiante de la lista local.
        adapter.remove("${estudiante.nombre} - ${estudiante.edad} años (${estudiante.promedio})")
        adapter.notifyDataSetChanged() // Notifica al adaptador que los datos cambiaron.
    }

    // Método llamado cuando la actividad vuelve a estar visible.
    override fun onResume() {
        super.onResume()
        cargarListaEstudiantes() // Recarga la lista de estudiantes.
    }

    // Recarga la lista de estudiantes desde la base de datos y actualiza el adaptador.
    private fun cargarListaEstudiantes() {
        estudiantes = obtenerEstudiantes() // Obtiene los estudiantes actualizados.
        val nombres = estudiantes.map { "${it.nombre} - ${it.edad} años (${it.promedio})" }.toMutableList()
        adapter.clear() // Limpia los datos existentes en el adaptador.
        adapter.addAll(nombres) // Agrega los nuevos datos.
        adapter.notifyDataSetChanged() // Notifica cambios al adaptador.
    }

    // Muestra los cursos de un estudiante en otra actividad.
    private fun verCursos(estudiante: Estudiante) {
        Log.d("MainActivity", "ID del estudiante seleccionado: ${estudiante.id}")
        val intent = Intent(this, ListaCursosActivity::class.java)
        intent.putExtra("ESTUDIANTE_ID", estudiante.id) // Pasa el ID del estudiante.
        startActivity(intent) // Inicia la actividad de lista de cursos.
    }
}
