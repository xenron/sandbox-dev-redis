package redis.util;

import java.util.Calendar;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ExLock {
	//private static Jedis jedis = new Jedis("192.168.51.128");
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.51.128", 6379, 2000);
	public static boolean getLock(String lockkey, long getTimeOut) {
		Jedis jedis = pool.getResource();
		long beginTime = Calendar.getInstance().getTimeInMillis();
		long optRes = jedis.setnx(lockkey, "");
		if (optRes == 1)
			return true;
		else {
			long nowTime = Calendar.getInstance().getTimeInMillis();
			int waitTime=1;
			while (nowTime - beginTime < getTimeOut) {
				optRes = jedis.setnx(lockkey, "");
				waitTime++;
				if (optRes == 1){
					System.out.println("线程"+Thread.currentThread().getName()+",等待次数="+waitTime);
					return true;
				}
				sleep(5);
			}
			System.out.println("线程"+Thread.currentThread().getName()+",等待次数="+waitTime);
			return false;
		}
	}

	public static boolean getLock(String lockkey, long getTimeOut, String ower,
			long lockTimeOut) {
		Jedis jedis = pool.getResource();
		long beginTime = Calendar.getInstance().getTimeInMillis();
		String optRes = jedis.set(lockkey, ower, "nx", "ex", lockTimeOut);
		if ("OK".equals(optRes))
			return true;
		else {
			long nowTime = Calendar.getInstance().getTimeInMillis();
			while (nowTime - beginTime < getTimeOut) {
				optRes = jedis.set(lockkey, ower, "nx", "ex", lockTimeOut);
				if ("OK".equals(optRes)){
					System.out.println("线程"+Thread.currentThread().getName()+"等待获取锁成功");
					return true;
				}
				sleep(5);
			}
			return false;
		}
	}

	public static boolean relLock(String lockkey) {
		Jedis jedis = pool.getResource();
		long optRes = jedis.del(lockkey);
		if (optRes == 1)
			return true;
		else
			return false;
	}

	public static boolean relLock(String lockkey, String ower) {
		Jedis jedis = pool.getResource();
		String keyvalue = jedis.get(lockkey);
		long optRes = -1;
		if (ower.equals(keyvalue)) {
			optRes = jedis.del(lockkey);
		}else
			System.out.println(ower+"不能释放属于"+keyvalue+"的锁");
		if (optRes == 1)
			return true;
		else
			return false;
	}

	private static void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//public static void testPurchase
	// private long static retryGet(long beginTime,long getTimeOut){
	//
	// }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
