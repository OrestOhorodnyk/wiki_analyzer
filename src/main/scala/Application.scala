import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.stream.ThrottleMode
import akka.stream.alpakka.sse.scaladsl.EventSource
import akka.stream.scaladsl.{Sink, Source}
import spray.json.DefaultJsonProtocol

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Future}

case class Meta(
                 uri: String,
                 request_id: String,
                 id: String,
                 dt: String,
                 domain: String,
                 stream: String,
                 topic: String,
                 partition: Int,
                 offset: Long
               )

case class Message(
                    `$schema`: String,
                    meta: Meta,
                    //                     id: Int, // some message missing this field and it cause flow failure
                    `type`: String,
                    namespace: Int,
                    title: String,
                    comment: String,
                    timestamp: Int,
                    user: String,
                    bot: Boolean,
                    server_url: String,
                    server_name: String,
                    server_script_path: String,
                    wiki: String,
                    parsedcomment: String

                  )

object MyMessageJsonProtocol extends DefaultJsonProtocol {
  implicit val metaFormat = jsonFormat9(Meta.apply)
  implicit val messageFormat = jsonFormat14(Message.apply)

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
      .map(_.parseJson.convertTo[Message])
      //      .runWith(Sink.onComplete {
      //        case Success(message) => println(message)
      //        case Failure(t) => println("An error has occurred: " + t.getMessage + t.fillInStackTrace() + t.getLocalizedMessage)
      //      })
      .runWith(Sink.foreach(message => println(message.title)))
}