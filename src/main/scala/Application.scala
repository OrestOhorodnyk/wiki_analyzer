import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.scaladsl.{Broadcast, BroadcastHub, Flow, GraphDSL, Keep, Merge, Sink, Source}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpEntity, HttpRequest, HttpResponse, Uri}
import akka.stream.ThrottleMode
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Message(id: String, user: String, `type`: String)

object MyMessageJsonProtocol extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat3(Message.apply)

}

import MyMessageJsonProtocol._
import spray.json._


object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val send: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()


  val eventSource: Source[ServerSentEvent, NotUsed] =
    EventSource(
      uri = Uri("https://stream.wikimedia.org/v2/stream/recentchange"),
      send,
      retryDelay = 1.second
    )

  val events =
    eventSource
      .throttle(elements = 1, per = 500.milliseconds, maximumBurst = 1, ThrottleMode.Shaping)
//      .take(10)
      .map(_.getData())
      //      .map(_.parseJson.convertTo[Message])
      .runWith(Sink.foreach(println))

}
