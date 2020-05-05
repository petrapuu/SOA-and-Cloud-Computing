package MyREST.MyRESTRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;


public class CustomerService {

	private static HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
	
	public CustomerService() {

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
		
		for(Customer customer : allCustomers) {
			if(customer.getName().toLowerCase().contains(lowerCaseName)) {
				resultCustomers.add(customer);
			}
		}
		return resultCustomers;
	}


	public Customer addCustomer(Customer customer) {
		customer.setId(getMaxId() + 1);
		customerMap.put(customer.getId(), customer);
		return customer;
	}
	
	public int getMaxId() {
		int max = 0;
		for (int id:customerMap.keySet()) {
			if (max <= id) max = id;
		}
		return max;
	}


	public Customer updateCustomer(Customer customer) {
		Customer c = customerMap.get(customer.getId());
		if (c == null) {
			throw new DataNotFoundException("No customer found with id " + customer.getId());
		}
		if (customer.getName() != null) c.setName(customer.getName());
		return c;
	}


	public Customer deleteCustomer(int id) {
		return customerMap.remove(id);
		
	}


	/*public ArrayList<Integer> getCustomerHistory(int id) {
		if (customerMap.containsKey(id)){
			return customerMap.get(id).getRoomHistory();
		}
		return null;
	}*/
	

}
