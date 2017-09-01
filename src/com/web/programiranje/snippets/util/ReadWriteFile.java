package com.web.programiranje.snippets.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.web.programiranje.snippets.model.User;

public class ReadWriteFile {
	
	private String usersFilePath = "C://Users//Privat//Desktop//Web Programiranje//data//users.txt";
	
	public ReadWriteFile(){}
	
	/**
	 * Write all users to file
	 * @param user
	 */
	public void writeUserToFile(List<User> users){
		try {
			FileOutputStream f = new FileOutputStream(new File(usersFilePath));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(users);
			
			System.out.println("Users saved.");
			
			o.close();
			f.close();
		}catch (FileNotFoundException e) {
			System.out.println("File not found");
		}catch (IOException e) {
			System.out.println("Error initializing stream");
		}
	}
	
	/**
	 * Read all users from file
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public ArrayList<User> readUserFromFile() throws FileNotFoundException, ClassNotFoundException, IOException{
		ArrayList<User> users = new ArrayList<>();

		File f = new File(usersFilePath);
		if(f.isFile() && f.length() > 0){
			FileInputStream fi = new FileInputStream(f);
			ObjectInputStream oi = new ObjectInputStream(fi);
	
			// Read objects
			while (true) {
				try{
					Object o = oi.readObject();
					users = ((ArrayList<User>) o);
				}catch (EOFException e){oi.close();return users;};
			}
		}
		return users;
	}
	
}
