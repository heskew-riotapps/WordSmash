package com.riotapps.word.hooks;

import java.util.List;

import com.riotapps.word.data.OpponentGroupData;
import com.riotapps.word.utils.ApplicationContext;

public class OpponentGroupService {

	 	
		public static OpponentGroup getOpponentGroup(int id){
			List<OpponentGroup> opponentGroups = OpponentGroupData.getActiveOpponentGroups();
			for (OpponentGroup o : opponentGroups){
				if (o.getId() == id){
					return o;
				}
			}
			return null;
		}
		
		public static List<OpponentGroup> getInactiveOpponentGroups(){
			//cache perhaps
			return OpponentGroupData.getInactiveOpponentGroups();
			
		}
		
		
}
