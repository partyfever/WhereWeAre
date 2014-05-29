package controller;

import helper.FacesUtils;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import manager.UserManager;
import model.User;

/**
 * The SecurityController is a session scoped controller, 
 * which is responsible for authenticating a user. After login, 
 * the SecurityController holds a reference to the current user. 
 * Since the controller is session scoped, it is auto-deleted after 
 * the session expires. 
 */
@ManagedBean(name = "securityController")
@SessionScoped
public class SecurityController implements Serializable {

	private static final long serialVersionUID = -1442125383815393808L;
	//Authenticated User
	private User curUser=null;
	
	/** 
	 * Check if a user is logged in
	 * @return true, if user is logged in
	 */
	public boolean isLoggedIn(){
		return curUser!=null;
	}
	
	/**
	 * Log in a user with name user and password password
	 * @param user	Username
	 * @param password	Password for User user
	 * @return true, if user was logged in
	 */
	boolean logInUser(String user, String password){
		UserManager um=FacesUtils.getUserManager();
		this.curUser=um.logInUser(user, password);
		return isLoggedIn();
	}
	
	/**
	 * Logout the current user
	 */
	public void logout(){
		this.curUser=null;
		
	}

	/**
	 * Get the current user
	 * @return
	 */
	public User getCurUser() {
		return curUser;
	}
}
