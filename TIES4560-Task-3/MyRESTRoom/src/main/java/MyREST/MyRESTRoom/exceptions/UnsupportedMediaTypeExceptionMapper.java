package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnsupportedMediaTypeExceptionMapper implements ExceptionMapper<NotSupportedException>{

	@Override
	public Response toResponse(NotSupportedException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 415, "http://page.that.doesnt.exist.org");
		return Response.status(Status.UNSUPPORTED_MEDIA_TYPE)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}

}
