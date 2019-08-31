package cn.o2o.wonhigh.data.process.platform.execute

import cn.o2o.wonhigh.data.process.platform.job.{Job, JobStatus}

trait ExecutorProxy {
  def deployJob(job: Job)
  def getJobStatus(jobId: String): JobStatus
  def listJobStatus(): List[JobStatus]
  def killJob(jobId: String)
}
