package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 
public class WordList {

	@SerializedName("Words")
	public List<String> Words = new ArrayList<String>();

}
