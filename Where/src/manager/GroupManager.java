package manager;
import java.util.List;

import model.Group;

/**
 * GroupManager for accessing groups
 */
public interface GroupManager {
	/**
	 * Get all Groups from the server 
	 * @return all groups
	 */
	public List<Group> getGroups();
	/**
	 * Query a singe group from the server
	 * @param name name of the group
	 * @return Group with name or null, if group does not exist
	 */
	public Group getGroupByName(String name);
	/**
	 * Create a new Group 
	 * @param group Group to create
	 */
	public void createGroup(Group group);
}
