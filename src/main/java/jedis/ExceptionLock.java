package jedis;

import redis.util.ExLock;

public class ExceptionLock {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("--------");
		for(int i=0;i<100;i++){
			System.out.println(i+" "+ExLock.relLock("productOrder"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("---1111111111-----");
	}

}
