package cn.o2o.wonhigh.data.process.platform.job

import java.util.concurrent.{Executors, ScheduledExecutorService}

import cn.o2o.wonhigh.data.process.platform.execute.ExecutorProxy
import cn.o2o.wonhigh.data.process.platform.job.exception.JobException
import cn.o2o.wonhigh.data.process.platform.store.JobStorage

class JobManager(jobStorage: JobStorage, jobExecutor: ExecutorProxy) {
  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1);

  def createJob(job: Job): Unit = {
    jobStorage.getJob(job.jobId) match {
      case Some(_) => throw new JobException(s"Job(jobId:${job.jobId}) is already exist! ")
      case None => jobStorage.saveJob(job)
    }
  }

  def updateJob(job: Job): Unit = {
    jobStorage.getJob(job.jobId) match {
//      case Some(Job(_, _, _, _, 1, 1,_)) => jobStorage.saveJob(jobOption.get.)
//      case Some(Job(_, _, _, _, 1, 2,_)) => throw new Exception(s"Job(jobId:${jobId}) is already starting! ")
//      case Some(job) => throw new Exception(s"job state is not correct(actualState:${job.actualState},desiredState: ${job.desiredState}) ")
      case Some(_) => jobStorage.saveJob(job)
      case None => throw new JobException(s"Job(jobId:${job.jobId}) is not exist! ")
    }
  }

  def getJob(jobID: String): Option[Job] = {
    jobStorage.getJob(jobID)
  }

  def listJobs(): List[Job] = jobStorage.listJob()

  def listJobNeedToChangeState(): List[Job] = jobStorage.listJobNeedToChangeState()

  def startJob(job: Job): Unit = {
    println("启动job: " + job)
    val jobInstanceId = jobExecutor.deployJob(job)
    println(s"启动job: $job 成功，instanceId:$jobInstanceId")
    job.actualState=2
    job.instanceId = jobInstanceId
    jobStorage.saveJob(job)
  }

  def killJob(job: Job): Unit = {
    println(s" kill job: $job")
    jobExecutor.killJob(job.instanceId)
    job.actualState=3
    jobStorage.saveJob(job)
  }
}


