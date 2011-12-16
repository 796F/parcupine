package parkservice.resources;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import AuthNet.Rebill.CreateCustomerProfileTransactionResponseType;
import AuthNet.Rebill.MessagesTypeMessage;
import AuthNet.Rebill.OrderExType;
import AuthNet.Rebill.ProfileTransAuthCaptureType;
import AuthNet.Rebill.ProfileTransactionType;
import AuthNet.Rebill.ServiceSoap;

import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;
import parkservice.model.ParkResponse;
import parkservice.model.QrcodeRequest;
import parkservice.model.RateResponse;
import parkservice.model.RefillRequest;
import parkservice.model.RefillResponse;
import parkservice.model.UnparkRequest;
import parkservice.model.UnparkResponse;

@Path("/park")
public class ParkResource {
	private long innerAuthenticate(AuthRequest in){
		UserDao userDb = new UserDao();
		User user = null;
		try{
			user = userDb.getUserByEmail(in.getEmail());
		}catch(RuntimeException e){
			return -1;
		}
		if(user==null){
			return -1;
		}else{
			return user.getUserID();
		}
	}

	private CreateCustomerProfileTransactionResponseType chargeUser(int pay_amount, int profileId, int paymentProfileId, int uid){
		java.math.BigDecimal amount = java.math.BigDecimal.valueOf(Double.parseDouble(""+(pay_amount/100)+"."+(pay_amount%100)));

		//try to charge their payment profile the pay_amount
		ProfileTransAuthCaptureType auth_capture = new ProfileTransAuthCaptureType();
		auth_capture.setCustomerProfileId(profileId);
		auth_capture.setCustomerPaymentProfileId(paymentProfileId);
		auth_capture.setAmount(amount);
		OrderExType order = new OrderExType();

		Date x = new Date();
		String dateString = (x.getMonth()+1>9 ? ""+(x.getMonth()+1): "0"+(x.getMonth()+1));
		dateString+=(x.getDate()>9 ? ""+x.getDate(): "0"+x.getDate());
		dateString+=x.getYear()+1900;
		dateString+=(x.getHours()>9 ? ""+x.getHours(): "0"+x.getHours());
		dateString+=(x.getMinutes()>9 ? ""+x.getMinutes(): "0"+x.getMinutes());

		//for us, invoice set to uid:MMddyyyyhhmm
		order.setInvoiceNumber(uid+":"+dateString);

		auth_capture.setOrder(order);
		ProfileTransactionType trans = new ProfileTransactionType();
		trans.setProfileTransAuthCapture(auth_capture);

		ServiceSoap soap = SoapAPIUtilities.getServiceSoap();
		return soap.createCustomerProfileTransaction(SoapAPIUtilities.getMerchantAuthentication(), trans, null);
	}

	//Refill, Park, Unpark
	@POST
	@Path("/park")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ParkResponse parkUser(JAXBElement<ParkRequest> info){
		//check if user is currently parked.  
		ParkResponse output = new ParkResponse();
		ParkRequest in = info.getValue();

		int payment_type = in.getPaymentType();
		int uid = in.getUid();
		String lot = in.getLot();
		String spot = in.getSpot();
		int iterations = in.getIterations();

		AuthRequest userInfo = in.getUserinfo();
		//does user supplied info authenticate?
		if(uid == innerAuthenticate(userInfo)){
			ParkingStatusDao psd = new ParkingStatusDao();
			ParkingInstance ps = psd.getUserParkingStatus(uid);
			//was user previously parked?
			if((new Date()).compareTo(ps.getParkingEndTime())>0){
				//nowtime is after previous end time.  user was not parked.  
				ParkingRateDao prd = new ParkingRateDao();
				ParkingRate pr = prd.getParkingRateByName(spot, lot);
				int spot_id = in.getSpotid();
				int pay_amount = iterations*pr.getParkingRateCents();
				Date start = new Date(); //start os now
				Date end = new Date(); //end is iterations of increment + old time.  
				end.setMinutes(end.getMinutes() + iterations*pr.getTimeIncrementsMins());

				PaymentAccountDao pad = new PaymentAccountDao();
				List<PaymentAccount> pad_list = pad.getAllPaymentMethodForUser(uid);
				int profileId = -1;
				int paymentProfileId = -1;
				if(pad_list.size()>0){
					PaymentAccount pa = pad_list.get(0);
					profileId = Integer.parseInt(pa.getCustomerId()); //this is profileId
					paymentProfileId = Integer.parseInt(pa.getPaymentMethodId()); //this is paymentProfileId
				}

				if(paymentProfileId>0){
					CreateCustomerProfileTransactionResponseType response = chargeUser(pay_amount, profileId, paymentProfileId, uid);
					if(response.getResultCode().value().equalsIgnoreCase("Ok")){
						//if charge completes, store parking instance into db
						//mark user as parked
						ParkingInstance newPark = new ParkingInstance();
						newPark.setPaidParking(true);
						newPark.setParkingBeganTime(start);
						newPark.setParkingEndTime(end);
						newPark.setUserId(uid);
						newPark.setSpaceId(spot_id);

						Payment p = new Payment();
						p.setAmountPaidCents(pay_amount);
						p.setPaymentDateTime(start);
						p.setPaymentType(PaymentType.CreditCard);
						p.setPaymentRefNumber(Arrays.asList(response.getDirectResponse().split(",")).get(6));
						newPark.setPaymentInfo(p);

						boolean result = false;
						try{
							result = psd.addNewParkingAndPayment(newPark);
						}catch(Exception e){

						}
						//then set output to ok
						if(result){
							output.setParkingInstanceId(psd.getUserParkingStatus(uid).getParkingInstId());
							output.setResp("OK");
						}else{
							output.setResp("DAO_ERROR");
						}
					}else{
						output.setResp("BAD_PAY");
					}
				}else{
					output.setResp("NO_PAYMENT_PROFILE");
				}
				//send back info for app to display new session.  


			}else{
				//nowtime is before previous end time.  user currently parked.  
				//THIS SHOULD NEVER HAPPEN.  ONLY occurs if two users log into separate apps, then one parks, the other still logged in.  
				//alert user that they're currently parked somewhere else.  
				output.setResp("USER_PARKED");
			}

		}else{
			output.setResp("BAD_AUTH");
		}
		return output;
	}

	@POST
	@Path("/refill")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RefillResponse refillTime(JAXBElement<RefillRequest> info){
		RefillResponse  output = new RefillResponse ();
		RefillRequest in = info.getValue();
		int uid = in.getUid();
		ParkingRateDao prd = new ParkingRateDao();
		ParkingRate pr = prd.getParkingRateByName(in.getLot(), in.getSpot());
		int iterations = in.getIterations();
		int pay_amount = pr.getParkingRateCents()*iterations;
		int payment_type = in.getPaymentType();
		int spotid = in.getSpotid();
		if(uid == innerAuthenticate(in.getUserinfo())){

			ParkingStatusDao psd = new ParkingStatusDao();
			ParkingInstance pi = psd.getUserParkingStatus(uid);

			Date oldEndTime = pi.getParkingEndTime();
			Date newEndTime= new Date();
			newEndTime.setMinutes(oldEndTime.getMinutes()+iterations*pr.getTimeIncrementsMins());

			//get user payment information.  
			PaymentAccountDao pad = new PaymentAccountDao();
			List<PaymentAccount> pad_list = pad.getAllPaymentMethodForUser(uid);
			int profileId = -1;
			int paymentProfileId = -1;

			if(pad_list.size()>0){
				PaymentAccount pa = pad_list.get(0);
				profileId = Integer.parseInt(pa.getCustomerId()); //this is profileId
				paymentProfileId = Integer.parseInt(pa.getPaymentMethodId()); //this is paymentProfileId
			}
			if(paymentProfileId>0){
				//charge user for refill
				CreateCustomerProfileTransactionResponseType response = chargeUser(pay_amount, profileId, paymentProfileId, uid);
				if(response.getResultCode().value().equalsIgnoreCase("Ok")){
					//update databases

					ParkingInstance newPark = new ParkingInstance();
					newPark.setPaidParking(true);
					newPark.setParkingBeganTime(oldEndTime);
					newPark.setParkingEndTime(newEndTime);
					newPark.setUserId(uid);
					newPark.setSpaceId(spotid);

					Payment p = new Payment();
					p.setAmountPaidCents(pay_amount);
					p.setPaymentDateTime(oldEndTime);
					p.setPaymentType(PaymentType.CreditCard);
					p.setPaymentRefNumber(Arrays.asList(response.getDirectResponse().split(",")).get(6));
					newPark.setPaymentInfo(p);

					boolean result = false; 
					try{
						result = psd.refillParkingForParkingSpace(spotid, newEndTime, p);
					}catch(Exception e){

					}
					if(result){
						output.setResp("OK");
						output.setParkingInstanceId(psd.getUserParkingStatus(uid).getParkingInstId());
					}else{
						output.setResp("WAS_NOT_PARKED");
					}

				}else{
					output.setResp("BAD_PAY");
				}
			}else{
				output.setResp("NO_PAY_PROFILE");
			}

		}else{
			output.setResp("BAD_AUTH");
		}


		return output;
	}

	@POST
	@Path("/unpark")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UnparkResponse unparkUser(JAXBElement<UnparkRequest> info){
		UnparkResponse  output = new UnparkResponse ();
		UnparkRequest in = info.getValue();
		Date endTime = in.getEnd();
		long uid = in.getUid();
		long spotid = in.getSpotid();
		long parkingInstanceId = in.getParkingInstanceId();
		if(uid==innerAuthenticate(in.getUserinfo())){
			ParkingStatusDao psd = new ParkingStatusDao();
			boolean result = false;
			try{
				 result = psd.unparkBySpaceIdAndParkingInstId(spotid, parkingInstanceId, endTime);
			}catch(Exception e){
				
			}
			if(result){
				output.setResp("OK");
			}else{
				output.setResp("DAO_ERROR");
			}

		}else{
			output.setResp("BAD_AUTH");
		}
		return output;
	}

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "Hello I'm the Parking Service :X";
	}
}
