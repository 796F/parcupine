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

import com.parq.server.dao.GeolocationDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.Geolocation;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.FindSpotRequest;
import parkservice.model.FindSpotResponse;

@Path("/")
public class MapsResource {
	@Context 
	ContextResolver<JAXBContextResolver> providers;

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FindSpotResponse getNearbySpots(JAXBElement<FindSpotRequest> input ){
		FindSpotRequest in = input.getValue();
		FindSpotResponse output = new FindSpotResponse();
		AuthRequest userInfo = in.getUserInfo();
		if(in.getUid()==innerAuthenticate(userInfo)){
			double x = in.getLat();
			double y = in.getLon();
			GeolocationDao gld = new GeolocationDao();
			List<Geolocation> list = null;
			try{
				double span = 0.01; //1.11km, so appx 1x1 mile box.  
				list = gld.findCloseByParkingLocation(x-span, x+span, y-span, y+span);
			}catch(Exception e){}
			if(list!=null){
				for(Geolocation spot : list){
					output.add(spot.getLatitude(), spot.getLongitude(), spot.getLocationIdentifier());
				}
			}
		}

		return output;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "__ L. O. S. T. __ Season Premiere";
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

}
