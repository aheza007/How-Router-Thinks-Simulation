/*
 * Student Name: Desire AHEZA
 * Lab4:AdjacencyTable
 * Version 1
 * Course: NETWORK SYSTEM DESIGN
 * Professor SMITH 
 */


package lab4Layer1sim.lab4;

import imported.lab4Layer1sim.lab4.ll2p.UIManager;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

		/*
		 * The Adjacency table maintains a list of AdjacencyTableEntry 
		 * objects in a collection. 
		 */
		public class AdjacencyTable {// the table uses the Set interface!
			
		public	Set<AdjacencyTableEntry> table; 
			
			
			public AdjacencyTable(){// treeset fits my use nicely!
		
				table = new TreeSet<AdjacencyTableEntry>(); 
				
			}
			
		/*
		 * AddEntry-it adds an entry to the Layer 1 adjacency table
		 * LL2PAddressMAC probably from an input field from the user screen
		 * NewIPAddress is the IP address of the remote android router simulator in dotted
		 * decimal notation*nextHOP IP ADDRESS*
		 * in General,this method is passed an LL2P address as an Integer and an 
		 * IP address that the LL2P address points to.  The LL2P address
		 * is an address of a node our lab network 
		 * (someone else’s Android device) that you are “adjacent” to.  
		 */
		public void addEntry(Integer LL2PAddressMAC, String NewIPAddress){
			AdjacencyTableEntry newEntry = new AdjacencyTableEntry(LL2PAddressMAC,NewIPAddress);
			try{
				table.add(newEntry);
			}catch(Exception e){
				table.add(newEntry);
			}
		}
		public AdjacencyTableEntry removeEntry(Integer MACToBeRemoved){
			Iterator<AdjacencyTableEntry> tableIterator = table.iterator();
			boolean found=false;
			AdjacencyTableEntry tmp=null;
			while(tableIterator.hasNext() && !found){
				tmp = tableIterator.next();
				if(tmp.getMACAddress().equals(MACToBeRemoved)){
					found = true;
					table.remove(tmp);
				}
			}
			if(!found){}//do nothing
			return tmp;
				}
		public InetAddress getIPAddressForMAC(Integer IpKeyMAC){
			Iterator<AdjacencyTableEntry> tableIterator = table.iterator();
			
			boolean found=false;
			AdjacencyTableEntry tmp=null;
			InetAddress returnIP=null;
			while(tableIterator.hasNext() && !found){
				tmp = tableIterator.next();
//				String tmpp=String.valueOf(tmp.getMACAddress());
//				Integer ctmp=Integer.valueOf(tmpp, 16);
				if(tmp.getMACAddress().equals(IpKeyMAC)){
					found = true;
					returnIP=tmp.getIpInetAddress();
				}
			}
			if(!found){}//do nothing
			return returnIP;
		}
		
	}

