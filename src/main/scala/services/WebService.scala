package services

trait WebService {
  def dataOnPostRequest(fileUrl: String): Option[String]

  def dataOnGetRequest(fileUrl: String): String
}
