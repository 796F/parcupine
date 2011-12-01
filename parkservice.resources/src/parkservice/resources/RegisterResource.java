package parkservice.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import parkservice.model.RegisterRequest;
import parkservice.model.RegisterResponse;
import parkservice.model.ResponseCode;
import parkservice.model.TestResponseCode;

import AuthNet.Rebill.ArrayOfCustomerPaymentProfileType;
import AuthNet.Rebill.CreateCustomerProfileResponseType;
import AuthNet.Rebill.CreditCardType;
import AuthNet.Rebill.CustomerPaymentProfileType;
import AuthNet.Rebill.CustomerProfileType;
import AuthNet.Rebill.PaymentType;
import AuthNet.Rebill.ServiceSoap;
import AuthNet.Rebill.ValidationModeEnum;

import com.parq.server.dao.UserDao;
import com.parq.server.dao.exception.DuplicateEmailException;
import com.parq.server.dao.model.object.User;

@Path("/register")
public class RegisterResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisterResponse register(JAXBElement<RegisterRequest> input){
		RegisterRequest info = input.getValue();
		User newUser = new User();
		newUser.setEmail(info.getEmail());
		newUser.setPassword(info.getPassword());
		UserDao userDb = new UserDao();
		//getByEmail throws exception when user is not in db, aka rs=null.
		//thus user must be in db or this will throw exception.  

		
		boolean result = false;
		try{
			result = userDb.createNewUser(newUser);
			
		}catch(DuplicateEmailException dup){
			
		}catch(IllegalStateException e){
			
		}
		if(result){
				return new RegisterResponse(TestResponseCode.OK);
		}else{
			return new RegisterResponse(TestResponseCode.BAD_INFO);
		}
		
//		ServiceSoap soap = SoapAPIUtilities.getServiceSoap();
//		CustomerPaymentProfileType new_payment_profile = new CustomerPaymentProfileType();
//
//		PaymentType new_payment = new PaymentType();
//
//		CreditCardType new_card = new CreditCardType();
//		new_card.setCardNumber(info.getCreditCard());
//		new_card.setCardCode(info.getCccNumber());
//
//		try{
//			javax.xml.datatype.XMLGregorianCalendar cal = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar();
//			cal.setMonth(info.getExpMonth());
//			cal.setYear(info.getExpYear());
//			new_card.setExpirationDate(cal);
//			// System.out.println(new_card.getExpirationDate().toXMLFormat());
//		}
//		catch(javax.xml.datatype.DatatypeConfigurationException dce){
//			System.out.println(dce.getMessage());
//		}
//
//		new_payment.setCreditCard(new_card);
//		//new_payment.setBankAccount(new_bank);
//
//		new_payment_profile.setPayment(new_payment);
//
//		CustomerProfileType m_new_cust = new CustomerProfileType();
//		//TODO: grab returned UID and stick here. 
//		m_new_cust.setMerchantCustomerId("ourkey");
//
//		m_new_cust.setEmail(info.getEmail());
//		m_new_cust.setDescription("User_ID:" + "ourkey " + "Name:" +info.getHolderName());
//
//		ArrayOfCustomerPaymentProfileType pay_list = new ArrayOfCustomerPaymentProfileType();
//		pay_list.getCustomerPaymentProfileType().add(new_payment_profile);
//
//		m_new_cust.setPaymentProfiles(pay_list);
//
//		CreateCustomerProfileResponseType response = soap.createCustomerProfile(SoapAPIUtilities.getMerchantAuthentication(),m_new_cust,ValidationModeEnum.LIVE_MODE);
//		if(result){
//
//			if(response != null){
//
//				long profileId = response.getCustomerProfileId();
//				long paymentProfileId = response.getCustomerPaymentProfileIdList().getLong().get(0);
//
//				//store profileID and paymentProfileId using dao.
//				return new RegisterResponse(ResponseCode.OK);
//
//			}
//			else{
//				return new RegisterResponse(ResponseCode.BAD_PAY);
//			}
//
//		}else{
//			return new RegisterResponse(ResponseCode.BAD_INFO);
//		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Register Service!!!";
	}
}
