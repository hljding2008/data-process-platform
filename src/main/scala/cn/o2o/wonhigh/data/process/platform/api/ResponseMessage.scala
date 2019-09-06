package cn.o2o.wonhigh.data.process.platform.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import cn.o2o.wonhigh.data.process.platform.job.{Job, JobResource}
import spray.json.DefaultJsonProtocol

case class ResponseMessage(val responseType: String, var msg: Any){
  def message(msg:String): ResponseMessage ={
    this.msg = msg
    this
  }
}

//object Response extends DefaultJsonProtocol with SprayJsonSupport {
//  implicit val PortofolioFormats2ResponseMessage = jsonFormat2(ResponseMessage)
//  def ok = ResponseMessage("ok",null)
//  def err = ResponseMessage("error",null)
//
//  def main(args: Array[String]): Unit = {
//    import spray.json._
//    val job = Job("1","2","3",JobResource(1,2,3),1,2,"")
////    val mm = ok.msg(job)
////    print(mm.toJson)
//  }
//}
