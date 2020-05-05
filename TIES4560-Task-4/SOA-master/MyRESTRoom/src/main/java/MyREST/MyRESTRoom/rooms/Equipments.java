package MyREST.MyRESTRoom.rooms;

import java.net.URI;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;
import MyREST.MyRESTRoom.response.ResponseMaker;

@PermitAll
public class Equipments {

	private EquipmentService service = new EquipmentService();
	private ResponseMaker responseMaker = new ResponseMaker();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoomEquipments(@PathParam("roomId") int roomId, @Context UriInfo uriInfo){
		List<Equipment> equipments = service.getRoomEquipments(roomId);
    	URI uri = uriInfo.getAbsolutePath();
    	
		if(equipments.isEmpty() || equipments == null) {
    		return responseMaker.getNoContentResponse(uri, equipments);
    	}
    	return responseMaker.getAcceptedResponse(uri, equipments);

	}
	/*
	 * Jätän tyyppimuunnokset nyt tekemättä, muut linkitykset toimivat
	 */
	@POST
	@RolesAllowed("ADMIN")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response addRoomEquipments(@PathParam("roomId") int roomId, Equipment eq, @Context UriInfo uriInfo){
		Equipment equipment = service.addRoomEquipments(roomId, eq);
    	URI uri = uriInfo.getAbsolutePathBuilder()
    			.build();
    	equipment.addLink(uri.toString(), "self");
    	URI uri2 = uriInfo.getBaseUriBuilder()
    			.path(Rooms.class)
    			.path(Integer.toString(roomId))
    			.build();
    	equipment.addLink(uri2.toString(), "room");
    	uri2 = uriInfo.getBaseUriBuilder()
    			.path(Rooms.class)
    			.build();
    	equipment.addLink(uri2.toString(), "rooms");
    	
    	return responseMaker.getAcceptedResponse(uri, equipment);
	}
	
	@DELETE
	@RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
	public Response deleteRoomEquipments(@PathParam("roomId") int roomId, @Context UriInfo uriInfo) {
		List<Equipment> equipments = service.deleteRoomEquipments(roomId);
		if (equipments.isEmpty() || equipments == null) {
			throw new DataNotFoundException("Room with id " + roomId + " has no equipments.");
		}
    	URI uri = uriInfo.getAbsolutePath();
    	return responseMaker.getAcceptedResponse(uri, equipments);
	}

}
