package codis;

import redis.clients.jedis.Jedis;

import com.wandoulabs.jodis.JedisResourcePool;
import com.wandoulabs.jodis.RoundRobinJedisPool;

public class CodisUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JedisResourcePool jedisPool = RoundRobinJedisPool.create()
		        .curatorClient("172.30.0.128:2181", 30000).zkProxyDir("/zk/codis/db_test/proxy").build();
		try (Jedis jedis = jedisPool.getResource()) {
		    jedis.set("foo", "bar");
		    String value = jedis.get("foo");
		    System.out.println(value);
		}
	}

}
