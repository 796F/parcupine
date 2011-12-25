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
import com.parq.server.dao.GeolocationDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.ParkingSpaceDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.Geolocation;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.ParkingSpace;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.GpsRequest;
import parkservice.model.QrcodeRequest;
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
		QrcodeRequest input = info.getValue();
		AuthRequest userInfo = input.getUserInfo();
		RateResponse test = new RateResponse();
		long uid=input.getUid();
		if(uid==innerAuthenticate(userInfo)){
			//http://www.parqme.com/x86gg0/a80

			ParkingRateDao p = new ParkingRateDao();
			//getbyname should use p.getParkingRateByName(x86gg0, a808);
			ParkingRate pr = null;
			try{
				pr =p.getParkingRateByName(input.getLot(), input.getSpot());
			}catch (Exception e){}

			GeolocationDao gdao = new GeolocationDao();
			ParkingSpaceDao psdao = new ParkingSpaceDao();
			ParkingSpace pspace = null;
			Geolocation loc = null; 
			try{
				loc = gdao.getLocationById(pr.getLocationId());
				pspace = psdao.getParkingSpaceBySpaceId(pr.getSpaceId());
			}catch (Exception e){}

			RateResponse output = new RateResponse();
			if(loc!=null && pspace!=null){
				output.setLat(loc.getLatitude());
				output.setLon(loc.getLongitude());

				output.setLocation(pspace.getSpaceName());
				output.setSpotid(pr.getSpaceId());
				output.setMinTime(pr.getMinParkMins());
				output.setMaxTime(pr.getMaxParkMins());
				output.setDefaultRate(pr.getParkingRateCents());
				output.setMinIncrement(pr.getTimeIncrementsMins());
				return output;
			}else{
				output.setLocation("ONE WAS NULL");
				return output;
			}
		}else{
			test.setLocation("BAD_AUTH");
			return test;
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
		long uid=input.getUid();
		if(uid==innerAuthenticate(userInfo)){
			double x = input.getLat();
			double y = input.getLon();

			GeolocationDao gdao = new GeolocationDao();
			List<Geolocation> spots = gdao.findCloseByParkingLocation(x-0.0004, x+0.0004, y-0.0004, y+0.0004);


			for(Geolocation g: spots){
				RateResponse output = new RateResponse();
				ParkingRateDao p = new ParkingRateDao();
				ParkingRate pr;
				//											this is main_lot	   input.getspot is 1412
				try{
					pr = p.getParkingRateByName(g.getLocationIdentifier(), input.getSpot());

					ParkingSpaceDao psdao = new ParkingSpaceDao();
					ParkingSpace pspace = null;
					try{
						pspace = psdao.getParkingSpaceBySpaceId(pr.getSpaceId());
					}catch (Exception e){}
					if(pspace!=null){
						output.setLat(g.getLatitude());
						output.setLon(g.getLongitude());
						//pr.getLocation() return main_lot
						output.setLocation(pspace.getSpaceName());
						output.setSpotid(pr.getSpaceId());
						output.setMinTime(pr.getMinParkMins());
						output.setMaxTime(pr.getMaxParkMins());
						output.setDefaultRate(pr.getParkingRateCents());
						output.setMinIncrement(pr.getTimeIncrementsMins());
					}else{
						output.setLocation("SPACE_NULL");
					}
				}catch (Exception e){
					output.setLocation("EXCEPTION GETTING PARKING RATE");
				}
				return output;

			}
			test.setLocation("NO_SPOTS");
			return test;

		}else{
			test.setLocation("BAD_AUTH");
			return test;
		}
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
