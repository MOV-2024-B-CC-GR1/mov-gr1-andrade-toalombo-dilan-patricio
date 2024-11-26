package models

data class Curso(
    val id: Int,
    var titulo: String,
    var descripcion: String,
    var duracionHoras: Float,
    var activo: Boolean
)
