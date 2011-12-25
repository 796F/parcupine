package parkservice.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import parkservice.model.AuthResponse;
import parkservice.model.ParkSync;


@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext>{

			private final JAXBContext context;
			private final Class[] cTypes = {AuthResponse.class,ParkSync.class};
			private final Set<Class> types;
			
			public JAXBContextResolver() throws JAXBException {
				this.context = new JSONJAXBContext (JSONConfiguration.natural().build(), cTypes);
				this.types= new HashSet(Arrays.asList(cTypes));
			}
	
			public JAXBContext getContext(Class<?> objectType) {
				return (types.contains(objectType)) ? context : null;
			}
	
}
