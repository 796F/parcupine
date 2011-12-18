package parkservice.client;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.ParkRequest;

import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.model.object.PaymentAccount;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestPark {

	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		AuthRequest in = new AuthRequest();
		in.setEmail("xia.umd@gmail.com");
		in.setPassword("a");
		
		ParkRequest pr = new ParkRequest();
		pr.setIterations(2);
		pr.setPaymentType(0);
		pr.setSpotid(101);
		pr.setUid(30);
		pr.setUserinfo(in);
		
		
		String outstring = service.path("park").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.resources").build();
	}

}
