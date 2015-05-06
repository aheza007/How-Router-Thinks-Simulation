/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: ARPDaemon
 * Date:3/13/2014
 * 
 * 
 */
package imported.lab4Layer1sim.lab4.ll2p;

import lab4Layer1sim.lab4.LL2PDaemon;

public class ARPDaemon {
	
	Factory myFactory;
	ARPTable myARPTable;
	LL2PDaemon myLL2PDaemon;
	public ARPDaemon(){
		
	}
	public void getObjectReferences(Factory parentFactory){
		myFactory=parentFactory;
		myARPTable=myFactory.getARTableObjectReference();//get ARP Table Reference
		myLL2PDaemon=myFactory.getLL2PDaemonObjectReference();
	}
	/*
	 * this method just call the ARP table to add or update this address pair. this will be called
	 * by the layer2Daemon when an ARP update is sent or received
	 */
	public void addOrUpdate(Integer layer3Address, Integer layer2Address){
		myARPTable.addrOrUpdate(layer3Address, layer2Address);
	}
}
