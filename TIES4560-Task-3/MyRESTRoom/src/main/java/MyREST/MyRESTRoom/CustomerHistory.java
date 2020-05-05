package MyREST.MyRESTRoom;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import MyREST.MyRESTRoom.rooms.Rooms;

@XmlRootElement
public class CustomerHistory {
	
	private ArrayList<Integer> roomHistory;
	private List<Link> links = new ArrayList<>();
	private List<Link> roomLinks = new ArrayList<>();

	public List<Link> getRoomLinks() {
		return roomLinks;
	}

	public void setRoomLinks(List<Link> roomLinks) {
		this.roomLinks = roomLinks;
	}

	public void addLink(String url, String rel){
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
		}
	
	public void addRoomLink(String url, List<Link> newlist, String rel){
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		newlist.add(link);
		}
	
	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public ArrayList<Integer> getRoomHistory() {
		return roomHistory;
	}
	
	public void setRoomHistory(ArrayList<Integer> roomHistory) {
		this.roomHistory = roomHistory;
	}
	
	public void updateCustomerHistory(CustomerHistory newHistory) {
		Set<Integer> set = new HashSet<>();
		if(newHistory.getRoomHistory() != null) set.addAll(newHistory.getRoomHistory());
		if(roomHistory != null) set.addAll(roomHistory);
		roomHistory = new ArrayList<>(set);
	}

	public void createRoomLinks(UriInfo uriInfo) {
		List<Link> newLinks = new ArrayList<>();
		for(Integer i : roomHistory) {
			URI uri = uriInfo.getBaseUriBuilder()
					.path(Rooms.class)
					.path(Integer.toString(i.intValue()))
					.build();
			addRoomLink(uri.toString(), newLinks, "Link to room " + i);
		}
		roomLinks = newLinks;
	}

}
