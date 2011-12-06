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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.GeolocationDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.Geolocation;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.GpsRequest;
import parkservice.model.QrcodeRequest;
import parkservice.model.RateResponse;

@Path("/getrate")
public class GetRateResource {

	/**
	 * returns User_ID, or -1 if bad
	 * */
	private int innerAuthenticate(AuthRequest in){
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

			ParkingRateDao p = new ParkingRateDao();
			//getbyname should use p.getParkingRateByName(x86gg0, a808);
			ParkingRate pr =p.getParkingRateByName(input.getLot(), input.getSpot());
			GeolocationDao gdao = new GeolocationDao();
			Geolocation loc = gdao.getLocationById(pr.getLocationId());
			
			return new RateResponse(
					
					loc.getLatitude(), loc.getLongitude(),
					pr.getLocationName(), pr.getSpaceId(),
					pr.getMinParkMins(), pr.getMaxParkMins(), pr.getParkingRateCents(), pr.getTimeIncrementsMins(),null);

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
		RateResponse test = new RateResponse();
		int uid=-1;
		if((uid=innerAuthenticate(userInfo))>0){
			double x = input.getLat();
			double y = input.getLon();

			GeolocationDao gdao = new GeolocationDao();
			List<Geolocation> spots = gdao.findCloseByParkingLocation(x-0.0004, x+0.0004, y-0.0004, y+0.0004);
			
			for(Geolocation g: spots){
					ParkingRateDao p = new ParkingRateDao();

					//											this is main_lot	   input.getspot is 1412
					ParkingRate pr = p.getParkingRateByName(g.getLocationIdentifier(), input.getSpot());
					
					return new RateResponse(g.getLatitude(), g.getLongitude(),
							pr.getLocationName(), pr.getSpaceId(),
							pr.getMinParkMins(), pr.getMaxParkMins(), pr.getParkingRateCents(), pr.getTimeIncrementsMins(),null);

			}
			test.setLocation("NO_SPOTS");
			return test;
			
		}else{
			test.setLocation("BAD_AUTH");
			return test;
		}
	}

	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "Hello getrate path1";
	}
}
