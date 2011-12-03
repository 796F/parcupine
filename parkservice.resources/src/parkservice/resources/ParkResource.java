package parkservice.resources;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import com.parq.server.dao.UserDao;
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
	//Refill, Park, Unpark
	@POST
	@Path("/park")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ParkResponse parkUser(JAXBElement<ParkRequest> info){
		ParkResponse output = new ParkResponse();
		ParkRequest in = info.getValue();
		int pay_amount = in.getAmount();
		Date start = in.getStart();
		Date end = in.getEnd();
		int payment_type = in.getPaymentType();
		int spot_id = in.getSpotid();
		int uid = in.getUid();
		
		AuthRequest userInfo = in.getUserinfo();
		if(uid == innerAuthenticate(userInfo)){
			//if auth goes through
			
			//get user payment info
			
			//try to charge their payment profile the pay_amount
			
			//if charge completes, store parking instance into db
			
			//then set output to ok
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
		
		
		return output;
	}
	@POST
	@Path("/unpark")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UnparkResponse unparkUser(JAXBElement<UnparkRequest> info){
		UnparkResponse  output = new UnparkResponse ();
		
		
		return output;
	}
	
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello1() {
		return "Hello I'm the Parking Service :X";
	}
}
