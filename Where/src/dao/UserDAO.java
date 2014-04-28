package dao;

import helper.HibernateUtil;

import org.hibernate.Query;

import model.User;

/**
 * UserDAO is responsible for storing Group Objects
 */
public class UserDAO extends GenericDAOImpl<User,Integer>{

	/**
	 * Check if a user exists in the db with the name userName
	 * @param userName Name of the user 
	 * @return true, if db contains entry with username userName
	 */
	public boolean checkUserExists(String userName) {
		final Query query = HibernateUtil.getSession().createQuery(
				" select count(*) from User where name=:uName");
		query.setString("uName", userName);
		final Number n = (Number) query.uniqueResult();
		if (n != null && n.intValue() == 0)
			return false;
		return true;	
	}

	/**
	 * Check if provided userName and password is correct
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean checkUserCredentials(String userName, String password) {
		final Query query = HibernateUtil.getSession().createQuery(
				" select count(*) from User where name=:uName and password=:password");
		query.setString("uName", userName);
		query.setString("password", password);
		final Number n = (Number) query.uniqueResult();
		if (n != null && n.intValue() == 0)
			return false;
		return true;
	}

	/**
	 * Retrieve a user by username
	 * @param userName
	 * @return
	 */
	public User getUserByName(String userName) {
		final Query query = HibernateUtil.getSession().createQuery(
				" from User u where  u.name=:username");

		query.setString("username", userName);

		return super.findOne(query);
	}

	/**
	 * Update the location of the User user to lat and lng
	 * @param user
	 * @param lat
	 * @param lng
	 * @return updated user object
	 */
	public User updateUserLocation(User user,Double lat,Double lng) {
		final User curUser=this.load(user.getId());
		curUser.setLat(lat);
		curUser.setLng(lng);
		this.save(curUser);
		return curUser;
		
	}

}
