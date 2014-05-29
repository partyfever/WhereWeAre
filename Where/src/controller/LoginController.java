package controller;

import helper.FacesUtils;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import security.SaltedSHA256PasswordEncoder;

import com.icesoft.faces.context.effects.JavascriptContext;

/**
 * The LoginController provides capabilities for authenticating users 
 */
@ManagedBean(name = "loginController")
@ViewScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = -8505211480153505833L;

	private String userName;
	private String password;
	
	private PasswordEncoder passwordEncoder;
	public LoginController(){
		try {
			this.passwordEncoder=new SaltedSHA256PasswordEncoder("secret");
		} catch (NoSuchAlgorithmException e) {
			
		}
	}
	/**
	 * Action Method called after Login Button pressed in a Login Dialog
	 * 
	 * @return
	 */
	public String login() {
		final SecurityController sc = (SecurityController) FacesUtils
				.getManagedBean("securityController");
		// Try to login in user with given user/password credentials
		final String pwd=this.passwordEncoder.encode(password);
		if (!sc.logInUser(userName, pwd)) {
			FacesUtils.addErrorMessage("Falsche User/Passwort Kombination!");
		} else {
			JavascriptContext.addJavascriptCall(
					FacesContext.getCurrentInstance(), "loginDialog.hide();");
			FacesUtils.getMapViewController().updateUser(sc.getCurUser());
		}
		// reset password
		password = "";
		return "";
	}

	/***
	 * GETTER AND SETTER
	 ***/
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

}
