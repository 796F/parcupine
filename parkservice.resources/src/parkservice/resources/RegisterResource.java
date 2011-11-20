package parkservice.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import parkservice.model.RegisterRequest;
import parkservice.model.RegisterResponse;

import com.parq.server.dao.UserDao;
import com.parq.server.dao.model.object.User;

@Path("/register")
public class RegisterResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisterResponse login(JAXBElement<RegisterRequest> input){
		RegisterRequest info = input.getValue();
		User newUser = new User();
		newUser.setEmail(info.getEmail());
		newUser.setPassword(info.getPassword());
		newUser.setUserName(info.getHolderName());
		UserDao userDb = new UserDao();
		//getByEmail throws exception when user is not in db, aka rs=null.
		//thus user must be in db or this will throw exception.  
		
		/* user creation more verbose? */
		boolean result = userDb.createNewUser(newUser);
		/*  Dao layer needs to support these fields in table "User"
 			fname TEXT(64) NOT NULL, 
 			lname TEXT(64) NOT NULL,
 			parkstate TINYINT(1) NOT NULL,
 			parkloc POINT NOT NULL,
		 */
		if(result){
			return new RegisterResponse(11);
		}else{
			return new RegisterResponse(33);
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Register Service!!!";
	}
}
