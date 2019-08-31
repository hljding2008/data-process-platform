package cn.o2o.wonhigh.data.process.platform.store

import cn.o2o.wonhigh.data.process.platform.job.Job

class JobStorage {
  def getJob(jobId: String): Job = null

  def listJob(): List[Job] = null

  def saveJob(job: Job) = {}

}
