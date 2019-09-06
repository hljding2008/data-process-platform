package cn.o2o.wonhigh.data.process.platform.execute

import cn.o2o.wonhigh.data.process.platform.job.{Job, JobInstance, JobStatus}

trait ExecutorProxy {
  def deployJob(job: Job): String

  def getJobStatus(jobId: String): JobStatus

  def listJobStatus(): List[JobStatus]

  def killJob(jobId: String, mode: String = "cancel")

  def getJobInstance(jobInstanceId: String): JobInstance
}
