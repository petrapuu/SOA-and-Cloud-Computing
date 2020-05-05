package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>{

	@Override
	public Response toResponse(NotFoundException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 404, "http://page.that.doesnt.exist.org");
		return Response.status(Status.NOT_FOUND)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}


}
