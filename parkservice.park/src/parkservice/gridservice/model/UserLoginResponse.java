package parkservice.gridservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import parkservice.model.ParkSync;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class UserLoginResponse {
	private String name;
	private String email;
	private String license;
	private long uid;
	private int balance;
	private boolean autherized;
	ParkSync sync;
	/**
	 * @return the sync
	 */
	public ParkSync getSync() {
		return sync;
	}
	/**
	 * @param sync the sync to set
	 */
	public void setSync(ParkSync sync) {
		this.sync = sync;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public void setAutherized(boolean autherized) {
		this.autherized = autherized;
	}

	public boolean isAutherized() {
		return autherized;
	}
}
