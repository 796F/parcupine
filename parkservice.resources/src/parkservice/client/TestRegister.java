package parkservice.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.RegisterRequest;
import parkservice.model.RegisterResponse;

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
		
		RegisterRequest in = new RegisterRequest();
		in.setEmail("tralala@bwahaha.com");
		in.setPassword("singlaugh");
		in.setCscNumber("000");
		in.setCreditCard("5466160160030126");
		in.setExpYear(2015);
		in.setExpMonth(4);
		in.setHolderName("Joker");
		in.setZipcode("10101");

		String outstring = service.path("register").type(MediaType.APPLICATION_JSON).post(String.class, in);
		
	
		System.out.println(outstring);

	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.resources").build();
	}

}
