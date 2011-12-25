package parkservice.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import parkservice.model.AuthRequest;
import parkservice.model.AuthResponse;

import com.parq.server.dao.UserDao;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TestAuth {
	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		AuthRequest in = new AuthRequest();
		in.setEmail("miguel@parqme.com");
		in.setPassword("gg");
		
		String outstring = service.path("").type(MediaType.APPLICATION_JSON).post(String.class, in);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://75.101.132.219:8080/parkservice.auth").build();
	}

}