/**
  * Created by linwx on 2017/10/24.
  */

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.util.{Failure, Success, Try}

/**
  * @author lwx
  *         Created by linwx on 2017/10/24.
  */
object JsonTool {
  var mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def object2Json(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def json2Object[T: Manifest](value: String): Option[T] = {
    Try(mapper.readValue[T](value)) match {
      case Success(d) => {
        Some(d)
      }
      case Failure(e) => {
        e.printStackTrace()
        None
      }
    }
  }

}
