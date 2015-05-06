/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: ARPTable_Entry
 * Date:3/13/2014
 */
package imported.lab4Layer1sim.lab4.ll2p;

import java.util.Calendar;

/*
 * the ARPTABLE ENTERY class provides the data structure for the entries in the ARP TABLE
 */
public class ARP_TableEntry implements Comparable<ARP_TableEntry>{
	private Integer ll2PAddress;
	private Integer ll3PAddress;
	private long lastTimeTouched;
	
	
	public ARP_TableEntry(){
		ll2PAddress=0;
		ll3PAddress=0;
		lastTimeTouched=0;
	}
	/*
	 * (non-Javadoc)
	 * Constructor:  ARP_TableEntry(Integer, Integer)-the constructor is passed two 
	 * addresses. it should set the time value for the age inseconds to the current time in 
	 * seconds
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public ARP_TableEntry(Integer ll3pAddress, Integer MACAddress){
		ll3PAddress=ll3pAddress;
		ll2PAddress=MACAddress;
		lastTimeTouched=System.currentTimeMillis()/1000;
	}
	public long getLastTimeTouched(){
		return lastTimeTouched;
	}
	public Integer getLL2PAddress(){//return requested LL2P address
		return ll2PAddress;
	}
	public Integer getLL3PAddress(){//return requested LL3P address
		return ll3PAddress;
	}
	//need to add COMMENTS
	public void updateTime(){//set the last time touched to the current time in seconds
		lastTimeTouched=Calendar.getInstance().getTimeInMillis()/1000;
	}
	public long getCurrentAgeInSeconds(){
		long forHowLong=Calendar.getInstance().getTimeInMillis()/1000-lastTimeTouched;
		return forHowLong;
	}
	
	@Override
	public int compareTo(ARP_TableEntry newEntry) {
		// TODO Auto-generated method stub
		int sameobject=0;
		int beforeobject=-1;
		int afterobject=1;
		return newEntry.ll3PAddress>this.ll3PAddress?afterobject
			   :newEntry.ll3PAddress<this.ll3PAddress?beforeobject:sameobject;
	}
	

}
