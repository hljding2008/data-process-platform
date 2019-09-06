package cn.o2o.wonhigh.data.process.platform.job

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
  *
  * @param jobId
  * @param jobName
  * @param sql
  * @param resource
  * @param actualState 1.未开始 2.启动中（待调度）3.启动中 4.运行中 5.停止中（待调度）6.停止中 7.已停止
  * @param desiredState
  * @param instanceId
  */
case class Job(val jobId: String, val jobName: String, val sql: String, val resource: JobResource, var actualState: Int, var desiredState: Int,var instanceId:String=null)

object JobStateEnum extends Enumeration {
  type JobState = Value
  val Ready = Value(1)
  val Running = Value(2)
  val Stopped = Value(3)
}

case class JobResource(val memory: Int, val vCores: Int, val executionSlots: Int)

case class JobInstance(jobInstanceId:String,instanceUrl:String)

object JobJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortofolioFormats2JobResource = jsonFormat3(JobResource)
  implicit val PortofolioFormats2Job = jsonFormat7(Job)
  implicit val PortofolioFormats2JobInstance = jsonFormat2(JobInstance)

  def main(args: Array[String]): Unit = {
    import spray.json._
    val job = Job("1", "2", "3", JobResource(1, 2, 3), JobStateEnum.Ready.id, JobStateEnum.Stopped.id)
    //    print(JobResource(1,2,3).toJson)
    print(job.toJson)
  }
}

