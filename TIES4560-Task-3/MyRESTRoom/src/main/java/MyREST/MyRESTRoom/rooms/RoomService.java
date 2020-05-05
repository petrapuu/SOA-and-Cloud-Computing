package MyREST.MyRESTRoom.rooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MyREST.MyRESTRoom.exceptions.DataNotFoundException;

public class RoomService {
	
	// HashMap that is used to store rooms while server is running.
	private static HashMap<Integer,Room> roomMap = new HashMap<Integer,Room>();

	// Room values here are for testing...
	public RoomService() {
		if (roomMap.size() == 0) {
			Room blue = new Room(1, "Blue", 10);
			Room red = new Room(2, "Red", 20);
			Room yellow = new Room(3, "Yellow", 6);
			Room green = new Room(4, "Green", 4);
			
			roomMap.put(1, blue);
			roomMap.put(2, red);
			roomMap.put(3, yellow);
			roomMap.put(4, green);
		}
	}
	
	/**
	 * Returns all the rooms as an ArrayList from hashmap values.
	 * @return All the rooms as an ArrayList.
	 */
	public List<Room> getAllRooms() {
		return new ArrayList<Room>(roomMap.values());
	}

	/**
	 * Adds a new room to roomMap.
	 * User can create the room without id, because this method will set id automatically.
	 * @param r New room to be added.
	 * @return New room that was added succesfully.
	 */
	public Room addRoom(Room r) {
		r.setId(getMaxId()+1);
		roomMap.put(r.getId(), r);
		return r;
	}
	
	/**
	 * Updates an already existing room from roomMap.
	 * @param r Room that will be updated.
	 * @return If room exists then method returns updated version of the room.
	 * If room doesn't exist method throws DataNotFoundException.
	 */
	public Room updateRoom(Room r) {
		Room ro = roomMap.get(r.getId());
		if(ro == null) {
    		throw new DataNotFoundException("Room Service doesn't include a room with id " + r.getId() + ".");
    	}
		if(r.getCapacity() != 0) ro.setCapacity(r.getCapacity());
		if(r.getRoomName() != null) ro.setRoomName(r.getRoomName());
		return ro;
	}
	
	/**
	 * Deletes a room from roomMap.
	 * @param id Int value to indicate the key value (same as room value)
	 * @return The room that was removed. If room doesn't exist then method returns
	 * DataNotFoundException.
	 */
	public Room deleteRoom(int id) {
		Room room = roomMap.remove(id);
		return room;
	}

	/**
	 * Gets a room with specific id from roomMap using param 'id'
	 * @param id Int value to indicate the key value (same as room value)
	 * @return The room with id 'id'. If room doesn't exist the method returns
	 * DataNotFoundException.
	 */
	public Room getRoomWithId(int id) {
		Room r = roomMap.get(id);
		return r;
	}
	
	/**
	 * Gets a list of rooms which name match to parameter 'name'.
	 * @param name String value that is used in name comparing.
	 * @return A list of rooms that contain 'name' in their name.
	 */
	public List<Room> getRoomWithName(String name) {
		List<Room> allRooms = getAllRooms();
		List<Room> resultRooms = new ArrayList<>();
		String lowerCaseName = name.toLowerCase();
		
		for(Room room : allRooms) {
			if(room.getRoomName().toLowerCase().contains(lowerCaseName)) {
				resultRooms.add(room);
			}
		}
		return resultRooms;
	}
	
	/**
	 * Method that is used to get the max id from the roomMap.
	 * This is used when creating new rooms to make sure there won't be any
	 * rooms with the same id.
	 * @return Max int value presenting room's id value.
	 */
	public int getMaxId() {
		int max = 0;
		for (int id:roomMap.keySet()) {
			if (max <= id) max = id;
		}
		return max;
	}

	/**
	 * Gets a list of rooms that have higher or equal capacity than 'over'.
	 * @param over Int value that is used when comparing capacities.
	 * @return A list of rooms that have higher or equal capacity than 'over'.
	 */
	public List<Room> getRoomsWithHigherCapacity(int over) {
		List<Room> allRooms = getAllRooms();
		List<Room> resultRooms = new ArrayList<>();
		
		for(Room room : allRooms) {
			if(room.getCapacity() >= over) {
				resultRooms.add(room);
			}
		}
		return resultRooms;
	}
	
	/**
	 * Gets a list of rooms that have lower or equal capacity than 'lower'.
	 * @param lower Int value that is used when comparing capacities.
	 * @return A list of rooms that have lower or equal capacity than 'lower'.
	 */
	public List<Room> getRoomsWithLowerCapacity(int lower) {
		List<Room> allRooms = getAllRooms();
		List<Room> resultRooms = new ArrayList<>();
		
		for(Room room : allRooms) {
			if(room.getCapacity() < lower) {
				resultRooms.add(room);
			}
		}
		return resultRooms;
	}

	/**
	 * Gets a list of rooms that have capacity between 'over' and 'lower'.
	 * Room's that have capacities matching 'over' or 'lower' are included.
	 * @param over Int value that is used when comparing capacities.
	 * @param lower Int value that is used when comparing capacities.
	 * @return A list of rooms that match the comparisons.
	 */
	public List<Room> getRoomsWithCapacityBetween(int over, int lower) {
		List<Room> allRooms = getAllRooms();
		List<Room> resultRooms = new ArrayList<>();
		
		for(Room room : allRooms) {
			if(room.getCapacity() <= lower && room.getCapacity() >= over) {
				resultRooms.add(room);
			}
		}
		return resultRooms;
	}
}