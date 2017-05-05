import org.slf4j.{Logger, LoggerFactory}
import services.{ConfFileService, FileService, HttpService}

object FileOperationMain  {
  def main(args: Array[String]):Unit= {
    val logger: Logger = LoggerFactory.getLogger("FileOperationMain")

    val fileObject: FileModification = new FileModification(new FileService,new ConfFileService,new HttpService)

    val status: String = fileObject.convertToHtml()
    logger.info(s"File Conversion to html : $status")
  }
}
