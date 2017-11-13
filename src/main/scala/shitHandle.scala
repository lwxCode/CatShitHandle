import java.text.SimpleDateFormat
import java.util.Date

import scala.math.random

/**
  * Created by linwx on 2017/9/7.
  */
object shitHandle {
  // 生成一个2,4,6之间的随机数
  def r = (random * 2).round.toInt * 2 + 2


  // 递归直到有一个方案3个随机数不重复
  def f(a: Int)(b: Int)(c: Int): String = {
    if (a != b && a != c && b != c)
      "  \r\n晓康本周周" + a + "铲屎 \r\n黄帅本周周" + b + "铲屎 \r\n文翔本周周" + c + "铲屎\r\n"
    else
      f(r)(r)(r)
  }

  def main(args: Array[String]) {
    val now: Date = new Date
    val allDFormat: SimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    val dFormat: SimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd")
    val date = dFormat.format(now)
    if (RedisTool.isExist(date)) {
      println("the program only can run once a day!!\r\n" +
        RedisTool.get[(String, String)](date).get)
    } else {
      val s = f(r)(r)(r)
      if (RedisTool.set(date, (s, allDFormat.format(now)))) {
        println(date + "\r\n" + s)
      } else {
        println("RedisFailed")
      }
    }
  }
}
