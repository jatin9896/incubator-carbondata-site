import com.google.inject.Inject
import org.slf4j.LoggerFactory
import services._

object FileOperationMain  {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger("FileOperationMain")

    val fileObject = new FileModification(new FileService,new ConfFileService,new HttpService)

    val status = fileObject.convertToHtml()
    logger.info(s"File Conversion to html : $status")
  }
}

