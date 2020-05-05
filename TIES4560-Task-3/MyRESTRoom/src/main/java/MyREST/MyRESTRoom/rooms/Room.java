package MyREST.MyRESTRoom.rooms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Room {
	
	private int id;
	private String roomName;
	private int capacity;
	private List<Link> links = new ArrayList<>();
	
	// This is used to generate example values!
	public Room(int id, String roomName, int capacity) {
		this.id = id;
		this.roomName = roomName;
		this.capacity = capacity;
	}
	
	
	// Getters and setters.
	public Room() { }
	
	public void addLink(String url, String rel){
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
		}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
