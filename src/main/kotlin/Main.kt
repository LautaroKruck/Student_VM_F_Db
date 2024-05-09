import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File

fun main() = application {

    val icon = painterResource("sample.png")
    val windowState = GetWindowState(
        windowWidth = 800.dp,
        windowHeight = 800.dp
    )
    val fileManagement = FileManagement()
    val studentsFile = File("studentList.txt")
    val repository = StudentRepository()

    val studentViewModelFile =  StudentViewModelFile(fileManagement, studentsFile)
    val studentViewModelDb = StudentViewModelDb(repository)

    var selectedViewModel by remember { mutableStateOf<IStudentViewModel?>(null) }

    var title by remember { mutableStateOf("") }

    if (selectedViewModel == null) {

        Window(
            onCloseRequest = { exitApplication() },
            title = "Select ViewModel",
            resizable = false,
            state = windowState,
            icon = icon
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedViewModel == null) {
                    Button(
                        onClick = {
                            selectedViewModel = studentViewModelFile
                            title = "My Students File"
                        }
                    ) {
                        Text("File")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            selectedViewModel = studentViewModelDb
                            title = "My Students DB"
                        }
                    ) {
                        Text("Database")
                    }
                }
            }
        }
    }

    selectedViewModel?.let { viewModel ->
        MainWindowStudents(
            title = title,
            icon = icon,
            windowState = windowState,
            resizable = false,
            onCloseMainWindow = { exitApplication() },
            studentViewModel =  viewModel
        )
    }
}