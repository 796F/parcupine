package parkservice.resources;



import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.GeolocationDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.Geolocation;
import com.parq.server.dao.model.object.ParkingInstance;
import com.parq.server.dao.model.object.ParkingRate;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.AuthResponse;
import parkservice.model.parkSync;

@Path("/")
public class AuthResource{
	@Context 
	ContextResolver<JAXBContextResolver> providers;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public AuthResponse login(JAXBElement<AuthRequest> input){
		
		
		AuthRequest info = input.getValue();
		AuthResponse x = new AuthResponse();
		UserDao userDb = new UserDao();
		User user = null;
		try{
			user = userDb.getUserByEmail(info.getEmail());
		}catch(RuntimeException e){
		}
		if(user==null){
			x.setUid(-1);
			return x;
		}else if(user.getPassword().equals(info.getPassword())){
			long uid = user.getUserID();
			x.setUid(uid);
			ParkingStatusDao psd = new ParkingStatusDao();
			//if the password for the email matches, return user info.  
			Date endTime = null;
			ParkingInstance pi = null;
			try{
				pi = psd.getUserParkingStatus(uid);
				endTime = psd.getUserParkingStatus(uid).getParkingEndTime();
			}catch(Exception e){
			}
			if(endTime==null){
				//no endtime stored, user wasn't parked.  
				x.setParkstate(0);
				return x;
			}else if(endTime.compareTo(new Date())<0){
				//if end time is before now
				x.setParkstate(0);
				return x;
			}else{
				//if end time is after now, gather needed information and then return. 
				
				ParkingRateDao prd = new ParkingRateDao();
				try{
					ParkingRate pr = prd.getParkingRateBySpaceId(pi.getSpaceId());
					GeolocationDao gld = new GeolocationDao();
					Geolocation location = gld.getLocationById(pr.getLocationId());
					parkSync sync = new parkSync();
					sync.setLat(location.getLatitude());
					sync.setLon(location.getLongitude());
					sync.setEndTime(endTime.getTime());
					sync.setDefaultRate(pr.getParkingRateCents());
					sync.setParkingReferenceNumber(pi.getParkingRefNumber());
					sync.setMaxTime(pr.getMaxParkMins());
					sync.setMinIncrement(pr.getTimeIncrementsMins());
					sync.setMinTime(pr.getMinParkMins());
					sync.setSpotId(pi.getSpaceId());
					x.setSync(sync);
					x.setParkstate(1);
				}catch(Exception e){
					
				}
				
			}
			return x;
		}else{
			x.setUid(-1);
			return x;
		}

	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hi.  I am a bouncer.";
	}
}
