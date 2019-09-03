package cn.o2o.wonhigh.data.process.platform.job.execution

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.expressions.Expression
import org.apache.flink.table.plan.logical.rel.LogicalTableAggregate

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


class SqlNode(sql: String) extends ExecutionPlanNode {
  private val children = new ListBuffer[ExecutionPlanNode]();

  def execute(env: StreamExecutionEnvironment, stenv: StreamTableEnvironment): Unit = {
    //TODO:暂时简单实现，后续需要改为通过calcite分析sql

    sql.split(";").map(_.trim).foreach(str=>{
      str match {
        case ss:String if(ss.startsWith("select")||ss.startsWith("SELECT"))=>stenv.sqlQuery(sql)
        case _=>stenv.sqlUpdate(sql)
      }
    })
  }

  override def getChildren: Seq[ExecutionPlanNode] = children

  def addChild(child: ExecutionPlanNode): ExecutionPlanNode = {
    children.append(child)
    this
  }
}
