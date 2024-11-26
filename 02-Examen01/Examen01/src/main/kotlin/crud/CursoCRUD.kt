package crud

import models.Curso
import java.io.File

// Esta clase maneja las operaciones CRUD para los cursos, utilizando un archivo CSV como base de datos.
// Cada curso tiene un ID, título, descripción, duración (en horas) y un estado activo/inactivo.
class CursoCRUD(private val archivo: String) {

    // Método para agregar un nuevo curso al archivo.
    // Este método toma un objeto Curso, lo convierte en una línea de texto con formato CSV
    // y lo agrega al final del archivo.
    fun crear(curso: Curso) {
        val data = "${curso.id},${curso.titulo},${curso.descripcion},${curso.duracionHoras},${curso.activo}\n"
        File(archivo).appendText(data) // Escribe la nueva línea en el archivo.
        println("Curso agregado: $curso") // Mensaje de confirmación en consola.
    }

    // Método para leer todos los cursos almacenados en el archivo.
    // Este método abre el archivo CSV, lee todas las líneas, las convierte en objetos Curso y
    // devuelve una lista con todos los cursos.
    fun leer(): List<Curso> {
        val cursos = mutableListOf<Curso>() // Lista vacía para almacenar los cursos.
        File(archivo).forEachLine { linea -> // Recorre cada línea del archivo.
            val partes = linea.split(",") // Divide la línea por las comas.
            if (partes.size == 5) { // Verifica que haya exactamente 5 campos en la línea.
                val curso = Curso(
                    id = partes[0].toInt(), // Convierte el ID a entero.
                    titulo = partes[1], // Obtiene el título como texto.
                    descripcion = partes[2], // Obtiene la descripción como texto.
                    duracionHoras = partes[3].toFloat(), // Convierte la duración a decimal.
                    activo = partes[4].toBoolean() // Convierte el estado a booleano.
                )
                cursos.add(curso) // Agrega el curso a la lista.
            }
        }
        return cursos // Devuelve la lista de cursos.
    }

    // Método para actualizar un curso existente.
    // Este método toma el ID del curso a modificar y los nuevos valores para sus atributos.
    // Si un valor no se proporciona, se mantiene el valor original.
    fun actualizar(id: Int, titulo: String? = null, descripcion: String? = null, duracionHoras: Float? = null, activo: Boolean? = null) {
        val cursos = leer() // Lee todos los cursos del archivo.
        val actualizado = cursos.map { curso ->
            if (curso.id == id) {
                // Si el ID coincide, actualiza solo los valores proporcionados.
                curso.copy(
                    titulo = titulo ?: curso.titulo, // Si título no es nulo, se usa el nuevo valor.
                    descripcion = descripcion ?: curso.descripcion, // Lo mismo para descripción.
                    duracionHoras = duracionHoras ?: curso.duracionHoras, // Lo mismo para duración.
                    activo = activo ?: curso.activo // Y para el estado.
                )
            } else curso // Si el ID no coincide, deja el curso sin cambios.
        }
        guardarTodos(actualizado) // Guarda todos los cursos (incluido el actualizado) en el archivo.
        println("Curso actualizado.") // Mensaje de confirmación.
    }

    // Método para eliminar un curso por su ID.
    // Este método lee todos los cursos, filtra el que tenga el ID dado, y guarda el resto en el archivo.
    fun eliminar(id: Int) {
        val cursos = leer().filter { it.id != id } // Filtra los cursos que no tienen el ID dado.
        guardarTodos(cursos) // Sobrescribe el archivo con los cursos restantes.
        println("Curso eliminado.") // Mensaje de confirmación.
    }

    // Método privado para guardar una lista de cursos en el archivo.
    // Este método sobrescribe completamente el contenido del archivo con los datos actuales.
    private fun guardarTodos(cursos: List<Curso>) {
        val contenido = cursos.joinToString("\n") { curso ->
            // Convierte cada curso a una línea de texto con formato CSV.
            "${curso.id},${curso.titulo},${curso.descripcion},${curso.duracionHoras},${curso.activo}"
        }
        File(archivo).writeText(contenido) // Sobrescribe el archivo con las nuevas líneas.
    }
}
