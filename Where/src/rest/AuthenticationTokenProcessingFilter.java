package rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter which processe an provided token and handels authentication
 *
 *
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private final UserDetailsService userService;

	public AuthenticationTokenProcessingFilter(UserDetailsService userService) {

		this.userService = userService;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		HttpServletResponse httpResponse = this.getAsHttpResponse(response);
		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String userName = TokenUtils.getUserNameFromToken(authToken);
		try {
			if (userName != null) {

				UserDetails userDetails = this.userService
						.loadUserByUsername(userName);

				if (userDetails != null
						&& TokenUtils.validateToken(authToken, userDetails)) {

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication
							.setDetails(new WebAuthenticationDetailsSource()
									.buildDetails(httpRequest));
					SecurityContextHolder.getContext().setAuthentication(
							authentication);
				}
			}

			chain.doFilter(request, response);
		} catch (UsernameNotFoundException unfe) {
			chain.doFilter(request, response);
		} catch (Exception ex) {
			chain.doFilter(request, response);
		}
	}

	private HttpServletRequest getAsHttpRequest(ServletRequest request) {

		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}

	private HttpServletResponse getAsHttpResponse(ServletResponse request) {

		if (!(request instanceof HttpServletResponse)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletResponse) request;
	}

	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {

		/* Get token from header */
		String authToken = httpRequest.getHeader("X-Auth-Token");

		/* If token not found get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}

		return authToken;
	}
}
