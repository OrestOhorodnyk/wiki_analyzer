package ucu.functional.programing
package rout

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

object FooRouter {
  val route: Route = path("foo") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>foo rout/h1>"))
    }
  }
}
