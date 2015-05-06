package lab4Layer1sim.lab4;

import imported.lab4Layer1sim.lab4.ll2p.Factory;
import imported.lab4Layer1sim.lab4.ll2p.Utilities;

/*
 * object to keep a network number and its distance from your router together.  
 */
public class NetworkDistancePairClass {

	private Integer networkNumber;//this is an Integer which contains the network number (not the full LL3P address) of the remote network.
	private Integer distance;//this is the integer which contains the number of hops away that this network is
	Factory myFact;
	public NetworkDistancePairClass(){
		networkNumber=0;
		distance=0;
	}
	public NetworkDistancePairClass(Integer NetworkNum,Integer dist){
		networkNumber=NetworkNum;
		distance=dist;
	}
	/*
	 * SETTERS 
	 */
	public void setNetworkNumber(Integer NetworkNum){
		networkNumber=NetworkNum;
	}
	public void setDistance(Integer dist){
		distance=dist;
	}
	/*
	 * GETTERS
	 */
	public Integer getNetworkNumber(){
		return networkNumber;
	}
	public Integer getDistance(){
		return distance;
	}
	public String toString(){
		
		return (" Net# 0x:"+Utilities.padHexString(Integer.toHexString(networkNumber), 1)+
				" Dist 0x:"+Utilities.padHexString(Integer.toHexString(distance), 1));
	 //	return ("Net# :"+ String.format("%02d", distance));
	}
//	public String toHexSTRING(){
//
//	}
	public String toNoNAMEString(){//this is to fix my PAIRS built in LRP CLASS
		
		return (Utilities.padHexString(Integer.toHexString(networkNumber), 1)+
				Utilities.padHexString(Integer.toHexString(distance), 1));
	}
	public void getObjectReferences(Factory myFactory){
		myFact=myFactory;
	}

}
