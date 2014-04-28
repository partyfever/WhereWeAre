package controller;

import helper.FacesUtils;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import manager.GroupManager;
import manager.UserManager;
import model.Group;
import model.User;

import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * RegisterController is used for registering a new User
 */
@ManagedBean(name = "registerController")
@ViewScoped
public class RegisterController implements Serializable {

	private final GroupManager groupManager = FacesUtils.getGroupManager();
	private static final long serialVersionUID = 6735137016352950088L;

	private String selectedItem;
	private User user = new User();
	private String userName;
	private String password;
	private String password2;

	private final List<Group> groups;

	/**
	 * Init view with available groups
	 */
	public RegisterController() {
		groups = groupManager.getGroups();
	}

	/**
	 * Action method, which checks, if all necessary information was entered
	 * 
	 * @return Action outcome ""
	 */
	public String register() {

		if (!password.equals(password2)) {
			FacesUtils.addErrorMessage("Passwort stimmt nicht überein. ");
		} else if (password.length() < 3) {
			FacesUtils
					.addErrorMessage("Passwort muss mindestens 3 Zeichen lang sein");
		} else {
			final UserManager um = FacesUtils.getUserManager();
			// Check if user exists
			if (um.checkUserExists(this.userName)) {
				FacesUtils.addErrorMessage("User existiert bereits");
			} else {
				final User user = new User();
				final Group group = groupManager
						.getGroupByName(this.selectedItem);
				user.setName(this.userName);
				user.setPassword(this.password);
				user.setGroup(group);
				um.addUser(user);
				final SecurityController sc = FacesUtils
						.getSecurityController();
				sc.logInUser(this.userName, password);
				FacesUtils.getMapViewController().updateUser(sc.getCurUser());
				//Hide dialog
				JavascriptContext.addJavascriptCall(
						FacesContext.getCurrentInstance(),
						"registerDialog.hide();");
			}
		}
		return "";
	}
	/***
	 * GETTER AND SETTER
	 ***/
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<Group> getGroups() {
		return groups;
	}

}
