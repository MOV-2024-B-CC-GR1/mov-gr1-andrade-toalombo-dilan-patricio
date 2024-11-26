package utils

import java.io.File

// El objeto FileHandler proporciona funciones utilitarias para leer y escribir archivos.
// En este caso, se trata de archivos de texto, donde los datos se leen línea por línea o se escriben como una lista de cadenas.
object FileHandler {

    //  Método para leer un archivo de texto y devolver su contenido como una lista de líneas.
    // Recibe la ruta del archivo como parámetro y devuelve una lista de cadenas, donde cada cadena
    // corresponde a una línea del archivo.
    fun leerArchivo(ruta: String): List<String> {
        // Utiliza la función readLines de la clase File para leer el contenido del archivo línea por línea.
        return File(ruta).readLines()
    }

    // Método para escribir el contenido en un archivo de texto.
    // Recibe la ruta del archivo y una lista de cadenas, que se escriben como texto en el archivo.
    // Si el archivo ya existe, se sobrescribe. Si no existe, se crea un nuevo archivo.
    fun escribirArchivo(ruta: String, contenido: List<String>) {
        // Utiliza la función writeText de la clase File para escribir las cadenas en el archivo.
        // Las cadenas en la lista se unen con un salto de línea entre ellas.
        File(ruta).writeText(contenido.joinToString("\n"))
    }
}
