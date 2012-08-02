package parkservice.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBElement;

import AuthNet.Rebill.CreateCustomerProfileTransactionResponseType;
import AuthNet.Rebill.OrderExType;
import AuthNet.Rebill.ProfileTransAuthCaptureType;
import AuthNet.Rebill.ProfileTransactionType;
import AuthNet.Rebill.ServiceSoap;

import com.parq.server.dao.LicensePlateDao;
import com.parq.server.dao.MiscellaneousDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.UserPrePaidAccountBalanceDao;
import com.parq.server.dao.model.object.LicensePlate;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.Payment;
import com.parq.server.dao.model.object.UserPrePaidAccountBalance;
import com.parq.server.dao.model.object.UserSelfReporting;
import com.parq.server.dao.model.object.Payment.PaymentType;
import com.parq.server.dao.model.object.PaymentAccount;
import com.parq.server.dao.model.object.User;
import com.parq.server.dao.model.object.UserScore;
import com.parq.server.grid.GridManagementService;
import com.parq.server.grid.model.object.GridWithFillRate;
import com.parq.server.grid.model.object.ParkingLocationWithFillRate;
import com.sun.research.ws.wadl.Response;

import parkservice.gridservice.model.FindGridsByGPSCoordinateRequest;
import parkservice.gridservice.model.FindGridsByGPSCoordinateResponse;
import parkservice.gridservice.model.GetSpotLevelInfoRequest;
import parkservice.gridservice.model.GetSpotLevelInfoResponse;
import parkservice.gridservice.model.GetUpdatedGridInfoRequest;
import parkservice.gridservice.model.GetUpdatedGridInfoRespone;
import parkservice.gridservice.model.GetUpdatedSpotLevelInfoRequest;
import parkservice.gridservice.model.GetUpdatedSpotLevelInfoResponse;
import parkservice.gridservice.model.GetUpdatedStreetInfoRequest;
import parkservice.gridservice.model.GetUpdatedStreetInfoResponse;
import parkservice.gridservice.model.GpsCoorWithOrder;
import parkservice.gridservice.model.GpsCoordinate;
import parkservice.gridservice.model.ParkingSpaceWithStatus;
import parkservice.gridservice.model.SearchArea;
import parkservice.gridservice.model.SearchForStreetsRequest;
import parkservice.gridservice.model.SearchForStreetsResponse;
import parkservice.gridservice.model.SimpleParkingSpaceWithStatus;
import parkservice.gridservice.model.UserLoginResponse;
import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;
import parkservice.model.ParkResponse;
import parkservice.model.RefillRequest;
import parkservice.model.RefillResponse;
import parkservice.model.UnparkRequest;
import parkservice.model.UnparkResponse;
import parkservice.userscore.model.AddUserReportingRequest;
import parkservice.userscore.model.AddUserReportingResponse;
import parkservice.userscore.model.GetCountRequest;
import parkservice.userscore.model.GetCountResponse;
import parkservice.userscore.model.GetUserReportingHistoryRequest;
import parkservice.userscore.model.GetUserReportingHistoryResponse;
import parkservice.userscore.model.GetUserScoresRequest;
import parkservice.userscore.model.GetUserScoreResponse;
import parkservice.userscore.model.Score;
import parkservice.userscore.model.UpdateUserScoreRequest;
import parkservice.userscore.model.UpdateUserScoreResponse;
import parkservice.userscore.model.UserParkingStatusReport;

@Path("/")
public class ParkResource {
	@Context 
	ContextResolver<JAXBContextResolver> providers;
	/**
	 * returns User_ID, or -1 if bad
	 * */
	private User innerAuthenticate(AuthRequest in){
		UserDao userDb = new UserDao();
		User user = null;
		try{
			user = userDb.getUserByEmail(in.getEmail());
		}catch(RuntimeException e){
		}
		if(user!=null&&user.getPassword().equals(in.getPassword())){
			return user;
		}else{
			return null;
		}
	}

	private CreateCustomerProfileTransactionResponseType chargeUser(int pay_amount, long profileId, long paymentProfileId, long uid){
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


	@POST
	@Path("/park")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ParkResponse parkUser(JAXBElement<ParkRequest> info){
		Date start = new Date(); //start off now
		//check if user is currently parked.  
		ParkResponse output = new ParkResponse();
		ParkRequest in = info.getValue();

		long uid = in.getUid();
		//does user supplied info authenticate?
		User user = innerAuthenticate(in.getUserInfo());
		if(user != null && uid == user.getUserID()){

			ParkingStatusDao psd = new ParkingStatusDao();
			ParkingInstance ps = null;
			try{
				ps = psd.getUserParkingStatus(uid);
			}catch (Exception e){}
			//was user previously parked?
			if(ps==null||ps.getParkingEndTime().compareTo(start)<0){

				long spot_id = in.getSpotId();

				//nowtime is after previous end time.  user was not parked.  
				ParkingRateDao prd = new ParkingRateDao();
				ParkingRate pr = null; 
				try{
					pr = prd.getParkingRateBySpaceId(spot_id);
				}catch(Exception e){}
				if(pr!=null){
					int durationMinutes = in.getDurationMinutes();
					int payment_amount = in.getChargeAmount();
					int payment_type = in.getPaymentType();

					int iterations = durationMinutes/(pr.getTimeIncrementsMins());
					if(iterations*pr.getParkingRateCents()==payment_amount){
						//if the price, duration, and rate supplied match up,
						Date end = new Date(); //end is iterations of increment + old time.  
						long msec = 1000*durationMinutes*60;
						end.setTime(start.getTime()+msec);


						PaymentAccountDao pad = new PaymentAccountDao();
						List<PaymentAccount> pad_list = null;
						try{
							pad_list = pad.getAllPaymentMethodForUser(uid);
						}catch(Exception e){}

						long profileId = -1;
						long paymentProfileId = -1;
						long account_id = -1;
						if(pad_list!=null && pad_list.size()>0){
							PaymentAccount pa = pad_list.get(0);
							profileId = Integer.parseInt(pa.getCustomerId()); //this is profileId
							paymentProfileId = Integer.parseInt(pa.getPaymentMethodId()); //this is paymentProfileId
							account_id = pa.getAccountId();

						}

						if(paymentProfileId>0){
							CreateCustomerProfileTransactionResponseType response = chargeUser(payment_amount, profileId, paymentProfileId, uid);
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
								p.setAmountPaidCents(payment_amount);
								p.setPaymentDateTime(start);
								p.setPaymentType(PaymentType.CreditCard);
								p.setPaymentRefNumber(Arrays.asList(response.getDirectResponse().split(",")).get(6));
								p.setAccountId(account_id);
								newPark.setPaymentInfo(p);

								boolean result = false;
								try{
									result = psd.addNewParkingAndPayment(newPark);
								}catch(Exception e){}
								//then set output to ok
								if(result){
									ParkingInstance finalInstance = psd.getUserParkingStatus(uid);
									output.setParkingReferenceNumber(finalInstance.getParkingRefNumber());
									output.setEndTime(end.getTime());
									output.setResp("OK");


									try{
										File file = new File("/user_logs/ParkRequests.txt");
							            file.createNewFile();
							            FileOutputStream fout = new FileOutputStream(file, true);
										fout.write(("Park() asked at "+new Date() + " INFO: spot id=" + spot_id + " uid=" + uid).getBytes());
										fout.close();
									}catch (Exception e){//Catch exception if any
										System.err.println("Error: " + e.getMessage());
									}
									
								}else{
									output.setResp("DAO_ERROR");
								}
							}else{
								output.setResp("BAD_PAY");
							}
						}else{
							output.setResp("NO_PAYMENT_PROFILE");
						}

					}

					//send back info for app to display new session.  

				}else{
					output.setResp("parkrate doesn't exist, check spotid");
				}
			}else{
				//nowtime is before previous end time.  user currently parked.  
				//THIS SHOULD NEVER HAPPEN.  ONLY occurs if two users log into separate apps, then one parks, the other still logged in.  
				//alert user that they're currently parked somewhere else.  
				output.setResp("USER_PARKED");
			}

		}else{
			output.setResp("BAD_AUTH, check login fields or uid");
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
		long uid = in.getUid();
		int payment_type = in.getPaymentType();
		
		User user = innerAuthenticate(in.getUserInfo());
		if(user != null && uid == user.getUserID()){
			String parkingReference = in.getParkingReferenceNumber();
			ParkingStatusDao psd = new ParkingStatusDao();
			ParkingInstance pi = null; try{
				pi =psd.getUserParkingStatus(uid);
			}catch(Exception e){}

			if(pi!=null && pi.getParkingRefNumber().equals(parkingReference)){
				long spotid = in.getSpotId();
				int durationMinutes = in.getDurationMinutes();
				int pay_amount = in.getChargeAmount();
				ParkingRateDao prd = new ParkingRateDao();
				ParkingRate pr = null;
				try{
					pr = prd.getParkingRateBySpaceId(spotid);
				}catch(Exception e){}
				if(pr!=null && pay_amount/pr.getParkingRateCents()==durationMinutes/pr.getTimeIncrementsMins()){
					//if the rate, amount, and duration match up,
					Date oldEndTime = pi.getParkingEndTime();
					Date newEndTime= new Date();
					long msec = 1000*durationMinutes*60;
					newEndTime.setTime(oldEndTime.getTime()+msec);

					//get user payment information.  
					PaymentAccountDao pad = new PaymentAccountDao();
					List<PaymentAccount> pad_list = null; 
					try{
						pad_list =pad.getAllPaymentMethodForUser(uid);
					}catch(Exception e){}
					int profileId = -1;
					int paymentProfileId = -1;
					long account_id = -1;
					if(pad_list!=null && pad_list.size()>0){
						PaymentAccount pa = pad_list.get(0);
						profileId = Integer.parseInt(pa.getCustomerId()); //this is profileId
						paymentProfileId = Integer.parseInt(pa.getPaymentMethodId()); //this is paymentProfileId
						account_id = pa.getAccountId();
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
							p.setAccountId(account_id);
							newPark.setPaymentInfo(p);

							boolean result = false; 
							try{
								result = psd.refillParkingForParkingSpace(spotid, newEndTime, p);
							}catch(Exception e){}
							if(result){
								ParkingInstance finalInstance = psd.getUserParkingStatus(uid);
								output.setParkingReferenceNumber(finalInstance.getParkingRefNumber());
								output.setEndTime(newEndTime.getTime());
								output.setResp("OK");
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
					output.setResp("PR may be null, or 3way check failed");
				}

			}else{
				output.setResp("BAD_INST_REF");
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
		long uid = in.getUid();
		
		User user = innerAuthenticate(in.getUserInfo());
		if(user != null && uid == user.getUserID()){
			ParkingStatusDao psd = new ParkingStatusDao();
			boolean result = false;
			try{
				result = psd.unparkBySpaceIdAndParkingRefNum(in.getSpotId(),in.getParkingReferenceNumber(), new Date());
			}catch(Exception e){
				output.setResp("EXCEPTION CAUGHT.  spotid:" + in.getSpotId() + " refNum" + in.getParkingReferenceNumber());
				return output;
			}
			if(result){
				output.setResp("OK");
			}else{
				output.setResp("dao FALSE.  spotid:" + in.getSpotId() + " refNum" + in.getParkingReferenceNumber());
			}

		}else{
			output.setResp("BAD_AUTH");
		}
		return output;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "Peter Parker: I love you Mary Jane!";
	}
	
	
	
	/****************************************************
	 *
	 * New Grid search and management methods added by Gordon
	 *
	 ***************************************************/
	
	
	@POST
	@Path("/FindGridsByGPSCoordinateRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FindGridsByGPSCoordinateResponse[] findGridByGPSCoor(JAXBElement<FindGridsByGPSCoordinateRequest> jaxbRequest){
		FindGridsByGPSCoordinateRequest request = jaxbRequest.getValue();
		long lastUpdateDateTime = request.getLastUpdateTime();
		List<SearchArea> searchArea = request.getSearchArea();
		
		// get all the grids with fill status with in the given bounding box
		List<GridWithFillRate> gridWFillRate = getGridWithFillRates(searchArea);
		
		List<FindGridsByGPSCoordinateResponse> responseList = new ArrayList<FindGridsByGPSCoordinateResponse>();
		for (GridWithFillRate grid: gridWFillRate) {
			// only send the items that has been recently updated
			if (grid.getLastUpdatedDateTime() > lastUpdateDateTime) {
				FindGridsByGPSCoordinateResponse response = new FindGridsByGPSCoordinateResponse();
				response.setGridId(grid.getGridId());
				response.setFillRate(1.0 * grid.getFillRate() / 100);
				response.setLatitude(grid.getLatitude());
				response.setLongitude(grid.getLongitude());
				responseList.add(response);
			}
		}
		
		// return the array representation of the FindGridsByGPSCoordinateResponse
		return responseList.toArray(new FindGridsByGPSCoordinateResponse[0]);
	}
	
	@POST
	@Path("/GetUpdatedGridInfoRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetUpdatedGridInfoRespone[] getUpdatedGridInfo(
				JAXBElement<GetUpdatedGridInfoRequest> jaxbRequest){
		GridManagementService gridService = GridManagementService.getInstance();
		GetUpdatedGridInfoRequest request = jaxbRequest.getValue();
		List<Long> gridList = new ArrayList<Long>();
		for (long gridId : request.getGridIds()) {
			gridList.add(gridId);
		}
		
		List<GridWithFillRate> gridFillRateList = gridService.getGridStatus(gridList);
		GetUpdatedGridInfoRespone[] responses = new GetUpdatedGridInfoRespone[gridFillRateList.size()];
		for (int i = 0; i < gridFillRateList.size(); i++) {
			GetUpdatedGridInfoRespone grid = new GetUpdatedGridInfoRespone();
			grid.setFillRate((0.0 + gridFillRateList.get(i).getFillRate()) / 100);
			grid.setGridId(gridFillRateList.get(i).getGridId());
			responses[i] = grid;
		}
		return responses;
	}
	
	private List<GridWithFillRate> getGridWithFillRates(
			List<SearchArea> searchArea) {
		
		Set<GridWithFillRate> gridWFillRate = new HashSet<GridWithFillRate>();
		GridManagementService gridService = GridManagementService.getInstance();
		
		// go through all the bounding boxes
		for (SearchArea se : searchArea) {
			GpsCoordinate northEastCorner = se.getNorthEastCorner();
			GpsCoordinate southWestCorner = se.getSouthWestCorner();
			
			// get all the grids with fill status with in the given bounding box
			List<GridWithFillRate> tempWithFillRate = gridService.findGridWithFillrateByGPSCoor(
					northEastCorner.getLatitude(), northEastCorner.getLongitude(), 
					southWestCorner.getLatitude(), southWestCorner.getLongitude());
			
			gridWFillRate.addAll(tempWithFillRate);
		}
		return new ArrayList<GridWithFillRate>(gridWFillRate);
	}

	@POST
	@Path("/SearchForStreetsRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SearchForStreetsResponse[] searchForStreets(JAXBElement<SearchForStreetsRequest> jaxbRequest){
		
		SearchForStreetsRequest request = jaxbRequest.getValue();
		List<SearchArea> searchArea = request.getSearchArea();
		// get all the Parking Blocks with fill status within the given gps bounding box
		List<ParkingLocationWithFillRate> parkingLocationsWithFillRate = getParkingLocaitonWithFillRate(searchArea);

		List<SearchForStreetsResponse> responseList = new ArrayList<SearchForStreetsResponse>();
		// create the individual street response
		for (ParkingLocationWithFillRate pl : parkingLocationsWithFillRate) {
			SearchForStreetsResponse response = new SearchForStreetsResponse();
			response.setFillRate(1.0 * pl.getFillRate() / 100);
			response.setStreetId(pl.getLocationId());
			// set the gps coordinate for the streets
			for (int i = 0; i < pl.getGeoPoints().size(); i++) {
				GpsCoorWithOrder gpsCoor = new GpsCoorWithOrder();
				gpsCoor.setLatitude(pl.getGeoPoints().get(i).getLatitude());
				gpsCoor.setLongitude(pl.getGeoPoints().get(i).getLongitude());
				gpsCoor.setOrder(i);
				response.getGpsCoor().add(gpsCoor);
			}
			responseList.add(response);
		}
		
		// return the array representation of the StreetStatusList
		return responseList.toArray(new SearchForStreetsResponse[0]);
	}
	
	private List<ParkingLocationWithFillRate> getParkingLocaitonWithFillRate(
			List<SearchArea> searchArea) {
		
		Set<ParkingLocationWithFillRate> resultSet = new HashSet<ParkingLocationWithFillRate>();
		GridManagementService gridService = GridManagementService.getInstance();
		
		// go through all the bounding boxes
		for (SearchArea se : searchArea) {
			GpsCoordinate northEastCorner = se.getNorthEastCorner();
			GpsCoordinate southWestCorner = se.getSouthWestCorner();
		
			List<ParkingLocationWithFillRate> parkingLocationsWithFillRate = gridService
				.findStreetByGPSCoor(					
						northEastCorner.getLatitude(), northEastCorner.getLongitude(), 
						southWestCorner.getLatitude(), southWestCorner.getLongitude());
			
			resultSet.addAll(parkingLocationsWithFillRate);
		}

		return new ArrayList<ParkingLocationWithFillRate>(resultSet);
	}

	@POST
	@Path("/GetUpdatedStreetInfoRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetUpdatedStreetInfoResponse[] getUpdatedStreetInfo(JAXBElement<GetUpdatedStreetInfoRequest> jaxbRequest){
		GetUpdatedStreetInfoRequest request = jaxbRequest.getValue();
		long lastUpdateDateTime = request.getLastUpdateTime();
		List<SearchArea> searchArea = request.getSearchArea();
		
		List<GetUpdatedStreetInfoResponse> responseList = new ArrayList<GetUpdatedStreetInfoResponse>();
		List<ParkingLocationWithFillRate> parkingLocationsWithFillRate = 
				getParkingLocaitonWithFillRate(searchArea);
		
		// create the individual street response
		for (ParkingLocationWithFillRate pl : parkingLocationsWithFillRate) {
			if (pl.getLastUpdatedDateTime() > lastUpdateDateTime) {
				GetUpdatedStreetInfoResponse response = new GetUpdatedStreetInfoResponse();
				response.setFillRate(1.0 * pl.getFillRate() / 100);
				response.setStreetId(pl.getLocationId());
				responseList.add(response);
			}
		}

		// return the array representation of the GetUpdatedStreetInfoResponse
		return responseList.toArray(new GetUpdatedStreetInfoResponse[0]);
	}
	
	@POST
	@Path("/GetStreetInfoRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetSpotLevelInfoResponse[] getStreetInfo(JAXBElement<GetSpotLevelInfoRequest> jaxbRequest){
		
		GetSpotLevelInfoRequest request = jaxbRequest.getValue();
		List<SearchArea> searchArea = request.getSearchArea();
		
		List<GetSpotLevelInfoResponse> responseList = new ArrayList<GetSpotLevelInfoResponse>();
		
		// get all the Parking Blocks with fill status within the given gps bounding box
		Set<ParkingLocationWithFillRate> parkingLocationsWithFillRate = 
			new HashSet<ParkingLocationWithFillRate>(getParkingLocaitonWithFillRate(searchArea));
		
		// for each of the parking block, get all the parking space information inside the block
		for (ParkingLocationWithFillRate pl : parkingLocationsWithFillRate) {
			GetSpotLevelInfoResponse response = new GetSpotLevelInfoResponse();
			response.setStreetId(pl.getLocationId());
			response.setFillRate(1.0 * pl.getFillRate() / 100);
			
			// insert the spaces list
			for (ParkingSpace ps :  pl.getSpaces()) {
				ParkingSpaceWithStatus spaceWithStatus = new ParkingSpaceWithStatus();
				spaceWithStatus.setSpaceId(ps.getSpaceId());
				spaceWithStatus.setSpaceName(ps.getSpaceIdentifier());
				spaceWithStatus.setStreetId(pl.getLocationId());
				spaceWithStatus.setLatitude(ps.getLatitude());
				spaceWithStatus.setLongitude(ps.getLongitude());
				spaceWithStatus.setSegment(ps.getSegment());
				// set the parking space status
				if (pl.isSpaceAvaliable(ps.getSpaceId())) {
					spaceWithStatus.setStatus("available");
				} else {
					spaceWithStatus.setStatus("parked");
				}
				response.getParkingSpace().add(spaceWithStatus);
			}
			responseList.add(response);
		}
		
		// return the array representation of the GetUpdatedStreetInfoResponse
		return responseList.toArray(new GetSpotLevelInfoResponse[0]);
	}
	
	@POST
	@Path("/GetUpdatedSpotLevelInfoRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetUpdatedSpotLevelInfoResponse[]
			getUpdatedSpotLevelInfo(JAXBElement<GetUpdatedSpotLevelInfoRequest> jaxbRequest) {
		
		GetUpdatedSpotLevelInfoRequest request = jaxbRequest.getValue();
		List<SearchArea> searchArea = request.getSearchArea();
		
		List<GetUpdatedSpotLevelInfoResponse> responseList = new ArrayList<GetUpdatedSpotLevelInfoResponse>();
		
		// get all the Parking Blocks with fill status within the given gps bounding box
		Set<ParkingLocationWithFillRate> parkingLocationsWithFillRate = new HashSet<ParkingLocationWithFillRate>(
				getParkingLocaitonWithFillRate(searchArea));
		
		// for each of the parking block, get all the parking space information inside the block
		for (ParkingLocationWithFillRate pl : parkingLocationsWithFillRate) {
			GetUpdatedSpotLevelInfoResponse response = new GetUpdatedSpotLevelInfoResponse();
			response.setStreetId(pl.getLocationId());
			
			// insert the spaces list
			for (ParkingSpace ps :  pl.getSpaces()) {
				SimpleParkingSpaceWithStatus spaceWithStatus = new SimpleParkingSpaceWithStatus();
				spaceWithStatus.setSpaceId(ps.getSpaceId());
				// set the parking space status
				if (pl.isSpaceAvaliable(ps.getSpaceId())) {
					spaceWithStatus.setStatus("available");
				} else {
					spaceWithStatus.setStatus("parked");
				}
				response.getParkingSpace().add(spaceWithStatus);
			}
			responseList.add(response);
		}
		
		// return the array representation of the GetUpdatedSpotLevelInfoResponse
		return responseList.toArray(new GetUpdatedSpotLevelInfoResponse[0]);
	}
	
	@POST
	@Path("/GetUserScoreRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetUserScoreResponse
			getUserScore(JAXBElement<GetUserScoresRequest> jaxbRequest) {
		
		GetUserScoresRequest request = jaxbRequest.getValue();
		UserDao userDao = new UserDao();
		List<UserScore> userScores = userDao.getScoreHistoryForUser(request.getUserId());
		
		List<Score> scores = new ArrayList<Score>();
		for (UserScore uScore : userScores) {
			Score score = new Score();
			score.setScoreId(uScore.getScoreId());
			score.setUserId(uScore.getUserId());
			score.setScore1(uScore.getScore1());
			score.setScore2(uScore.getScore2());
			score.setScore3(uScore.getScore3());
			scores.add(score);
		}
		
		GetUserScoreResponse response = new GetUserScoreResponse();
		response.setScores(scores);
		return response;
	}
	
	@POST
	@Path("/UpdateUserScoreRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateUserScoreResponse
			updateUserScore(JAXBElement<UpdateUserScoreRequest> jaxbRequest) {
	
		UpdateUserScoreRequest request = jaxbRequest.getValue();
		UserDao userDao = new UserDao();
		
		UserScore uScore = new UserScore();
		uScore.setScoreId(request.getScore().getScoreId());
		uScore.setUserId(request.getScore().getUserId());
		uScore.setScore1(request.getScore().getScore1());
		uScore.setScore2(request.getScore().getScore2());
		uScore.setScore3(request.getScore().getScore3());
		
		boolean updateSuccessful = userDao.updateUserScore(uScore);
		UpdateUserScoreResponse response = new UpdateUserScoreResponse();
		response.setUpdateSuccessful(updateSuccessful);
		return response;
	}
	
	@POST
	@Path("/GetCountRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetCountResponse getCount(JAXBElement<GetCountRequest> countRequest) {
		MiscellaneousDao mDao = new MiscellaneousDao();
		long countId = mDao.getNextCount();
		GetCountResponse response = new GetCountResponse();
		response.setCount((int)(countId % 4));
		return response;
	}
	
	@POST
	@Path("/AddUserReportingRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AddUserReportingResponse addUserReporting(JAXBElement<AddUserReportingRequest> jaxbRequest) {
		AddUserReportingRequest request = jaxbRequest.getValue();
		ParkingStatusDao statusDao = new ParkingStatusDao();
		MiscellaneousDao mDao = new MiscellaneousDao();
		
		String status = "AVAILABLE";
		Date curTime = new Date(System.currentTimeMillis());
		List<ParkingInstance> parkingInstance = 
			statusDao.getParkingStatusBySpaceIds(new long[]{request.getSpaceId()});
		if (parkingInstance != null && 
				!parkingInstance.isEmpty() && 
				parkingInstance.get(0).getParkingEndTime().compareTo(curTime) > 0) {
			status = "PARKED";
		}
		
		UserSelfReporting report = new UserSelfReporting();
		report.setParkingSpaceStatus(status);
		report.setReportDateTime(curTime);
		report.setUserId(request.getUserId());
		report.setSpaceId(request.getSpaceId());
		report.setScore1(request.getScore1());
		report.setScore2(request.getScore2());
		report.setScore3(request.getScore3());
		report.setScore4(request.getScore4());
		report.setScore5(request.getScore5());
		report.setScore6(request.getScore6());
		
		boolean isSucccessful = mDao.insertUserSelfReporting(report);
		AddUserReportingResponse response = new AddUserReportingResponse();
		response.setUpdateSuccessful(isSucccessful);
		return response;
	}
	
	@POST
	@Path("/GetUserReportingHistoryRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public GetUserReportingHistoryResponse getUserReportingHistory(
			JAXBElement<GetUserReportingHistoryRequest> jaxbRequest) {
	
		GetUserReportingHistoryRequest request = jaxbRequest.getValue();
		MiscellaneousDao mDao = new MiscellaneousDao();
		List<UserSelfReporting> selfReports = mDao.getUserSelfReportingHistoryForUser(request.getUserId());
		
		List<UserParkingStatusReport> reports = new ArrayList<UserParkingStatusReport>();
		for (UserSelfReporting sReport : selfReports) {
			UserParkingStatusReport report = new UserParkingStatusReport();
			report.setReportId(sReport.getReportId());
			report.setSpaceId(sReport.getSpaceId());
			report.setUserId(sReport.getUserId());
			report.setReportDateTime(sReport.getReportDateTime());
			report.setScore1(sReport.getScore1());
			report.setScore2(sReport.getScore2());
			report.setScore3(sReport.getScore3());
			report.setScore4(sReport.getScore4());
			report.setScore5(sReport.getScore5());
			report.setScore6(sReport.getScore6());
			reports.add(report);
		}
		
		GetUserReportingHistoryResponse response = new GetUserReportingHistoryResponse();
		response.setReports(reports);
		return response;
	}
	
	@POST
	@Path("/AuthRequest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserLoginResponse login(JAXBElement<AuthRequest> jaxbRequest) {
		AuthRequest authRequest = jaxbRequest.getValue();
		
		UserLoginResponse response = new UserLoginResponse();
		User user = innerAuthenticate(authRequest);
		if(user != null){
			response.setAutherized(true);
			response.setEmail(authRequest.getEmail());
			response.setUid(user.getUserID());
			//Get license plate
			LicensePlateDao lpDao = new LicensePlateDao();
			List<LicensePlate> plates = lpDao.getLicensePlateByUserId(user.getUserID());
			if (plates != null && !plates.isEmpty()) {
				response.setLicense(plates.get(0).getPlateNum());
			}
			// Get Prepaid account
			UserPrePaidAccountBalanceDao prePaidDao = new UserPrePaidAccountBalanceDao();
			UserPrePaidAccountBalance balance = 
						prePaidDao.getUserPrePaidAccountBalance(user.getUserID());
			if (balance != null) {
				response.setBalance(balance.getAccountBalance());
			}
		} else {
			response.setAutherized(false);
		}
		return response;
	}
}
