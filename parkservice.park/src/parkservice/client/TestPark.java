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
		//{"resp":"OK","rateObject":{"defaultRate":337,"lat":0.0,"location":"space name 1","lon":0.0,"maxTime":0,"minIncrement":5,"minTime":0,"spotid":61}}
		AuthRequest in = new AuthRequest();
		in.setEmail("miguel@parqme.com");
		in.setPassword("a");
		
		ParkRequest pr = new ParkRequest();
		pr.setPaymentType(0);
		pr.setSpotId(61);
		pr.setUid(41);
		pr.setUserInfo(in);
		pr.setDurationMinutes(5);
		pr.setChargeAmount(337);
		
		String outstring = service.path("park").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://75.101.132.219:8080/parkservice.park").build();
	}

}
