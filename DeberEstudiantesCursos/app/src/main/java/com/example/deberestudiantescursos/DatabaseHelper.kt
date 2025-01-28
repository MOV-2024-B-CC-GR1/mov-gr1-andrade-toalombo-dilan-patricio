package com.example.deberestudiantescursos

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Clase DatabaseHelper para manejar la creación y gestión de la base de datos SQLite
class DatabaseHelper(
    context: Context // Contexto proporcionado por la actividad o aplicación
) : SQLiteOpenHelper(context, "estudiantesDB", null, 1) { // Nombre de la base de datos: "estudiantesDB", versión: 1

    // Método llamado al crear la base de datos por primera vez
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla Estudiante
        db?.execSQL(
            """
            CREATE TABLE Estudiante (
                id INTEGER PRIMARY KEY AUTOINCREMENT, -- ID único del estudiante (clave primaria)
                nombre TEXT NOT NULL,                -- Nombre del estudiante (obligatorio)
                edad INTEGER NOT NULL,               -- Edad del estudiante (obligatorio)
                promedio REAL NOT NULL               -- Promedio del estudiante (obligatorio, tipo real para decimales)
            )
            """
        )

        // Crear tabla Curso
        db?.execSQL(
            """
            CREATE TABLE Curso (
                id INTEGER PRIMARY KEY AUTOINCREMENT,     -- ID único del curso (clave primaria)
                nombre TEXT NOT NULL,                    -- Nombre del curso (obligatorio)
                descripcion TEXT NOT NULL,               -- Descripción del curso (obligatoria)
                duracionHoras INTEGER NOT NULL,          -- Duración del curso en horas (obligatoria)
                estudianteId INTEGER NOT NULL,           -- ID del estudiante asociado (clave foránea)
                FOREIGN KEY (estudianteId) REFERENCES Estudiante(id) -- Relación con la tabla Estudiante
            )
            """
        )
    }

    // Método llamado cuando se actualiza la versión de la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Elimina las tablas existentes si ya existen
        db?.execSQL("DROP TABLE IF EXISTS Curso")      // Eliminar la tabla Curso
        db?.execSQL("DROP TABLE IF EXISTS Estudiante") // Eliminar la tabla Estudiante

        // Crear nuevamente las tablas
        onCreate(db)
    }
}
