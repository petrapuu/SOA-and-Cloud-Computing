package MyREST.MyRESTRoom.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataNotFoundMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException e) {
		ErrorMessage msg = new ErrorMessage(e.getMessage(), 404, "http://page.that.doesnt.exist.org");
		return Response.status(Status.NOT_FOUND)
				.entity(msg)
				.build();
	}

}
