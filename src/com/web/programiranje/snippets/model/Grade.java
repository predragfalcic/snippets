package com.web.programiranje.snippets.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Grade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int positiveClicks;
	private int negativeClicks;
	private ArrayList<User> users;
	
	public Grade() {
		super();
		this.positiveClicks = 0;
		this.negativeClicks = 0;
		this.users = new ArrayList<>();
	}
	
	public Grade(int positiveClicks, int negativeClicks) {
		super();
		this.positiveClicks = positiveClicks;
		this.negativeClicks = negativeClicks;
	}

	public int getPositiveClicks() {
		return positiveClicks;
	}

	public void setPositiveClicks(int positiveClicks) {
		this.positiveClicks = positiveClicks;
	}

	public int getNegativeClicks() {
		return negativeClicks;
	}

	public void setNegativeClicks(int negativeClicks) {
		this.negativeClicks = negativeClicks;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "Grade [positiveClicks=" + positiveClicks + ", negativeClicks=" + negativeClicks + ", users=" + users
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + negativeClicks;
		result = prime * result + positiveClicks;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grade other = (Grade) obj;
		if (negativeClicks != other.negativeClicks)
			return false;
		if (positiveClicks != other.positiveClicks)
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
}
