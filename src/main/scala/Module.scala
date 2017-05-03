import com.google.inject.AbstractModule
import services.{ConfFileService, DataService, FileService, HttpService, ResourceService, WebService}

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[DataService]).to(classOf[FileService])
    bind(classOf[ResourceService]).to(classOf[ConfFileService])
    bind(classOf[WebService]).to(classOf[HttpService])
  }
}
