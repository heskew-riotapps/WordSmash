package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class Word {

	@SerializedName("W")
	private String W;
	
	public String getWord() {
		return W;
	}
	public void setWord(String word) {
		this.W = word;
	}
}
