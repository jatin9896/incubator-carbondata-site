import com.google.inject.AbstractModule
import services._

class Module extends AbstractModule{
  override def configure() = {
    bind(classOf[DataService]).to(classOf[FileService])
    bind(classOf[ResourceService]).to(classOf[ConfFileService])
    bind(classOf[WebService]).to(classOf[HttpService])
  }
}
