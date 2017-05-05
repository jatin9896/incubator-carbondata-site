import com.google.inject.Inject
import org.slf4j.{Logger, LoggerFactory}
import services.{DataService, ResourceService, WebService}

class FileModification @Inject()(dataService: DataService, resourceService: ResourceService, webService: WebService) {
  val logger: Logger = LoggerFactory.getLogger(classOf[FileModification])
  val url: String = resourceService.readString("apiUrl")
  val inputFileExtension: String = ".md"
  val outputFileExtension: String = ".html"
  val headerContent: String = dataService.readFromFile(resourceService.readString("headerPath"))
  val footerContent: String = dataService.readFromFile(resourceService.readString("footerPath"))
  val location: String = resourceService.readString("outputFileLocation")
  val fileReadObject: MdFilehandler = new MdFilehandler(resourceService, dataService)
  val failMessage: String ="failure"
  /**
    * reads list of files , converts file extension to output file extension and writes file to the location
    *
    * @return status of each file i.e. success or failure
    */
  def convertToHtml(): String = {
    val listOfFiles: List[String] = resourceService.readListOfString("fileList")
    val statusList: List[String] = listOfFiles.map { file =>
      val fileURLContent: String = webService.dataOnGetRequest(url + file + inputFileExtension)
      val getFileData: Option[String] = webService.dataOnPostRequest(fileURLContent)
      getFileData match {
        case Some(data: String) => val fileData = fileReadObject.convertMdExtension(data)
          logger.info(s"Begin writing [ $file outputFileExtension ] at $location")
          val statusHtmlFile = dataService.writeToFile(location + file + outputFileExtension, headerContent + fileData + footerContent)
          if (statusHtmlFile) {
            logger.info(s"Successfully written [ $file $outputFileExtension ] at $location")
            "Success"
          }
          else {
            failMessage
          }
        case None => logger.error(s"$file Conversion failed ")
          failMessage
      }
    }

    if (statusList.contains("Failure")) {
      "Some Files Failed To Convert"
    }
    else {
      "All files successfully Converted"
    }
  }
}

