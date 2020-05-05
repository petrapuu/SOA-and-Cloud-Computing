package MyREST.MyRESTRoom.exceptions;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = -567225108930646790L;
	public BadRequestException(String message) {
		super(message);
	}
}
