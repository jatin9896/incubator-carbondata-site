package services

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.io.Source


class ConfFileService extends ResourceService {
  val logger = LoggerFactory.getLogger(classOf[ConfFileService])

  /**
    * Reads a filename from application.conf file
    *
    * @param key key in conf file having String
    * @return string
    */
  def readString(key: String): String = {
    ConfigFactory.load().getString(key)
  }

  /**
    * Reads list of filename from application.conf file
    *
    * @param key key in conf file having Array of String
    * @return list of string
    */
  def readListOfString(key: String): List[String] = {
    val listOfFiles = ConfigFactory.load().getStringList(key).asScala.toList
    logger.info(s"List of files : $listOfFiles")
    listOfFiles
  }
}
