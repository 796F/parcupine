package parkservice.model;

public enum ResponseCode {
	//how verbose should responsecode be?
	//EditUser, Park, Refill, Register, Unpark
	OK("Process Completed"), 
	BAD_INFO("Bad Information Supplied"), 
	BAD_PAY("Bad Payment Information Supplied"), 
	SERVER_ERROR("PARQ Server Error"),
	MERCHANT_ERROR("Merchant Server Error");
	
	
	String desc;
	ResponseCode(String input){
		this.desc=input;
	}
	public String getInfo(){
		return desc;
	}
}
