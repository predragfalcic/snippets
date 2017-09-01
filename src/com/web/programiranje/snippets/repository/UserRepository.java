package com.web.programiranje.snippets.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.web.programiranje.snippets.model.User;
import com.web.programiranje.snippets.util.BCrypt;
import com.web.programiranje.snippets.util.JsonWebTokenImpl;
import com.web.programiranje.snippets.util.ReadWriteFile;

public class UserRepository {
	
	private ReadWriteFile rwf = new ReadWriteFile(); 
	private ArrayList<User> users = new ArrayList<>();
	
	/**
	 * Default constructor
	 */
	public UserRepository(){}
	
	/**
	 * Register the user, save him to file
	 * @param u
	 * @return
	 */
	public void register(User u){
		
		System.out.println("register(User u)");
		
		// Hash a password
        String hashed = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
		
        u.setPassword(hashed);
        u.setRole("regUser");
		users.add(u);
		
		rwf.writeUserToFile(users);
		
	}
	
	public JSONObject login(String username, String password){
		System.out.println("login(username, password)");
		try {
			readFromFile();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject jo = new JSONObject();
		
		// Find user by his username
		User foundUser = findUserByUsername(username);
		if(foundUser == null){
			jo.put("error", "User with that username does not exist.");
			System.out.println("User with that username does not exist.");
			return jo;
		}
		
		// If user is found check if password is correct
		if(!BCrypt.checkpw(password, foundUser.getPassword())){
			jo.put("error", "Incorrect password");
			System.out.println("Incorrect password");
			return jo;
		}
		
		String token = JsonWebTokenImpl.createJWT(foundUser.getUsername(), foundUser.getRole(), foundUser.getStatus(), foundUser.getProfileImage());
		
		// If user is found and password is correct
		jo.put("token", token);
		
		return jo;
	}
	
	/**
	 * Set users token
	 * @param username
	 * @param token
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void setUsersToken(String username, String token) throws FileNotFoundException, ClassNotFoundException, IOException{
		for (User user : users) {
			if(user.getUsername().equals(username)){
				user.setToken(token);
			}
		}
		rwf.writeUserToFile(users);
		users = rwf.readUserFromFile();
	}
	
	/**
	 * Return all users
	 * @return
	 */
	public ArrayList<User> getUsers(){
		return users;
	}
	
	/**
	 * Find user by his username
	 * @param username
	 * @return
	 */
	public User findUserByUsername(String username){
		for (User user : users) {
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Find user by his email address
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email){
		for (User user : users) {
			if(user.getEmail().equals(email)){
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Read all data from file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void readFromFile() throws ClassNotFoundException, IOException{
		users = rwf.readUserFromFile();
	}
	
	/**
	 * Save users profile picture
	 * @param uploadedInputStream
	 * @param serverLocation
	 */
	public void saveFile(InputStream uploadedInputStream, String serverLocation) {

        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            outpuStream = new FileOutputStream(new File(serverLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
	
	/**
	 * Return all users if logged user is admin
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<User> getAllRegUsers(HttpServletRequest request) throws FileNotFoundException, ClassNotFoundException, IOException{
		
		String role = JsonWebTokenImpl.parseRequest(request, "role");
		
		System.out.println("User sa rolom: " + role + " zeli sve korisnike da vidi");
		
		if(!role.equalsIgnoreCase("admin")){
			return null;
		}
		
		users.clear();
		users = rwf.readUserFromFile();
		
		ArrayList<User> regUsers = new ArrayList<>();
		
		for (User user : users) {
			System.out.println(user.toString());
			if(user.getRole().equals("regUser")){
				regUsers.add(user);
			}
		}
		
		return regUsers;
	}
	
	public String blockUser(String username, HttpServletRequest request) throws FileNotFoundException, ClassNotFoundException, IOException{
		String role = JsonWebTokenImpl.parseRequest(request, "role");
		
		System.out.println("User sa rolom: " + role + " zeli da blokira korisnika sa username " + username);
		
		if(!role.equalsIgnoreCase("admin")){
			System.out.println("Nije admin");
			return null;
		}
		
		users = rwf.readUserFromFile();
		
		for (User user : users) {
			if(user.getUsername().equals(username)){
				if(user.getStatus().equals("Blocked")){
					return "User is already blocked";
				}else{
					user.setStatus("Blocked");
				}
			}
		}
		
		rwf.writeUserToFile(users);
		
		for (User user : users) {
			System.out.println("Users status after block " + user.getStatus());
		}
		
//		users.clear();
//		users = rwf.readUserFromFile();
		
		return "OK";
	}
	
	public String unblockUser(String username, HttpServletRequest request) throws FileNotFoundException, ClassNotFoundException, IOException{
		String role = JsonWebTokenImpl.parseRequest(request, "role");
		
		System.out.println("User sa rolom: " + role + " zeli da odblokira korisnika sa username " + username);
		
		if(!role.equalsIgnoreCase("admin")){
			System.out.println("Nije admin");
			return null;
		}
		
		users = rwf.readUserFromFile();
		
		for (User user : users) {
			if(user.getUsername().equals(username)){
				if(user.getStatus().equals("Active")){
					return "User is already active";
				}else{
					user.setStatus("Active");
				}
			}
		}
		
		rwf.writeUserToFile(users);
		
		for (User user : users) {
			System.out.println("Users status after unblock " + user.getStatus());
		}
		
//		users.clear();
//		users = rwf.readUserFromFile();
		
		return "OK";
	}
}


















