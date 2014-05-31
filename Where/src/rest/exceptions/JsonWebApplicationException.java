package rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Exception which renders error messages as json 
 * @author Sebastian
 *
 */
public class JsonWebApplicationException extends WebApplicationException {

	public JsonWebApplicationException(int status, int errorCode, String message) {
		super(Response.status(status).entity(getErrorJson(errorCode, message))
				.type(MediaType.APPLICATION_JSON).build());

	}

	public JsonWebApplicationException(int status, Error error) {
		this(status, error.getCode(), error.getDescription());
	}

	public JsonWebApplicationException(int status, Error error,
			String additionalMessage) {
		this(status, error.getCode(), error.getDescription() + "\n"
				+ additionalMessage);
	}

	private static JSONObject getErrorJson(int errorCode, String message) {
		JSONObject json = new JSONObject();
		try {
			json.put("errorCode", errorCode);
			json.put("message", message);

		} catch (JSONException e) {

		}
		return json;
	}
}
