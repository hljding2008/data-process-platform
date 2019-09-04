package cn.o2o.wonhigh.data.process.platform

object DataProcessPlatform {
  def main(args: Array[String]): Unit = {
    val ctx = PlatformContext.INSTANCE.initialize
    ctx.start
    try {
      val server = new WebServer("localhost",8080,ctx)
      try {
        server.start()
        Thread.currentThread.join()
      } finally if (server != null) server.close()
    }

  }
}
