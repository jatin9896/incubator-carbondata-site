
import org.mockito.Mockito.when
import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import org.slf4j.LoggerFactory
import services.{FileService, ResourceService, WebService}

class FileModificationSpec extends FlatSpec with MockitoSugar {
  val logger = LoggerFactory.getLogger("FileModificationSpec")
  val dataServiceMock = mock[FileService]
  val webServiceMock = mock[WebService]
  val data="some data"
  val convertedData="some converted data"
  val resourceServiceMock = mock[ResourceService]
  when(resourceServiceMock.readString("apiUrl")).thenReturn("fake-url/")
  when(resourceServiceMock.readString("outputFileLocation")).thenReturn("fake-location/")
  when(resourceServiceMock.readString("headerPath")).thenReturn("headerUrl")
  when(resourceServiceMock.readString("footerPath")).thenReturn("footerUrl")
  when(dataServiceMock.readFromFile("headerUrl")).thenReturn("header-content")
  when(dataServiceMock.readFromFile("footerUrl")).thenReturn("footer-content")
  when(resourceServiceMock.readString("MdFileLocation")).thenReturn("fake-mdlocation/")
  val mdFileHandlerMock = mock[MdFilehandler]
  when(resourceServiceMock.readListOfString("fileList")).thenReturn(List("fake-file"))
  when(webServiceMock.dataOnGetRequest("fake-url/fake-file.md")).thenReturn(data)
  when(webServiceMock.dataOnPostRequest(data)).thenReturn(Some(convertedData))
  when(mdFileHandlerMock.convertMdExtension(convertedData)).thenReturn("data after md conversion")
  when(dataServiceMock.writeToFile("fake-location/fake-file.html", "header-content" + convertedData + "footer-content")).thenReturn(true)
  when(dataServiceMock.writeToFile("fake-mdlocation/fake-file.md", data)).thenReturn(true)
  val fileModification = new FileModification(dataServiceMock, resourceServiceMock, webServiceMock)

    it should "return successful convert list of files to html files" in {
    assert(fileModification.convertToHtml() === "All files successfully Converted")
  }
}
