package com.web.programiranje.snippets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Snippet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String description;
	private String code;
	private String language;
	private String url;
	private String expiration;
	private String user; // User that created snippet
	private Boolean canBeCommented; // Snippet can be commented, default true
	private ArrayList<Comment> comments; // Comments on this snippets
	
	public Snippet() {
		super();
	}

	public Snippet(String description, String code, String language, String url, String expiration, String username) {
		super();
		this.id = generateString(new Random(), "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM", 10);
		this.description = description;
		this.code = code;
		this.language = language;
		this.url = url;
		this.expiration = expiration;
		this.user = username;
		this.canBeCommented = true;
		this.comments = new ArrayList<>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Boolean getCanBeCommented() {
		return canBeCommented;
	}

	public void setCanBeCommented(Boolean canBeCommented) {
		this.canBeCommented = canBeCommented;
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((canBeCommented == null) ? 0 : canBeCommented.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Snippet other = (Snippet) obj;
		if (canBeCommented == null) {
			if (other.canBeCommented != null)
				return false;
		} else if (!canBeCommented.equals(other.canBeCommented))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (expiration == null) {
			if (other.expiration != null)
				return false;
		} else if (!expiration.equals(other.expiration))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Snippet [id=" + id + ", description=" + description + ", code=" + code + ", language=" + language
				+ ", url=" + url + ", expiration=" + expiration + ", user=" + user + ", canBeCommented="
				+ canBeCommented + ", comments=" + comments + "]";
	}

	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
}
