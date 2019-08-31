package cn.o2o.wonhigh.data.process.platform.job.execution

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

trait ExecutionPlanNode {

//  def execute(env: StreamExecutionEnvironment)
//  def getParent
  def getChildren: Seq[ExecutionPlanNode]
}
