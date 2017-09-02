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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + negativeClicks;
		result = prime * result + positiveClicks;
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
		return true;
	}
	
}
