package cn.o2o.wonhigh.data.process.platform.job

import cn.o2o.wonhigh.data.process.platform.execute.ExecutorProxy
import cn.o2o.wonhigh.data.process.platform.store.JobStorage

class JobManager(jobStorage: JobStorage, jobExecutor: ExecutorProxy) {

  def createJob(): Unit = {
    val job = JobFactory.getJob()
    jobStorage.saveJob(job);
  }

  def listJobs(): Unit = {
//    val job = JobFactory.getJob()
//    jobStorage.saveJob(job);
  }

  def listJobInstances(): Unit = {
    //    val job = JobFactory.getJob()
    //    jobStorage.saveJob(job);
  }

  def startJob(job: Job): Unit = {
    jobExecutor.deployJob(job)
  }

  def killJob(jobId: String): Unit = {
    jobExecutor.killJob(jobId)
  }
}
