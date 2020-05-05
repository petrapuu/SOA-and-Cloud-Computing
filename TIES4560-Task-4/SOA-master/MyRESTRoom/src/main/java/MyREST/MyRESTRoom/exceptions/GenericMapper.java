package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//@Provider
public class GenericMapper implements ExceptionMapper<Throwable>{

	@Override
	public Response toResponse(Throwable e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 500, "http://page.that.doesnt.exist.org");
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.type(MediaType.APPLICATION_JSON)
				.entity(msg)
				.build();
	}

}
