package rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import manager.UserManager;
import manager.UserManagerImpl;
import model.Group;
import model.User;

import org.codehaus.jackson.map.annotate.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rest.UriHelper;
import rest.exceptions.Error;
import rest.exceptions.JsonWebApplicationException;
import security.SecurityHelper;
import transfer.UserTransfer;
import dao.UserDAO;
import eli.JsonViews;

/**
 * UserResource
 * 
 * @author Sebastian
 * 
 */
@Component
@Path("/users")
public class UserResource {

	@Autowired
	private UserDetailsService userService;
	@Autowired
	private UserDAO userDao;

	private UserManager userManager = new UserManagerImpl();
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Context
	UriInfo uriInfo;

	/**
	 * Get all Users
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ JsonViews.User.class })
	public List<User> getUsers() {
		List<User> users = this.userManager.getUsers();
		for (User u : users) {
			UriHelper.addUserLinks(uriInfo, u);
		}
		return users;
	}

	/**
	 * Get single user by id
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ JsonViews.User.class })
	@Path("{id}")
	public User getUser(@PathParam("id") Integer id) {
		User user = this.userManager.find(id);
		if (user == null) {
			throw new WebApplicationException(404);
		}
		return UriHelper.addUserLinks(uriInfo, user);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ JsonViews.User.class })
	public User create(User userEntry) {
		// Check all params are submitted
		if (UriHelper.isNullOrEmpty(userEntry.getName())||userEntry.getGroupId()==0
				|| UriHelper.isNullOrEmpty(userEntry.getPassword())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.WRONG_FORM_PARAMS,
					"Please provide name, password!");
		}
		// Check if user exists already
		if (this.userDao.checkUserExists(userEntry.getUsername())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.DUPLICATE_USER);
		}
		// Create user
		userEntry.setId(0);
		userEntry.addRole("user");
		userEntry.setPassword(this.passwordEncoder.encode(userEntry
				.getPassword()));
		Group group=new Group();
		group.setId(userEntry.getGroupId());
		this.userManager.addUser(userEntry);
		return userEntry;
	}

	@GET
	@Path("unauthorized")
	@Produces(MediaType.APPLICATION_JSON)
	public UserTransfer unauthorized() {
		throw new JsonWebApplicationException(401, Error.UNAUTHORIZED);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	@JsonView({ JsonViews.NoDetail.class })
	public User update(@PathParam("id") Integer id, User user) {
	

		if (id == null || (user.getId() != 0 && id != user.getId())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.WRONG_RESSOURCE,
					"name and/or color not provided");
		}
		User authUser = SecurityHelper.getUser();
		if (authUser.getId() != id) {
			throw new JsonWebApplicationException(404, Error.UNAUTHORIZED,
					"Cannot change location of different user.");
		}
		User dbUser = this.userManager.find(id);
		dbUser.setLat(user.getLat());
		dbUser.setLng(user.getLng());
		this.userManager.addUser(dbUser);
		return user;
	}

}
