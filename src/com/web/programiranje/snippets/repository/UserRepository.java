package com.web.programiranje.snippets.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.web.programiranje.snippets.model.User;
import com.web.programiranje.snippets.util.ReadWriteFile;

public class UserRepository {
	
	private ReadWriteFile rwf = new ReadWriteFile(); 
	private List<User> users = new ArrayList<>();
	
	/**
	 * Register the user, save him to file
	 * @param u
	 * @return
	 */
	public void register(User u){
		users.add(u);
		
		rwf.writeUserToFile(users);
		
	}
	
	/**
	 * Return all users
	 * @return
	 */
	public List<User> getUsers(){
		return users;
	}
	
	public User findUserByUsername(String username){
		for (User user : users) {
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}
	
	public User findUserByEmail(String email){
		for (User user : users) {
			if(user.getEmail().equals(email)){
				return user;
			}
		}
		return null;
	}
	
	public void readFromFile() throws ClassNotFoundException, IOException{
		users = rwf.readUserFromFile();
	}
}


















