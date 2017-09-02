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
	
	public Comment() {
		super();
	}

	public Comment(String text, String user, Date dateTimeCommented, Grade grade) {
		super();
		this.text = text;
		this.user = user;
		this.dateTimeCommented = dateTimeCommented;
		this.grade = grade;
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

	@Override
	public String toString() {
		return "Comment [text=" + text + ", user=" + user + ", dateTimeCommented=" + dateTimeCommented + ", grade="
				+ grade + "]";
	}
}
