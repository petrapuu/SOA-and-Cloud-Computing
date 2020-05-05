package MyREST.MyRESTRoom;

import java.net.URI;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import MyREST.MyRESTRoom.exceptions.ErrorMessage;
import MyREST.MyRESTRoom.response.ResponseMaker;
import MyREST.MyRESTRoom.security.MyCustomSecurityContext;

@Path("/")
public class CustomerHistories {

	private CustomerHistoryService service = new CustomerHistoryService();
	private ResponseMaker responseMaker = new ResponseMaker();
	
	@GET
	@RolesAllowed({"ADMIN", "USER"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerHistory(@PathParam("customerId") int customerId, @Context UriInfo uriInfo, @Context ContainerRequestContext crc){
		MyCustomSecurityContext security = (MyCustomSecurityContext) crc.getSecurityContext();
		if(security.isUserInRole("ADMIN")) {
			CustomerHistory history = service.getCustomerHistory(customerId);
	    	URI uri = uriInfo.getAbsolutePathBuilder().build();
	    	if(history != null) {
		    	return responseMaker.getAcceptedResponse(uri, history);
			}
			ErrorMessage nodata = new ErrorMessage("Customer with id " + customerId + " doesn't have room history.", 204, "http//:sucks.to.be.you.com");
			return responseMaker.getNoContentResponse(uri, nodata); 
		}
		if (security.isUserInRole("USER")) {
			if (security.getCustomerInt() == customerId) {
				CustomerHistory history = service.getCustomerHistory(customerId);
				URI uri = uriInfo.getAbsolutePathBuilder().build();
				if(history != null) {
			    	return responseMaker.getAcceptedResponse(uri, history);
				}
				ErrorMessage nodata = new ErrorMessage("Customer with id " + customerId + " doesn't have room history.", 204, "http//:sucks.to.be.you.com");
				return responseMaker.getNoContentResponse(uri, nodata); 
			}
    		else {
				ErrorMessage msg = new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com");
				return responseMaker.getUnAuthorizedResponse(msg);
			}	
		}
		// Shouldn't ever come here!
		return responseMaker.getUnAuthorizedResponse(new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com"));
	}
	
	@POST
	@RolesAllowed("ADMIN")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response addCustomerHistory(@PathParam("customerId") int customerId, CustomerHistory newHistory, @Context UriInfo uriInfo){
		CustomerHistory history = service.addCustomerHistory(customerId, newHistory);
    	URI uri = uriInfo.getAbsolutePathBuilder()
    			.build();
    	history.addLink(uri.toString(), "self");
    	history.createRoomLinks(uriInfo);
    	return responseMaker.getAcceptedResponse(uri, history);
	}	
	
	@PUT
	@RolesAllowed("ADMIN")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomerHistory(@PathParam("customerId") int customerId, CustomerHistory newHistory, @Context UriInfo uriInfo) {
		CustomerHistory history = service.addCustomerHistory(customerId, newHistory);
    	URI uri = uriInfo.getAbsolutePathBuilder()
    			.build();
    	history.createRoomLinks(uriInfo);
    	return responseMaker.getAcceptedResponse(uri, history);
	}
	
	@DELETE
	@RolesAllowed("ADMIN")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCustomerHistory(@PathParam("customerId") int customerId, CustomerHistory customerHistory, @Context UriInfo uriInfo){
		CustomerHistory history = service.deleteCustomerHistory(customerId, customerHistory);
    	URI uri = uriInfo.getAbsolutePathBuilder().build();
		return responseMaker.getAcceptedResponse(uri, history);
	}
}
