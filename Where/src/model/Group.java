package model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElementRef;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;

import rest.resources.UserResource;

import eli.JsonViews;

/**
 * Group POJO
 */
@Entity
@Table(name = "grp")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1136865455880159424L;
	@XmlElementRef
	@Transient
	private List<AtomLink> links = new ArrayList<AtomLink>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;

	@OneToMany(mappedBy = "group")
	@JsonView({ JsonViews.Group.class })
	private List<User> users = new ArrayList<User>();
	private String color;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonView({ JsonViews.Group.class })
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<AtomLink> getLinks() {
		return links;
	}

	public void setLinks(List<AtomLink> links) {
		this.links = links;
	}

}
