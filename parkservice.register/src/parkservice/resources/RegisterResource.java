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
	public RegisterResponse register(JAXBElement<RegisterRequest> input){
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
		RegisterResponse test = new RegisterResponse();
		if(result){
			test.setResponsecode("OK");
			return test;
		}else{
			test.setResponsecode("BAD");
			return test;
		}
	}
	
	@Path("/delete")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisterResponse delete(JAXBElement<RegisterRequest> input){
		RegisterRequest info = input.getValue();
		UserDao userDb = new UserDao();
		boolean result = userDb.deleteUserById(userDb.getUserByEmail(info.getEmail()).getUserID());
		RegisterResponse test = new RegisterResponse();
		
		if(result){
			test.setResponsecode("DELETE OK");
			return test;
		}else{
			test.setResponsecode("DELETE BAD");
			return test;
		}
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Register Service!!!";
	}
}
