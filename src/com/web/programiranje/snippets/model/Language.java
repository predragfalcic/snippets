package com.web.programiranje.snippets.model;

import java.io.Serializable;

public class Language implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	public Language(){}
	
	public Language(String name){
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Language [name=" + name + "]";
	}
}
