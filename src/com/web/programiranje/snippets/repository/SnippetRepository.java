package com.web.programiranje.snippets.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.web.programiranje.snippets.model.Comment;
import com.web.programiranje.snippets.model.Grade;
import com.web.programiranje.snippets.model.Snippet;
import com.web.programiranje.snippets.model.User;
import com.web.programiranje.snippets.util.JsonWebTokenImpl;
import com.web.programiranje.snippets.util.ReadWriteFile;

public class SnippetRepository {
	private ArrayList<Snippet> snippets = new ArrayList<>();
	private ReadWriteFile rwf = new ReadWriteFile();
	
	private UserRepository ur = new UserRepository();
	
	public SnippetRepository(){}
	
	/**
	 * Add Snippet to list and save it to file
	 * @param s
	 * @return
	 */
	public void addSnippet(Snippet s){
		snippets.add(s);
		rwf.writeSnippetToFile(snippets);
		for (Snippet snippet : snippets) {
			System.out.println(snippet.toString());
		}
	}
	
	/**
	 * Update snippet 
	 * @param s
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String updateSnippet(Snippet s) throws FileNotFoundException, ClassNotFoundException, IOException{
		// Clear the list and read all snippets from file
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		// Find snippet to be updated
		for (Snippet snippet : snippets) {
			// Snippet was found
			if(snippet.getId().equals(s.getId())){
				// Set the old snippet as the new one
				snippet.setComments(s.getComments());;
			}
		}
	
		// Save new snippets to file
		rwf.writeSnippetToFile(snippets);
		
		return "OK";
	}
	
	/**
	 * Find Snippet by it's description
	 * @param description
	 * @return
	 */
	public Snippet findSnippetByDescription(String description){
//		System.out.println(snippets.size());
		// If snippet exists return it
		for (Snippet s : snippets) {
			if(s.getDescription().contains(description)){
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Read snippets from file and return them as list
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Snippet> getSnippets() throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		return snippets;
	}
	
	/**
	 * Read data from file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void readFromFile() throws ClassNotFoundException, IOException{
		snippets = rwf.readSnippetFromFile();
	}
	
	public Snippet findSnippet(Snippet s){
		for (Snippet snippet : snippets) {
			if(snippet.equals(s)){
				return snippet;
			}
		}
		return null;
	}
	
	/**
	 * Save snippet to file
	 * @param name
	 * @return
	 */
	public String addSnippet(String description, String code, String lang, String url, String expiration, HttpServletRequest request){
		
		// Get the users username if he is loggedin or set the username as guest if the user is not logged in
		String username = JsonWebTokenImpl.parseRequest(request, "user");

		System.out.println("User sa usernamom: " + username + " zeli da doda snippet");
		
		// Set language undefined if not selected
		if(!(lang.length() > 0)){
			lang = "undefined";
		}
		
		Snippet newSnippet = new Snippet(description, code, lang, url, expiration, username);
		
		if(findSnippet(newSnippet) != null){
			return "That snippet already exists";
		}
		
		snippets.add(newSnippet);
		
		rwf.writeSnippetToFile(snippets);
		return "OK";
	}
	
	/**
	 * Get snipet from database
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Snippet> getAllSnippets() throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		
		return snippets;
	}
	
	/**
	 * Get all snippets that the current user created
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Snippet> getAllUserSnippets(HttpServletRequest request) throws FileNotFoundException, ClassNotFoundException, IOException{
		// Get the users username if he is loggedin
		String username = JsonWebTokenImpl.parseRequest(request, "user");
		
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		ArrayList<Snippet> usersSnippets = new ArrayList<>();
		
		// Get all snippets for the user with the found username
		for (Snippet snippet : snippets) {
			if(snippet.getUser().equals(username)){
				usersSnippets.add(snippet);
			}
		}
		
		return usersSnippets;
	}
	
	/**
	 * Return snippet if found one
	 * @param s
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public Snippet detailsSnippet(Snippet s) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		snippets = rwf.readSnippetFromFile();
		
		Snippet newSnippet = findSnippet(s);

		if(newSnippet == null){
			return null;
		}
		
		return newSnippet;
	}
	
	/**
	 * Find snippet by given id
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Snippet findSnippetById(String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets = rwf.readSnippetFromFile();
		
		for (Snippet snippet : snippets) {
			if(snippet.getId().equals(id)){
				return snippet;
			}
		}
		return null;
	}
	
	/**
	 * Deletes snippet from database
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String deleteSnippet(String id) throws FileNotFoundException, ClassNotFoundException, IOException{
//		System.out.println(id);
		Snippet deletedSnippet = findSnippetById(id);
		
		if(deletedSnippet == null){
			return "Snippet not found";
		}
		
		snippets.remove(deletedSnippet);
		
		rwf.writeSnippetToFile(snippets);
		
		return "OK";
	}
	
	/**
	 * Disable commenting on this snippet
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String blockComments(String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		for (Snippet snippet : snippets) {
			if(snippet.getId().equals(id)){
				snippet.setCanBeCommented(false);
			}
		}
		
		rwf.writeSnippetToFile(snippets);
		
		return "OK";
	}
	
	/**
	 * Enable commenting on this snippet
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String unblockComments(String id) throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		for (Snippet snippet : snippets) {
			if(snippet.getId().equals(id)){
				snippet.setCanBeCommented(true);
			}
		}
		
		rwf.writeSnippetToFile(snippets);
		
		return "OK";
	}
	
	/**
	 * Find and increment the number of positiv likes for it
	 * @param id
	 * @param comment
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String likeComment(String id, Comment comment, String user_id) throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		// Find the comment to like
		for (Snippet snippet : snippets) {
			for (Comment c : snippet.getComments()) {
//				System.out.println(comment.toString());
				if(c.toString().equals(comment.toString())){
					// Check if user has already liked comment
					Grade g = c.getGrade();
					
					if(g.getUsers() == null){
						System.out.println("Usao je ovde");
						g.setUsers(new ArrayList<User>());
					}
					
//					System.out.println(g.getUsers());
					
					for (User user : g.getUsers()) {
//						System.out.println(user.getUsername());
						if(user.getUsername().equals(user_id)){
							return "You already gave your grade this comment";
						}
					}
					// If not then do the rest
					g.setPositiveClicks(g.getPositiveClicks() + 1);
					// Set the user who commented it
					User u = ur.findUserByUsername(user_id);
//					System.out.println(u.toString());
					ArrayList<User> gradeUsers = g.getUsers();
					
					gradeUsers.add(u);
					
					g.setUsers(gradeUsers);
					c.setGrade(g);
					
					rwf.writeSnippetToFile(snippets);
				}
			}
		}
		
		return "OK";
	}
	
	/**
	 * Find comment to dislike
	 * check if it is already like or disliked
	 * if not dislike it
	 * @param id
	 * @param comment
	 * @param user_id
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String dislikeComment(String id, Comment comment, String user_id) throws FileNotFoundException, ClassNotFoundException, IOException{
		snippets.clear();
		snippets = rwf.readSnippetFromFile();
		
		// Find the comment to like
		for (Snippet snippet : snippets) {
			for (Comment c : snippet.getComments()) {
//				System.out.println(comment.toString());
				if(c.toString().equals(comment.toString())){
					// Check if user has already liked comment
					Grade g = c.getGrade();
					
					if(g.getUsers() == null){
//						System.out.println("Usao je ovde");
						g.setUsers(new ArrayList<User>());
					}
					
//					System.out.println(g.getUsers());
					
					for (User user : g.getUsers()) {
//						System.out.println(user.getUsername());
						if(user.getUsername().equals(user_id)){
							return "You already gave your grade this comment";
						}
					}
					// If not then do the rest
					g.setNegativeClicks(g.getNegativeClicks() + 1);
					// Set the user who commented it
					User u = ur.findUserByUsername(user_id);
//					System.out.println(u.toString());
					ArrayList<User> gradeUsers = g.getUsers();
					
					gradeUsers.add(u);
					
					g.setUsers(gradeUsers);
					c.setGrade(g);
					
					rwf.writeSnippetToFile(snippets);
				}
			}
		}
		
		return "OK";
	}
}
























