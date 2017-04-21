package services

import java.io.{File, PrintWriter}


class FileService extends DataService {

  /**
    * writes file to the destination provided by path parameter
    *
    * @param path storage location of the file
    * @param data contents of the file
    */
  def writeToFile(path: String, data: String): Boolean = {
    val writer = new PrintWriter(new File(path))
    writer.write(data)
    writer.close()
    true
  }

  def readFromFile(path:String): String ={
    scala.io.Source.fromFile(path).mkString
  }

}
