
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StudentViewModelDb(
    private val repository: StudentRepository,

    ) : IStudentViewModel {

    companion object {
        private const val MAXCHARACTERS = 10
        private const val MAXNUMSTUDENTSVISIBLE = 7
    }

    private var _newStudent = mutableStateOf("")
    override val newStudent: State<String> = _newStudent

    private val _students = mutableStateListOf<String>()
    override val students: List<String> = _students

    private val _infoMessage = mutableStateOf("")
    override val infoMessage: State<String> = _infoMessage

    private val _showInfoMessage = mutableStateOf(false)
    override val showInfoMessage: State<Boolean> = _showInfoMessage

    private val _selectedIndex = mutableStateOf(-1)
    override val selectedIndex: State<Int> = _selectedIndex

    private val _showEditStudent = mutableStateOf(false)
    override val showEditStudent: State<Boolean> = _showEditStudent


    override fun showEditStudent(show: Boolean) {
        _showEditStudent.value = show
    }

    override fun editStudent(selectedStudent: Int, newName: String) {
        _students[selectedStudent] = newName
    }

    override fun addStudent() {
        if (_newStudent.value.isNotBlank()) {
            _students.add(_newStudent.value.trim())
            _newStudent.value = ""
        }
    }

    override fun removeStudent(index: Int) {
        _students.removeAt(index)
    }

    override fun loadStudents() {
        val loadedStudents = repository.getAllStudents()
        _students.addAll(loadedStudents)
    }

    override fun saveStudents() {
        try {
            repository.updateStudents(_students)
            updateInfoMessage("Guardado correctamente")

        } catch (e: Exception) {
            updateInfoMessage("Error al guardar")
        }
    }

    override fun clearStudents() {
        _students.clear()
    }

    override fun shouldShowScrollStudentListImage(): Boolean = _students.size > MAXNUMSTUDENTSVISIBLE

    override fun newStudentChange(name: String) {
        _newStudent.value = name
    }

    override fun studentSelected(index: Int) {
        _selectedIndex.value = index
    }

    override fun showInfoMessage(show: Boolean) {
        _showInfoMessage.value = show
    }

    private fun updateInfoMessage(message: String) {
        _infoMessage.value = message
        _showInfoMessage.value = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            _showInfoMessage.value = false
            _infoMessage.value = ""
        }
    }
}
