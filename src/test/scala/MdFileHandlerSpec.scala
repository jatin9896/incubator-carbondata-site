import org.scalatest.{FlatSpec, FunSpec}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito.when
import services.{ResourceService, WebService, FileService}

class MdFileHandlerSpec extends FlatSpec with MockitoSugar{

  val dataServiceMock = mock[FileService]
  val webServiceMock = mock[WebService]
  val resourceServiceMock = mock[ResourceService]

   val mdFileObject=new MdFilehandler(resourceServiceMock,dataServiceMock)

  when(resourceServiceMock.readListOfString("fileListToRetain")).thenReturn(List("fake-file"))
  when(resourceServiceMock.readString("outputFileLocation")).thenReturn("/fake-location")
  when(dataServiceMock.readFromFile("src/main/webapp/fake-file.html"))thenReturn("<a href=\"README.html\"></a>")
  when(dataServiceMock.writeToFile("/fake-location/fake-file.html",""))thenReturn (true)
  it should "convert list of file content having readme.html extension to readme.md" in{
    assert(mdFileObject.convertReadMeExtension()===List("<a href=\"README.md\"></a>"))
  }
  it should "convert all md extension to html except readme link" in {
      assert(mdFileObject.convertMdExtension("<a href=\"link.md\"></a>")==="<a href=\"link.html\"></a>")
  }
  it should "convert all image tag link to github image directory link " in {
    val expectedOutput="<img src=\"https://github.com/apache/incubator-carbondata/blob/master/docs/img.jpg></img>"
    assert(mdFileObject.convertMdExtension("<img src=\"../docs/img.jpg></img>")===expectedOutput)
  }
  it should "add target=_blank in all link having http in href " in {
      assert(mdFileObject.convertMdExtension("<a href=\"http://github.com/something\"></a>")==="<a href=\"http://github.com/something\" target=_blank></a>")
  }
  it should "add target=_blank in all link having https in href " in {
      assert(mdFileObject.convertMdExtension("<a href=\"https://github.com/something\"></a>")==="<a href=\"https://github.com/something\" target=_blank></a>")
  }
  it should "remove user-content from all the ids in html " in {
    assert(mdFileObject.convertMdExtension("<h1 id=\"user-content-#pageid\"> </h1>")==="<h1 id=\"#pageid\"> </h1>")
  }

}
