package rest.exceptions;

/**
 * Enum containing error codes for json error messages

 *
 */
public enum Error {
	WEB_SERVICE_GENERAL(0, "Folgender Fehler ist beim Aufruf entstanden:"), 
	DATABASE(
			1, "A database error has occured."), 
	WRONG_FORM_PARAMS(2,
			"Missing required parameters!"),
	// DBErrors
	DUPLICATE_USER(400, "This user already exists."), 
	UNAUTHORIZED(401,
			"Unauthorized access. Please provide a valid authentication token."), 
	DATABASE_UNKNOWN(
			1, "A general database error has occured."), 
	RESSOURCE_NOT_FOUND(
			404, "Ressource does not exist."), 
	
	EDITOR_OR_ADMIN(401,"Editor or admin access neccessary."), 
	UNKNOWN(500,"Unbekannter Fehler aufgetreten!"), WRONG_URL_PARAMS(3,"URL parameter is not valid."), 
	USER_DOES_NOT_EXIST(407,"User does not exist in db."), UNKNOWN_ERROR_AUTH(501,"Unkown error during authentication."), WRONG_RESSOURCE(409,"You are trying to update a ressource, but posted the representation of another ressource");

	private final int code;
	private final String description;

	private Error(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return code + ": " + description;
	}

}
