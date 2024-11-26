package models

import java.util.Date

data class Estudiante(
    val id: Int,
    var nombre: String,
    var edad: Int,
    var activo: Boolean,
    var fechaRegistro: Date
)
