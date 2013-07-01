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
 	
	public static OpponentGroup getInactiveOpponentGroup(int id){
		List<OpponentGroup> opponentGroups = OpponentGroupData.getInactiveOpponentGroups();
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
		
		public static List<OpponentGroup> getActiveOpponentGroups(){
			//cache perhaps
			return OpponentGroupData.getActiveOpponentGroups();
			
		}
		
		
		/*
		public static void saveOpponentGroup(OpponentGroup opponentGroup){
			List<OpponentGroup> opponentGroups = OpponentGroupData.getActiveOpponentGroups();
			
			boolean exists = false;
			for (OpponentGroup o : opponentGroups){
				if (o.getId() == opponentGroup.getId()){
					o.setActivated(opponentGroup.isActivated());
					exists = true;
				}
			}
			if (!exists){
				opponentGroups.add(opponentGroup);
			}
			
			OpponentGroupData.saveOpponentGroups(opponentGroups);
		
		}
		*/
		

}
