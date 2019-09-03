package cn.o2o.wonhigh.data.process.platform

import cn.o2o.wonhigh.data.process.platform.execute.{ExecutorProxy, FlinkExecutorProxy}
import cn.o2o.wonhigh.data.process.platform.job.JobManager
import cn.o2o.wonhigh.data.process.platform.store.{InMemoryJobStorage, JobStorage}

class PlatformContext() {
  private var jobStorage: JobStorage = null
  private var jobExecutor: ExecutorProxy = null
  private var jobManager: JobManager = null

  def getJobStorage = jobStorage
  def getJobExecutor = jobExecutor
  def getJobManager = jobManager

  def start = {
    println("context started....")
  }

  def initialize(): PlatformContext = {
    jobStorage = new InMemoryJobStorage
    jobExecutor = new FlinkExecutorProxy
    jobManager = new JobManager(jobStorage, jobExecutor)
    this
  }
}

object PlatformContext {
  lazy val INSTANCE = new PlatformContext()
}
