package rest.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import manager.GroupManager;
import manager.GroupManagerImpl;
import model.Group;
import model.User;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rest.UriHelper;
import rest.exceptions.Error;
import rest.exceptions.JsonWebApplicationException;
import eli.JsonViews;

/**
 * GroupResource
 * 
 * @author Sebastian
 * 
 */
@Component
@Path("/groups")
public class GroupResource {

	private GroupManager groupManager = new GroupManagerImpl();
	@Autowired
	private ObjectMapper mapper;
	@Context
	UriInfo uriInfo;

	/**
	 * GET /groups Groups without users
	 * 
	 * @param showUser
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ JsonViews.NoDetail.class })
	public List<Group> getGroups() {
		return this.getAllGroupsWithLinks();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}/users")
	@JsonView({ JsonViews.User.class })
	public List<User> readUsers(@PathParam("id") Integer id) {
		Group groupEntry = this.groupManager.getGroupById(id);
		if (groupEntry == null) {
			throw new JsonWebApplicationException(404,
					Error.RESSOURCE_NOT_FOUND);
		}
		List<User> users = groupEntry.getUsers();
		for (User user : users) {
			UriHelper.addUserLinks(uriInfo, user);
		}
		return users;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public String read(@PathParam("id") String path)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectWriter viewWriter;
		Integer id = null;
		try {
			id = Integer.parseInt(path);
		} catch (Exception e) {
		}
		if (id != null) {
			Group groupEntry = this.groupManager.getGroupById(id);
			if (groupEntry == null) {
				throw new JsonWebApplicationException(404,
						Error.RESSOURCE_NOT_FOUND);
			}
			viewWriter = this.mapper.writerWithView(JsonViews.NoDetail.class);
			UriHelper.addGroupLinks(uriInfo, groupEntry);
			return viewWriter.writeValueAsString(groupEntry);
		} else {
			viewWriter = this.mapper.writerWithView(JsonViews.Group.class);
			return viewWriter.writeValueAsString(this.getAllGroupsWithLinks());
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ JsonViews.NoDetail.class })
	public Group create(Group group) {
		if (UriHelper.isNullOrEmpty(group.getName())
				|| UriHelper.isNullOrEmpty(group.getColor())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.WRONG_FORM_PARAMS,
					"name and/or color not provided");
		}
		this.groupManager.createGroup(group);
		UriHelper.addGroupLinks(uriInfo, group);
		return group;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	@JsonView({ JsonViews.NoDetail.class })
	public Group update(@PathParam("id") Integer id, Group group) {
		if (UriHelper.isNullOrEmpty(group.getName())
				|| UriHelper.isNullOrEmpty(group.getColor())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.WRONG_FORM_PARAMS,
					"name and/or color not provided");
		}

		if (id == null || (group.getId() != 0 && id != group.getId())) {
			throw new JsonWebApplicationException(400,
					rest.exceptions.Error.WRONG_RESSOURCE,
					"name and/or color not provided");
		}
		if (group.getId() == 0) {

			group.setId(id);
		}

		this.groupManager.createGroup(group);
		UriHelper.addGroupLinks(uriInfo, group);
		return group;
	}

	private List<Group> getAllGroupsWithLinks() {
		List<Group> groups = this.groupManager.getGroups();
		for (Group g : groups) {
			UriHelper.addGroupLinks(uriInfo, g);
		}
		return groups;
	}

}
