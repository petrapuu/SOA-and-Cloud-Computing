package MyREST.MyRESTRoom;

import java.net.URI;
import java.util.List;

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

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;
import MyREST.MyRESTRoom.exceptions.ErrorMessage;
import MyREST.MyRESTRoom.response.ResponseMaker;
import MyREST.MyRESTRoom.security.MyCustomSecurityContext;

@Path("/customers")
//@Singleton
public class Customers {

	//@Context
	//private MyCustomSecurityContext security;
	
	private CustomerService customerService = new CustomerService();
	private ResponseMaker responseMaker = new ResponseMaker();
	
	@RolesAllowed("ADMIN")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCustomers(@Context UriInfo uriInfo){
		List<Customer> allCustomers = customerService.getAllCustomers();
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		if (allCustomers.isEmpty() || allCustomers == null) {

    		return responseMaker.getNoContentResponse(uri, allCustomers);
		}
		return responseMaker.getAcceptedResponse(uri, allCustomers);
	}
	
	@GET
	@Path("/{customerId : \\d+}")
	@RolesAllowed({"ADMIN", "USER"})
    @Produces(MediaType.APPLICATION_JSON)
	public Response getCustomerById(@PathParam("customerId") int id, @Context UriInfo uriInfo, @Context ContainerRequestContext crc) {
		MyCustomSecurityContext security = (MyCustomSecurityContext) crc.getSecurityContext();
		if(security.isUserInRole("ADMIN")) {
			Customer returnedCustomer = customerService.getCustomer(id);
			if (returnedCustomer == null) {
				throw new DataNotFoundException("No customer found with id " + id);
			}
			URI uri = uriInfo.getAbsolutePathBuilder().build();
			return responseMaker.getAcceptedResponse(uri, returnedCustomer);
		}
		
		if (security.isUserInRole("USER")) {
			int securityCustomerId = security.getCustomerInt();
			if (securityCustomerId == id) {
				Customer returnedCustomer = customerService.getCustomer(id);
				URI uri = uriInfo.getAbsolutePathBuilder().build();
				return responseMaker.getAcceptedResponse(uri, returnedCustomer);
			}
			else {
				ErrorMessage msg = new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com");
				return responseMaker.getUnAuthorizedResponse(msg);
			}
		}
		//shouldn't ever come here
		return responseMaker.getUnAuthorizedResponse(new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com"));
	}
	
    @GET
    @Path("/{customerName : [a-zA-Z][a-zA-Z_0-9]*}")
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerByName(@PathParam("customerName") String name, @Context UriInfo uriInfo) {
    	List<Customer> customers = customerService.getCustomerWithName(name);
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		if (customers.isEmpty() || customers == null) {

    		return responseMaker.getNoContentResponse(uri, customers);
		}
		return responseMaker.getAcceptedResponse(uri, customers);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCustomer(Customer customer, @Context UriInfo uriInfo) {
    	Customer newCustomer = customerService.addCustomer(customer);
    	String newId = String.valueOf(newCustomer.getId());
    	URI uri = uriInfo.getAbsolutePathBuilder()
    			.path(newId)
    			.build();
    	newCustomer.addLink(uri.toString(), "self");
    	URI uri2 = uriInfo.getBaseUriBuilder()
    			.path(Customers.class)
    			.path(Customers.class, "getCustomerHistories")
    			.resolveTemplate("customerId", newCustomer.getId())
    			.build();
    	newCustomer.addLink(uri2.toString(), "history");
    	URI uri3 = uriInfo.getBaseUriBuilder()
    			.path(Customers.class)
    			.build();
    	newCustomer.addLink(uri3.toString(), "customers");
		return  responseMaker.getAcceptedResponse(uri, newCustomer);
    }
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBasicCustomer(Customer customer, @Context UriInfo uriInfo) {
    	Customer newCustomer = customerService.addBasicCustomer(customer);
    	String newId = String.valueOf(newCustomer.getId());
    	URI uri = uriInfo.getAbsolutePathBuilder()
    			.path(newId)
    			.build();
    	newCustomer.addLink(uri.toString(), "self");
    	URI uri2 = uriInfo.getBaseUriBuilder()
    			.path(Customers.class)
    			.path(Customers.class, "getCustomerHistories")
    			.resolveTemplate("customerId", newCustomer.getId())
    			.build();
    	newCustomer.addLink(uri2.toString(), "history");
    	URI uri3 = uriInfo.getBaseUriBuilder()
    			.path(Customers.class)
    			.build();
    	newCustomer.addLink(uri3.toString(), "customers");
		return  responseMaker.getAcceptedResponse(uri, newCustomer);
    }    
    @PUT
    @Path("/{customerId : \\d+}")
    @RolesAllowed({"ADMIN", "USER"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("customerId") int id, Customer customer, @Context UriInfo uriInfo, @Context ContainerRequestContext crc) {
    	MyCustomSecurityContext security = (MyCustomSecurityContext) crc.getSecurityContext();
    	if(security.isUserInRole("ADMIN")) {
        	customer.setId(id);
        	Customer updatedCustomer = customerService.updateCustomer(customer);
        	URI uri = uriInfo.getAbsolutePathBuilder().build();
        	return responseMaker.getAcceptedResponse(uri, updatedCustomer);
    	}
    	if (security.isUserInRole("USER")) {
    		if (security.getCustomerInt() == id) {
    			customer.setId(id);
    			Customer updatedCustomer = customerService.updateBasicCustomer(customer);
            	URI uri = uriInfo.getAbsolutePathBuilder().build();
            	return responseMaker.getAcceptedResponse(uri, updatedCustomer);
    		}
    		else {
				ErrorMessage msg = new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com");
				return responseMaker.getUnAuthorizedResponse(msg);
			}	
    	}
    	//shouldn't ever come here
		return responseMaker.getUnAuthorizedResponse(new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com"));
    }
    
    @DELETE
    
    @Path("/{customerId : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN", "USER"})
    public Response deleteCustomer(@PathParam("customerId") int id, @Context UriInfo uriInfo, @Context ContainerRequestContext crc) {
    	MyCustomSecurityContext security = (MyCustomSecurityContext) crc.getSecurityContext();
    	if(security.isUserInRole("ADMIN")) {
	    	Customer c = customerService.deleteCustomer(id);
	    	URI uri = uriInfo.getAbsolutePathBuilder().build();
	    	if(c == null) {
	    		throw new DataNotFoundException("No customer found with id " + id);
	    	}
    	return responseMaker.getAcceptedResponse(uri, c);
    	}
    	if (security.isUserInRole("USER")) {
    		if (security.getCustomerInt() == id) {
    	    	Customer c = customerService.deleteCustomer(id);
    	    	URI uri = uriInfo.getAbsolutePathBuilder().build();
    	    	return responseMaker.getAcceptedResponse(uri, c);
    		}
    		else {
				ErrorMessage msg = new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com");
				return responseMaker.getUnAuthorizedResponse(msg);
			}	
    	}
    	//shouldn't ever come here
		return responseMaker.getUnAuthorizedResponse(new ErrorMessage("User can't access target resource", 401, "http//:sucks.to.be.you.com"));
    }
   
    
    @Path("/{customerId : \\d+}/history")
    @RolesAllowed({"ADMIN", "USER"})
    public CustomerHistories getCustomerHistories() {
    		return new CustomerHistories();
    }
    	
}
