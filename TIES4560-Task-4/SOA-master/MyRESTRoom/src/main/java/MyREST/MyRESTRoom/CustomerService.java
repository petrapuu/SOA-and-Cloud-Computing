package MyREST.MyRESTRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;
import MyREST.MyRESTRoom.exceptions.UserInfoInsufficientException;
import MyREST.MyRESTRoom.exceptions.AlreadyInUseException;

public class CustomerService {
	

	private static HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();

	// Customer values here are for admin
	public CustomerService() {
		if (customerMap.size() == 0) {
			List<String> adminRoles = new ArrayList<>();
			adminRoles.add("ADMIN");
			adminRoles.add("USER");

			Customer admin = new Customer(1, "admin", "admin", "admin", adminRoles);

			customerMap.put(1, admin);
		}
	}

	public List<Customer> getAllCustomers() {
		return new ArrayList<Customer>(customerMap.values());
	}

	public Customer getCustomer(int id) {
		return customerMap.get(id);
	}

	public List<Customer> getCustomerWithName(String name) {
		List<Customer> allCustomers = getAllCustomers();
		List<Customer> resultCustomers = new ArrayList<>();
		String lowerCaseName = name.toLowerCase();

		for (Customer customer : allCustomers) {
			if (customer.getName().toLowerCase().contains(lowerCaseName)) {
				resultCustomers.add(customer);
			}
		}
		return resultCustomers;
	}

	/**
	 * method for admin to add customers. normal registration happens via addBasicCustomer-method
	 * @param customer
	 * @return
	 */
	public Customer addCustomer(Customer customer) {
		if (isLoginReserved(customer)) {
			throw new AlreadyInUseException("Username " + customer.getLogin() + " already in use.");
		}
		if (customer.getLogin() == null || customer.getPassword() == null || customer.getName() == null) {
			throw new UserInfoInsufficientException("Login name, password or name missing.");
		}
		customer.setId(getMaxId() + 1);
		customerMap.put(customer.getId(), customer);
		return customer;
	}


	public int getMaxId() {
		int max = 0;
		for (int id : customerMap.keySet()) {
			if (max <= id)
				max = id;
		}
		return max;
	}

	public Customer updateCustomer(Customer customer) {
		if (customer.getLogin() != null) {
			if (isLoginReserved(customer)) {
				throw new AlreadyInUseException("Username " + customer.getLogin() + " already in use.");
			}
		}
		Customer c = customerMap.get(customer.getId());

		if (c == null) {
			throw new DataNotFoundException("No customer found with id " + customer.getId());
		}
		if (customer.getName() != null)
			c.setName(customer.getName());
		if (customer.getLogin() != null)
			c.setLogin(customer.getLogin());
		if (customer.getPassword() != null)
			c.setPassword(customer.getPassword());
		if (customer.getRole() != null) {
			c.setRole(customer.getRole());
		}
		return c;
	}
	
	public Customer updateBasicCustomer(Customer customer) {
		if (customer.getLogin() != null) {
			if (isLoginReserved(customer)) {
				throw new AlreadyInUseException("Username " + customer.getLogin() + " already in use.");
			}
		}
		Customer c = customerMap.get(customer.getId());
		// might be catched earlier and this check is not needed
		if (c == null) {
			throw new DataNotFoundException("No customer found with id " + customer.getId());
		}
		if (customer.getName() != null)
			c.setName(customer.getName());
		if (customer.getLogin() != null)
			c.setLogin(customer.getLogin());
		if (customer.getPassword() != null)
			c.setPassword(customer.getPassword());

		// Basic user can't upadate role
		List<String> user = new ArrayList<String>();
		user.add("USER");
		c.setRole(user);
		return c;
	}

	public Customer deleteCustomer(int id) {
		return customerMap.remove(id);

	}

	public boolean isLoginReserved(Customer customer) {
		Collection<Customer> customers = customerMap.values();
		for (Customer c : customers) {
			if (c.getLogin().equals(customer.getLogin())) {
				return true;
			}
		}
		return false;
	}

	public Customer findCustomer(String username, String password) {
		Collection<Customer> customers = customerMap.values();
		for (Customer c : customers) {
			if (c.getLogin().equals(username)) {
				if (c.getPassword().equals(password)) {
					return c;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public Customer addBasicCustomer(Customer customer) {
		if (isLoginReserved(customer)) {
			throw new AlreadyInUseException("Username " + customer.getLogin() + " already in use.");
		}
		
		if (customer.getLogin() == null || customer.getPassword() == null || customer.getName() == null) {
			throw new UserInfoInsufficientException("Login name, password or name missing.");
		}
		
		// Making new customer be user
		List<String> user = new ArrayList<String>();
		user.add("USER");
		customer.setRole(user);
		
		// Setting id
		customer.setId(getMaxId() + 1);
		customerMap.put(customer.getId(), customer);
		return customer;
	}

	public String getPassword(String username) {
		Collection<Customer> customers = customerMap.values();
		for(Customer c : customers) {
			if (c.getLogin().equals(username)){
				return c.getPassword();
			}
		}
		return null;
	}

	public Customer getCustomerByUsername(String username) {
		Collection<Customer> customers = customerMap.values();
		for(Customer c : customers) {
			if (c.getLogin().equals(username)){
				return c;
			}
		}
		return null;
	}



	/*
	 * public ArrayList<Integer> getCustomerHistory(int id) { if
	 * (customerMap.containsKey(id)){ return customerMap.get(id).getRoomHistory(); }
	 * return null; }
	 */

}
