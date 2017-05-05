package services

import java.io.{File, PrintWriter}

import org.slf4j.LoggerFactory


class FileService extends DataService {

  val logger = LoggerFactory.getLogger("FileService")

  /**
    * writes file to the destination provided by path parameter
    *
    * @param path storage location of the file
    * @param data contents of the file
    */
  def writeToFile(path: String, data: String): Boolean = {
    try {
      val writer: PrintWriter = new PrintWriter(new File(path))
      writer.write(data)
      writer.close()
      true
    }
    catch {
      case ex: Exception =>
        logger.error(s"Fails to write the file at path $path")
        false
    }
  }


  def readFromFile(path: String): String = {
    val fileTest: File = new File(path)
    if (fileTest.isFile && fileTest.exists()) {
      scala.io.Source.fromFile(path).mkString
    }
    else {
      logger.error(s"Fails to read file at location $path")
      logger.info("Returning the empty content")
      ""
    }
  }

}
