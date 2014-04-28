package dao;

import helper.HibernateUtil;

import java.util.List;

import org.hibernate.Query;


import model.Group;

/**
 * GroupDAO is responsible for storing Group Objects
 */
public class GroupDAO extends GenericDAOImpl<Group,Integer>{
	/**
	 * Return all Groups
	 * @return List of all Groups
	 */
	public List<Group> getAll() {
		final Query query = HibernateUtil.getSession().createQuery(
				" from Group ");
		return super.findMany(query);
	}

	/**
	 * Get a Group by name
	 * @param name	Name of the group
	 * @return Single Group by name
	 */
	public Group getGroupByName(String name) {
		final Query query = HibernateUtil.getSession().createQuery(
				" from Group u where  u.name=:name");
		query.setString("name", name);
		return super.findOne(query);
	}
}
