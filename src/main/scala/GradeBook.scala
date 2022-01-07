import java.sql.DriverManager
import java.sql.Connection
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * A Scala JDBC connection example by Alvin Alexander,
 * https://alvinalexander.com
 */
object GradeBook {

  def main(args: Array[String]):Unit = {
    // connect to the database named "mysql" on the localhost
    val driver = "com.mysql.cj.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/training" // Modify for whatever port you are running your DB on
    val username = "root"
    val password = "Spart@11" // Update to include your password

    MyReader.MyReader("names.txt")


    // var nums = new RandomNums
    // var arr2 = nums.SetOfrandom(11)
    // println(arr2)
    // arr2.foreach(x => println(x))

    // var connection:Connection = null

    // try {
    //   // make the connection
    //   Class.forName(driver)
    //   connection = DriverManager.getConnection(url, username, password)

    //   // create the statement, and run the select query
    //   val statement = connection.createStatement()
    //   val resultSet = statement.executeQuery("SELECT * FROM transactions") // Change query to your table
    //   while ( resultSet.next() ) {
    //     print(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3))
    //     println()
    //   }
    // } catch {
    //   case e: Exception => e.printStackTrace
    // }
    // connection.close()
  }


  class RandomNums {
      val r = scala.util.Random

      def SetOfrandom(x: Int): ArrayBuffer[Int] = {
        var arr = new ArrayBuffer[Int]()

        for (i <- 1 until x) {
            arr += r.nextInt
        }
        return arr

      }
    }

    object MyReader {
        val nameArray = ArrayBuffer[String]()
        def MyReader(x: String) {
            for (line <- io.Source.fromFile(x).getLines) {
                nameArray += line
            }
            println(nameArray)
        }
    }


}