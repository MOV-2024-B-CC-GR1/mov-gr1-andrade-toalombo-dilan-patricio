package com.example.deberestudiantescursos

// Declaración de una clase de datos para representar un Estudiante
data class Estudiante(
    val id: Int,           // Identificador único del estudiante (clave primaria en la base de datos)
    val nombre: String,    // Nombre del estudiante
    val edad: Int,         // Edad del estudiante
    val promedio: Double   // Promedio académico del estudiante
)
