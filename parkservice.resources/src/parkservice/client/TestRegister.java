package parkservice.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.RegisterRequest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;



public class TestRegister {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		RegisterRequest in = new RegisterRequest();
		in.setEmail("xia.umd@gmail.com");
		in.setPassword("a");
		in.setCccNumber(000);
		in.setCreditCard("SAMP-LECR-EDIT-CARD");
		in.setExpDate("ExExpirationDate");
		in.setHolderName("ExCardHolderName");
		in.setBillingAddress("ExBillingAddress");

		String outstring = service.path("register").type(MediaType.APPLICATION_JSON).post(String.class, in);
		
		System.out.println(outstring);
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.register").build();
	}

}
