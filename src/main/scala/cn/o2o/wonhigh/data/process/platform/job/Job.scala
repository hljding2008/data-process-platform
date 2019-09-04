package cn.o2o.wonhigh.data.process.platform.job

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Job(val jobId: String, val jobName: String, val sql: String,val resource: JobResource,val actualState:Int,val desiredState:Int)

object JobStateEnum extends Enumeration {
  type JobState = Value
  val Ready = Value(1)
  val Running = Value(2)
  val Stopped = Value(3)
}

//class JobState()
//case class Ready() extends JobState
//case class Running() extends JobState
//case class Stopped() extends JobState

case class JobResource(val memory:Int,val vCores:Int,val executionSlots:Int)

object JobJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortofolioFormats2JobResource = jsonFormat3(JobResource)
  implicit val PortofolioFormats2Job = jsonFormat6(Job)
  def main(args: Array[String]): Unit = {
    import spray.json._
    val job = Job("1","2","3",JobResource(1,2,3),JobStateEnum.Ready.id,JobStateEnum.Stopped.id)
//    print(JobResource(1,2,3).toJson)
    print(job.toJson)
  }
}

