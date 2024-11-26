package crud

import models.RelacionEstudianteCurso
import java.io.File

// La clase RelacionCRUD maneja las operaciones CRUD (Crear, Leer, Eliminar) para las relaciones entre estudiantes y cursos.
// Esta clase utiliza un archivo CSV como almacenamiento de las relaciones entre los estudiantes y los cursos.
// Cada relación tiene un ID de estudiante y un ID de curso.
class RelacionCRUD(private val archivo: String) {

    // Método para agregar una nueva relación entre un estudiante y un curso al archivo.
    // Recibe un objeto RelacionEstudianteCurso, que contiene los IDs del estudiante y el curso relacionados,
    // y lo agrega como una nueva línea en el archivo CSV.
    fun crear(relacion: RelacionEstudianteCurso) {
        // Se convierte el objeto RelacionEstudianteCurso en una cadena de texto con formato CSV (ID del estudiante, ID del curso).
        // La nueva relación se agrega al final del archivo con el método appendText.
        File(archivo).appendText("${relacion.estudianteId},${relacion.cursoId}\n")
        // Imprime un mensaje de confirmación en consola, indicando que la relación ha sido agregada.
        println("Relación agregada: $relacion")
    }

    // Método para leer todas las relaciones almacenadas en el archivo CSV.
    // Este método abre el archivo, lee cada línea, la convierte en un objeto RelacionEstudianteCurso y
    // devuelve una lista de todas las relaciones.
    fun leer(): List<RelacionEstudianteCurso> {
        val relaciones = mutableListOf<RelacionEstudianteCurso>() // Lista vacía para almacenar las relaciones.

        // Recorre cada línea del archivo CSV.
        File(archivo).forEachLine { linea ->
            val partes = linea.split(",") // Divide la línea en partes usando la coma como separador.

            // Verifica que la línea tenga exactamente 2 partes (estudianteId y cursoId).
            if (partes.size == 2) {
                // Convierte las partes de la línea en un objeto RelacionEstudianteCurso.
                val relacion = RelacionEstudianteCurso(
                    estudianteId = partes[0].toInt(), // Convierte el primer valor en un ID de estudiante.
                    cursoId = partes[1].toInt() // Convierte el segundo valor en un ID de curso.
                )
                // Agrega la relación a la lista de relaciones.
                relaciones.add(relacion)
            }
        }

        // Devuelve la lista completa de relaciones leídas desde el archivo.
        return relaciones
    }

    // Método para eliminar una relación específica basada en el ID de estudiante y el ID de curso.
    // Este método filtra las relaciones que no coinciden con los valores de estudianteId y cursoId proporcionados
    // y guarda las relaciones restantes en el archivo.
    fun eliminar(estudianteId: Int, cursoId: Int) {
        // Filtra todas las relaciones para que solo queden aquellas cuya combinación de estudianteId y cursoId
        // no coincidan con los parámetros proporcionados.
        val relaciones = leer().filter { it.estudianteId != estudianteId || it.cursoId != cursoId }
        // Llama al método privado guardarTodos para sobrescribir el archivo con las relaciones filtradas.
        guardarTodos(relaciones)
        // Imprime un mensaje de confirmación indicando que la relación ha sido eliminada.
        println("Relación eliminada.")
    }

    // Método privado que sobrescribe el archivo con una nueva lista de relaciones.
    // Recibe una lista de objetos RelacionEstudianteCurso y la convierte en texto CSV,
    // luego guarda el contenido en el archivo, sobrescribiéndolo completamente.
    private fun guardarTodos(relaciones: List<RelacionEstudianteCurso>) {
        // Convierte cada relación en una línea CSV, separando el estudianteId y cursoId por una coma.
        val contenido = relaciones.joinToString("\n") {
            "${it.estudianteId},${it.cursoId}"
        }
        // Sobrescribe el archivo con el contenido de las relaciones actualizadas.
        File(archivo).writeText(contenido)
    }
}
