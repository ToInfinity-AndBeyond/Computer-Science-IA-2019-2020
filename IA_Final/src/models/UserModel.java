package models;

public class UserModel {
	private String username;
	private String passwd;
	private String type;
	private String name;

	public UserModel() {
	}

	public UserModel(String username, String passwd, String name, String type) {
		this.username = username;
		this.passwd = passwd;
		this.name = name;
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
