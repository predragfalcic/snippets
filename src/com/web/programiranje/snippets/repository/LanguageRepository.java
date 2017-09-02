package com.web.programiranje.snippets.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.web.programiranje.snippets.model.Language;
import com.web.programiranje.snippets.util.JsonWebTokenImpl;
import com.web.programiranje.snippets.util.ReadWriteFile;

public class LanguageRepository {

	private ArrayList<Language> languages = new ArrayList<>();
	private ReadWriteFile rwf = new ReadWriteFile();
	
	public LanguageRepository(){}
	
	/**
	 * Add language to list and save it to file
	 * @param l
	 * @return
	 */
	public void addLanguage(Language l){
		languages.add(l);
		rwf.writeLanguageToFile(languages);
		for (Language language : languages) {
			System.out.println(language.toString());
		}
	}
	
	/**
	 * Find language by it's name
	 * @param name
	 * @return
	 */
	public Language findLanguageByName(String name){
		System.out.println(languages.size());
		// If language exists return it
		for (Language language : languages) {
			if(language.getName().equalsIgnoreCase(name)){
				return language;
			}
		}
		return null;
	}
	
	/**
	 * Read languages from file and return them as list
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Language> getLanguages() throws FileNotFoundException, ClassNotFoundException, IOException{
		languages.clear();
		languages = rwf.readLanguageFromFile();
		return languages;
	}
	
	/**
	 * Read data from file
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void readFromFile() throws ClassNotFoundException, IOException{
		languages = rwf.readLanguageFromFile();
	}
	
	/**
	 * Checks if language exists and if not save it to file
	 * @param name
	 * @return
	 */
	public String addLanguage(String name, HttpServletRequest request){
		
		String role = JsonWebTokenImpl.parseRequest(request, "role");

		System.out.println("User sa rolom: " + role + " zeli da doda programski jezik sa nazivom " + name);

		if (!role.equalsIgnoreCase("admin")) {
			System.out.println("Nije admin");
			return null;
		}
		
//		// Check if language exists
		Language l = findLanguageByName(name);
//		
//		//If exists dont save it
		if(l != null){
			return "Language with that name already exists";
		}
		
		languages.add(new Language(name));
		
		rwf.writeLanguageToFile(languages);
		return "OK";
	}
	
	/**
	 * Get all languages from database
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Language> getAllLanguages(HttpServletRequest request) throws FileNotFoundException, ClassNotFoundException, IOException{
		languages.clear();
		languages = rwf.readLanguageFromFile();
		
		
		return languages;
	}
	
}
