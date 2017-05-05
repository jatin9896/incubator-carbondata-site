import com.google.inject.Inject
import org.slf4j.{Logger, LoggerFactory}
import services.{DataService, ResourceService}

import scala.util.matching.Regex

class MdFilehandler @Inject()(resourceService: ResourceService, dataService: DataService) {

  val logger: Logger = LoggerFactory.getLogger(classOf[MdFilehandler])

  /**
    * converts .md extension to .html extension
    * changes location of image from local to git repository
    *
    * @param input
    * @return contents of file
    */
  def convertMdExtension(input: String): String = {
    val modifyContentPattern: Regex = new Regex("id=\"user-content-")
    val modifyMdPattern: Regex = new Regex(".md")
    val modifyImagePattern: Regex = new Regex("<img src=\"../docs")
    val modifyHttpsFileLink: Regex ="""(<a href=\"https)://([a-zA-Z0-9-/.]+)(\")""".r
    val modifyHttpFileLink: Regex ="""(<a href=\"http)://([a-zA-Z0-9-/.]+)(\")""".r
    val replacingImageContent: String = "<img src=\"https://github.com/apache/incubator-carbondata/blob/master/docs"
    val contentAfterRemovingUserContent: String = modifyContentPattern replaceAllIn(input, "id=\"")
    val contentAfterReplacingId: String = modifyMdPattern replaceAllIn(contentAfterRemovingUserContent, ".html")
    val contentAfterReplacingImage: String = modifyImagePattern replaceAllIn(contentAfterReplacingId,replacingImageContent)
    val contentAfterReplacingHttpsFileLink: String = modifyHttpsFileLink replaceAllIn(contentAfterReplacingImage, "$1://$2$3 target=_blank")
    val contentAfterReplacingFileLink: String = modifyHttpFileLink replaceAllIn(contentAfterReplacingHttpsFileLink, "$1://$2$3 target=_blank")
    contentAfterReplacingFileLink
  }

  /**
    * This handles the exeception cases for retaining .md extensions in some files
    *
    * @return list of files
    */
  def convertReadMeExtension(): List[String] = {
    val listOfFiles: List[String] = resourceService.readListOfString("fileListToRetain")
    logger.info(s"List of files to retain .md extensions : $listOfFiles")
    val location: String = resourceService.readString("outputFileLocation")
    val outputFileExtension: String = ".html"
    val modifyMdPattern: Regex = new Regex("(README)(.html)")
    listOfFiles.map { file =>
      val fileURLContent: String = dataService.readFromFile("src/main/webapp/" + file + outputFileExtension)
      val fileContent: String = modifyMdPattern replaceAllIn(fileURLContent, "$1.md")
      dataService.writeToFile(location + file + outputFileExtension, fileContent)
      fileContent
    }

  }

}

