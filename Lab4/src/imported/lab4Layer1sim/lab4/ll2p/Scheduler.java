package imported.lab4Layer1sim.lab4.ll2p;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 *Check to see if any ARP entries have expired.  If we have not heard from a neighbor within 60 seconds then we will remove its ARP entry from the ARP table
 *Send a Lab Routing Protocol (LRP) packet to each neighbor.  A neighbor will be defined as a node that is present in the ARP table.  
 *We will send a routing update every  seconds. 
 */
public class Scheduler {
//	private LRP packetLRP;
	private ScheduledThreadPoolExecutor myThreadPoolManager;//the thread Manager 
	private ARPTable myARPTable;//we refer to it when we wanna create a thread in ARPTable.
	private RouteTable routeTable;//reference to the routing table
    private LRPDaemonShell myLRPDaemon;//referencing to the LRP
	
	public Scheduler(){
		myThreadPoolManager= new ScheduledThreadPoolExecutor(3);
	}
	public void getObjectReferences(Factory myFactory){
		myARPTable=myFactory.getARTableObjectReference();
		routeTable=myFactory.getRouteTableObjectReference();
		myLRPDaemon=myFactory.getLRPDaemonShellReference();
		
		myThreadPoolManager.scheduleAtFixedRate(myARPTable , NetworkConstants.ROUTER_BOOT_TIME, NetworkConstants.ROUTE_UPDATE_VALUE , TimeUnit.SECONDS);
		//myThreadPoolManager.
		myThreadPoolManager.scheduleAtFixedRate(routeTable , NetworkConstants.ROUTER_BOOT_TIME, NetworkConstants.ROUTE_UPDATE_VALUE, TimeUnit.SECONDS);
		
		myThreadPoolManager.scheduleAtFixedRate(myLRPDaemon, NetworkConstants.ROUTER_BOOT_TIME, NetworkConstants.ROUTE_UPDATE_VALUE, TimeUnit.SECONDS);
	}
}
