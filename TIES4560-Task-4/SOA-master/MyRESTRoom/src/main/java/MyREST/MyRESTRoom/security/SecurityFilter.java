package MyREST.MyRESTRoom.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import MyREST.MyRESTRoom.Customer;
import MyREST.MyRESTRoom.CustomerService;
import MyREST.MyRESTRoom.exceptions.ErrorMessage;
import MyREST.MyRESTRoom.response.ResponseMaker;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	
	@Context private ResourceInfo resInfo;
	
	private ResponseMaker responsemaker = new ResponseMaker();
	
	private final ErrorMessage FORBIDDEN_ERRORMSG = new ErrorMessage("Access blocked from all users!", 403, "http://sucks.to.be.you.com");
	private final ErrorMessage UNAUTHORIZED_ERRORMSG = new ErrorMessage("User can't access target resource!", 401, "http://sucks.to.be.you.com");
	private final ErrorMessage LOGIN_ERRORMSG = new ErrorMessage("User must login to get access!", 401, "http://sucks.to.be.you.com");
	private final ErrorMessage LOGINFAILED_ERRORMSG = new ErrorMessage("Login failed! Check your login name and password.", 401, "http://sucks.to.be.you.com");
	private final ErrorMessage AUTHENTICATION_ERRORMSG = new ErrorMessage("Authentication failed. "
			+ "User needs to use either Basic Authentication or Digest Access Authentication.", 401, "http://sucks.to.be.you.com");
	
	/**
	 * Filter method that checks user has rights to get resource.
	 * Method first checks that username and password given in the Authorization header match.
	 * After that it checks that user has rights from method and class annotations. 
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		CustomerService customerservice = new CustomerService();
		Digest digest = new Digest();
		Customer customer = null;
		
		Method method = resInfo.getResourceMethod();
		
		// Checking if user is trying to add a new basic user. If so, we can skip everything below.
		// This is the only method that doesn't require authentication from the user.
		if(method.getName().equals("addBasicCustomer")) {
			return;
		}
		
		// Getting values from Authorization header and decoding 'username' and 'password'.
		List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

		// If header is null we assume that client is trying to authenticate with Digest method.
		// Sending response that holds values for Digest Authentication.
		if (authHeader == null) {	
			requestContext.abortWith(responsemaker.getDigestResponse(digest.createIds()));
			return;
		}
		
		// Part were authentication type is identified from authHeader's first value.
		// SecurityFilter supports Basic Authentication and Digest Access Authentication.
		String[] authtype = new String[2];
		try {
			authtype = authHeader.get(0).split(" ");
		} catch (PatternSyntaxException pse) {
			pse.printStackTrace(System.err);
			requestContext.abortWith(responsemaker.getUnAuthorizedResponse(AUTHENTICATION_ERRORMSG));
		}
		
		// Basic authentication type.
		if (authtype[0].equals("Basic")) {
			String username = "";
			String password = "";
			try {
				String decodedString = Base64.decodeAsString(authtype[1]);
				StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
				username = tokenizer.nextToken();								
				password = tokenizer.nextToken();
			} catch (NoSuchElementException nse) {
				requestContext.abortWith(responsemaker.getUnAuthorizedResponse(LOGIN_ERRORMSG));
				return;
			}
			
			// Finding target customer from customer service. If customer with 'username' doesn't exist
			// or given 'password' doesn't match, request will abort.
			customer = customerservice.findCustomer(username, password);
			if (customer == null) {
				requestContext.abortWith(responsemaker.getUnAuthorizedResponse(LOGINFAILED_ERRORMSG));
				return;
			}
			
			// Getting scheme and adding customer and scheme to SecurityContext for later use.
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new MyCustomSecurityContext(customer, scheme));
			
			
		// Digest Access Authentication type	
		} else if (authtype[0].equals("Digest")) {
			
			// Using Digest class method to check if content in authHeader is right.
			// If Digest method succeeds then 'customer' will get a customer value different than null.
			// Null value will lead to aborting the attempt.
			customer = digest.checkResponse(authHeader, requestContext.getMethod());
			if (customer == null) {
				requestContext.abortWith(responsemaker.getUnAuthorizedResponse(LOGINFAILED_ERRORMSG));
				return;
			}		

			// Getting scheme and adding customer and scheme to SecurityContext for later use.
			String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
			requestContext.setSecurityContext(new MyCustomSecurityContext(customer, scheme));
			
			// Client authentication ends here. RequestContext should now have a MyCustomSecurityContext
			// that holds autheticated customer object in it.
		}

		// Starting to check annotations --->
		
		// Checking first from method level
		if (method.isAnnotationPresent(PermitAll.class)) {
			return;
		}
		if (method.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(responsemaker.getForbiddenResponse(FORBIDDEN_ERRORMSG));
		}
		RolesAllowed rolesAllowed = method.getAnnotation(RolesAllowed.class);
		if (rolesAllowed != null) {
			if (authorizationSucceeded(rolesAllowed.value(), requestContext)) {
				return;
			}
			requestContext.abortWith(responsemaker.getUnAuthorizedResponse(UNAUTHORIZED_ERRORMSG));
		}
		
		
		// Checking then from class level
		Class<?> resClass = resInfo.getResourceClass();

		if (resClass.isAnnotationPresent(PermitAll.class)) {
			return;
		}
		if (resClass.isAnnotationPresent(DenyAll.class)) {
			requestContext.abortWith(responsemaker.getForbiddenResponse(FORBIDDEN_ERRORMSG));
		}
		rolesAllowed = resClass.getAnnotation(RolesAllowed.class);
		if (rolesAllowed != null) {
			if (authorizationSucceeded(rolesAllowed.value(), requestContext)) {
				return;
			}
			requestContext.abortWith(responsemaker.getUnAuthorizedResponse(UNAUTHORIZED_ERRORMSG));
		}
		
		// Getting here means that user will get access through securityfilter --->

	}

	/**
	 * Checks if requestContext has user in it. If we find user, we check if user has a role
	 * matching roles in array 'roles'.
	 * @param roles Roles that are fetched from method or class annotations.
	 * @param requestContext 
	 * @return true if user has a matching role, otherwise false.
	 */
	private boolean authorizationSucceeded(String[] roles, ContainerRequestContext requestContext) {
		
		if (roles.length > 0 && !isAuthenticated(requestContext)) {
			return false;
		}
		
		SecurityContext security = requestContext.getSecurityContext();
		for (String role : roles) {
            if (security.isUserInRole(role)) {
                return true;
            }
        }
		return false;
	}

	/**
	 * Finds customer from requestContext.
	 * @param requestContext
	 * @return True if customer was found, otherwise false.
	 */
	private boolean isAuthenticated(ContainerRequestContext requestContext) {
		return requestContext.getSecurityContext().getUserPrincipal() != null;
	}

}
