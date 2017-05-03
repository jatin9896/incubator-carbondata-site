package services

trait DataService {
  def writeToFile(path: String, data: String): Boolean
  def readFromFile(path:String):String
}
