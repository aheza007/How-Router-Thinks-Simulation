/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: ForwardingTable
 * Date:3/13/2014
 * 
 * 
 */
package imported.lab4Layer1sim.lab4.ll2p;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * it only keeps records of the BEST known routes
 */
public class ForwardingTable extends RouteTable {

	public void AddFibEntry(RouteTableEntry newEntry){//– used to add a record to the route table.  If the route already exists in the forwarding table then you
											//should only add the new route if it’s a closer route than the one already in the table.
		Iterator<RouteTableEntry> tableIterator = table.iterator();
		RouteTableEntry temp = null;
		boolean found = false;
		
		// Go through the table to search if the new entry is already there
		while (tableIterator.hasNext() && !found)
		{
			temp = tableIterator.next();
			if(temp.getNetworkDistancePair().getNetworkNumber().equals(newEntry.getNetworkDistancePair().getNetworkNumber()))
			{
				found = true;
			}
		}
		
		// add the new entry in the table if it was not found
		if(!found)
		{
			table.add(newEntry);
		}
			
		else if( temp.getNetworkDistancePair().getDistance()>newEntry.getNetworkDistancePair().getDistance()|| temp.getSourceLL3P().equals(newEntry.getSourceLL3P()))
		{
			table.remove(temp);
			
			table.add(newEntry);
		}
	}
	
	public ArrayList<RouteTableEntry> getForwardList(){
		ArrayList<RouteTableEntry> myForwardingTable=new ArrayList<RouteTableEntry>(table);
		return myForwardingTable;
	}
	
	public void AddRouteList(ArrayList<RouteTableEntry> myRouteList){//This will allow to pass the Forwarding table the complete routing table (from the Route Table).  
//	 	Iterator<RouteTableEntry> listIterator=myRouteList.iterator();
//	 	RouteTableEntry tmp=null;
//	 	while(listIterator.hasNext()){
//	 		tmp=listIterator.next();
//	 		AddFibEntry(tmp);
//	 	}
	 	for (int i=0; i<myRouteList.size();i++){
	 		AddFibEntry(myRouteList.get(i));
	 	}
 	}
 
	
	public Integer getNextHopAddress(Integer LL3PNetworknumber) {//This method will be given a destination LL3P Network number. It will return the next hop in the path to the network. 
		Iterator<RouteTableEntry> tableIterator=table.iterator();
		RouteTableEntry tmp=null;
		//boolean found=false;
		Integer nextHop=null;
		while(tableIterator.hasNext()){
			tmp=tableIterator.next();
			if (tmp.getNetworkDistancePair().getNetworkNumber().equals(LL3PNetworknumber)){
				nextHop=tmp.getNextHOP();
			}
		}
		return nextHop;
	 }
    public ArrayList<RouteTableEntry> getFIBExcludingLL3PAddress(Integer LL3PAddress) {//this returns the forwarding information, removing the specified LL3P address. This is the method that will be used when we want to build an array list that contains routes to send to the specified address.
    	Iterator<RouteTableEntry> tableIterator=table.iterator();
    	ArrayList<RouteTableEntry> treatedForwaedingList= new ArrayList<RouteTableEntry>();
		RouteTableEntry tmp=null;
		//boolean found=false;
		try{
		while(tableIterator.hasNext()){
			tmp=tableIterator.next();
			Integer tmpLL3=tmp.getSourceLL3P();
			if(!(tmpLL3.equals(LL3PAddress))){
					Integer tmpNetworkNumber = tmp.getNetworkDistancePair().getNetworkNumber();
					//String  hen=Integer.toHexString(LL3PAddress);
				    Integer tmpLL3pNetworkNumber = Utilities.getNetworkNumber(Integer.toHexString(LL3PAddress));
					if (!(tmpNetworkNumber.equals(tmpLL3pNetworkNumber))){
						treatedForwaedingList.add(tmp);
			 }
	 		}
		}
		}
		catch(Exception e){
			
		}
		return treatedForwaedingList;
    }
}
