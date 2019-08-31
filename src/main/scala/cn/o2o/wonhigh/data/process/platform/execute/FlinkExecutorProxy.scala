package cn.o2o.wonhigh.data.process.platform.execute

import cn.o2o.wonhigh.data.process.platform.job.{Job, JobStatus}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.scala._
import org.apache.flink.api.scala._
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.types.Row

import scala.collection.mutable.ArrayBuffer
import scala.util.parsing.json.JSON

class FlinkExecutorProxy extends ExecutorProxy {

  override def deployJob(job: Job): Unit = ???

  override def getJobStatus(jobId: String): JobStatus = {
    val resultStr = HttpClientUtil.get(FlinkExecutorProxy.FLINK_SERVER_URL + FlinkExecutorProxy.LIST_JOBS + FlinkExecutorProxy.SLASH + jobId)
    print(JSON.parseFull(resultStr))
    null
  }

  override def listJobStatus(): List[JobStatus] = {
    val resultStr = HttpClientUtil.get(FlinkExecutorProxy.FLINK_SERVER_URL + FlinkExecutorProxy.LIST_JOBS)
    print(JSON.parseFull(resultStr))
    null
  }

  override def killJob(jobId: String): Unit = ???
}

object FlinkExecutorProxy {
  //TODO:改为配置化
  private final val FLINK_SERVER_HOST = "spark-001"
  private final val FLINK_SERVER_PORT = 14100
  private final lazy val FLINK_SERVER_URL = {"http://"+FLINK_SERVER_HOST+FLINK_SERVER_PORT}
  private final val LIST_JOBS = "/v1/jobs"
  private final val SLASH = "/";

  def deployJob() = {
    val jarFiles = ArrayBuffer("E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-0.10_2.11\\1.9.0\\flink-connector-kafka-0.10_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-0.9_2.11\\1.9.0\\flink-connector-kafka-0.9_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-connector-kafka-base_2.11\\1.9.0\\flink-connector-kafka-base_2.11-1.9.0.jar", "E:\\zhang.dk\\repository\\org\\apache\\kafka\\kafka-clients\\0.10.2.1\\kafka-clients-0.10.2.1.jar", "E:\\zhang.dk\\repository\\org\\apache\\flink\\flink-json\\1.9.0\\flink-json-1.9.0.jar")
    import scala.collection.JavaConversions.bufferAsJavaList
    val javaEnv = new PlatformStreamExecutionEnvironment(FLINK_SERVER_HOST, FLINK_SERVER_PORT, jarFiles)
    val env = new StreamExecutionEnvironment(javaEnv) //
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val stenv = StreamTableEnvironment.create(env)

    val ddl = "CREATE TABLE tbl1 (" + "  a bigint," + "  h varchar, " + "  msg VARCHAR " + ")" + "PARTITIONED BY (a, h) " +
      "  with (" + "  'connector.type' = 'kafka'," + "  'connector.version' = '0.10'," + "  'connector.topic' = 'event_topic'," + "  'connector.startup-mode' = 'earliest-offset'," + "  'connector.properties.0.key' = 'bootstrap.servers'," + "  'connector.properties.0.value' = 'club-kafka-qa:9092'," + "  'update-mode' = 'append'," + "  'format.type' = 'json'," + "  'format.derive-schema' = 'true' " + ")"
    stenv.sqlUpdate(ddl)
    System.out.println(ddl)
    val result = stenv.sqlQuery("SELECT * from tbl1")
    print(result.getSchema)

    implicit def max(x: String) = Integer.parseInt(x)

    result.toAppendStream[Row]
    val subResult = javaEnv.executeDetached(ddl)
    println(subResult.getJobID)
  }

  def main(args: Array[String]): Unit = {
    //        new FlinkExecutorProxy().listJobStatus
    //    FlinkExecutorProxy.deployJob()
    new FlinkExecutorProxy().getJobStatus("1864c074ad229d54058a4af61dc1b180")
  }


}
