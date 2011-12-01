package parkservice.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.RegisterRequest;
import parkservice.model.RegisterResponse;

import com.parq.server.dao.UserDao;
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
		in.setEmail("mike@umd.edu");
		in.setPassword("a");
		in.setCccNumber("000");
		in.setCreditCard("SAMP-LECR-EDIT-CARD");
		in.setExpYear(15);
		in.setExpMonth(4);
		in.setHolderName("USERNAME");
		in.setBillingAddress("ExBillingAddress");

		String outstring = service.path("register").type(MediaType.APPLICATION_JSON).post(String.class, in);
		
	
		System.out.println(outstring);
		
		
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.resources").build();
	}

}
