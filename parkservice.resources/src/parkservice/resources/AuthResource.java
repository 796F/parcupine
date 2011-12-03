package parkservice.resources;


/*
 * 1.  web xml to map service class package and url pattern
 * 2.  java pojo's for request and response
 * 3.  Resource class that has annotations and methods to call.  
 * 4.  Auth database is different from others, restricts access and thus more secure.  
 * */

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.ParkingStatusDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.User;

import parkservice.model.AuthRequest;
import parkservice.model.AuthResponse;

@Path("/auth")
public class AuthResource {


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
			x.setUid(-1);
			x.setParkstate(-1);
			return x;
		}
		if(user==null){
			x.setUid(-1);
			x.setParkstate(-1);
			return x;
		}else if(user.getPassword().equals(info.getPassword())){
			ParkingStatusDao gg = new ParkingStatusDao();

			//if the password for the email matches, return user info.  

			Date endTime = null;
			try{
				endTime = gg.getUserParkingStatus(user.getUserID()).getParkingEndTime();
			}catch(Exception e){
				x.setParkstate(0);
			}
			if(endTime==null){
				//no endtime stored
				x.setParkstate(0);
			}else if(endTime.compareTo(new Date())<0){
				//if end time is before now
				x.setParkstate(0);
			}else{
				//if end time is after now
				x.setParkstate(1);
			}
			x.setUid(user.getUserID());
			return x;
		}else{
			x.setParkstate(-1);
			x.setUid(-1);
			return x;
		}

	}


	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello peter parker";
	}
}
