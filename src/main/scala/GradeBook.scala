import java.sql.DriverManager
import java.sql.Connection
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import java.util.Scanner
import java.io.PrintWriter
import java.io.File
import java.util.Calendar
import scala.io.StdIn.readLine
import java.util.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
* A Scala JDBC connection example by Alvin Alexander,
* https://alvinalexander.com
*/
object GradeBook {
  def main(args: Array[String]):Unit = {
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/bigdata" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "Spart@11" // Update to include your password
    val log = new PrintWriter(new File("query.log"))
   
    var connection:Connection = null

    // make the connection
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)

    var firstNames = List("Gaylord", "Hosea", "Vera", "Kacy", "Scottie", "Deeanna", "Brant", "Jeffie", "Monte", "Terrell", "Annelle", "Lucila", "Marybelle", "Carmen", "Coretta", "Melida", "Darlene", "Minta", "Roxie", "Theressa")
    var lastNames = List("Balcom", "Jack", "Fuselier", "Poplar", "Wallen", "Soukup", "Dipaola", "Parnell", "Heilig", "Metzer", "Ziolkowski", "Tiger", "Ensign", "Caylor", "Harriss", "Waiters", "Mund", "Lofton", "Galyean", "Hoaglin")
   




  //  PopulateDB(firstNames, lastNames, connection, log)
    println("*****Welcome to GradeBook***")
    println("Enter 1 to populate db.")
    println("Enter 2 to add new student.")
    println("Enter 3 to give assigment to students.")
    println("Enter 4 to see all students")
    println("Enter 5 to rank student by major")
    println("Enter 6 to show all students and their grades.")
    println("Enter 7 to delete student")
    var i = readInt()
    while( i != 99) {
    // i:Int match Block
      i match {
      case 1  => Tasks.PopulateDB(firstNames, lastNames, connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 2  => println("**Adding new student**")
        Tasks.AddStudent(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 3  => Tasks.TestStudents(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 4  => println("Showing all students:")
        Tasks.ShowStudents(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 5  => println("Ranking students.")
        Tasks.RankStudents(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 6  => println("showing all students and their grades")
      Tasks.ShowStudGrades(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt()
      case 7  => println("Deleting a record")
        Tasks.DeleteRecord(connection, log)
        println("Please enter next choice or enter 99 to end program: ")
        i = readInt() 
      // catch the default with a variable so you can print it
      case whoa  => println("Unexpected case: " + whoa.toString)
        println("Please try again or enter 99 to end program: ")
        i = readInt()
      } 
    }

  connection.close()
  log.close()

  }
   object Tasks{
    def PopulateDB(l1:List[String], l2:List[String], connection:Connection, log:java.io.PrintWriter): Unit = {
        val statement = connection.createStatement()
        var r = scala.util.Random
        val Major = (s""""CS"""")
        for (i <- 1 until 20) {
          val StudentID = i + 2912        
          val LastName = (s""""${l2(i)}"""")
          val FirstName = (s""""${l1(i)}"""")
          statement.executeUpdate("INSERT INTO students (StudentID  , firstname  ,  LastName  ,  Major) VALUES ( "+StudentID+"   ,  " +  FirstName + ", " + LastName + " , " + Major + ");")
          log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'INSERT INTO students (StudentID  , firstname  ,  LastName  ,  Major , Gender, AverageScore) VALUES ( "+StudentID+"   ,  " +  FirstName + ", " + LastName +" , " + Major + ";'\n")
        }

        val resultSet = statement.executeQuery("SELECT * FROM students" ) // Change query to your table
        log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'SELECT * FROM students ';\n")
        while (resultSet.next()) {
          print(
            resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4)
          )
          println()
        }
    }


    def AddStudent(connection:Connection, log:java.io.PrintWriter): Unit = {
      val statement = connection.createStatement()
      var r = scala.util.Random
      var sID = 976 + r.nextInt(900)
      println("Enter new student's firstname: ")
      var firstName = readLine()
      firstName = s""""$firstName""""
      println("Lastname: ")
      var lastName = readLine()
      lastName = s""""$lastName""""
      println("Major")
      var major = readLine()
      major = s""""$major""""
      statement.executeUpdate("INSERT INTO students (StudentID  , firstname  ,  LastName  ,  Major) VALUES ( "+sID+" ,  " +  firstName + ", " + lastName + " , " + major + ");")
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'INSERT INTO students (StudentID  , firstname  ,  LastName  ,  Major) VALUES ( "+sID+"   ,  " +  firstName + ", " + lastName +" , " + major +";'\n")
      val resultSet = statement.executeQuery("SELECT * FROM students" ) // Change query to your table
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'SELECT * FROM students ';\n")
      while (resultSet.next()) {
        print(
          resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4)
        )
        println()
      }
    }

    def DeleteRecord(connection:Connection, log:java.io.PrintWriter): Unit = {
      val statement = connection.createStatement()
      println("Please enter studentID to be deleted: ")
      val id = readInt()
      statement.executeUpdate("DELETE FROM Assignments WHERE StudentID = " + id + "" )
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'DELETE FROM Assignments WHERE StudentID =  "+ id +" ';\n")
      statement.executeUpdate("DELETE FROM students WHERE StudentID = " + id + "" ) // Change query to your table
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'DELETE FROM students WHERE StudentID =  "+ id +" ';\n")
      val resultSet = statement.executeQuery("SELECT * FROM students" )
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'SELECT * FROM students ';\n")
      print("StudentID--Firstname-Lastname--Major")
      while (resultSet.next()) {
        print(
          resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4)
        )
        println()
      }

    }




    def TestStudents(connection:Connection, log:java.io.PrintWriter): Unit = {
      val statement = connection.createStatement()
      var arr = ArrayBuffer[Int]()
      var r = scala.util.Random
      println("Please enter assignment subject: ")
      var subj = readLine()
      val resultset = statement.executeQuery("SELECT StudentID FROM Students")
      log.write(Calendar.getInstance().getTimeInMillis + " -Executing 'SELECT StudentID FROM Students';\n")
      while (resultset.next()) {
       arr += resultset.getInt(1)
      }
      var subject = s""""$subj""""
      val aID = 505 + r.nextInt(99)
      var c = LocalDate.now()
      var date = c.toString()
      date = s""""$date""""
      println(date)
      for (i<- 0 until arr.length - 1) {
        var score = 60 + r.nextInt(40)
        var sID = arr(i)
        statement.executeUpdate("INSERT INTO Assignments (StudentID, AssignmentID, Subject, Score, Date) VALUES ( "+sID+" ,  " +  aID + ", " + subject + " , " + score + ", " + date +" );")
        log.write(Calendar.getInstance().getTimeInMillis + " -Executing 'INSERT INTO Assignments(StudentID, AssignmentID, Subject, Score, Date) VALUES ( "+sID+" ,  " +  aID + ", " + subject + " , " + score + ", " + date +";\n")
      }
      val resultset1 = statement.executeQuery("SELECT * FROM Assignments")
      log.write(Calendar.getInstance().getTimeInMillis + " -Executing 'SELECT * FROM Assignments';\n")
      while (resultset1.next()) {
      print(
        resultset1.getString(1) + " " + resultset1.getString(2) + " " + resultset1.getString(3) + " " + resultset1.getString(4) + " " + resultset1.getString(5)
      )
      println()
      }
    }

    def ShowStudents(connection:Connection, log:java.io.PrintWriter): Unit = {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM students" ) // Change query to your table
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'SELECT * FROM students ';\n")
      print("StudentID--Firstname-Lastname--Major")
      while (resultSet.next()) {
        print(
          resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4)
        )
        println()
      }

    }

    def ShowStudGrades(connection:Connection, log:java.io.PrintWriter): Unit = {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("select students.StudentID, students.FirstName, students.LastName, Major, assignments.AssignmentID, assignments.Subject, assignments.Score from Students inner join assignments ON Students.StudentID = assignments.StudentID" ) // Change query to your table
      log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'select students.StudentID, students.FirstName, students.LastName, Major, assignments.AssignmentID, assignments.Subject, assignments.Score from Students inner join assignments ON Students.StudentID = assignments.StudentID ';\n")
      print("StudentID--Firstname-Lastname--Major...")
      while (resultSet.next()) {
        print(
          resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + " "  + resultSet.getString(5) + " " + resultSet.getString(6) + " "  + resultSet.getString(7)
        )
        println()
      }

    }

    def RankStudents(connection:Connection, log:java.io.PrintWriter): Unit = {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT assignments.studentID, students.FirstName, students.LastName, avg(score) as averagescore" +
          " from assignments" + " inner join " +
          " students ON Students.StudentID = assignments.StudentID" +
          " group by students.studentID  order by AverageScore desc ") 
        log.write(Calendar.getInstance().getTimeInMillis + " - Executing 'SELECT assignments.studentID, students.FirstName, students.LastName, avg(score) as averagescore" +
          " from assignments" + " inner join " +
          " students ON Students.StudentID = assignments.StudentID" +
          " group by students.studentID  order by AverageScore desc ';\n")
        while (resultSet.next()) {
          print(
            resultSet.getString(1) + "  " + resultSet.getString(2) + "  " + resultSet.getString(3) + "  " + resultSet.getString(4)
          )
          println()
        }
      }
  }
}
