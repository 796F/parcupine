

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
//{"parkingReferenceNumber":"23:151:1325113034","spotId":151,"durationMinutes":5,"chargeAmount":337,"paymentType":0,"uid":23,"userInfo""email":"xia.umd@gmail.com","password":"aaaaaa"}}
		AuthRequest in = new AuthRequest();
		in.setEmail("miguel@parqme.com");
		in.setPassword("a");
		//{"endTime":1327861318462,"parkingReferenceNumber":"1:3:1327860719","resp":"OK"}

		RefillRequest pr = new RefillRequest();
		pr.setParkingReferenceNumber("1:3:1327860719");
		pr.setChargeAmount(337);
		pr.setDurationMinutes(10);
		pr.setPaymentType(0);
		pr.setSpotId(3);
		pr.setUid(1);
		pr.setUserInfo(in);
		
		String outstring = service.path("refill").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://75.101.132.219/parkservice.park").build();
	}
}
