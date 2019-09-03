package cn.o2o.wonhigh.data.process.platform

import java.net.URI

object DataProcessPlatform {
  def main(args: Array[String]): Unit = {
    PlatformContext.INSTANCE.initialize.start
    try {
      val server = new WebServer(URI.create("http://localhost:0"))
      try {
        server.start()
        Thread.currentThread.join()
      } finally if (server != null) server.close()
    }

  }
}
