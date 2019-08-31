package cn.o2o.wonhigh.data.process.platform.job.execution

abstract class ExecutionPlanNode(parentNode: ExecutionPlanNode) {
  this(parentNode: ExecutionPlanNode){
    print(11111)
  }
  def execute

  def getParent = parentNode

  def getChildren: List[ExecutionPlanNode]
}
