package cn.o2o.wonhigh.data.process.platform.store

import cn.o2o.wonhigh.data.process.platform.job.Job

class InMemoryJobStorage extends JobStorage {
  private val jobMap = scala.collection.mutable.Map[String, Job]()

  override def getJob(jobId: String): Option[Job] = jobMap.get(jobId)

  def listToRunningJob(): List[Job] = {
    listJob()
  }

  override def listJob(): List[Job] = jobMap.values.toList

  override def listJobNeedToChangeState():  List[Job] = {
    listJob().filter(job=>job.actualState!=job.desiredState)
  }

  override def saveJob(job: Job): Unit = jobMap.put(job.jobId,job)
}
