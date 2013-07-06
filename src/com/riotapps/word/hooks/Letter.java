package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class Letter {

	@SerializedName("C")
	private String C;
	
	@SerializedName("V")
	private int V;
	
	public String getCharacter() {
		return C;
	}
	public void setCharacter(String character) {
		this.C = character;
	}
	public int getValue() {
		return V;
	}
	public void setValue(int value) {
		this.V = value;
	}
	
	
}
