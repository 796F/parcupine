package parkservice.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;
import parkservice.model.RefillRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestRefill {
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		AuthRequest in = new AuthRequest();
		in.setEmail("xia.umd@gmail.com");
		in.setPassword("a");
		
		RefillRequest pr = new RefillRequest();
		pr.setChargeAmount(100);
		pr.setPaymentType(0);
		pr.setSpotid(101);
		pr.setUid(30);
		pr.setUserinfo(in);
		pr.setParkingReferenceNumber("30:101:1324102182"); //information is given back by response object.  
		
		String outstring = service.path("park").path("refill").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.resources").build();
	}
}
