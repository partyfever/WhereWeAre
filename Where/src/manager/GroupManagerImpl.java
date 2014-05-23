package manager;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import model.Group;
import dao.GroupDAO;

/**
 * Implementation of GroupManager which access 
 * the Hibernate GroupDAO
 * @author Sebastian
 *
 */
@ManagedBean(name = "groupManager")
@ApplicationScoped
public class GroupManagerImpl implements GroupManager {

	private GroupDAO dao = new GroupDAO();

	public List<Group> getGroups() {
		return dao.getAll();
	}
	
	public Group getGroupByName(String name){
		return dao.getGroupByName(name);
	}

	public void createGroup(Group group) {
		dao.save(group);
	}

	@Override
	public Group getGroupById(Integer id) {
		return dao.find(id);
	}

	
	

}
