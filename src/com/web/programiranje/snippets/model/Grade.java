package com.web.programiranje.snippets.model;

import java.io.Serializable;

public class Grade implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int positiveClicks;
	private int negativeClicks;
	
	public Grade() {
		super();
		this.positiveClicks = 0;
		this.negativeClicks = 0;
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

	@Override
	public String toString() {
		return "Grade [positiveClicks=" + positiveClicks + ", negativeClicks=" + negativeClicks + "]";
	}
}
