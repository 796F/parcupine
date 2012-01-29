

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.RegisterRequest;

import com.parq.server.dao.PaymentAccountDao;
import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.PaymentAccount;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



public class TestRegister {

	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		RegisterRequest rq = new RegisterRequest();
		rq.setCreditCard("5466160160030126");
		rq.setCscNumber("000");
		rq.setEmail("miguel@parqme.com");
		rq.setExpMonth(4);
		rq.setExpYear(2015);
		rq.setAddress("2406 Oakmere Road");
		rq.setHolderName("Michael Xia");
		rq.setPassword("a");
		rq.setZipcode("19810");

		String outstring = service.path("register").type(MediaType.APPLICATION_JSON).post(String.class, rq);
		System.out.println(outstring);
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.user").build();
	}

}
