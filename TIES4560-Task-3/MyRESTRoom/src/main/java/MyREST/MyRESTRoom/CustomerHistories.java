package MyREST.MyRESTRoom;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import MyREST.MyRESTRoom.response.ResponseMaker;

import javax.ws.rs.core.Response.Status;

@Path("/")
public class CustomerHistories {

	private CustomerHistoryService service = new CustomerHistoryService();
	private ResponseMaker responseMaker = new ResponseMaker();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerHistory(@PathParam("customerId") int customerId, @Context UriInfo uriInfo){
		CustomerHistory history = service.getCustomerHistory(customerId);
    	URI uri = uriInfo.getAbsolutePathBuilder().build();
    	return responseMaker.getAcceptedResponse(uri, history); 
	}
	
	@POST
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteCustomerHistory(@PathParam("customerId") int customerId, CustomerHistory customerHistory, @Context UriInfo uriInfo){
		CustomerHistory history = service.deleteCustomerHistory(customerId, customerHistory);
    	URI uri = uriInfo.getAbsolutePathBuilder().build();
		return responseMaker.getAcceptedResponse(uri, history);
	}
}
