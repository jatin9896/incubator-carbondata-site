package services

import com.typesafe.config.ConfigFactory
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory

class HttpService extends WebService {
  val logger = LoggerFactory.getLogger(classOf[ConfFileService])

  /**
    * gets content of the file through rest call
    *
    * @param fileUrl file Url
    * @return contents of the file in responseBody if found else None is returned
    */
  def dataOnPostRequest(fileUrl: String): Option[String] = {
    val httpClient = new DefaultHttpClient()
    val httpRequest: HttpPost = new HttpPost(ConfigFactory.load().getString("mdLink"))
    httpRequest.setHeader("Content-type", "text/plain")

    val test = new StringEntity(fileUrl)
    httpRequest.setEntity(test)
    val httpResponse: HttpResponse = httpClient.execute(httpRequest)
    val responseBody = EntityUtils.toString(httpResponse.getEntity)
    logger.info(s"status : {${httpResponse.getStatusLine.toString.contains("OK")}}")
    if (httpResponse.getStatusLine.toString.contains("OK")) {
      Some(responseBody.toString)
    }
    else {
      logger.error(s"Fetching file fails {${httpResponse.getStatusLine}}")
      None
    }

  }

  /**
    * get content of file on get request
    *
    * @param fileUrl file url
    * @return contents of the file
    */
  def dataOnGetRequest(fileUrl: String): String = {
    scala.io.Source.fromURL(fileUrl).mkString
  }


}
