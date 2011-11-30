package parkservice.resources;


/*App designer needed
web interface -> BB
App function
	- clock
	- sqlite history
	- in app registration/payment
	- Tutorial
	- JSON client

Merchant plus integration.  

different tables per client.  
	number of clients not too high, avoid data duplication of client_id


Client table, containing [user_id, spot_id, rate_obj]
	client information tells backend what table to use,
	which then we know user x for client c, has a specific rate object for a spot.  

INSTEAD OF BUILDING_ID, SHOULD BE PARKINGLOT_ID


user_id
spot_id


parkinglot_id		lat/lon
client_id

lat/lon is analogous to parkinglot_id and client_id because it serves the same purpose
to pinpoint the user to a parking_lot, which then gives us the client inevitably.  



 * 
 * */

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.model.object.Client;
import com.parq.server.dao.model.object.ParkingRate;

import parkservice.model.AuthRequest;
import parkservice.model.GpsRequest;
import parkservice.model.QrcodeRequest;
import parkservice.model.RateResponse;
import parkservice.model.SpecialRate;

@Path("/getrate")
public class GetRateResource {

	/**
	 * returns User_ID, or -1 if bad
	 * */
	private int innerAuthenticate(AuthRequest input){
		//methods call this to request permission to park/unparq/refill etc.
		return 1;
	}

	private RateResponse getRate(int uid, int spot){

		RateResponse output = new RateResponse();
		output.setLocation("test");
		return output;
	}

	@POST
	@Path("/qrcode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RateResponse unwrapQrcode(JAXBElement<QrcodeRequest> info){
		QrcodeRequest input = info.getValue();
		AuthRequest userInfo = input.getUserInfo();
		int uid;
		if((uid=innerAuthenticate(userInfo))>0){
			ClientDao x = new ClientDao();
			//http://www.parqme.com/x86gg0/a80
			input.getLot();
			input.getSpot();
			
			ParkingRateDao p = new ParkingRateDao();
			//getbyname should use p.getParkingRateByName(x86gg0, a808);
			ParkingRate pr =p.getParkingRateByName(input.getLot(), input.getSpot());
			//parkingrate object should be changed...
			return new RateResponse(
					0.86F, -0.51F /*lat/lon info needs to be returned*/,
					pr.getLocationName(), pr.getSpaceId(),
					60 /*min park time*/, 
					pr.getMaxParkMins(), pr.getParkingRateCents(), pr.getTimeIncrementsMins(),null);
			
			//if authenticated
			//return getRate(uid, 0);
		}else{

			return null;
		}		
	}

	@POST
	@Path("/gps")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RateResponse unwrapGps(JAXBElement<GpsRequest> gpsrequest){
		GpsRequest input = gpsrequest.getValue();	
		AuthRequest userInfo = input.getUserInfo();

		input.getSpot();

		int uid;
		if((uid=innerAuthenticate(userInfo))>0){
			return getRate(uid, 0);
		}
		return null;
	}

	@GET
	@Path("/hello1")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "Hello getrate path1";
	}
	@GET
	@Path("/hello2")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello2() {
		return "Hello getrate path TWO";
	}
}
