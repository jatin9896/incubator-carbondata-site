import com.google.inject.Inject
import org.slf4j.LoggerFactory
import services.{DataService, ResourceService, WebService}

class FileModification @Inject()(dataService: DataService, resourceService: ResourceService, webService: WebService) {
  val logger = LoggerFactory.getLogger(classOf[FileModification])
  val url = resourceService.readString("apiUrl")
  val inputFileExtension = ".md"
  val outputFileExtension = ".html"
  val headerContent: String = dataService.readFromFile(resourceService.readString("headerPath"))
  val footerContent: String = dataService.readFromFile(resourceService.readString("footerPath"))
  val location = resourceService.readString("outputFileLocation")
  val fileReadObject = new MdFilehandler(resourceService, dataService)
  val failMessage="failure"
  /**
    * reads list of files , converts file extension to output file extension and writes file to the location
    *
    * @return status of each file i.e. success or failure
    */
  def convertToHtml(): String = {
    val listOfFiles = resourceService.readListOfString("fileList")
    val statusList = listOfFiles.map { file =>
      val fileURLContent = webService.dataOnGetRequest(url + file + inputFileExtension)
      val getFileData = webService.dataOnPostRequest(fileURLContent)
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

