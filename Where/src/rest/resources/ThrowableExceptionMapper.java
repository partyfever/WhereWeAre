package rest.resources;

import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import rest.exceptions.Error;
import rest.exceptions.JsonWebApplicationException;
import transfer.TextWebApplicationException;

/**
 * This class is responsible for catching all different kind of exceptions and transform 
 * it into JsonWebApplicationException, which contains a JSON as response
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger log = Logger
			.getLogger(ThrowableExceptionMapper.class);
	@Context
	HttpServletRequest request;

	@Override
	public Response toResponse(Throwable t) {

	t.printStackTrace();

		if (t instanceof JsonWebApplicationException) {
			return ((JsonWebApplicationException) t).getResponse();
		} else if (t instanceof WebApplicationException) {
			WebApplicationException ex = (WebApplicationException) t;
			String detailMessage=t.getMessage();
			if(detailMessage==null){
				detailMessage="Please check the HTTP Response Status.";
			}
			JsonWebApplicationException json = new JsonWebApplicationException(
					ex.getResponse().getStatus(), Error.WEB_SERVICE_GENERAL,
					detailMessage);
			return json.getResponse();
		} else if (t instanceof javax.persistence.PersistenceException) {
			JsonWebApplicationException json = new JsonWebApplicationException(
					Status.BAD_REQUEST.getStatusCode(), Error.DATABASE_UNKNOWN,
					t.getLocalizedMessage());
		
			return json.getResponse();
		} else {
			JsonWebApplicationException json = new JsonWebApplicationException(
					Status.INTERNAL_SERVER_ERROR.getStatusCode(),Error.UNKNOWN,
					t.getLocalizedMessage());
		
			return json.getResponse();
		}
	}

	private String buildErrorMessage(HttpServletRequest req) {
		StringBuilder message = new StringBuilder();
		String entity = "(empty)";

		try {
			// How to cache getInputStream:
			// http://stackoverflow.com/a/17129256/356408
			InputStream is = req.getInputStream();
			// Read an InputStream elegantly:
			// http://stackoverflow.com/a/5445161/356408
			Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
			entity = s.hasNext() ? s.next() : entity;
		} catch (Exception ex) {
			// Ignore exceptions around getting the entity
		}

		message.append("Uncaught REST API exception:\n");
		message.append("URL: ").append(getOriginalURL(req)).append("\n");
		message.append("Method: ").append(req.getMethod()).append("\n");
		message.append("Entity: ").append(entity).append("\n");

		return message.toString();
	}

	private String getOriginalURL(HttpServletRequest req) {
		// Rebuild the original request URL:
		// http://stackoverflow.com/a/5212336/356408
		String scheme = req.getScheme(); // http
		String serverName = req.getServerName(); // hostname.com
		int serverPort = req.getServerPort(); // 80
		String contextPath = req.getContextPath(); // /mywebapp
		String servletPath = req.getServletPath(); // /servlet/MyServlet
		String pathInfo = req.getPathInfo(); // /a/b;c=123
		String queryString = req.getQueryString(); // d=789

		// Reconstruct original requesting URL
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}

		if (queryString != null) {
			url.append("?").append(queryString);
		}

		return url.toString();
	}
}
