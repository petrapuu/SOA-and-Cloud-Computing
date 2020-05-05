package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UserInfoInsufficientExceptionMapper implements ExceptionMapper<UserInfoInsufficientException>{

	@Override
	public Response toResponse(UserInfoInsufficientException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 400, "http://user.info.insufficient.org");
		return Response.status(Status.BAD_REQUEST)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}

}
