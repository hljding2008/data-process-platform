package cn.o2o.wonhigh.data.process.platform.job

import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}

import cn.o2o.wonhigh.data.process.platform.execute.ExecutorProxy

//import javax.print.attribute.standard.JobState

class JobMonitor(val jobManager: JobManager) extends Runnable {
  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1);

  def start()={
    scheduler.submit(this)
  }
  override def run(): Unit = {
    println("JobMonitor： check state")
    changeState()
    healthCheck()
    scheduler.schedule(this, 10, TimeUnit.SECONDS)
  }

  def changeState() = {
    val needToChangeState = jobManager.listJobNeedToChangeState()
    needToChangeState.foreach(job => {
      (job.actualState, job.desiredState) match {
        case (1, 2) => jobManager.startJob(job)
        case (2, 3) => jobManager.killJob(job)
        case _ => println(s"异常状态变更：$job")
      }

    })

  }

  def healthCheck() = {

  }


}
