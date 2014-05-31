package security;

import model.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import rest.exceptions.Error;
import rest.exceptions.JsonWebApplicationException;

/**
 * Helper for getting the current authenticated user 
 * based on the provided auth token
 *
 */
public class SecurityHelper {

	public static User getUser(){
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String
				&& ((String) principal).equals("anonymousUser")) {
			throw new JsonWebApplicationException(401,Error.UNAUTHORIZED);
		}
		User userDetails = (User) principal;
		return userDetails;
	}
}
