package com.objects;

import java.util.Arrays;
import java.util.List;

public class UserObject {
	/* store user info on get auth to display*/
	private String fname;
	
	private String lname;
	private String email;
	private String phone;
	
	public UserObject(){}
	
	public UserObject(String result){
		List<String> fields = Arrays.asList(result.split("&"));
		this.fname=fields.get(0);
		this.lname=fields.get(1);
		this.email=fields.get(2);
		this.phone = fields.get(3);
	}

	public UserObject(String fname, String lname, String email, String phone) {
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phone = phone;
	}
//	public boolean checkIntegrity(){
//		if(this.getFname().equals("0")){
//			return false;
//		}else if(this.getFname().equals("2")){
//			return false;
//		}else{
//			return true;
//		}
//	}
	
	public String getFname() {
		return fname;
	}
	public String getLname() {
		return lname;
	}
	public String getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	
}
