package parkservice.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

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
	
	//Refill, Park, Unpark
	@POST
	@Path("/park")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ParkResponse parkUser(JAXBElement<ParkRequest> info){
		ParkResponse output = new ParkResponse();
		
		
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
