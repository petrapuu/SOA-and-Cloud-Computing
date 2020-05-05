package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyInUseExceptionMapper implements ExceptionMapper<AlreadyInUseException>{

	@Override
	public Response toResponse(AlreadyInUseException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 409, "http://page.that.doesnt.exist.org");
		return Response.status(Status.CONFLICT)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}

}
