package cn.o2o.wonhigh.data.process.platform.job.execution

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.scala.StreamTableEnvironment

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


class SqlNode(sql: String) extends ExecutionPlanNode {
  private val children = new ListBuffer[ExecutionPlanNode]();

  def execute(env: StreamExecutionEnvironment, stenv: StreamTableEnvironment): Unit = {
    stenv.sqlUpdate(sql)
  }

  override def getChildren: Seq[ExecutionPlanNode] = children

  def addChild(child: ExecutionPlanNode) = children.append(child)
}
