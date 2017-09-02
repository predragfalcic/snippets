package com.web.programiranje.snippets.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.web.programiranje.snippets.model.Snippet;
import com.web.programiranje.snippets.util.JsonWebTokenImpl;
import com.web.programiranje.snippets.util.ReadWriteFile;

public class SnippetRepository {
	private ArrayList<Snippet> snippets = new ArrayList<>();
	private ReadWriteFile rwf = new ReadWriteFile();
	
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
}
























