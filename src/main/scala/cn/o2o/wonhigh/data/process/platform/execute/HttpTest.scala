package cn.o2o.wonhigh.data.process.platform.execute

import java.io.IOException

import com.github.kevinsawicki.http.HttpRequest


object HttpTest {
  def main(args: Array[String]): Unit = {
    System.out.println(111)
    val httpRequest = HttpRequest.get("http://spark-001:8788/proxy/application_1567168406553_0001/v1/jobs");
    val responseBody = httpRequest.body();
    System.out.println(responseBody);
  }


}
