package ucu.functional.programing
import scala.concurrent.duration._
import scala.async.Async.{async, await}

import akka.io.IO
import akka.util.Timeout
import akka.http.scaladsl.Http
import akka.actor.{ActorSystem}
import akka.http.scaladsl.model.{HttpMethods,HttpEntity,HttpRequest,HttpResponse,Uri}
//import akka.stream.{FlowMaterializer}
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import akka.pattern.ask

import HttpEntity._

import scala.concurrent.Future

object HttpClient {
  implicit val askTimeout: Timeout = 1000.millis

//  def makeRequest(port: Int, path: String)(implicit system: ActorSystem, materializer: Materializer): Future[HttpResponse] = {
//    implicit val ec = system.dispatcher
//
//    async {
//      val connFuture = IO(Http).ask(Http.Connect("127.0.0.1", port)).mapTo[Http.OutgoingConnection]
//      val connection = await(connFuture)
//      val request = HttpRequest(HttpMethods.GET, Uri(path))
//      Flow(List(request -> 'NoContext)).produceTo(connection.processor)
//      await(Flow(connection.processor).map(_._1).toFuture)
//    }
//
//  }
}
