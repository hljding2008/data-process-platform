package cn.o2o.wonhigh.data.process.platform.job

case class Job(val jobId: String,val jobName: String,val sql: String){
  var currentState = null//READY、TO_RUNNING、RUNNING、TO_STOP、STOPED

}

