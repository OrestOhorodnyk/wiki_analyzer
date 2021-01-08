package ucu.functional.programing
package rout

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

object BarRouter {
  val route: Route = path("bar") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Bar rout/h1>"))
    }
  }

}
