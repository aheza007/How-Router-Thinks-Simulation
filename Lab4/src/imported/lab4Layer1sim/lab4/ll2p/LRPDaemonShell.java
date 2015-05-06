/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: LRPDaemonShell
 * Date:3/13/2014
 * 
 * 
 */
package imported.lab4Layer1sim.lab4.ll2p;

import java.util.ArrayList;
import java.util.Iterator;

import lab4Layer1sim.lab4.LL2PDaemon;

public class LRPDaemonShell implements Runnable{

	private ARPTable arpTable;//You will need the reference for the ARPTable. 
	//You’ll use this when you create send and send routing updates because you send a routing 
	//table to every adjacent router in the ARP table.
	
	private RouteTable routeTable;// You need a reference to the routingTable.
	private ForwardingTable myForwardingTable;//You need a reference to the forwardingTable.
	//private Factory myFactory;
	private UIManager uiManager;
	private LRPClass myLRP;
	private Factory myActivity;
	private Integer destLL3P;
	private String stringToSend;
	private Integer mySequence=0;
	//you’ll need the uiManager reference to let the uiManager know it’s time to update a display, and to raise toasts.
	//You ll need the reference to the activity object. You can get it from the factory (like the other objects above).
	
	private LL2PDaemon myLayer2Daemon;// – you’ll need the reference to the layer2Daemon so you can send LRP Packets to the LL2PDaemon for framing and transmission (next week).
	
	public LRPDaemonShell(){//Nothing to do here except get created!
		
	}
	
	
	public void getObjectReferences(Factory fact){//– IMPORTANT – the LRPDaemon is the owner and keeper of the routingTable and forwardingTable.  
												//Get the references for these from the factory and add a route to the routing table to yourself.  
			myActivity=fact;									//This should a zero length route to your own network number. Then get the other references to the other members listed above.
		uiManager=fact.getUIManager();
		arpTable=fact.getARTableObjectReference();
		
		myLRP=fact.getLRPObjectReference();
		myLayer2Daemon=fact.getLL2PDaemonObjectReference();
		routeTable=fact.getRouteTableObjectReference();
		myForwardingTable=fact.getForwardingTableObjectReference();
		
		
	}
	
	public RouteTable getRouteTable(){// Provide a method to return the routingTable.
		return routeTable;
	}
	
	public ArrayList<RouteTableEntry>  getRoutingTableAsList(){//– provide a method to return an ArrayList containing the routes in the routing table.  This method must be implemented exactly as spelled here. It must return a “List<RouteTableEntry>” object.  It will be used by a class I will supply to you in the final lab.
		return getRouteTable().getRouteList();
	}
	public ForwardingTable getFIB(){//– provide a method to return the forwarding table.
		myForwardingTable.AddRouteList(getRoutingTableAsList());
		return myForwardingTable;
	}
	public ArrayList<RouteTableEntry> getForwardingTableAsList(){//– provide a method to return an ArrayList containing the routes in the forwarding table.
		myActivity.runOnUiThread(new Runnable() { 
			public void run() {
				//uiManager.raiseToast("ADDING ROUTEs BASED on ARP TABLEs");
				uiManager.resetRouteTableListAdapter();
				//myForwardingTable.RemoveOldRoutes();
				uiManager.resetforwadingTableListAdapter();
				
			}
		});
		return getFIB().getRouteList();
	}
	/*
	 * This method will be called by the Layer2Daemon when an LRP Packet is received.  It does two important things:
	 * First it ‘touches’ the ARP entry that contains the LL2P address that sent us this LRP packet.  
	 * This is why the LRPDaemon has the arpTable reference. If we don’t do this, then ARP Table entries might expire.
	 * Second, it unpacks the routing update and adds or updates every route in the routing table.
	 * If the routing table changed, it also updates the forwarding table and notifies 
	 * the UIManager that the screen display of the forwarding table needs to be updated
	 */
	public void receiveNewLRP(byte[] lrpPacket, Integer LL2PSource) {
		//arpTable.updateBasedOnLL2P(LL2PSource);
		myLRP=new LRPClass(lrpPacket);
		arpTable.addrOrUpdate(myLRP.getSourceLL3Address(), LL2PSource);
		for(int i=0;i<myLRP.getLRPCOUNTER();i++){
			//routeTable.AddEntry(myLRP.getSourceLL3Address(), myLRP.getNetworkPair().get(i).getNetworkNumber(), 
				//	myLRP.getNetworkPair().get(i).getDistance()+1,myLRP.getSourceLL3Address());
			Integer networkN = myLRP.getNetworkPair().get(i).getNetworkNumber();
			Integer distance =myLRP.getNetworkPair().get(i).getDistance();
			routeTable.AddEntry(myLRP.getSourceLL3Address(), networkN , distance+1,myLRP.getSourceLL3Address());
		}		
		
	}
	//method to build routing table based on ARP ENTRIES
	
	public void builROuteBasedonARPTable(){
		
		ArrayList<ARP_TableEntry> listARP=arpTable.getARPTableAsList();
		ARP_TableEntry tmpARP=null;
		Iterator <ARP_TableEntry> ARPIterator=listARP.iterator();
		// Going through ARP Table and adding a routeTableEntry to the routing table. 
				while (ARPIterator.hasNext())
				{
					String dstLL3p=null;
					Integer netnumber=0;
					//Integer nextHop=null;
					tmpARP = ARPIterator.next();
					dstLL3p=Utilities.padHexString(Integer.toHexString(tmpARP.getLL3PAddress()),2);
					netnumber=Utilities.getNetworkNumber(dstLL3p);
					routeTable.AddEntry(Integer.parseInt((NetworkConstants.LL3P_Address),16), netnumber,1,Integer.parseInt(dstLL3p,16));
					
				}
	}
	
	
	/*
	 * this method sends LRP packet to all neighbors and also updates the routing table with directly
	 * attached route table entries
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub	
		routeTable.AddEntry(Integer.parseInt(NetworkConstants.LL3P_Address,16), 01, 0 ,Integer.parseInt(NetworkConstants.LL3P_Address,16));
		
		myActivity.runOnUiThread(new Runnable() { 
			public void run() {
				uiManager.raiseToast("READ TO SEND LRP PACKET");
			}
		});
		
	   try{
  	   builROuteBasedonARPTable();
	   }
	   catch(Exception e){
		   
	   }
	   try{
  	   int destLL3P,destLL2P;
	   int arpEntries=arpTable.getARPTableAsList().size();
	   if (arpEntries>0){
	   for(int i=0;i<arpTable.getARPTableAsList().size();i++){
			destLL3P=arpTable.getARPTableAsList().get(i).getLL3PAddress(); 
			destLL2P=arpTable.getARPTableAsList().get(i).getLL2PAddress();
			
			myLRP=new LRPClass(Integer.parseInt(NetworkConstants.LL3P_Address),getFIB(),destLL3P);
			if(mySequence<=15){
				myLRP.setSequenceNumber(mySequence);
				mySequence=mySequence+1;
				}
				else
					mySequence=0;
			myLayer2Daemon.sendLL2PFrame(myLRP.getLRPPacketBytes(), destLL2P, NetworkConstants.Lab_Routing_Protocol);
		
		 }
	   }
		
	   }
	   catch(Exception e){
		   
	   	}
  	   }

}
