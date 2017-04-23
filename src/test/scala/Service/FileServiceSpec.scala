package Service

import java.io.File

import org.scalatest.{FlatSpec, FunSuite}
import services.FileService

class FileServiceSpec extends FlatSpec{

  val fileServiceObject=new FileService
  it should "throw error to write a file on non existing location" in  {
    assert(fileServiceObject.writeToFile("","")===false)
  }
  it should "write a file on temp location " in  {
    assert(fileServiceObject.writeToFile("src/test/scala/Service/temp.txt","some testing on writing a file")===true)
    new File("src/test/scala/Service/temp.txt").delete()
  }

  it should "throw error to read a file on non existing location" in  {
    assert(fileServiceObject.readFromFile("/fake-Location")==="")
  }

  it should "read data from a file correctly" in{
    fileServiceObject.writeToFile("src/test/scala/Service/tempread.txt","some testing on writing a file")
    assert(fileServiceObject.readFromFile("src/test/scala/Service/tempread.txt")==="some testing on writing a file")
    new File("src/test/scala/Service/tempread.txt").delete()
  }
}
