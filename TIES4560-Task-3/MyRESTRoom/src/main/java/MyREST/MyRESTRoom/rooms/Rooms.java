package MyREST.MyRESTRoom.rooms;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;
import MyREST.MyRESTRoom.response.ResponseMaker;


/**
 * Root resource (exposed at "rooms" path)
 */
@Path("/rooms")
public class Rooms {
	
	private RoomService roomService = new RoomService();
	private ResponseMaker responseMaker = new ResponseMaker();
	
	
	/**
	 * Gives all the rooms from the Room Service.
	 * @param uriInfo javax UriInfo that gives the absolute path.
	 * @return Response that has all the rooms in it's entity.
	 */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms(@Context UriInfo uriInfo) {
    	List<Room> rooms = roomService.getAllRooms();
    	URI uri = uriInfo.getAbsolutePath();
    	
		if(rooms.isEmpty() || rooms == null) {
    		return responseMaker.getNoContentResponse(uri, rooms);
    	}
    	return responseMaker.getAcceptedResponse(uri, rooms);
    }
    
    /**
     * Gives a room from the Room Service by room's id number.
     * Path accepts only integer values, for example: rooms/4
     * @param id number that is used as room's id number.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has room with the searched id number in it's entity.
     */
    @GET
    @Path("/{roomId : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") int id, @Context UriInfo uriInfo) {
    	Room room = roomService.getRoomWithId(id);

    	if(room == null) {
    		throw new DataNotFoundException("Room Service doesn't include a room with id " + id + ".");
    	}
    	
    	URI uri = uriInfo.getAbsolutePath();
    	return responseMaker.getAcceptedResponse(uri, room);
    }
    
    /**
     * Gives possible multiple rooms from the Room Service.
     * Multiple rooms occur, when Room Service has multiple rooms that share
     * the same name. If Room Service doesn't have any rooms that equal
     * the PathParam 'roomName' then it returns an empty list.
     * @param name that is used to find a room from service.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has a list of rooms in it's entity.
     */
    @GET
    @Path("/{roomName : [a-zA-Z][a-zA-Z_0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomByName(@PathParam("roomName") String name, @Context UriInfo uriInfo) {
    	List<Room> rooms = roomService.getRoomWithName(name);
    	URI uri = uriInfo.getAbsolutePath();
    	if(rooms.isEmpty() || rooms == null) {
    		return responseMaker.getNoContentResponse(uri, rooms);
    	}
    	return responseMaker.getAcceptedResponse(uri, rooms);
    }
    
    /**
     * Gives all the rooms that have higher or equal capacity than QueryParam 'over'. 
     * @param over int value that is used when capacities are compared.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has a list of rooms in it's entity.
     */
    @GET
    @Path("/highercap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsHigherCapacityThan(@QueryParam("over") int over, @Context UriInfo uriInfo) {
    	List<Room> rooms = roomService.getRoomsWithHigherCapacity(over);
    	URI uri = uriInfo.getAbsolutePath();
    	if(rooms.isEmpty() || rooms == null) {
    		return responseMaker.getNoContentResponse(uri, rooms);
    	}
    	return responseMaker.getAcceptedResponse(uri, rooms);
    }
    
    /**
     * Gives all the rooms that have lower or equal capacity than QueryParam 'lower'. 
     * @param lower int value that is used when capacities are compared.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has a list of rooms in it's entity.
     */
    @GET
    @Path("/lowercap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsLowerCapacityThan(@QueryParam("lower") int lower, @Context UriInfo uriInfo) {
    	List<Room> rooms = roomService.getRoomsWithLowerCapacity(lower);
    	URI uri = uriInfo.getAbsolutePath();
    	if(rooms.isEmpty() || rooms == null) {
    		return responseMaker.getNoContentResponse(uri, rooms);
    	}
    	return responseMaker.getAcceptedResponse(uri, rooms);
    }
    
    /**
     * Gives all the rooms that have a capacity between 'over' and 'lower'.
     * Capacities same as 'over' and 'lower' also included.
     * @param over int value that is used when capacities are compared.
     * @param lower int value that is used when capacities are compared.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has a list of rooms in it's entity.
     */
    @GET
    @Path("/capbetween")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomsCapacityBetween(@QueryParam("over") int over,
    											@QueryParam("lower") int lower,
    											@Context UriInfo uriInfo) {
    	List<Room> rooms = roomService.getRoomsWithCapacityBetween(over, lower);
    	URI uri = uriInfo.getAbsolutePath();
    	if(rooms.isEmpty() || rooms == null) {
    		return responseMaker.getNoContentResponse(uri, rooms);
    	}
    	return responseMaker.getAcceptedResponse(uri, rooms);
    }
    
    /**
     * Adds a new room to Room Service.
     * @param room Room to be added.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has newly created room in it's entity.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room, @Context UriInfo uriInfo) {
    	Room newRoom = roomService.addRoom(room);
    	URI uri = uriInfo.getBaseUriBuilder()
    			.path(Rooms.class)
    			.path(Integer.toString(room.getId()))
    			.build();
    	newRoom.addLink(uri.toString(),"self");
    	URI uri2 = uriInfo.getBaseUriBuilder()
    			.path(Rooms.class)
    			.path(Rooms.class, "getRoomEquipments")
    			.resolveTemplate("roomId", newRoom.getId())
    			.build();
    	newRoom.addLink(uri2.toString(), "equipments");
    	uri2 = uriInfo.getBaseUriBuilder()
    			.path(Rooms.class)
    			.build();
    	newRoom.addLink(uri2.toString(), "rooms");
    	
    	return responseMaker.getAcceptedResponse(uri, newRoom);
    }
    
    /**
     * Updates an already existing room in the Room Service.
     * @param id Id of the room that is going to get updated.
     * @param room New room that is going to get merged into the old room.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has updated version of the room in it's entity.
     */
    @PUT
    @Path("/{roomId : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(@PathParam("roomId") int id, Room room, @Context UriInfo uriInfo) {
    	room.setId(id);
    	URI uri = uriInfo.getAbsolutePath();
    	Room updatedRoom = roomService.updateRoom(room);
    	return responseMaker.getAcceptedResponse(uri, updatedRoom);
    }
    
    /**
     * Deletes a room from the Room Service.
     * @param id Id of the room to be removed.
     * @param uriInfo javax UriInfo that gives the absolute path.
     * @return A response that has the removed room in it's entity.
     */
    @DELETE
    @Path("/{roomId : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") int id, @Context UriInfo uriInfo) {
    	Room roomRemoved = roomService.deleteRoom(id);
		if(roomRemoved == null) {
			throw new DataNotFoundException("Room Service doesn't include a room with id " + id + ".");
		}

    	URI uri = uriInfo.getAbsolutePath();
    	return responseMaker.getAcceptedResponse(uri, roomRemoved);
    }
    
    /**
     * Nested call to equipments.
     * @return new Equipments to continue.
     */
    @Path("/{roomId : \\d+}/equipments")
    public Equipments getRoomEquipments() {
    	return new Equipments();
    }
    
}