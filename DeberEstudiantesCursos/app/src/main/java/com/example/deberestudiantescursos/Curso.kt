package com.example.deberestudiantescursos

// Clase de datos (data class) para representar un Curso
data class Curso(
    val id: Int, // ID único del curso (clave primaria en la base de datos)
    val nombre: String, // Nombre del curso
    val descripcion: String, // Descripción del curso (detalles sobre su contenido o propósito)
    val duracionHoras: Int, // Duración del curso en horas
    val estudianteId: Int // ID del estudiante al que pertenece este curso (clave foránea en la base de datos)
)
