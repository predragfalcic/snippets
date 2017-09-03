package com.web.programiranje.snippets.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import com.web.programiranje.snippets.model.Comment;
import com.web.programiranje.snippets.model.Grade;
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	@POST
	@Path("/add")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String add(@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("file") InputStream uploadedInputStream,
			   @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("firstName") String firstName, @FormDataParam("lastName") String lastName,
			   @FormDataParam("email") String email, @FormDataParam("phone") String phone, @FormDataParam("location") String location) throws FileNotFoundException, ClassNotFoundException, IOException {
		
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
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONObject login(User u) throws FileNotFoundException, ClassNotFoundException, IOException{
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
	
	/**
	 * Get snippets for the current user
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/snippets/user/all")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Snippet> getAllUsersSnippets() throws FileNotFoundException, ClassNotFoundException, IOException{
		return sr.getAllUserSnippets(request);
	}
	
	/**
	 * Add snippet to database
	 * @param snippet
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/snippets/add")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject addSnippet(Snippet snippet) throws ClassNotFoundException, IOException{
		sr.readFromFile();
		JSONObject jo = new JSONObject();
		
		// If snippet does not have id generate one
		if(snippet.getId() == null){
			snippet.setId(Snippet.generateString(new Random(), "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM", 10));
		}
		
		System.out.println(snippet.getLanguage());
		
		// If snippet language is null set it to undefined
		if(snippet.getLanguage() == null || snippet.getLanguage().length() == 0){
			snippet.setLanguage("undefined");
		}
		
//		System.out.println(snippet.toString());
		String response = sr.addSnippet(snippet.getDescription(), snippet.getCode(), snippet.getLanguage(), snippet.getUrl(), snippet.getExpiration(), request);

		if (response.equals("OK")) {
			jo.put("status", "OK");
		} else {
			jo.put("status", response);
		}

		return jo;
	}
	
	/**
	 * Display the details for the selected snippet
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/snippets/details/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Snippet getSnippet(@PathParam("id") String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		System.out.println(id);
		Snippet foundSnippet = sr.findSnippetById(id);
		return foundSnippet;
	}
	
	/**
	 * Delete selected snippet
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@GET
	@Path("/snippets/delete/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject deleteSnippet(@PathParam("id") String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = sr.deleteSnippet(id);
		
		if (response.equals("OK")) {
			jo.put("status", "OK");
		} else {
			jo.put("status", response);
		}
		
		return jo;
	}
	
	/**
	 * Find the snippet that should be commented 
	 * Add comment to the found snippet
	 * Save snippet in database
	 * @param comment
	 * @param id
	 * @return Snippet
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/snippets/{id}/comment/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Snippet addComment(@PathParam("id") String id, Comment comment) throws ClassNotFoundException, IOException{
		sr.readFromFile();
		JSONObject jo = new JSONObject();
		
		// Find snippet which is commented
		Snippet snippet = sr.findSnippetById(id);
		
		// Get all snippet comments
		ArrayList<Comment> snippetComments = snippet.getComments();
	
		// Add new comment to the list
		snippetComments.add(new Comment(comment.getText(), comment.getUser(), new Date(), new Grade()));
		
		// Set the new list with comments to snippet
		snippet.setComments(snippetComments);
		
//		System.out.println(snippet.toString());
		
		// Update snippet
		String response = sr.updateSnippet(snippet);
		
		return snippet;
	}
	
	/**
	 * Delete comments from snippet
	 * @param id
	 * @param comment
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/snippets/{id}/comment/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Snippet deleteComment(@PathParam("id") String id, Comment comment) throws ClassNotFoundException, IOException{
		sr.readFromFile();
		JSONObject jo = new JSONObject();
		
		// Find snippet
		Snippet snippet = sr.findSnippetById(id);
		
		// Get all snippet comments
		ArrayList<Comment> snippetComments = snippet.getComments();
		
		// Delete comment from the snippet
		snippetComments.remove(comment);
		
		// Set the new list with comments to snippet
		snippet.setComments(snippetComments);
		
		// Update snippet
		String response = sr.updateSnippet(snippet);
		
		return snippet;
	}
	
	@POST
	@Path("/snippets/comments/block")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject blockComments(Snippet s) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		System.out.println("Should be blocked to comments");
		
		String response = sr.blockComments(s.getId());
		
		if(response.equals("OK")){
			jo.put("status", "Comments have been blocked");
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
	@Path("/snippets/comments/unblock")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject unblockComments(Snippet s) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = sr.unblockComments(s.getId());
		
		if(response.equals("OK")){
			jo.put("status", "User has been unblocked");
		}else{
			jo.put("status", response);
		}
		
		return jo;
	}
	
	/**
	 * Find the comment in the snippet
	 * Checks if user has already liked it 
	 * and if not then likes the comment
	 * @param s_id
	 * @param user_id
	 * @param c
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/snippets/{s_id}/{user_id}/comments/like")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject likeComments(@PathParam("s_id") String s_id, @PathParam("user_id") String user_id, Comment c) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = sr.likeComment(s_id, c, user_id);
		
		Snippet s = sr.findSnippetById(s_id);
		
		if(response.equals("OK")){
			jo.put("status", s);
		}else{
			jo.put("status", response);
		}
		
		return jo;
	}
	
	/**
	 * Dislike comment if user didn't already liked or disliked it
	 * @param s_id
	 * @param user_id
	 * @param c
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@POST
	@Path("/snippets/{s_id}/{user_id}/comments/dislike")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject dislikeComments(@PathParam("s_id") String s_id, @PathParam("user_id") String user_id, Comment c) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		String response = sr.dislikeComment(s_id, c, user_id);
		
		Snippet s = sr.findSnippetById(s_id);
		
		if(response.equals("OK")){
			jo.put("status", s);
		}else{
			jo.put("status", response);
		}
		
		return jo;
	}
	
	@GET
	@Path("/snippets/search/lang/{l}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject searchSnippetsByLanguage(@PathParam("l") String l) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
		JSONObject jo = new JSONObject();
		
		ArrayList<Snippet> foundSnippets = sr.searchSnippetsByLanguage(l);
		
		if(foundSnippets.size() == 0){
			jo.put("status", "No snippets found");
		}else{
			jo.put("status", foundSnippets);
		}
		
		return jo;
	}
}









