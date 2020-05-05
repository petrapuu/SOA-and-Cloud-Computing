package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException>{

	@Override
	public Response toResponse(NotAllowedException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 405, "http://page.that.doesnt.exist.org");
		return Response.status(Status.METHOD_NOT_ALLOWED)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}

}
