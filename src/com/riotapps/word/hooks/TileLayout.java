package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.utils.Utils;

public class TileLayout {

	@SerializedName("StarterTiles")
	public List<StarterTile> StarterTiles = new ArrayList<StarterTile>();
	
	@SerializedName("BonusTiles")
	public List<BonusTile> BonusTiles = new ArrayList<BonusTile>();
	
	class StarterTile {
		@SerializedName("id")
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	class BonusTile {
		@SerializedName("id")
		private int id;
		
		@SerializedName("M")
		private int M;
		
		@SerializedName("S")
		private String S;

		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getMultiplier() {
			return M;
		}
		public void setMultiplier(int multiplier) {
			this.M = multiplier;
		}
		public String getScope() {
			return S;
		}
		public void setScope(String scope) {
			this.S = scope;
		}
	}
	
	public int getRandomStarterTilePosition(){
		int randomIndex = Utils.getRandomNumberFromRange(0, this.StarterTiles.size() - 1);
		
		return this.StarterTiles.get(randomIndex).getId();
		
		
	}
	
}
