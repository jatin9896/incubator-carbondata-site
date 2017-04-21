package scala

import org.scalatest.FunSpec
import org.scalatest.mockito.MockitoSugar
import services.{DataService, ResourceService, WebService}

class FileModificationSpec extends FunSpec with MockitoSugar {
  val dataServiceMock = mock[DataService]
  val webServiceMock = mock[WebService]
  val resourceServiceMock = mock[ResourceService]
  val fileModification = new FileModification(dataServiceMock, resourceServiceMock, webServiceMock)



}
