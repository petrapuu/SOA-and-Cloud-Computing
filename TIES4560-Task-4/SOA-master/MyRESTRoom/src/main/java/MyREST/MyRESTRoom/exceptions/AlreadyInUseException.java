package MyREST.MyRESTRoom.exceptions;

public class AlreadyInUseException extends RuntimeException {
	
	private static final long serialVersionUID = 3350270870034333940L;

	public AlreadyInUseException(String message) {
		super(message);
	}

}
