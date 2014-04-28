package manager;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.User;
import dao.UserDAO;

@ManagedBean(name = "userManager")
@SessionScoped
public class UserManagerImpl implements UserManager {

	private UserDAO userDAO = new UserDAO();

	public boolean addUser(User user) {
		if (user != null) {
			userDAO.save(user);
			return true;
		}
		return false;
	}

	public User updateUserLocation(User user, Double lat, Double lng) {
		if (user != null) {
			return userDAO.updateUserLocation(user, lat, lng);
		}
		return null;
	}

	public boolean checkUserExists(String userName) {
		return userDAO.checkUserExists(userName);
	}

	public User logInUser(String userName, String password) {
		if (userDAO.checkUserCredentials(userName, password)) {
			final User user = userDAO.getUserByName(userName);
			
			return user;
		}
		return null;
	}

}
