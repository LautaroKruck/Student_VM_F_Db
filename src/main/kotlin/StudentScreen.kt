import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester

@Composable
@Preview
fun StudentScreen(
    studentViewModel : IStudentViewModel
) {

    val newStudent by studentViewModel.newStudent
    val students = studentViewModel.students

    val newStudentFocusRequester = remember { FocusRequester() }
    val studentListFocusRequester = remember { FocusRequester() }

    val infoMessage by studentViewModel.infoMessage
    val showInfoMessage by studentViewModel.showInfoMessage

    val showImgScrollStudentList = remember { derivedStateOf { studentViewModel.shouldShowScrollStudentListImage() } }

    val selectedIndex by studentViewModel.selectedIndex

    LaunchedEffect(key1 = true) {  // key1 = true asegura que esto se ejecute solo una vez
        // Carga inicial de datos desde un archivo
        studentViewModel.loadStudents()
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
                    studentViewModel.newStudentChange(it)
                },
                onButtonAddNewStudentClick = {
                    studentViewModel.addStudent()
                    newStudentFocusRequester.requestFocus()
                }
            )
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                StudentList(
                    studentsState = students,
                    selectedIndex = selectedIndex,
                    focusRequester = studentListFocusRequester,
                    onStudentSelected = { index -> studentViewModel.studentSelected(index) },
                    onIconDeleteStudentClick = { index -> studentViewModel.removeStudent(index) },
                    onButtonClearStudentsClick = { studentViewModel.clearStudents() },

                )
                ImageUpDownScroll(
                    showImgScrollStudentList = showImgScrollStudentList.value,
                )
            }
        }
        SaveChangesButton(
            modifier = Modifier.fillMaxSize().weight(1f),
            onButtonSaveChangesClick = {
                studentViewModel.saveStudents()
                newStudentFocusRequester.requestFocus()
            }
        )
    }

    if (showInfoMessage) {
        InfoMessage(
            message = infoMessage,
            onCloseInfoMessage = {
                studentViewModel.showInfoMessage(false)
                newStudentFocusRequester.requestFocus()
            }
        )
    }

    // Solicitar el foco solo cuando cambia el tamaño de la lista
    LaunchedEffect(students.size) {
        newStudentFocusRequester.requestFocus()
    }
}