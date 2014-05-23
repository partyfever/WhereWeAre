package rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import model.AtomLink;
import model.Group;
import model.User;
import rest.resources.GroupResource;
import rest.resources.UserResource;

public class UriHelper {

	/**
	 * Add user depending links
	 * 
	 * @param user
	 * @return
	 */
	public static User addUserLinks(UriInfo uriInfo,User user) {
		UriBuilder builder = uriInfo.getBaseUriBuilder();
		builder.path(UserResource.class);
		builder.path(UserResource.class, "getUser");
		user.getLinks().add(
				new AtomLink("self", builder.build(user.getId()).toString()));
		builder = uriInfo.getBaseUriBuilder();
		builder.path(GroupResource.class);
		builder.path(GroupResource.class, "read");

		if (user.getGroup() != null) {
			user.setGroupId(user.getGroup().getId());
			user.getLinks().add(
					new AtomLink("group", builder
							.build(user.getGroup().getId()).toString()));
		}
		return user;
	}
	/**
	 * Check if the String toCheck is null or has an length of 0
	 * 
	 * @param toCheck
	 * @return true, if toCheck is null or has an length of 0
	 */
	public static boolean isNullOrEmpty(String toCheck) {
		if (toCheck == null || toCheck.length() == 0)
			return true;
		return false;
	}

	public static Group addGroupLinks(UriInfo uriInfo,Group group) {
		UriBuilder builder = uriInfo.getBaseUriBuilder();
		builder.path(GroupResource.class);
		builder.path(GroupResource.class, "read");
		group.getLinks().add(
				new AtomLink("self", builder.build(group.getId()).toString()));
		builder=uriInfo.getBaseUriBuilder();
		builder=builder.path(GroupResource.class);
		builder.path(GroupResource.class, "readUsers");
		group.getLinks().add(
				new AtomLink("users", builder.build(group.getId()).toString()));
	
		return group;
	}
}
