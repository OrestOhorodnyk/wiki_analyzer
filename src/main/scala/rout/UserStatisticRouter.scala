package ucu.functional.programing
package rout

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route

object UserStatisticRouter {
  val route: Route = path("user-statistic") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>UserStatistic rout/h1>"))
    }
  }
}
