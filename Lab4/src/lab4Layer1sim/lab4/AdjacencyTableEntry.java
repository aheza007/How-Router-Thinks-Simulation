/*
 * Student Name: Desire AHEZA
 * Lab4:AdjacencyTableEntry
 * Version 1
 * Course: NETWORK SYSTEM DESIGN
 * Professor SMITH 
 */

package lab4Layer1sim.lab4;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * this is a class of objects which contain the
 * matched pair of an LL2P address and its actual IP address in the local network.
 */
	public class AdjacencyTableEntry implements Comparable<AdjacencyTableEntry>  {
	private InetAddress ipInetAddress;
	private String ipStringAddr;
	private Integer ll2pAddress;
	
	public AdjacencyTableEntry(){
		ipStringAddr=null;
		ipInetAddress=null;
		//ipAddr(0);
		ll2pAddress=0;
	}
	
	
	public AdjacencyTableEntry(Integer ll2pAdd, String ipadd){
	
		setipAddr(ipadd);
		
		ll2pAddress=ll2pAdd;
	}
	
	/* SETTERS METHODs
	 * A set method for the IP address as a String and 
	 * should also properly modify/set the InetAddress object.
	 */
		
	public void setipAddr(String newIPaddress){
		   ipStringAddr=newIPaddress; 
		   try { //getByName to change IP Address (string) to InetAddress object.
				ipInetAddress=InetAddress.getByName(newIPaddress);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}
	public void setipAddr(Integer newIPaddress){
		   ll2pAddress=newIPaddress; 
	}
	
	/*GETTERS MOTHODS
	 * 
	 * 
	 */
	public InetAddress getIpInetAddress() {
		return ipInetAddress;
	}
	public String getipAddr(){
		return ipStringAddr;
	}
	public Integer getMACAddress() {
		return ll2pAddress;
	}
	
	/*toString Method for debugging and display
	 * 
	 *LL2P: 0x<LL2P hex address here>   IP:  <IP Here>
	 */
	public String toString(){
		String returnString= Integer.toHexString(getMACAddress());
		returnString="LL2P: 0x"+returnString+"  IP:"+ipStringAddr ;
		return returnString;
	}
	/*provide the ability for the Set Interface to compare these objects.  
	 Provide a method in this class that accepts 
	 an Object and compares it to the “this” object. 
	 Return -1 if this is “before”, return 0 if this is the same object as
	 the one being compared,and return +1 if this is “after” the object being compared
	*/

	@Override
	public int compareTo(AdjacencyTableEntry newEntry) {
		int sameobject=0;
		int beforeobject=-1;
		int afterobject=1;
		
		return newEntry.ll2pAddress>this.ll2pAddress?afterobject : newEntry.ll2pAddress < this.ll2pAddress?beforeobject:sameobject;
	} 
	
	}