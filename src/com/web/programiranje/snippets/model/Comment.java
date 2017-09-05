package com.web.programiranje.snippets.model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String text;
	private String user; // User who commented
	private Date dateTimeCommented;
	private Grade grade;
	private String userImage = "";
	
	public Comment() {
		super();
	}

	public Comment(String text, String user, Date dateTimeCommented, Grade grade, String img) {
		super();
		this.text = text;
		this.user = user;
		this.dateTimeCommented = dateTimeCommented;
		this.grade = grade;
		this.userImage = img;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTimeCommented == null) ? 0 : dateTimeCommented.hashCode());
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Comment other = (Comment) obj;
		if (dateTimeCommented == null) {
			if (other.dateTimeCommented != null)
				return false;
		} else if (!dateTimeCommented.equals(other.dateTimeCommented))
			return false;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public Date getDateTimeCommented() {
		return dateTimeCommented;
	}

	public void setDateTimeCommented(Date dateTimeCommented) {
		this.dateTimeCommented = dateTimeCommented;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	@Override
	public String toString() {
		return "Comment [text=" + text + ", user=" + user + ", dateTimeCommented=" + dateTimeCommented + ", grade="
				+ grade + "]";
	}
	
}
