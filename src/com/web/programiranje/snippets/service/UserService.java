package com.web.programiranje.snippets.service;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.web.programiranje.snippets.model.User;
import com.web.programiranje.snippets.repository.UserRepository;

@Path("/users")
public class UserService {

	private UserRepository userRepository = new UserRepository();
	
	@POST
	@Path("/add")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String add(User u) {
		try {
			userRepository.readFromFile();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User foundUser = userRepository.findUserByUsername(u.getUsername());
		if(foundUser != null){
			return "User exists with that username";
		}
		
		foundUser = userRepository.findUserByEmail(u.getEmail());
		if(foundUser != null){
			return "User exists with that email";
		}
		userRepository.register(u);
		System.out.println("User with " + u.getUsername() + " Registered");
		return "OK";
	}
}
