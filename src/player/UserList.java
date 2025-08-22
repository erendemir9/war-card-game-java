package player;

import java.util.ArrayList;
import java.util.List;

public class UserList {
	private List<User> users;

	// constructor
	public UserList() {
		this.users = new ArrayList<>();
	}

	// getting users
	public List<User> getUsers() {
		return this.users;
	}

	// setting users
	public void setUsers(List<User> users) {
		this.users = users;
	}

	// adding user to the list
	public void addUser(User user) {
		this.users.add(user);
	}

	// if id exists in user list
	public boolean idExists(String id) {
		for (User u : users) {
			if (u.getId() != null && u.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	// printing users in console
	public void printUsers() {
		for (User u : getUsers()) {
			System.out.println("username: " + u.getId() + " | Name: " + u.getName());
		}
	}
}