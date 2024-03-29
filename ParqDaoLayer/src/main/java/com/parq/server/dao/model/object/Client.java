package com.parq.server.dao.model.object;

import java.io.Serializable;

/**
 * @author GZ
 *
 */
public class Client implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6448691730521532161L;
	private long id = -1;
	private String name;
	private String address;
	private String clientDescription;
	private PaymentMethod paymentMethod;
	
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the clientDescription
	 */
	public String getClientDescription() {
		return clientDescription;
	}
	/**
	 * @param clientDescription the clientDescription to set
	 */
	public void setClientDescription(String clientDescription) {
		this.clientDescription = clientDescription;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the paymentMethod
	 */
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	
}
