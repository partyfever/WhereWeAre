package manager;

import model.User;

/**
 * UserManager for accessing Users 

 *
 */
public interface UserManager {
	/**
	 * Check if a user with the name userName exists
	 * @param userName The name to check.
	 * @return true, if userName exists already, otherwise false
	 */
	public boolean checkUserExists(String userName);
	/**
	 * Add a user to the dao
	 * @param user USer to add
	 * @return true, if user was successfully inserted
	 */
	public boolean addUser(User user);
	/**
	 * Try to login a user with the name userName and password password
	 * @param userName 
	 * @param password
	 * @return The authenticated USer OBject. 
	 */
	public User logInUser(String userName, String password);
	
	/**
	 * Update the current latitude/longitude of User user
	 * @param user 
	 * @param lat
	 * @param lng
	 * @return Updated User Object
	 */
	public User updateUserLocation(User user, Double lat, Double lng);
}
