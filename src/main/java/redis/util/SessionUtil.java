package redis.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SessionUtil {
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(),
			"192.168.51.128", 6379, 2000);

	public static void addUser(User u) {
		Jedis jedis = pool.getResource();
		Map<String, String> user = new HashMap<String, String>();
		user.put("uid", String.valueOf(u.getUid()));
		user.put("name", u.getName());
		user.put("pwd", u.getPasswd());
		if (!jedis.exists(String.valueOf(u.getUid()))) {
			jedis.hmset(String.valueOf(u.getUid()), user);
			jedis.expire(String.valueOf(u.getUid()), 1 * 60 * 60);// 一个小时的超时时间
		}
		jedis.close();
	}

	public static User checkUser(String uid) {
		Jedis jedis = pool.getResource();
		User u = null;
		if (jedis.exists(uid)){
			List<String> udata = jedis.hmget(uid, "name","uid","pwd");
			u = new User(udata.get(0),Long.parseLong(udata.get(1)),udata.get(2));
		}
		return u;
		
	}

	public static void delUser(Long uId) {
		Jedis jedis = pool.getResource();
		long count = jedis.del(String.valueOf(uId));
		if (count > 0)
			System.out.println("删除用户" + uId + "成功");
	}
}
