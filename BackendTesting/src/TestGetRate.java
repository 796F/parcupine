

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.GpsRequest;
import parkservice.model.QrcodeRequest;
import parkservice.model.RateResponse;

import com.parq.server.dao.ClientDao;
import com.parq.server.dao.ParkingRateDao;
import com.parq.server.dao.model.object.ParkingRate;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestGetRate {
	public static void main(String[] args) {
	
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		//login info
		AuthRequest in = new AuthRequest();
		in.setEmail("miguel@parqme.com");
		in.setPassword("a");
		//AU OR cid=11, main_lot, 1412
		QrcodeRequest g = new QrcodeRequest();
		g.setUserInfo(in);
		g.setUid(1); //not used by getrate yet.  
		g.setLot("main_lot");
		g.setSpot("1412");
		String output = service.path("qrcode").type(MediaType.APPLICATION_JSON).post(String.class, g);
		System.out.println(output);
		
		
		//lat/lon try 0, 0
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.rate").build();
	}
}
