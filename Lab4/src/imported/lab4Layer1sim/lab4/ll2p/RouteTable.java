/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: RouteTable
 * Date:3/13/2014
 * 
 * 
 */
package imported.lab4Layer1sim.lab4.ll2p;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import lab4Layer1sim.lab4.NetworkDistancePairClass;
import android.app.Activity;

/*
 * Routing table as the table which lists all the known paths to destination networks
 * The Route Table must contain all the routes learned from neighbors
 */
public class RouteTable implements Runnable {
	
	Set<RouteTableEntry> table;
	private ArrayList<RouteTableEntry> myRoutingTable;
	private Activity myActivity;
	private UIManager myUIManager;
	private Scheduler myScheduler;
	private NetworkDistancePairClass myPair;
	
	public RouteTable(){
		table = new TreeSet<RouteTableEntry>();
	}
	
	public ArrayList<RouteTableEntry> getRouteList(){//store my table in array list
	//	myRoutingTable=new ArrayList<RouteTableEntry>(table);
		
		//return myRoutingTable;
		
		ArrayList<RouteTableEntry> returnList = new ArrayList<RouteTableEntry>();
		Iterator<RouteTableEntry> tableIterator = table.iterator();
		try
		{
			while(table.iterator().hasNext())
			{
				returnList.add(tableIterator.next());
			}
			return returnList;
		}
		catch(Exception myErrorObject){
			myErrorObject.printStackTrace();
		}
		
		return returnList;
	}
	
	
	
	public void AddEntry(Integer source, Integer net, Integer distance, Integer nextHop) {

        NetworkDistancePairClass myPair = new NetworkDistancePairClass(net, distance);

        RouteTableEntry newEntry = new RouteTableEntry(source, myPair, nextHop);
        try {
            Iterator<RouteTableEntry> tableIterator = table.iterator();
            boolean found = false;
            RouteTableEntry tmp = null;

            while (tableIterator.hasNext() && !found) {

                tmp = tableIterator.next();
                if (tmp.getNetworkDistancePair().getNetworkNumber().equals(net)
                        && tmp.getNetworkDistancePair().getDistance().equals(distance)
                        && tmp.getSourceLL3P().equals(source)
                        && tmp.getNextHOP().equals(nextHop)) {

                    found = true;
                    tmp.UpdateLastTimeTouched();
                    
                }
            }

            if (!found)
                table.add(newEntry);

        }

        catch (Exception e) {

        }

    }
	//========================================================
	
//	public void AddEntry(Integer source, Integer net, Integer distance, Integer nextHop){//add new Entries
//		
//		Iterator<RouteTableEntry> tableIterator=table.iterator();
//		RouteTableEntry existingEntry=null;
//		
//		boolean found=false;
//		if(!tableIterator.hasNext())//fist time add
//		{
//			myPair=new NetworkDistancePairClass(net, distance);//use NetworkPairclass to build pairs
//			RouteTableEntry newEntry=new RouteTableEntry(source, myPair, nextHop);//new route	
//			table.add(newEntry);	
//		}
//		else
//					{
//					try
//					{
//						
//					while(tableIterator.hasNext()&&!found){
//						existingEntry=tableIterator.next();
//						
//						if((existingEntry.getSourceLL3P().equals(source)&&existingEntry.getNetworkDistancePair().getNetworkNumber().equals(net)
//								&&existingEntry.getNetworkDistancePair().getDistance().equals(distance)&&existingEntry.getNextHOP().equals(nextHop))){//check if the new route exist in my Table if yes update it
//							
//									found=true;
//									existingEntry.UpdateLastTimeTouched();
//								}
//						else
//						{
//									myPair=new NetworkDistancePairClass(net, distance);
//									RouteTableEntry newEntry=new RouteTableEntry(source, myPair, nextHop);	
//									table.add(newEntry);	
//						}
//						
//							
//						}
//					}
//					catch(Exception e)
//					{
//						e.printStackTrace();
//					}
//				
//					
//			    	}
//			}
	
	
//==================================================
	//ArrayList<RouteTableEntry>
	public void  RemoveOldRoutes() {//This method works through the route table and removes routes that have expired.
		Iterator<RouteTableEntry> tableIterator=table.iterator();
		RouteTableEntry newEntry=null;
		Set<RouteTableEntry> newTable=new TreeSet<RouteTableEntry>();
		try
		{
		while(tableIterator.hasNext()){
			newEntry=tableIterator.next();
			
			if(newEntry.isNotExpired()==false) {
					newTable.add(newEntry);		
				}
			}
		table.removeAll(newTable);
		}
		catch(Exception e)
		{
		e.printStackTrace();
		}
		
					
	}
	
//	/** Method that works through the route table and removes routes that have expired
//	 */
//	public void RemoveOldRoutes()
//	{
//		boolean oldRoute = false;
//		
//		Iterator<RouteTableEntry>tableIterator = table.iterator();
//		
//		RouteTableEntry entry = null;
//		while(tableIterator.hasNext())
//		{
//			entry = tableIterator.next();
//			if(entry.isNotExpired()==false)
//			{
//				oldRoute = true;
//			}
//			if(oldRoute)
//			{
//				try
//				{
//					table.remove(entry);
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//				}
//
//				tableIterator = table.iterator();
//			}
//		}
//	}
    public ArrayList<RouteTableEntry> removeEntry(Integer network, Integer sourceRouter) {//This removes the route to the specified network that was provided by the specified router.
    	Iterator<RouteTableEntry> tableIterator=table.iterator();
		RouteTableEntry newEntry=null;
		boolean found=false;
		while(tableIterator.hasNext()&&!found){
			newEntry=tableIterator.next();
			if(newEntry.getNetworkDistancePair().getNetworkNumber().equals(network)&&newEntry.getSourceLL3P().equals(sourceRouter)){
				try
					{
						found=true;
						table.remove(newEntry);
					}
				catch(Exception e)
					{
						e.printStackTrace();
					}
			}
		}
		
		return getRouteList();
    	
    }
    
	public void getObjectReference(Factory myFactory){//get some object that we may need
		myActivity=myFactory.getParentActivityReference();
		myUIManager=myFactory.getUIManager();
		myScheduler=myFactory.getSchedulerObjectReference();
		myPair=myFactory.getNetworkDistancePairObjectReference();
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		this.RemoveOldRoutes();
		
		// this has to be updated on the UI thread because this 
		// run() thread's not allowed to access UI stuff.
		myActivity.runOnUiThread(new Runnable() { 
			public void run() {
				myUIManager.myROUTINGTRICS();
//				myUIManager.resetRouteTableListAdapter();
//				myUIManager.resetforwadingTableListAdapter();
			//	myUIManager.raiseToast("Checked routes");
			}
		});

	}
	
}
