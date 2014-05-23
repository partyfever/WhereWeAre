package transfer;



public class TokenTransfer {

	private final String token;
	private final long expireDate;

	public TokenTransfer(String token, long expireDate) {

		this.token = token;
		this.expireDate=expireDate;
	}


	public String getToken() {

		return this.token;
	}


	public long getExpireDate() {
		return expireDate;
	}

}
