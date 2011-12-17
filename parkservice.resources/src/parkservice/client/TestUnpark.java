package parkservice.client;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.RefillRequest;
import parkservice.model.UnparkRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestUnpark {
		public static void main(String[] args) {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource service = client.resource(getBaseURI());
			
			AuthRequest in = new AuthRequest();
			in.setEmail("xia.umd@gmail.com");
			in.setPassword("a");
			
			Date end = new Date();
			long endLong = end.getTime();
			
			UnparkRequest pr = new UnparkRequest();
			pr.setEndTime(endLong);
			pr.setParkingInstanceId(22);
			pr.setSpotid(101);
			pr.setUid(30);
			pr.setUserinfo(in);
			
			
			String outstring = service.path("park").path("unpark").type(MediaType.APPLICATION_JSON).post(String.class, pr);
			System.out.println(outstring);
			
		}
			

		private static URI getBaseURI() {
			return UriBuilder.fromUri(
					"http://localhost:8080/parkservice.resources").build();
		}
	

}
