package transfer;

import java.util.Set;


public class UserTransfer {

	private final String name;

	private final Set<String >roles;


	public UserTransfer(String userName, Set<String> roles) {

		this.name = userName;
		this.roles = roles;
	}


	public String getName() {

		return this.name;
	}


	public Set<String> getRoles() {

		return this.roles;
	}

}
