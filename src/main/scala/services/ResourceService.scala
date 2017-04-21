package services


trait ResourceService {

  def readString(key: String): String

  def readListOfString(key: String): List[String]

}
