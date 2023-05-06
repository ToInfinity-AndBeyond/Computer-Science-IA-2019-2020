package managers;

import launcher.Launcher;
import models.UserModel;
import utils.FileUtil;

public class UserManager {
	public static int findUser(String username) {
		int index = 0;
		for (UserModel user : Launcher.userList) {
			if (user.getUsername().equals(username)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public static boolean isMatched(String username, String passwd) {
		int index = findUser(username);
		if (index > -1) {
			if (Launcher.userList.get(index).getPasswd().equals(passwd)) {
				UserModel user = Launcher.userList.get(index);
				Launcher.sessionUtil.login(username, user.getName(), user.getType());
				FileUtil.readFreeBlockFile(username);
				return true;
			}
		}
		return false;
	}
}
