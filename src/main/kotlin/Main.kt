
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.application
import androidx.compose.ui.unit.dp
import java.io.File

fun main() = application {

    val icon = painterResource("sample.png")
    val windowState = GetWindowState(
        windowWidth = 800.dp,
        windowHeight = 800.dp
    )
    val fileManagement = FileManagement()
    val studentsFile = File("studentList.txt")

    MainWindowStudents(
        title = "My Students",
        icon = icon,
        windowState = windowState,
        resizable = false,
        fileManagement = fileManagement,
        studentsFile = studentsFile,
        onCloseMainWindow = { exitApplication() }
    )
}