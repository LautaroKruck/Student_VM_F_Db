import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.delay
import java.io.File
import java.sql.Connection

@Composable
@Preview
fun StudentScreen(
    fileManagement: IFiles,
    studentsFile: File,
) {
    val maxCharacters = 10
    val maxNumStudentsVisible = 7

    val (newStudent, setNewStudent) = remember { mutableStateOf("") }
    val studentsState = remember { mutableStateListOf<String>() }

    val newStudentFocusRequester = remember { FocusRequester() }
    val studentListFocusRequester = remember { FocusRequester() }

    val (infoMessage, setInfoMessage) = remember { mutableStateOf("") }
    val (showInfoMessage, setShowInfoMessage) = remember { mutableStateOf(false) }

    val showImgScrollStudentList = remember { derivedStateOf { studentsState.size > maxNumStudentsVisible } }

    val (selectedIndex, setSelectedIndex) = remember { mutableStateOf(-1) } // -1 significa que no hay selección

    LaunchedEffect(key1 = true) {  // key1 = true asegura que esto se ejecute solo una vez
        // Carga inicial de datos desde un archivo
        val loadedStudents = fileManagement.leer(studentsFile)
        if (loadedStudents != null) {
            studentsState.addAll(loadedStudents)
        } else {
            setInfoMessage("No se pudieron cargar los datos de los estudiantes.")
            setShowInfoMessage(true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize().weight(3f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AddNewStudent(
                newStudent = newStudent,
                focusRequester = newStudentFocusRequester,
                onNewStudentChange = {
                    if (it.length <= maxCharacters) {
                        setNewStudent(it)
                    }
                },
                onButtonAddNewStudentClick = {
                    if (newStudent.isNotBlank()) {
                        studentsState.add(newStudent.trim())
                        setNewStudent("")
                    }
                    newStudentFocusRequester.requestFocus()
                }
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                StudentList(
                    studentsState = studentsState,
                    selectedIndex = selectedIndex,
                    focusRequester = studentListFocusRequester,
                    onStudentSelected = { index -> setSelectedIndex(index) },
                    onIconDeleteStudentClick = { studentsState.removeAt(it) }
                ) {
                    studentsState.clear()
                }
                ImageUpDownScroll(
                    showImgScrollStudentList = showImgScrollStudentList.value,
                )
            }
        }
        SaveChangesButton(
            modifier = Modifier.fillMaxSize().weight(1f),
            onButtonSaveChangesClick = {
                var error = ""
                val newStudentsFile = fileManagement.crearFic(studentsFile.absolutePath)
                if (newStudentsFile != null) {
                    for (student in studentsState) {
                        error = fileManagement.escribir(studentsFile, "$student\n")
                        if (error.isNotEmpty()) {
                            break
                        }
                    }
                    if (error.isNotEmpty()) {
                        setInfoMessage(error)
                    } else {
                        setInfoMessage("Fichero guardado correctamente")
                    }
                } else {
                    setInfoMessage("No se pudo generar el fichero studentList.txt")
                }
                setShowInfoMessage(true)
            }
        )
    }

    // Gestión de la visibilidad del mensaje informativo
    if (showInfoMessage) {
        InfoMessage(
            message = infoMessage,
            onDismiss = {
                setShowInfoMessage(false)
                setInfoMessage("")
                newStudentFocusRequester.requestFocus()
            }
        )
    }

    // Solicitar el foco solo cuando cambia el tamaño de la lista
    LaunchedEffect(studentsState.size) {
        newStudentFocusRequester.requestFocus()
    }

    // Automáticamente oculta el mensaje después de un retraso
    LaunchedEffect(showInfoMessage) {
        if (showInfoMessage) {
            delay(2000)
            setShowInfoMessage(false)
            setInfoMessage("")
            newStudentFocusRequester.requestFocus()
        }
    }
}

@Composable
fun StudentScreen(
    viewModel : StudentsViewModel
) {
    val newStudent by viewModel.newStudent

    AddNewStudent(
        newStudent = newStudent,
        focusRequester = newStudentFocusRequester,
        onNewStudentChange = { name -> viewModel.newStudentChange(name) },
        onButtonAddNewStudentClick = {
            viewModel.addStudent()
            newStudentFocusRequester.requestFocus()
        }
    )

}

fun getAllStudents(): Result<List<String>> {
    return try {
        val connectionDb = Database.getConnection()
        val students = mutableListOf<String>()
        connectionDb.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT name FROM students").use { rs ->
                    while (rs.next()) {
                        students.add(rs.getString("name"))
                    }
                }
            }
        }
        Result.success(students)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun updateStudents(students: List<String>): Result<Unit> {
    var connectionDb : Connection? = null
    return try {
        connectionDb = Database.getConnection()
        connectionDb.autoCommit = false
        connectionDb.createStatement().use { stmt ->
            stmt.execute("DELETE FROM students")
        }
        connectionDb.prepareStatement("INSERT INTO students (name) VALUES (?)").use { ps ->
            for (student in students) {
                ps.setString(1, student)
                ps.executeUpdate()
            }
        }
        connectionDb.commit()
        Result.success(Unit)
    } catch (e: Exception) {
        connectionDb?.rollback()
        Result.failure(e)
    } finally {
        connectionDb?.autoCommit = true
        connectionDb?.close()
    }
}