/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: LL3PDaemon
 * Date:4/3/2014
 * 
 * 
 */
package imported.lab4Layer1sim.lab4.ll2p;

import lab4Layer1sim.lab4.LL2PDaemon;

public class LL3PDaemon {
	
	ARPTable arpTable;//The LL3P Daemon need a reference to the ARP Table so you can get next hop LL2P information and ‘touch’ ARP entries when appropriate
	UIManager uiManager;// – The LL3P Daemon may need to raise toast messages, and you’ll need to tell the UI Manager that the LL3P packet has changed and the screen needs refreshing.
	LL3PClass myLL3P_Packet;//- The LL3P Daemon need a reference to the global LL3P Packet so you can update it and tell the UIManager to update the screen.
	LL2PDaemon layer2Daemon;// – The LL3P Daemon needs a reference to the LL2P Daemon so that the layer 3 Daemon can ask the layer 2 daemon to transmit a packet to the next hop when needed.
	ForwardingTable myFIB;// – The LL3P Daemon needs access to the FIB so it can get next hop Information. The FIB Should already contain all the methods necessary to get a next hop address for a given network destination.

	/*	Constructor – TO initialize locally owned variables 
	 *  You’ll need to set it!)
	 *
	 */
	public LL3PDaemon(){
		//arpTa
		myLL3P_Packet=new LL3PClass();
	}
	//– This will get references for all external objects such as the layer 2 daemon, the FIB, etc.
	public void getObjectReferences(Factory myFactory) {
		arpTable=myFactory.getARTableObjectReference();
		uiManager=myFactory.getUIManager();
		//myLL3P_Packet=myFactory.getLL3PObjectReference();
		layer2Daemon=myFactory.getLL2PDaemonObjectReference();
		myFIB=myFactory.myForwardingTable;
	}
	
	/*
	this receives a byte array that is a layer 3 packet. It builds an LL3P packet object, updates the screen, and determines if the packet should be forwarded 
	or delivered locally. If the packet was sourced by an adjacent node it should also touch the appropriate ARP entry (use the TTL field to determine if this 
	packet was sourced from an adjacent node). If your router is the destination of this LL3P packet, update the UI display with the fields from this LL3P packet.
	*/
	
	public void receiveLL3PPacket(byte[] newLL3Packet,Integer LL2PSource){
		myLL3P_Packet=new LL3PClass(newLL3Packet);
		Integer destLL3P=myLL3P_Packet.getIntDSTLL3P();
		Integer srcLL3P=myLL3P_Packet.getIntSRCLL3P();
		String destLL3Ps=myLL3P_Packet.getHexDSTLL3P();
		String myLL3p=NetworkConstants.LL3P_Address;
		if(destLL3Ps.equals(myLL3p)){
			//uiManager.raiseToast(myLL3P_Packet.toString());
			//uiManager.upDATELL3PLayout();
			uiManager.receiveChat(srcLL3P, myLL3P_Packet.getStringPayload());
			if((myLL3P_Packet.getTTLvalue()-1)==(254)){
				//arpTable.updateBasedOnLL2P(LL2PSource);
				arpTable.addrOrUpdate(myLL3P_Packet.getIntSRCLL3P(), LL2PSource);
			}
			
		}
		else{
			uiManager.raiseToast("this is not my Packet LET ME FORWARD IT");
			sendLL3PPacket(myLL3P_Packet);
			}
	}
	/*
	 * – this method is given an LL3P packet object and routes it properly to the next hop, adjusting TTL if necessary.  This method is called from an application 
	 * or from the class itself (see next method).
	 */
	public void sendLL3PPacket(LL3PClass ll3pPACKET) {
		Integer destLL3PNetNumber=ll3pPACKET.getIntDSTLL3P()/256;
//		boolean arpPRESENT=arpTable.LL3PIsInTable(ll3pPACKET.getIntDSTLL3P());
//		if (arpPRESENT==true){
			Integer nextHop=myFIB.getNextHopAddress(destLL3PNetNumber);
			//myLL3P_Packet.setDstLL3P(nextHop);
			myLL3P_Packet.setTTL(myLL3P_Packet.getTTLvalue()-1);
			layer2Daemon.sendLL2PFrame(myLL3P_Packet.getByteLL3P(), arpTable.getLL2AddressFor(nextHop), NetworkConstants.LL3P_packet);
//		}
//		else
//			uiManager.raiseToast("I can't find dest LL3P NEED DEFAULT ROUTE");
	}
	/*
	 *  This method will provide a service to the application layer. It is given a payload and a destination address. It builds an LL3P packet and passes this packet 
	 *  to the method “sendLL3PPacket(LL3P)” for transmission.
	 */
	public void sendPayloadToLL3PDestination(Integer LL3PAddress, byte[] payload) {
			myLL3P_Packet.setSourceLL3P(NetworkConstants.LL3P_Address);
			myLL3P_Packet.setDstLL3P(LL3PAddress);
			myLL3P_Packet.setTYPE(NetworkConstants.LL3P_packet);
			Integer currentID=myLL3P_Packet.getIntID();
			myLL3P_Packet.setID(currentID+1);
			myLL3P_Packet.setTTL(255);
			myLL3P_Packet.setPayload(payload);
			sendLL3PPacket(myLL3P_Packet);
			
	}
	/*
	this method is given an LL3P address and an LL2P address. It uses these to either add a new entry to the ARP table, or remove an entry from the ARP table.
	*/
	public void ARPUpdate(Integer ll3pAddress, Integer ll2pAddress) {
		arpTable.addrOrUpdate(ll3pAddress, ll2pAddress);
	}

}
