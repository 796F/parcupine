

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import parkservice.model.AuthRequest;
import parkservice.model.EditCCRequest;
import parkservice.model.EditUserRequest;
import parkservice.model.RefillRequest;

import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class TestEditEmail {

	public static void main(String[] args) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
//{"parkingReferenceNumber":"23:151:1325113034","spotId":151,"durationMinutes":5,"chargeAmount":337,"paymentType":0,"uid":23,"userInfo""email":"xia.umd@gmail.com","password":"aaaaaa"}}
		AuthRequest in = new AuthRequest();
		in.setEmail("cctest@gmail.com");
		in.setPassword("a");
		//{"endTime":1324848301801,"parkingReferenceNumber":"31:61:1324847703","resp":"OK"}

		EditUserRequest pr = new EditUserRequest();
		pr.setEmail("changeemail@gmail.com");
		pr.setPassword("");
		pr.setPhone("");
		pr.setUid(35);
		pr.setUserInfo(in);
		
//		pr.setAddress("2406 Oakmere Road");
//		pr.setCreditCard("5466160160030126");
//		pr.setCscNumber("000");
//		pr.setExpMonth(4);
//		pr.setExpYear(2015);
//		pr.setHolderName("Michael Xia");
//		pr.setUid(34);
//		pr.setUserInfo(in);
//		pr.setZipcode("19810");
		
		String outstring = service.path("update").type(MediaType.APPLICATION_JSON).post(String.class, pr);
		System.out.println(outstring);
		
	}
		

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/parkservice.user").build();
	}
}
