package MyREST.MyRESTRoom.response;

import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class ResponseMaker {
	
	public Response getAcceptedResponse(URI uri, Object entity) {
		return Response
    			.status(Status.ACCEPTED)
    			.header("Location", uri)
    			.entity(entity)
    			.build();
	}

	public Response getNoContentResponse(URI uri, Object entity) {
		return Response
    			.status(Status.NO_CONTENT)
    			.header("Location", uri)
    			.entity(entity)
    			.build();
	}
}
