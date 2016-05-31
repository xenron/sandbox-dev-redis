package jedis;

public class PurchaseThread extends Thread {
	private Long productId;
	private int num;
	private String userName;
	
	public PurchaseThread(Long productId,int num,String userName){
		super.setName(userName);
		this.productId=productId;
		this.num=num;
		this.userName=userName;
	}
	@Override
	public void run() {
		//TestExlock.testPurchase(productId, num, userName);
		TestExlock.testPurchaseWithLockTimeOut(productId, num, userName);
	}

}
