package jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import redis.util.ExLock;

public class TestExlock {
	//private static Jedis jedis = null;
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.51.128", 6379, 2000);
	public static void testPurchase(Long productId,int num,String userName){
		boolean getRes = ExLock.getLock("productOrder", 5000);
		if(!getRes){
			System.out.println("没有成功获取锁");
			return ;
		}
//		try {
//		Thread.sleep(20000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}		
		purchase(productId,num,userName);
		ExLock.relLock("productOrder");
	}
	public static void testPurchaseWithLockTimeOut(Long productId,int num,String userName){
		boolean getRes = ExLock.getLock("productOrder", 5000, userName, 20);
		if(!getRes){
			System.out.println("没有成功获取锁");
			return ;
		}
//		try {
//		Thread.sleep(20000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}		
		purchase(productId,num,userName);
		//ExLock.relLock("productOrder");
		ExLock.relLock("productOrder", userName);
	}
	
	public static void purchase(Long productId,int num,String userName){
		Jedis jedis = pool.getResource();
		String productNumStr = jedis.get(productId.toString());
		long purductNum = Long.parseLong(productNumStr);
		String order = productId+"-"+num;
		String orderKey=userName+"-Orders";
		if(purductNum>=num){
			jedis.rpush(orderKey, order);
			jedis.decrBy(productId.toString(), num);
			System.out.println(Thread.currentThread().getName()+"购买成功："+purductNum+",购买量："+num);
		}else{
			System.out.println(Thread.currentThread().getName()+"库存不足，购买失败,库存="+purductNum+",购买量："+num);
		}
			
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedisClient = pool.getResource();
		Long productId=10001l;
		Integer productInitNum=5;
		jedisClient.set(productId.toString(), productInitNum.toString());
		
		Thread t1 = new PurchaseThread(productId,2,"t1");
		t1.start();
//		try {
//		Thread.sleep(6000);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}		
		
		Thread t2 = new PurchaseThread(productId,1,"t2");
		t2.start();
		Thread t3 = new PurchaseThread(productId,3,"t3");
		t3.start();
		

		//System.out.println(ExLock.relLock("productOrder"));
	}

}
