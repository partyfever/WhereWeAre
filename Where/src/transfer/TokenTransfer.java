package transfer;


/**
 * Transfer the user token belonging to a user together
 * with the expiration date. 
 * 
 * @author Sebastian
 *
 */
public class TokenTransfer {

	private final String token;
	private final long expireDate;

	private final int id;
	
	public TokenTransfer(int id,String token, long expireDate) {
		this.id=id;
		this.token = token;
		this.expireDate=expireDate;
	}


	public String getToken() {

		return this.token;
	}


	public long getExpireDate() {
		return expireDate;
	}


	public int getId() {
		return id;
	}

}
