import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.io.File

class StudentsViewModel(
    private val fileManagement: IFiles,
    private val studentsFile: File
) {
    private var _newStudent = mutableStateOf("")
    val newStudent: State<String> = _newStudent
    fun addStudent() {
        if (_newStudent.value.isNotBlank()) {
            _students.add(_newStudent.value.trim())
            _newStudent.value = ""
        }
    }

    fun newStudentChange(name: String) {
        if (name.length <= MAXCHARACTERS) {
            _newStudent.value = name
        }
    }

    fun newStudentFocusRequester() {

    }
}