package cn.o2o.wonhigh.data.process.platform.store

import cn.o2o.wonhigh.data.process.platform.job.Job

trait JobStorage {
  def getJob(jobId: String): Option[Job]

  def listJob(): List[Job]

  def saveJob(job: Job)

}
