/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: ARPTable
 * Version:1
 * 
 */

package imported.lab4Layer1sim.lab4.ll2p;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ARPTable implements Runnable {
	private Set<ARP_TableEntry> table;
	private ArrayList<ARP_TableEntry> myARPTable;
	private Factory myFactory;
	
	public ARPTable(){
		table = new TreeSet<ARP_TableEntry>();
	}
    public void addEntry(Integer ll3Address, Integer ll2PAddress){//add the specified address pair to the table.
    	ARP_TableEntry newEntry = new ARP_TableEntry(ll3Address, ll2PAddress);
    	try{
    		table.add(newEntry);
    	}catch(Exception e){
    		table.add(newEntry);
    	}
    }
    public void getObjectReference(Factory parentFactory){
    	myFactory=parentFactory;
    	
    }
    /*
     * (non-Javadoc)
     * Given the LL3P Address, return the LL2P address associated with this address
     * this is the HEART of the ARP TABLE's purpose in LIFE
     * @see java.lang.Runnable#run()
     */
    public int getLL2AddressFor(Integer LL3PAddress){
    	Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	boolean found=false;
    	ARP_TableEntry tmp=null;
    	int returnLL2P=0;
    	while (tableIterator.hasNext() && !found){
    		tmp = tableIterator.next();
    		if(tmp.getLL3PAddress().equals(LL3PAddress)){
    			found=true;
    			returnLL2P=tmp.getLL2PAddress();
    		}
    	}
    		if(!found){}//do nothing
    		return returnLL2P;
    }
    //need to check you!!!
    public ARP_TableEntry removeLL2P(Integer MACToBeRemoved){//-remove the pair associated with the given LL2P address form the table
    	Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	boolean found=false;
    	ARP_TableEntry tmp=null;
    	while (tableIterator.hasNext() && !found){
    		tmp = tableIterator.next();
    		if(tmp.getLL2PAddress().equals(MACToBeRemoved)){
    			found=true;
    			table.remove(tmp);
    		}
    	}
    	if(!found){}//do nothing
    	return tmp;
    }	
    /*
     * return an ArrayList of ARP Table entries. this might be used by the UIManager at the same
     * point to display ARP Table Entries.
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public ArrayList<ARP_TableEntry> getARPTableAsList(){
    	ArrayList<ARP_TableEntry> returnList = new ArrayList<ARP_TableEntry>();
    	Iterator<ARP_TableEntry> tableIterator = table.iterator();
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
    
    
    public boolean Ll2PIsInTable(int LL2PAddress){//THis is a boolean method that returns true if the LL2P address is present in one of the 
    	//table pairs.
    	Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	boolean found=false;
    	ARP_TableEntry tmp=null;
    	while (tableIterator.hasNext() && !found){
    		tmp = tableIterator.next();
    		if(tmp.getLL2PAddress().equals(LL2PAddress)){
    			found=true;
    			return found;
    		}
    	}
    	if(!found){}//do nothing
    	return found;
    }
    public void updateBasedOnLL2P(int LL2PAddress){//i use this to get the pair of LL3PAddress and LL2PAddress
    	if (Ll2PIsInTable(LL2PAddress)){
    		Iterator<ARP_TableEntry> tableIterator= table.iterator();
        	boolean found=false;
        	ARP_TableEntry tmp=null;
        	while (tableIterator.hasNext() && !found){
        		tmp = tableIterator.next();
        		if(tmp.getLL2PAddress().equals(LL2PAddress)){
        			found=true;
        			tmp.updateTime();
        		}
        	}
        	if(!found){}//do nothing
    	}
    	if(!Ll2PIsInTable(LL2PAddress)){}
    	
    }
    public boolean LL3PIsInTable(int LL3PAddress){//THis is a boolean method that returns true if the LL3P address is present in one of the 
    	//table pairs.
    	Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	boolean found=false;
    	ARP_TableEntry tmp=null;
    	while (tableIterator.hasNext() && !found){
    		tmp = tableIterator.next();
    		if(tmp.getLL3PAddress().equals(LL3PAddress)){
    			found=true;
    			return found;
    		}
    	}
    	if(!found){}//do nothing
    	return found;
    }
    public void expireAndRemove(){//-this method exames all the ARP Table Entries and Removes any that are over the maximum age in seconds (currenty 60)
    	//Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	//boolean found=false;
    	ARP_TableEntry tmp=null;
    		
    		boolean oldAdjacent = false;
    		
    		Iterator<ARP_TableEntry> tableIterator = table.iterator();
    		
    		//RouteTableEntry entry = null;
    		while(tableIterator.hasNext())
    		{
    			tmp = tableIterator.next();
    			if(tmp.getCurrentAgeInSeconds()>60)
    			{
    				oldAdjacent = true;
    			}
    			if(oldAdjacent)
    			{
    				try
    				{
    					table.remove(tmp);
    				}
    				catch(Exception e)
    				{
    					e.printStackTrace();
    				}

    				tableIterator = table.iterator();
    			}
    		}
    		
    		
    	//}
    	//if(!found){}//do nothing
    }
    /*
    * This method is given an address pair. If the pair is already present it simply "touches"the 
    * the entry to update the last time references (this prevents the entry from aging). if the pair
    * is not there, then the pair is added to the table.
    * this will usually happen as a result of receiving an LL2P ARP_UPDATE or ARP_REPLY frame
    *
    */
    public void addrOrUpdate(Integer LL3PAddress, Integer LL2PAddress){
    	Iterator<ARP_TableEntry> tableIterator= table.iterator();
    	boolean found=false;
    	ARP_TableEntry tmp=null;
    	while (tableIterator.hasNext() && !found){
    		tmp = tableIterator.next();
    		if(tmp.getLL3PAddress().equals(LL3PAddress)&& tmp.getLL2PAddress().equals(LL2PAddress)){
    			found=true;
    			tmp.updateTime();
    		}
    	}
    	if(!found){
    		ARP_TableEntry newLL3PandLL2Pentries=new ARP_TableEntry(LL3PAddress, LL2PAddress);
    		table.add(newLL3PandLL2Pentries);
    	}//do nothing
    }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.expireAndRemove();
		
	}
}
