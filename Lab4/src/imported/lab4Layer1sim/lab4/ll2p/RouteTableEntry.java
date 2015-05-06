/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: RouteTableEntry
 * Date:3/13/2014
 * 
 * 
 */

package imported.lab4Layer1sim.lab4.ll2p;

import lab4Layer1sim.lab4.NetworkDistancePairClass;

/*
 * The RouteTableEntry class provides the structure for each route known by the router
 */
public class RouteTableEntry implements Comparable<RouteTableEntry> {
    
	private Integer sourceLL3P;//containing the router which we learned this route from.
	private NetworkDistancePairClass networkDistancePair;//– object containing a matched set of network and it’s distance away from this router. You might choose to implement these as two Integers as well.  
	private long lastTimeTouched;//At what time (in seconds) was this last referenced or updated. This is used to determine if a route is expired.
	private Integer nextHop;//the next Hop (what router should you send this packet to in order to reach the network it’s destined for?).  This is always the router you learned this route from.
    Factory myFactory;
	public RouteTableEntry(){
		sourceLL3P=0;
		networkDistancePair=new NetworkDistancePairClass();
		lastTimeTouched=0;
		nextHop=0;
	}
	
	public RouteTableEntry(Integer srcLL3P,NetworkDistancePairClass myPair, Integer nxtHop ){
		sourceLL3P=srcLL3P;
		networkDistancePair=myPair;
		nextHop=nxtHop;
		lastTimeTouched=System.currentTimeMillis()/1000;
	}
	/*
	 * SETTERS
	 */
	public void setSourceLL3P(Integer srcLL3P){
		sourceLL3P=srcLL3P;
	}
	public void setNetworkDistancePair(Integer netNumber, Integer dist){
		networkDistancePair=new NetworkDistancePairClass(netNumber, dist);
	}
	public void setNextHOP(Integer nxtHop){
		nextHop=nxtHop;
	}
	public void setLastTimeTouched(){
		lastTimeTouched=System.currentTimeMillis()/1000;
	}
	/*
	 * (non-Javadoc)
	 * GETTERs
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public Integer getSourceLL3P(){
		return sourceLL3P;
	}
	public Integer getNextHOP(){
		return nextHop;
	}
	public long getLastTimeTouched(){
		return lastTimeTouched;
	}
	public NetworkDistancePairClass getNetworkDistancePair(){
		return networkDistancePair;
	}
	public void UpdateLastTimeTouched() {//This will change the age to the current time, or zero depending on your implementation.
		 lastTimeTouched=System.currentTimeMillis()/1000;
	}
	
	
	public boolean isNotExpired() {//Boolean method that returns true if this route table entry is not expired. 
		
		//long time = System.currentTimeMillis()/1000;
		if(getCurrentAgeInSeconds()<NetworkConstants.ROUTE_TIMEOUT)
			return true;
		return false;
	}
	
	public long getCurrentAgeInSeconds(){ //There will be other methods that need to know the age of this object. The object is in charge of its own age so it should be working on that.
		return  System.currentTimeMillis()/1000-lastTimeTouched;
	}
	
    public String toString(){ //To show up in the routing table.
    	return ("SRC 0x:"+Utilities.padHexString(Integer.toHexString(sourceLL3P),2)+networkDistancePair.toString()+" NextHop 0x:"+Utilities.padHexString(Integer.toHexString(getNextHOP()),2)+ " Age: "+this.getCurrentAgeInSeconds());
    }
    public void getObjectReferences(Factory Factory){
    	myFactory=Factory;
    }

	/*
	 * //check the network number if it already exist in the table; if found then check if the corresponding route has the same source 
	//	as the new one or check if the distance are not equal: this is to allow the treatment of many route from on source.
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RouteTableEntry newEntry) {
		// TODO Auto-generated method stub 
		int sameobject=0;
		int beforeobject=-1;
		int afterobject=1;
		 if ((newEntry.networkDistancePair.getNetworkNumber().equals(this.networkDistancePair.getNetworkNumber()))){
			 		
			 			if (!(newEntry.getSourceLL3P().equals(this.getSourceLL3P())) || !(newEntry.networkDistancePair.getDistance().equals(this.networkDistancePair.getDistance())))
			 				 		return beforeobject;
			 			
			 			else
			 				return sameobject;
		   }
		 
		 return newEntry.networkDistancePair.getNetworkNumber()>(this.networkDistancePair.getNetworkNumber())?
				 afterobject:newEntry.networkDistancePair.getNetworkNumber()<this.networkDistancePair.getNetworkNumber()?beforeobject:sameobject;
	  
			 
	}
}
