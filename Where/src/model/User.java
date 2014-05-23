package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElementRef;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eli.JsonViews;

/**
 * User POJO
 */
@Entity
@Table
@JsonAutoDetect
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User implements Serializable, UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5571859345586343507L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	@JsonIgnore
	private Group group;

	private String name;

	@JsonView({ JsonViews.Detail.class })
	private String password;

	private Double lat;

	private Double lng;

	@XmlElementRef
	@Transient
	private List<AtomLink> links = new ArrayList<AtomLink>();

	@Transient
	@JsonView({ JsonViews.User.class })
	
	private int groupId;

	public User() {

		/* Reflection instantiation */
	}

	public User(String name, String passwordHash) {

		this.name = name;
		this.password = passwordHash;
	}

	@javax.xml.bind.annotation.XmlTransient
	@ElementCollection(fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<String> roles = new HashSet<String>();

	@JsonIgnore
	public Set<String> getRoles() {

		return this.roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@JsonIgnore
	public Group getGroup() {
		return group;
	}

	@JsonIgnore
	public void setGroup(Group group) {
		this.group = group;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<String> roles = this.getRoles();

		if (roles == null) {
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}

	@Override
	@JsonIgnore
	public String getUsername() {

		return this.name;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {

		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {

		return true;
	}

	@JsonIgnore
	public void addRole(String role) {

		this.roles.add(role);
	}
	
	public List<AtomLink> getLinks() {
		return links;
	}

	public void setLinks(List<AtomLink> links) {
		this.links = links;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
