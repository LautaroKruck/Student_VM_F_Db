import java.sql.SQLException

class StudentRepository {

    fun getAllStudents(): List<String> {
        val connectionDb = Database.getConnection()
        val students = mutableListOf<String>()
        val sql = "SELECT name FROM students"

        try {
            val stmt = connectionDb?.createStatement()
            val rs = stmt?.executeQuery(sql)
            while (rs?.next() == true) {
                students.add(rs.getString("name"))
            }
            rs?.close()
            stmt?.close()

        } catch (ex: SQLException) {
            ex.printStackTrace()

        } finally {
            Database.closeConnection(connectionDb)
        }
        return students
    }

    fun updateStudents(students: List<String>): Result<Unit> {
        val connectionDb = Database.getConnection()
        var ps: java.sql.PreparedStatement? = null
        val sqlDelete = "DELETE FROM students"
        val sqlInsert = "INSERT INTO students (name) VALUES (?)"

        return try {

            connectionDb?.autoCommit = false

            val stmt = connectionDb?.createStatement()
            stmt?.execute(sqlDelete)
            stmt?.close()
            ps = connectionDb?.prepareStatement(sqlInsert)

            for (student in students) {
                ps?.setString(1, student)
                ps?.executeUpdate()
            }

            connectionDb?.commit()
            Result.success(Unit)

        } catch (e: SQLException) {
            connectionDb?.rollback()
            Result.failure(e)

        } finally {
            ps?.close()
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }
}