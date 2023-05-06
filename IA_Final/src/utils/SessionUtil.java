package utils;

import java.util.ArrayList;

import interfaces.MainInterface;

public class SessionUtil {
	private String username = "";
	private String name = "";
	private String type = "";
	private boolean isLogin;

	private ArrayList<String> freeBlockList = new ArrayList<>();

	public SessionUtil() {
	}

	public void login(String username, String name, String type) {
		this.username = username;
		this.name = name;
		this.type = type;
		isLogin = true;

		MainInterface.lbName.setText(String.format("Welcome, %s!", name));
		MainInterface.lbType.setText(String.format("You are login as %s.", type));
	}

	public void logout() {
		username = "";
		name = "";
		type = "";
		isLogin = false;
		freeBlockList.clear();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public ArrayList<String> getFreeBlockList() {
		return freeBlockList;
	}

	public void setFreeBlockList(ArrayList<String> freeBlockList) {
		this.freeBlockList = freeBlockList;
	}
}
