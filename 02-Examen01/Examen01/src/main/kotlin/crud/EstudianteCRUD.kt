package crud

import models.Estudiante
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

// Esta clase maneja las operaciones de Crear, Leer, Actualizar y Eliminar (CRUD) para estudiantes.
// Los datos de los estudiantes se almacenan en un archivo CSV. Cada estudiante tiene un ID, nombre, edad,
// estado activo y fecha de registro.
class EstudianteCRUD(private val archivo: String) {

    // Utilizamos SimpleDateFormat para manejar las fechas en formato de texto legible, con formato "yyyy-MM-dd".
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    // Método para agregar un nuevo estudiante al archivo.
    // Este método toma un objeto Estudiante, lo convierte en una línea de texto con formato CSV
    // y lo agrega al final del archivo.
    fun crear(estudiante: Estudiante) {
        val data = "${estudiante.id},${estudiante.nombre},${estudiante.edad},${estudiante.activo},${dateFormat.format(estudiante.fechaRegistro)}\n"
        File(archivo).appendText(data) // Escribe la nueva línea en el archivo.
        println("Estudiante agregado: $estudiante") // Mensaje de confirmación en consola.
    }

    // Método para leer todos los estudiantes desde el archivo.
    // Este método abre el archivo CSV, lee todas las líneas, las convierte en objetos Estudiante y
    // devuelve una lista con todos los estudiantes.
    fun leer(): List<Estudiante> {
        val estudiantes = mutableListOf<Estudiante>() // Lista vacía para almacenar los estudiantes.
        File(archivo).forEachLine { linea -> // Recorre cada línea del archivo.
            val partes = linea.split(",") // Divide la línea por las comas.
            if (partes.size == 5) { // Verifica que haya exactamente 5 campos en la línea.
                val estudiante = Estudiante(
                    id = partes[0].toInt(), // Convierte el ID a entero.
                    nombre = partes[1], // Obtiene el nombre como texto.
                    edad = partes[2].toInt(), // Convierte la edad a entero.
                    activo = partes[3].toBoolean(), // Convierte el estado a booleano.
                    fechaRegistro = dateFormat.parse(partes[4]) // Convierte la fecha de texto a objeto Date.
                )
                estudiantes.add(estudiante) // Agrega el estudiante a la lista.
            }
        }
        return estudiantes // Devuelve la lista de estudiantes.
    }

    // Método para actualizar un estudiante existente.
    // Este método toma el ID del estudiante a modificar y los nuevos valores para sus atributos.
    // Si un valor no se proporciona, se mantiene el valor original.
    fun actualizar(id: Int, nombre: String? = null, edad: Int? = null, activo: Boolean? = null) {
        val estudiantes = leer() // Lee todos los estudiantes del archivo.
        val actualizado = estudiantes.map { estudiante ->
            if (estudiante.id == id) {
                // Si el ID coincide, actualiza solo los valores proporcionados.
                estudiante.copy(
                    nombre = nombre ?: estudiante.nombre, // Si nombre no es nulo, se usa el nuevo valor.
                    edad = edad ?: estudiante.edad, // Lo mismo para edad.
                    activo = activo ?: estudiante.activo // Y para el estado.
                )
            } else estudiante // Si el ID no coincide, deja el estudiante sin cambios.
        }
        guardarTodos(actualizado) // Guarda todos los estudiantes (incluido el actualizado) en el archivo.
        println("Estudiante actualizado.") // Mensaje de confirmación.
    }

    // Método para eliminar un estudiante por su ID.
    // Este método lee todos los estudiantes, filtra al que tenga el ID dado, y guarda el resto en el archivo.
    fun eliminar(id: Int) {
        val estudiantes = leer().filter { it.id != id } // Filtra los estudiantes que no tienen el ID dado.
        guardarTodos(estudiantes) // Sobrescribe el archivo con los estudiantes restantes.
        println("Estudiante eliminado.") // Mensaje de confirmación.
    }

    // Método privado para guardar una lista de estudiantes en el archivo.
    // Este método sobrescribe completamente el contenido del archivo con los datos actuales.
    private fun guardarTodos(estudiantes: List<Estudiante>) {
        val contenido = estudiantes.joinToString("\n") { estudiante ->
            // Convierte cada estudiante a una línea de texto con formato CSV.
            "${estudiante.id},${estudiante.nombre},${estudiante.edad},${estudiante.activo},${dateFormat.format(estudiante.fechaRegistro)}"
        }
        File(archivo).writeText(contenido) // Sobrescribe el archivo con las nuevas líneas.
    }
}
