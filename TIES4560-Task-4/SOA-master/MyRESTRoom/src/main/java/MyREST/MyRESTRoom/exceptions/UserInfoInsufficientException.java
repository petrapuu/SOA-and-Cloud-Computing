package MyREST.MyRESTRoom.exceptions;

public class UserInfoInsufficientException extends RuntimeException {
	
	private static final long serialVersionUID = -4933962869119852210L;

	public UserInfoInsufficientException(String message) {
		super(message);
	}

}