import java.io.{File, PrintWriter}

import com.google.inject.Inject
import org.slf4j.LoggerFactory
import services.{DataService, ResourceService}

import scala.util.matching.Regex

/**
  * Created by pallavi on 4/4/17.
  */
class MdFilehandler @Inject()(resourceService: ResourceService,dataService: DataService) {

  val logger = LoggerFactory.getLogger(classOf[MdFilehandler])

  /**
    * converts .md extension to .html extension
    * changes location of image from local to git repository
    *
    * @param input
    * @return contents of file
    */
  def ConvertMdExtension(input: String): String = {
    val modifyContentPattern = new Regex("id=\"user-content-")
    val modifyMdPattern = new Regex(".md")
    val modifyImagePattern = new Regex("<img src=\"../docs")
    val modifyHttpsFileLink ="""(<a href=\"https)://([a-zA-Z0-9-/.]+)(\")""".r
    val modifyHttpFileLink ="""(<a href=\"http)://([a-zA-Z0-9-/.]+)(\")""".r
    val contentAfterRemovingUserContent: String = modifyContentPattern replaceAllIn(input, "id=\"")
    val contentAfterReplacingId: String = modifyMdPattern replaceAllIn(contentAfterRemovingUserContent, ".html")
    val contentAfterReplacingImage: String = modifyImagePattern replaceAllIn(contentAfterReplacingId, "<img src=\"https://github.com/apache/incubator-carbondata/blob/master/docs")
    val contentAfterReplacingHttpsFileLink: String = modifyHttpsFileLink replaceAllIn(contentAfterReplacingImage, "$1://$2$3 target=_blank")
    val contentAfterReplacingFileLink: String = modifyHttpFileLink replaceAllIn(contentAfterReplacingHttpsFileLink, "$1://$2$3 target=_blank")
    contentAfterReplacingFileLink
  }

  /**
    * This handles the exeception cases for retaining .md extensions in some files
    *
    * @return list of files
    */
  def ConvertReadMeExtension(): List[String] = {
    val listOfFiles = resourceService.readListOfString("fileListToRetain")
    logger.info(s"List of files to retain .md extensions : $listOfFiles")
    val location = resourceService.readString("outputFileLocation")
    val outputFileExtension = ".html"
    val modifyMdPattern = new Regex("(README)(.html)")
    listOfFiles.map { file =>
      val fileURLContent = dataService.readFromFile("src/main/webapp/" + file + outputFileExtension)
      val fileContent = modifyMdPattern replaceAllIn(fileURLContent, "$1.md")
      dataService.writeToFile(location + file + outputFileExtension,fileContent)
      fileContent
    }

  }

}

