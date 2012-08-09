package parkservice.resources;



import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.ParkingSpaceDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.GpsRequest;
import parkservice.model.QrcodeRequest;
import parkservice.model.RateObject;
import parkservice.model.RateResponse;

@Path("/")
public class GetRateResource {
	@Context 
	ContextResolver<JAXBContextResolver> providers;


	@POST
	@Path("/qrcode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RateResponse unwrapQrcode(JAXBElement<QrcodeRequest> info){
//		QrcodeRequest input = info.getValue();
//		AuthRequest userInfo = input.getUserInfo();
//		RateResponse test = new RateResponse();
//		long uid=input.getUid();
//		if(uid==innerAuthenticate(userInfo)){
//			//http://www.parqme.com/x86gg0/a80
//
			RateResponse output = new RateResponse();
			
			//temp code, delete block {
			output.setResp("DEPRECATED");
			return output;
			//}
			
//			ParkingRateDao p = new ParkingRateDao();
//			//getbyname should use p.getParkingRateByName(x86gg0, a808);
//			ParkingRate pr = null;
//			try{
//				pr =p.getParkingRateByName(input.getLot(), input.getSpot());
//			}catch (Exception e){}
//			if(pr==null){
//				output.setResp("NO_SPOT");
//				return output;
//			}
//			GeolocationDao gdao = new GeolocationDao();
//			ParkingSpaceDao psdao = new ParkingSpaceDao();
//			ParkingSpace pspace = null;
//			Geolocation loc = null; 
//			try{
//				loc = gdao.getLocationById(pr.getLocationId());
//				pspace =psdao.getParkingSpaceBySpaceIdentifier(input.getLot(), input.getSpot());
//			}catch (Exception e){}
//
//			if(loc!=null && pspace!=null){
//				RateObject rate = new RateObject();
//				rate.setLat(loc.getLatitude());
//				rate.setLon(loc.getLongitude());
//				rate.setLocation(pspace.getSpaceName());
//				rate.setSpotId(pr.getSpaceId());
//				rate.setMinTime(pr.getMinParkMins());
//				rate.setMaxTime(pr.getMaxParkMins());
//				rate.setDefaultRate(pr.getParkingRateCents());
//				rate.setMinIncrement(pr.getTimeIncrementsMins());
//				output.setResp("OK");
//				output.setRateObject(rate);
//				return output;
//			}else{
//				String x = "";
//				if(loc==null) x+=pr.getLocationId();
//				if(pspace==null) x+="spacid:" +pr.getSpaceId() + " lot:" + input.getLot() + " spot:" + input.getSpot();
//				x+= pr.getLocationName() + pr.getLocationId() + pr.getMaxParkMins() + pr.getSpaceName() + pr.getTimeIncrementsMins();
//
//				output.setResp(x);
//				return output;
//			}
//		}else{
//			test.setResp("BAD_AUTH");
//			return test;
//		}		
	}

	@POST
	@Path("/gps")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RateResponse unwrapGps(JAXBElement<GpsRequest> gpsrequest){
		GpsRequest input = gpsrequest.getValue();	
		RateResponse output = new RateResponse();

		AuthRequest userInfo = input.getUserInfo();
		long uid=input.getUid();
		if(uid==innerAuthenticate(userInfo)){
			ParkingRateDao prd = new ParkingRateDao();
			ParkingRate pr = null;
			try{
				pr = prd.getParkingRateBySpaceId(input.getSpotId());
			}catch(Exception e){
				output.setResp("PR_NULL");
				return output;
			}
			RateObject rate = new RateObject();
			output.setResp("OK");
			rate.setLocation(input.getSpot());
			rate.setSpotId(input.getSpotId());
			rate.setMinTime(pr.getMinParkMins());
			rate.setMaxTime(pr.getMaxParkMins());
			rate.setDefaultRate(pr.getParkingRateCents());
			rate.setMinIncrement(pr.getTimeIncrementsMins());
			output.setRateObject(rate);
		}else{
			output.setResp("BAD_AUTH");
			
		}
		return output;
	}
	/**
	 * returns User_ID, or -1 if bad
	 * */
	private long innerAuthenticate(AuthRequest in){
		UserDao userDb = new UserDao();
		User user = null;
		try{
			user = userDb.getUserByEmail(in.getEmail());
		}catch(RuntimeException e){
		}
		if(user!=null&&user.getPassword().equals(in.getPassword())){
			return user.getUserID();
		}else{
			return -1;
		}
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "got RATE?";
	}
}
