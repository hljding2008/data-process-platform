package cn.o2o.wonhigh.data.process.platform.execute

import com.github.kevinsawicki.http.HttpRequest

object HttpClientUtil {
  def get(url: String): String = HttpRequest.get(url).body()

}
