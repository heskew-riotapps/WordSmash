package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 
public class Alphabet {

	@SerializedName("Letters")
	public List<Letter> Letters = new ArrayList<Letter>();

}
