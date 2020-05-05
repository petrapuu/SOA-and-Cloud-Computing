package MyREST.MyRESTRoom.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.glassfish.jersey.internal.util.Base64;

import MyREST.MyRESTRoom.Customer;
import MyREST.MyRESTRoom.CustomerService;

/**
 * Class that is used to solve Digest Access Authentication
 * requests from client.
 */
public class Digest {
	
	private CustomerService customerservice = new CustomerService();

	/**
	 * Method that creates a new MD5 string from
	 * the parameter String.
	 * @param id String that is going to get hashed.
	 * @return Hashed String 
	 */
	public static String getHash(String id) {
		String hashValue = "";
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digestedBytes = md.digest(id.getBytes());
			hashValue = new BigInteger(1, digestedBytes).toString(16);
			while (hashValue.length() < 32) { 
                hashValue = "0" + hashValue; 
            } 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashValue;
	}
	
	/**
	 * Creates a list of String values for Response
	 * that is sent to client in Digest Authentication.
	 * @return A list of Strings containing values Realm, Qop,
	 * Nonce and Opaque.
	 */
	public List<String> createIds() {
		List<String> list = new ArrayList<String>();
		list.add(createRealm());
		list.add(createQop());
		list.add(createNonce());
		list.add(createOpaque());
		return list;
	}
	
	/**
	 * Creates Nonce String value.
	 * @return Nonce String value
	 */
	private String createNonce() {
		return "nonce=\"" + generateRandom() + "\",";
	}
	
	/**
	 * Creates Opaque String value.
	 * @return Opaque String value
	 */
	private String createOpaque() {
		return "opaque=\"" + generateRandom() + "\"";
	}
	
	/**
	 * Creates Qop String value.
	 * @return Qop String value
	 */
	private String createQop() {
		return "qop=\"auth,auth-int\",";
	}
	
	/**
	 * Creates Realm String value.
	 * @return Realm String value
	 */
	private String createRealm() {
		String realm = "testrealm@example.com";
		return "realm=\"" + realm + "\",";
	}
	
	/**
	 * Creates a random encoded String value used in Nonce and Opaque String values.
	 * @return Random encoded String value
	 */
	private String generateRandom() {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.encodeAsString(nonceByte);
    }

	/**
	 * Checks response values that client has sent in it's second request.
	 * If request's 'response' value equals String value that Digest creates
	 * then this method returns the right customer that matches client's request.
	 * @param clientIds values that client has sended back in it's second request.
	 * @param method String value used when creating hash.
	 * @return If client's 'response' value and this method's hashed String value
	 * are equal then this method returns the customer with the right username.
	 * Otherwise method returns null.
	 */
	public Customer checkResponse(List<String> clientIds, String method) {
		// Parsing values from the clientIds (clientIds has only one value).
		String[] idList = clientIds.get(0).split(",");
		String username = parseId(idList[0]);
		String realm = parseId(idList[1]);
		String nonce = parseId(idList[2]);
		String uri = parseId(idList[3]);
		String response = parseId(idList[5]);
		
		// Trying to find customer with requested username.
		// If it doesn't exist checking process ends.
		Customer customer = customerservice.getCustomerByUsername(username);
		if (customer == null) {
			return null;
		}
		
		// Fetching password from the customer.
		String password = customer.getPassword();
		if (password == null) {
			return null;
		}
		
		// Creating the matching hash value.
		String HA1 = getHash(username + ":" + realm + ":" + password);
		String HA2 = getHash(method + ":" + uri);	
		String finalresponse = getHash(HA1 + ":" + nonce + ":" + HA2);
		
		// Checking if our generated hash value equals client's response value.
		if (finalresponse.equals(response)) {
			return customer;
		} else {
			return null;
		}
	}
	
	/**
	 * Basic parsing method that returns a String value
	 * between two " marks.
	 * @param id String that is getting parsed.
	 * @return A parsed String, e.g. 'abc"dfg"abc' returns 'dfg'.
	 */
	private String parseId(String id) {
		String[] split = id.split("\"");
		return split[1];
	}

}
