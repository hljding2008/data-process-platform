package cn.o2o.wonhigh.data.process.platform.job.execution

class SqlNode(parent: ExecutionPlanNode, sql: String) extends ExecutionPlanNode(parent) {
  override def execute: Unit = ???

  override def getParent: ExecutionPlanNode = ???

  override def getChildren: List[ExecutionPlanNode] = ???

}
