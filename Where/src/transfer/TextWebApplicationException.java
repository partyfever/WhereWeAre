package transfer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TextWebApplicationException extends WebApplicationException{
	  public TextWebApplicationException(Response.Status status,String message) {
	         super(Response.status(status)
	             .entity(message).type(MediaType.TEXT_PLAIN).build());
	     }
}
