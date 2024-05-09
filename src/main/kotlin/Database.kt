import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private const val URL = "jdbc:mysql://localhost:3306/studentdb"
    private const val USER = "studentuser"
    private const val PASSWORD = "password"

    init {
        try {
            // Asegurarse de que el driver JDBC de MySQL esté disponible
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
    fun getConnection(): Connection? {
        var conn: Connection? = null
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return conn
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}