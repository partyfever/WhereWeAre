package rest.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import manager.UserManager;
import manager.UserManagerImpl;
import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rest.TokenUtils;
import rest.exceptions.Error;
import rest.exceptions.JsonWebApplicationException;
import transfer.TokenTransfer;
import transfer.UserTransfer;
import dao.UserDAO;

/**
 * UserResource
 * 
 * @author Sebastian
 * 
 */
@Component
@Path("/authentication")
public class AuthenticationResource {

	@Autowired
	private UserDetailsService userService;
	@Autowired
	private UserDAO userDao;

	private UserManager userManager = new UserManagerImpl();
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Context
	UriInfo uriInfo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserTransfer getUser() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String
				&& ((String) principal).equals("anonymousUser")) {
			throw new JsonWebApplicationException(401, Error.UNAUTHORIZED);
		}
		User userDetails = (User) principal;

		return new UserTransfer(userDetails.getUsername(),
				userDetails.getRoles());
	}
	
	/**
	 * Authenticates a user and creates an authentication token.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A transfer containing the authentication token.
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public TokenTransfer authenticate(User user) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user.getName(), user.getPassword());
		Authentication authentication = this.authManager
				.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		 * Reload user as password of authentication principal will be null
		 * after authorization and password is needed for token generation
		 */
		UserDetails userDetails = this.userService.loadUserByUsername(user.getName());
		String token = TokenUtils.createToken(userDetails);
		long expires = TokenUtils.expireDate(token, userDetails);
		return new TokenTransfer(token, expires);
	}



}
