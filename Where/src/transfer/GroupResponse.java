package transfer;

import java.util.List;

import model.Group;

public class GroupResponse {

	private List<Group> groups;
	public GroupResponse(){
		
	}
	public GroupResponse(List<Group> groups) {
		super();
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
}
