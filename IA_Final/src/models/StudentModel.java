package models;

public class StudentModel extends UserModel {
	public StudentModel(String username, String passwd, String name) {
		super(username, passwd, name, "student");
	}
}
