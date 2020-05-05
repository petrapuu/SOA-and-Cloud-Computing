package MyREST.MyRESTRoom;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Customer implements Principal{
	private String name;
	private int id;
	private String login, password;
	private List<String> role = new ArrayList<>();
	private List<Link> links = new ArrayList<>();

	// This is used to generate example values!
	public Customer(int id, String name, String login, String password, List<String> role) {
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;
		this.role = role;
	}

	
	public Customer() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public void addLink(String url, String rel) {
		Link link = new Link();
		link.setLink(url);
		link.setRel(rel);
		links.add(link);
	}

	public String getName() {
		return name;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
