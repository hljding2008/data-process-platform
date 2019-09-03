package cn.o2o.wonhigh.data.process.platform

import java.io.IOException
import java.net.URI
import java.util.StringJoiner

import cn.o2o.wonhigh.data.process.paltform.api.ClusterApi
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory
import com.sun.jersey.api.core.PackagesResourceConfig
import com.sun.jersey.spi.container.servlet.ServletContainer
import org.glassfish.grizzly.http.server.{HttpHandler, HttpServer, Request, Response}
import org.glassfish.grizzly.http.util.HttpStatus
import org.glassfish.grizzly.servlet.{ServletRegistration, WebappContext}

object WebServer {
  val BASE_PATH = "/ws/v1"
  private val PACKAGES = Array[String](classOf[ClusterApi].getPackage.getName, "com.fasterxml.jackson.jaxrs.json")
}

class WebServer @throws[IOException]
(val endpoint: URI) extends AutoCloseable {
  final private val server: HttpServer = GrizzlyServerFactory.createHttpServer(endpoint, new HttpHandler() {
    @throws[Exception]
    override def service(rqst: Request, rspns: Response): Unit = {
      rspns.setStatus(HttpStatus.NOT_FOUND_404.getStatusCode, "Not found")
      rspns.getWriter.write("404: not found")
    }
  })
  val context = new WebappContext("WebappContext", WebServer.BASE_PATH)
  val registration: ServletRegistration = context.addServlet("ServletContainer", classOf[ServletContainer])
  registration.setInitParameter(ServletContainer.RESOURCE_CONFIG_CLASS, classOf[PackagesResourceConfig].getName)
  val sj = new StringJoiner(",")
  for (s <- WebServer.PACKAGES) {
    sj.add(s)
  }
  registration.setInitParameter(PackagesResourceConfig.PROPERTY_PACKAGES, sj.toString)
  registration.addMapping(WebServer.BASE_PATH)
  context.deploy(server)

  @throws[IOException]
  def start(): Unit = {
    server.start()
  }

  @throws[Exception]
  override def close(): Unit = {
    server.stop()
    println("api server closed...")
  }

  def port: Int = server.getListeners.iterator.next.getPort
}
