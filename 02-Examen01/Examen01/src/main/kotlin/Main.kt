import crud.CursoCRUD
import crud.EstudianteCRUD
import crud.RelacionCRUD
import models.Curso
import models.Estudiante
import models.RelacionEstudianteCurso
import java.util.Date

fun main() {
    // Rutas de los archivos CSV
    val archivoEstudiantes = "src/main/resources/estudiantes.csv"
    val archivoCursos = "src/main/resources/cursos.csv"
    val archivoRelaciones = "src/main/resources/relaciones.csv"

    // Inicializar las clases CRUD
    val estudianteCRUD = EstudianteCRUD(archivoEstudiantes)
    val cursoCRUD = CursoCRUD(archivoCursos)
    val relacionCRUD = RelacionCRUD(archivoRelaciones)

    // Crear estudiantes
    estudianteCRUD.crear(Estudiante(1, "Juan", 20, true, Date()))
    estudianteCRUD.crear(Estudiante(2, "Ana", 22, true, Date()))
    estudianteCRUD.crear(Estudiante(3, "Pedro", 19, true, Date()))
    estudianteCRUD.crear(Estudiante(4, "María", 23, true, Date()))
    estudianteCRUD.crear(Estudiante(5, "Lucía", 21, true, Date()))

    // Crear cursos
    cursoCRUD.crear(Curso(1, "Kotlin Básico", "Curso introductorio de Kotlin", 10.5f, true))
    cursoCRUD.crear(Curso(2, "Kotlin Avanzado", "Curso avanzado de Kotlin", 15.0f, true))
    cursoCRUD.crear(Curso(3, "Java Intermedio", "Curso para mejorar en Java", 12.0f, true))
    cursoCRUD.crear(Curso(4, "Desarrollo Web", "Curso de introducción al desarrollo web", 8.0f, true))
    cursoCRUD.crear(Curso(5, "Bases de Datos", "Curso sobre fundamentos de bases de datos", 14.0f, true))

    // Leer y mostrar estudiantes
    println("\nEstudiantes registrados:")
    estudianteCRUD.leer().forEach { println(it) }

    // Leer y mostrar cursos
    println("\nCursos registrados:")
    cursoCRUD.leer().forEach { println(it) }

    // Crear relaciones entre estudiantes y cursos
    relacionCRUD.crear(RelacionEstudianteCurso(1, 1))
    relacionCRUD.crear(RelacionEstudianteCurso(1, 2))
    relacionCRUD.crear(RelacionEstudianteCurso(2, 3))
    relacionCRUD.crear(RelacionEstudianteCurso(3, 4))
    relacionCRUD.crear(RelacionEstudianteCurso(4, 5))

    // Leer y mostrar relaciones
    println("\nRelaciones registradas:")
    relacionCRUD.leer().forEach { println(it) }

    // Actualizar un estudiante
    println("\nActualizando estudiante con ID 1...")
    estudianteCRUD.actualizar(1, nombre = "Juan Pérez", edad = 21)

    // Actualizar un curso
    println("\nActualizando curso con ID 2...")
    cursoCRUD.actualizar(2, titulo = "Kotlin Avanzado Completo", descripcion = "Curso mejorado con más contenido")

    // Eliminar un estudiante
    println("\nEliminando estudiante con ID 5...")
    estudianteCRUD.eliminar(5)

    // Eliminar un curso
    println("\nEliminando curso con ID 4...")
    cursoCRUD.eliminar(4)

    // Eliminar una relación
    println("\nEliminando relación entre estudiante ID 1 y curso ID 2...")
    relacionCRUD.eliminar(1, 2)

    // Leer y mostrar datos actualizados
    println("\nEstudiantes registrados después de actualizaciones:")
    estudianteCRUD.leer().forEach { println(it) }

    println("\nCursos registrados después de actualizaciones:")
    cursoCRUD.leer().forEach { println(it) }

    println("\nRelaciones registradas después de actualizaciones:")
    relacionCRUD.leer().forEach { println(it) }
}
