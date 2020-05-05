package MyREST.MyRESTRoom.security;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("webapi")
public class MyAppReg extends ResourceConfig {
	public MyAppReg() {
		register(RolesAllowedDynamicFeature.class);
	}
}
