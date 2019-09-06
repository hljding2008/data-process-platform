package cn.o2o.wonhigh.data.process.platform

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.ContentType.WithFixedCharset
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive0, Directive1, Route}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.stream.ActorMaterializer
import cn.o2o.wonhigh.data.process.platform.job.Job
import cn.o2o.wonhigh.data.process.platform.job.JobJsonSupport._
import cn.o2o.wonhigh.data.process.platform.job.exception.JobException
import spray.json._

import scala.concurrent.{ExecutionContextExecutor, Future}

class WebServer(val host: String, val port: Int, val ctx: PlatformContext = null) extends AutoCloseable {
  implicit val system: ActorSystem = ActorSystem("api-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val userRoute: Route = path("user" / "login") {
    WebServer.postWithTryCatch(as[Map[String, String]]) { param =>
      complete(HttpEntity(ContentTypes.`application/json`, "{\"code\":20000,\"data\":{\"token\":\"admin-token\"}}"))
    }
  } ~ path("user" / "login") {
    WebServer.optionsWithTryCatch(as[Map[String, String]]) { param =>
      complete(HttpEntity(ContentTypes.`application/json`, "{\"code\":20000,\"data\":{\"token\":\"admin-token\"}}"))
    }
  }

  val jobsRoute: Route = path("jobs") {
    WebServer.getWithTryCatch {
      val result = ctx.getJobManager.listJobs().toJson
      WebServer.completeOkWithJson(result)
    }
  } ~ path("jobs") {
    WebServer.postWithTryCatch(as[Job]) { job =>
      ctx.getJobManager.createJob(job)
      WebServer.completeOk
    }
  } ~ path("jobs") {
    WebServer.patchWithTryCatch(as[Job]) { job =>
      ctx.getJobManager.updateJob(job)
      WebServer.completeOk
    }
  } ~ path("jobs" / Segment) { jobID =>
    WebServer.getWithTryCatch {
      ctx.getJobManager.getJob(jobID) match {
        case Some(job: Job) => WebServer.completeOkWithJson(job.toJson)
        case None => WebServer.completeOk
      }
    }
  } ~ path("instance" / Segment) { jobInstanceID =>
    WebServer.getWithTryCatch {
      WebServer.completeOkWithJson(ctx.getJobExecutor.getJobInstance(jobInstanceID).toJson)
    }
  }
  val routes: Route =
    pathPrefix(WebServer.BASE_PATH) {
      userRoute ~ jobsRoute
    } ~ path("")(getFromResource("public/index.html"))

  var bindingFuture: Future[ServerBinding] = _

  @throws[IOException]
  def start(): Unit = {
    bindingFuture = Http().bindAndHandle(routes, host, port)
  }

  @throws[Exception]
  override def close(): Unit = {
    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
    println("api server closed...")
  }

}

object WebServer {
  val BASE_PATH = "v1"

  def completeOk: Route = complete(HttpEntity(ContentTypes.`application/json`, """{"code":20000}"""))

  def completeOk(result: String, charset: WithFixedCharset = ContentTypes.`application/json`): Route = {complete(HttpEntity(charset, s"""{"code":20000,"data":"$result"}"""))}

  def completeOkWithJson(result: JsValue, charset: WithFixedCharset = ContentTypes.`application/json`): Route = {complete(HttpEntity(charset, s"""{"code":20000,"data":${result.toString()}}"""))}

  def completeException(errMsg: String): Route = complete(HttpEntity(ContentTypes.`application/json`, "{\"code\":50000,\"message\":\"" + errMsg + "\"}"))

  def postEntity[T](um: FromRequestUnmarshaller[T]): Directive1[T] = post & entity(um)

  def optionsEntity[T](um: FromRequestUnmarshaller[T]): Directive1[T] = options & entity(um)

  def patchEntity[T](um: FromRequestUnmarshaller[T]): Directive1[T] = patch & entity(um)

  def completeWithTryCatch0(block: => Route): Route = {
    try {
      block
    } catch {
      case JobException(msg) => WebServer.completeException(msg)
    }
  }

  def completeWithTryCatch1[T](block: T => Route, param: T): Route = {
    try {
      block(param)
    } catch {
      case JobException(msg) => WebServer.completeException(msg)
    }
  }

  def getWithTryCatch(block: => Route): Route = get(completeWithTryCatch0(block))

  def postWithTryCatch[T](um: FromRequestUnmarshaller[T])(block: T => Route): Route = postEntity[T](um: FromRequestUnmarshaller[T]) { param =>
    completeWithTryCatch1(block, param)
  }

  def optionsWithTryCatch[T](um: FromRequestUnmarshaller[T])(block: T => Route): Route = optionsEntity[T](um: FromRequestUnmarshaller[T]) { param =>
    completeWithTryCatch1(block, param)
  }

  def patchWithTryCatch[T](um: FromRequestUnmarshaller[T])(block: T => Route): Route = patchEntity[T](um: FromRequestUnmarshaller[T]) { param =>
    completeWithTryCatch1(block, param)
  }

  def ggg[T](f: (T ⇒ String) ⇒ String): Unit = print(f)

  def main(args: Array[String]): Unit = {
    //    print(ggg{ WebServer.completeException(msg)})
    val d: Directive0 = Directive.Empty
    //    implicit val ev:String = ""
    //    d{WebServer.completeException("")}
    //    d.apply{WebServer.completeException("")}
    val t = new Test
    try {
      t.tapply()
    } catch {
      case JobException(msg) => print(msg)
    }

    //    println(get)
    //    println(get{WebServer.completeException("")})
    println()
  }

}

class Test {
  def tapply(): String = {
    throw new JobException("123123213123")
    ""
  }
}
