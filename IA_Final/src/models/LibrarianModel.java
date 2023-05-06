package models;

public class LibrarianModel extends UserModel {
	public LibrarianModel(String username, String passwd, String name) {
		super(username, passwd, name, "librarian");
	}
}
