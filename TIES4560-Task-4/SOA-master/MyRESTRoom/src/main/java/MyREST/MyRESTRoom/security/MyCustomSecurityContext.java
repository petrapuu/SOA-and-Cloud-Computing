package MyREST.MyRESTRoom.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import MyREST.MyRESTRoom.Customer;

public class MyCustomSecurityContext implements SecurityContext {
	private Customer customer;
	private String scheme;
	
	public MyCustomSecurityContext(Customer customer, String scheme) {
		this.customer = customer;
		this.scheme = scheme;
	}
	@Override
	public Principal getUserPrincipal() {
		return this.customer;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (customer.getRole() != null) {
			return customer.getRole().contains(role);
		} return false;
	}

	@Override
	public boolean isSecure() {
		return "https".contentEquals(this.scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}
	
	public int getCustomerInt() {
		return customer.getId();
	}

}
