package cn.o2o.wonhigh.data.process.platform

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.stream.ActorMaterializer
import cn.o2o.wonhigh.data.process.platform.job.Job
import spray.json._
import cn.o2o.wonhigh.data.process.platform.job.JobJsonSupport._

import scala.concurrent.{ExecutionContextExecutor, Future}

class WebServer(val host: String, val port: Int, val ctx: PlatformContext = null) extends AutoCloseable {
  implicit val system: ActorSystem = ActorSystem("api-server")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val jobsRoute: Route = path("jobs") {
    get {
      val result = ctx.getJobManager.listJobs().toJson.toString()
      complete(HttpEntity(ContentTypes.`application/json`,result))
    }
  } ~ path("jobs") {
    post {
      entity(as[Job]) { job =>
//        val job = new Gson().fromJson(jobStr, classOf[Job])
        ctx.getJobManager.createJob(job)
        WebServer.completeOk
      }
    }
  } ~ path("jobs" / Segment) { jobID =>
    get {
      ctx.getJobManager.getJob(jobID) match {
        case Some(job: Job) => complete(HttpEntity(ContentTypes.`application/json`, job.toJson.toString))
        case None => WebServer.completeOk
      }
    } ~
      delete {
        ctx.getJobManager.killJob(jobID)
        complete(HttpEntity(ContentTypes.`application/json`, s"remove job: $jobID"))
      }
  }
  val routes: Route =
    pathPrefix(WebServer.BASE_PATH) {
      jobsRoute
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

  def completeOk: Route = complete(HttpEntity.Empty)

  def postEntity[T](um: FromRequestUnmarshaller[T]): Directive1[T] = post & entity(um)

  def main(args: Array[String]): Unit = {
//    val job: Job = "{\"jobName\":\"1\",\"jobId\":\"2\",\"sql\":\"3\"}"
//    print(job.jobId)
    //    val server = new WebServer("localhost", 8080)
    //    server.start()
    //    println(new Gson().toJson(List[Job](new Job("1", "2", "3"))).toString())
  }

}
