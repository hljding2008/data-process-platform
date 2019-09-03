package cn.o2o.wonhigh.data.process.platform.job

import java.util.concurrent.{ScheduledExecutorService, TimeUnit}

class JobMonitor extends Runnable{
  private val jobManager: JobManager = null
  private val executor: ScheduledExecutorService = null
  override def run(): Unit = {

    executor.schedule(this,10,TimeUnit.SECONDS)
  }


}
