package redis.util;

public class User {
	private String name;
	private Long uid;
	private String passwd;
	
	public User(String name, Long uid, String passwd) {
		super();
		this.name = name;
		this.uid = uid;
		this.passwd = passwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
}
