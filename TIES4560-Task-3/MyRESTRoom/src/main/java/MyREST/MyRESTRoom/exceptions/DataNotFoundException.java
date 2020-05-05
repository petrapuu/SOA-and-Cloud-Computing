package MyREST.MyRESTRoom.exceptions;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 341201650484227487L;
	
	public DataNotFoundException(String message) {
		super(message);
	}


}
