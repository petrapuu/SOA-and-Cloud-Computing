package MyREST.MyRESTRoom.response;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import MyREST.MyRESTRoom.exceptions.ErrorMessage;

/**
 * Class that is used to make response writing easier.
 */
public class ResponseMaker {
	
	/**
	 * Creates a response with ACCEPTED status.
	 * @param uri URI that will be shown in returned response's Location header.
	 * @param entity Object that we want to send in Response entity.
	 * @return Response that corresponds method name.
	 */
	public Response getAcceptedResponse(URI uri, Object entity) {
		return Response
    			.status(Status.ACCEPTED)
    			.header("Location", uri)
    			.entity(entity)
    			.build();
	}

	/**
	 * Creates a response with NO_CONTENT status.
	 * @param uri URI that will be shown in returned response's Location header.
	 * @param entity Object that we want to send in Response entity.
	 * @return Response that corresponds method name.
	 */
	public Response getNoContentResponse(URI uri, Object entity) {
		return Response
    			.status(Status.OK)	// Status should be NO_CONTENT, but that doesn't show entity in Postman!
    			.header("Location", uri)
    			.entity(entity)
    			.build();
	}
	
	/**
	 * Creates a response with FORBIDDEN status.
	 * @param errormsg ErrorMessage that will be shown in entity.
	 * @return Response that corresponds method name.
	 */
	public Response getForbiddenResponse(ErrorMessage errormsg) {
		return Response
    			.status(Status.FORBIDDEN)
    			.entity(errormsg)
    			.build();
	}
	
	/**
	 * Creates a response with UNAUTHORIZED status.
	 * @param errormsg ErrorMessage that will be shown in entity.
	 * @return Response that corresponds method name.
	 */
	public Response getUnAuthorizedResponse(ErrorMessage errormsg) {
		return Response
    			.status(Status.UNAUTHORIZED)
    			.entity(errormsg)
    			.build();
	}
	
	/**
	 * Creates a response with UNAUTHORIZED status.
	 * This response is sent to client when client is using
	 * Digest Authentication to get access. Client will then send
	 * another request using this response's entity values to get the access.
	 * @param idList List that holds info that client needs to make the second request.
	 * @return Response that holds info for client.
	 */
	public Response getDigestResponse(List<String> idList) {
		String masterString = "Digest ";
		for(String str : idList) {
			masterString = masterString + str;
		}
		return Response
    			.status(Status.UNAUTHORIZED)
    			.header("WWW-Authenticate", masterString)
    			.build();
	}

}
