package com.web.programiranje.snippets.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import com.web.programiranje.snippets.model.Language;
import com.web.programiranje.snippets.model.Snippet;
import com.web.programiranje.snippets.model.User;
import com.web.programiranje.snippets.repository.LanguageRepository;
import com.web.programiranje.snippets.repository.SnippetRepository;
import com.web.programiranje.snippets.repository.UserRepository;

@Path("/users")
public class UserService {

	private UserRepository userRepository = new UserRepository();
	private LanguageRepository lr = new LanguageRepository();
	private SnippetRepository sr = new SnippetRepository();
	
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://Users//Privat//Desktop//Web Programiranje//img//";
	private static final String DEFAULT_IMAGE = "C://Users//Privat//Desktop//Web Programiranje//img//default.png";
	
	@Context
	HttpServletRequest request;
	
	/**
	 * Register user with the data entered
	 * @param username
	 * @param password
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 * @param location
	 * @return
	 */
	@POST
	@Path("/add")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String add(@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("file") InputStream uploadedInputStream,
			   @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("firstName") String firstName, @FormDataParam("lastName") String lastName,
			   @FormDataParam("email") String email, @FormDataParam("phone") String phone, @FormDataParam("location") String location) {
		
		try {
			userRepository.readFromFile();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User foundUser = userRepository.findUserByUsername(username);
		if(foundUser != null){
			return "User exists with that username";
		}
		
		foundUser = userRepository.findUserByEmail(email);
		if(foundUser != null){
			return "User exists with that email";
		}
		
		String filePath = "";

		if(fileDetail.getFileName() == null) {
			filePath = DEFAULT_IMAGE;
		}else{
			String fileName = username + fileDetail.getFileName();
			filePath = SERVER_UPLOAD_LOCATION_FOLDER  + fileName;
			userRepository.saveFile(uploadedInputStream, filePath);
			System.out.println("File saved to server location : " + filePath);
		}
		
		User u = new User(username, password, "regUser", firstName, lastName, email, phone, location, fileDetail.getFileName());
		
		userRepository.register(u);
		System.out.println("User with " + username + " registered");
		return "User with username " + username + " registered";
	}
	
	/**
	 * Login the user if he exists and create token
	 * @param u
	 * @return
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject login(User u){
		System.out.println("User to be logged in: " + u.getUsername() + " - " + u.getPassword());
		JSONObject jo = new JSONObject();
		jo = userRepository.login(u.getUsername(), u.getPassword());
		if(jo.containsKey("token")){
            try {
				userRepository.setUsersToken(u.getUsername(), (String) jo.get("token"));
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		return jo;
	}
	
	/**
	 * Return a list of all registered users from data base
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> all_users() throws FileNotFoundException, ClassNotFoundException, IOException{
//		System.out.println("Vrati sve korisnike");
//		System.out.println(request.getHeader("authorization"));
		return userRepository.getAllRegUsers(request);
	}

	/**
	 * Block the user with the choosen username
	 * @param username
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/block/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject blockUser(@PathParam("username") String username) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = userRepository.blockUser(username, request);
		
		if(response.equals("OK")){
			jo.put("status", "User has been blocked");
		}else{
			jo.put("status", response);
		}
		
		return jo;
	}
	
	/**
	 * Unblock the user that is blocked 
	 * @param username
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/unblock/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject unblockUser(@PathParam("username") String username) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = userRepository.unblockUser(username, request);
		
		if(response.equals("OK")){
			jo.put("status", "User has been unblocked");
		}else{
			jo.put("status", response);
		}
		
		return jo;
	}
	
	/**
	 * Return a list with all languages from database
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/languages/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Language> getAllLanguages() throws FileNotFoundException, ClassNotFoundException, IOException{
		return lr.getAllLanguages(request);
	}
	
	/**
	 * Add new language with the given name
	 * if language with that name does not
	 * already exist
	 * @param name
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@POST
	@Path("/languages/add/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addLanguage(@PathParam("name") String name) throws ClassNotFoundException, IOException{
		lr.readFromFile();
		
		JSONObject jo = new JSONObject();

		String response = lr.addLanguage(name, request);

		if (response.equals("OK")) {
			jo.put("status", "Language was added");
		} else {
			jo.put("status", response);
		}

		return jo;
	}
	
	/**
	 * Return a list with all snippets from database
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/snippets/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Snippet> getAllSnippets() throws FileNotFoundException, ClassNotFoundException, IOException{
		return sr.getAllSnippets();
	}
	
	@POST
	@Path("/snippets/add")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addSnippet(Snippet snippet) throws ClassNotFoundException, IOException{
		sr.readFromFile();
		JSONObject jo = new JSONObject();
		if(snippet.getId() == null){
			snippet.setId(Snippet.generateString(new Random(), "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM", 10));
		}
		
		if(snippet.getLanguage() == null || snippet.getLanguage().length() == 0){
			snippet.setLanguage("undefined");
		}
		
		System.out.println(snippet.toString());
		String response = sr.addSnippet(snippet.getDescription(), snippet.getCode(), snippet.getLanguage(), snippet.getUrl(), snippet.getExpiration(), request);

		if (response.equals("OK")) {
			jo.put("status", "OK");
		} else {
			jo.put("status", response);
		}

		return jo;
	}
	
	@GET
	@Path("/snippets/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Snippet getSnippet(@PathParam("id") String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		System.out.println(id);
		Snippet foundSnippet = sr.findSnippetById(id);
		System.out.println(foundSnippet.toString());
		return foundSnippet;
	}
	
}
