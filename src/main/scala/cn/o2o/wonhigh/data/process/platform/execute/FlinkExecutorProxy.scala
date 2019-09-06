package cn.o2o.wonhigh.data.process.platform.execute

import cn.o2o.wonhigh.data.process.platform.job.{Job, JobInstance, JobResource, JobStatus}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.scala.StreamTableEnvironment

import scala.collection.mutable.ArrayBuffer
import scala.util.parsing.json.JSON

class FlinkExecutorProxy extends ExecutorProxy {

  //TODO:改为配置化
  private final val FLINK_SERVER_HOST = "spark-001"
  private final val FLINK_SERVER_PORT = 14100
  private final lazy val FLINK_SERVER_URL = {
    "http://" + FLINK_SERVER_HOST + ":" + FLINK_SERVER_PORT
  }

  override def deployJob(job: Job): String = {
    FlinkExecutorProxy.deployJob(job, FLINK_SERVER_HOST, FLINK_SERVER_PORT)
  }

  override def getJobStatus(jobId: String): JobStatus = {
    val resultStr = HttpClientUtil.get(FLINK_SERVER_URL + FlinkExecutorProxy.LIST_JOBS + FlinkExecutorProxy.SLASH + jobId)
    print(JSON.parseFull(resultStr))
    null
  }

  override def listJobStatus(): List[JobStatus] = {
    val resultStr = HttpClientUtil.get(FLINK_SERVER_URL + FlinkExecutorProxy.LIST_JOBS)
    print(JSON.parseFull(resultStr))
    null
  }

  override def killJob(jobId: String,mode:String = "cannel"): Unit = {
    val resultStr = HttpClientUtil.get(FLINK_SERVER_URL + FlinkExecutorProxy.LIST_JOBS + FlinkExecutorProxy.SLASH + jobId + FlinkExecutorProxy.CANCEL_JOB)
    print(JSON.parseFull(resultStr))
  }

  override def getJobInstance(jobInstanceId: String): JobInstance = JobInstance(jobInstanceId,s"$FLINK_SERVER_URL/#/job/$jobInstanceId/overview")
}

object FlinkExecutorProxy {
  private final val LIST_JOBS = "/v1/jobs"
  private final val CANCEL_JOB = "/yarn-cancel"
  private final val SLASH = "/";

  def deployJob(job: Job, flinkServerHost: String, flinkServerPort: Int): String = {
    //TODO:改为从配置加载
    val jarFiles = ArrayBuffer("E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-0.10_2.11\\1.9.0\\flink-connector-kafka-0.10_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-0.9_2.11\\1.9.0\\flink-connector-kafka-0.9_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-base_2.11\\1.9.0\\flink-connector-kafka-base_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\kafka\\kafka-clients\\0.10.2.1\\kafka-clients-0.10.2.1.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-json\\1.9.0\\flink-json-1.9.0.jar")
    import scala.collection.JavaConversions.bufferAsJavaList
    val javaEnv = new PlatformStreamExecutionEnvironment(flinkServerHost, flinkServerPort, jarFiles)
    val env = new StreamExecutionEnvironment(javaEnv) //
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val stenv = StreamTableEnvironment.create(env)
    job.sql.split(";").map(_.trim).foreach(str => {
      str match {
        case nullStr if (nullStr == null || nullStr.equals("")) => {}//do nothing TODO：改为option判空
        case ss: String if (ss.startsWith("select") || ss.startsWith("SELECT")) => stenv.sqlQuery(ss)
        case ss: String => stenv.sqlUpdate(ss)
        case _ => println("非法字符")
      }
    })
    val subResult = javaEnv.executeDetached(job.jobId)
    //flink的job作为platform中job的一个instance的id
    val jobInstanceId = subResult.getJobID.toString
    jobInstanceId
  }

  def main(args: Array[String]): Unit = {
    val sql = "CREATE TABLE tbl1 (a bigint, h varchar, msg VARCHAR ) " +
            "PARTITIONED BY (a, h)  " +
            "with ('connector.type' = 'kafka','connector.version' = '0.10','connector.topic' = 'event_topic','connector.startup-mode' = 'earliest-offset','connector.properties.0.key' = 'bootstrap.servers','connector.properties.0.value' = 'club-kafka-qa:9092','update-mode' = 'append', 'format.type' = 'json','format.derive-schema' = 'true');" +
            "CREATE TABLE tbl2 (a bigint, h varchar, msg VARCHAR ) " +
            "PARTITIONED BY (a, h)  " +
            "with ('connector.type' = 'kafka','connector.version' = '0.10','connector.topic' = 'sink_topic','connector.startup-mode' = 'earliest-offset','connector.properties.0.key' = 'bootstrap.servers','connector.properties.0.value' = 'club-kafka-qa:9092','update-mode' = 'append', 'format.type' = 'json','format.derive-schema' = 'true');" +
            "insert into tbl2 select * from tbl1";
    println(sql)
    val job = Job("2","select job",sql,JobResource(1,2,3),1,2)
//        new FlinkExecutorProxy().deployJob(job)
//        new FlinkExecutorProxy().getJobStatus("9b2b019c258d11c9193066e26a094473")
    println(new FlinkExecutorProxy().deployJob(job))
  }


}
