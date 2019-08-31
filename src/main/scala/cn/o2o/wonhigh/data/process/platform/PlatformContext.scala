package cn.o2o.wonhigh.data.process.platform

import cn.o2o.wonhigh.data.process.platform.job.JobManager

class PlatformContext {
  private val jobManager: JobManager = null
  def start = {print("context started....")}

}
