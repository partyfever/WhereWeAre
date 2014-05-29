package rest;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


/**
 * {@link AuthenticationEntryPoint} that rejects all requests with an unauthorized error message.
 * 

 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String url=request.getRequestURL().toString();
		String[] urlParts=url.split("/rest");
		if(urlParts.length==0)
			response.sendError(401);
		else {
			url = urlParts[0] + "/rest/user/unauthorized";
			response.sendRedirect(url);
		}
		
		
	}
}