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
		in.setEmail("miguel@parqme.com");
		in.setPassword("a");
		//{"endTime":1324848301801,"parkingReferenceNumber":"31:61:1324847703","resp":"OK"}

		RefillRequest pr = new RefillRequest();
		pr.setParkingReferenceNumber("12:71:1325022511");
		pr.setChargeAmount(337);
		pr.setDurationMinutes(5);
		pr.setPaymentType(0);
		pr.setSpotId(71);
		pr.setUid(12);
		pr.setUserInfo(in);
		
		String outstring = service.path("refill").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.park").build();
	}
}
