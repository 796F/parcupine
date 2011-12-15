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
		int j = 5;
		int k =9;
		double a=4;
		double b=6;
		double d;
		d = (k/j)*b/a+j++;
		
		int outstring = (9/5)*6/4+j++;
		System.out.println(d);
		
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.resources").build();
	}

}
