/**
  * @author lwx
  *         Created by linwx on 2017/10/19.
  */

import redis.clients.jedis._

import scala.util.{Failure, Success, Try}

class RedisTool private(val url: String, val port: Int) {
  val config: JedisPoolConfig = new JedisPoolConfig
  config.setMaxWaitMillis(2000)
  config.setMaxIdle(10)
  config.setMaxTotal(20)
  private val pool = new JedisPool(config, url, port)

  def getJedis(): Option[Jedis] = {
    Try(pool.getResource()) match {
      case Failure(e) => {
        println(e.getMessage);
        Some(null)
      }
      case Success(d) => {
        Some(d)
      }
    }
  }


  def returnResource(redis: Option[Jedis]) {
    if (redis.nonEmpty) {
      pool.returnResourceObject(redis.get);
    }
  }

  /**
    * 存储到redis
    *
    * @param key
    * @param value
    * @return
    */
  def set(key: String, value: Any): (Boolean, Option[Jedis]) = {
    val result = getJedis()
    if (result.isEmpty) (false, result)
    val jedis = result.get
    Try(jedis.set(key, JsonTool.object2Json(value))) match {
      case Success(d) =>
        (true, result)
      case Failure(e) => {
        e.printStackTrace()
        (false, result)
      }
    }

  }

  /**
    * 从 redis 取值
    *
    * @param key
    * @tparam T
    * @return
    */
  def get[T: Manifest](key: String): (Option[T], Option[Jedis]) = {
    val result = getJedis()
    if (result.isEmpty) (None, result)
    val jedis = result.get

    Try(JsonTool.json2Object[T](jedis.get(key))) match {
      case Success(d) =>
        (d, result)
      case Failure(e) => {
        e.printStackTrace()
        (None, result)
      }
    }

  }

  /**
    * 根据keys删除redis里的数据
    *
    * @param keys
    * @return
    */
  def del(keys: String*): (Boolean, Option[Jedis]) = {
    val result = getJedis()
    if (result.isEmpty) (false, result)
    val jedis = result.get

    Try(jedis.del(keys: _*)) match {
      case Success(d) =>
        (keys.size == d, result)
      case Failure(e) => {
        e.printStackTrace()
        (false, result)
      }
    }
  }

  def isExist(key: String): (Boolean, Option[Jedis]) = {
    val result = getJedis()
    if (result.isEmpty) (false,result)
    val jedis = result.get
    Try(jedis.exists(key)) match {
      case Success(d) =>
        (d,result)
      case Failure(e) => {
        e.printStackTrace()
        (false,result)
      }
    }
  }
}

object RedisTool {
  private val redisTool: RedisTool = new RedisTool("127.0.0.1", 6379);

  def set(key: String, value: Any): Boolean = {
    val result = redisTool.set(key, value)
    redisTool.returnResource(result._2)
    result._1
  }

  def get[T: Manifest](key: String): Option[T] = {
    val result = redisTool.get(key)
    redisTool.returnResource(result._2)
    result._1
  }

  def del(key: String*): Boolean = {
    val result = redisTool.del(key: _*)
    redisTool.returnResource(result._2)
    result._1
  }

  def isExist(key:String) :Boolean = {
    val result = redisTool.isExist(key)
    redisTool.returnResource(result._2)
    result._1
  }


}