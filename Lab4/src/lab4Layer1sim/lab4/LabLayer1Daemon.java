/*
 * Student Name: Desire AHEZA
 * Lab4:Layer1Daemon
 * Version 1
 * Course: NETWORK SYSTEM DESIGN
 * Professor SMITH 
 * Date:3/13/2014
 */

package lab4Layer1sim.lab4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.os.AsyncTask;

import imported.lab4Layer1sim.lab4.ll2p.Factory;
import imported.lab4Layer1sim.lab4.ll2p.LL2P;
import imported.lab4Layer1sim.lab4.ll2p.NetworkConstants;
import imported.lab4Layer1sim.lab4.ll2p.UIManager;

/*
 * 	The this class has two  jobs:  
 *	1.	Send data to a specified LL2P Address.
 * 	2.	Continuously listen for incoming UDP packets and 
 * 		do something intelligent with them when they arrive
 * 
 *
 */
public class LabLayer1Daemon {

	LL2P ll2PFrame;
	UIManager uiManager;
	AdjacencyTable MAC_IPAddressTable;
	LL2PDaemon myLL2PDaemon;
	// define constant for port
	private final static int portNumber=35000;
	private final static int receivePort=35000;
	
	//define a socket
	DatagramSocket sendSocket;
	DatagramSocket receiveSocket;
	
	
	/*Constructor- We need the factory reference to get the LL2P reference. the Factory
	 * needs to call this last..this "timing" needs to be removed in order to eliminate
	 * the binding between these classes
	 */
	public LabLayer1Daemon(){
		MAC_IPAddressTable=new AdjacencyTable();
		openUDPPort(); // opens the UDP ports and initializes them
		new listenForUDPPacket().execute(receiveSocket);
	}
	/*
	 * This method is called by the factory and calls the factory back to get references need by the Layer 1 daemon. 
	 * As of this writing the Layer 1 Daemon needs references to the LL2P  frame object and the UIManager.  
	 * Later it will need a reference to the layer 2 Daemon so it can pass the LL2P object up to the Layer
	 * 2 daemon for processing.
	 */
public void getObjectReferences(Factory myFactory){
	ll2PFrame=myFactory.getLL2PObjectReference();
	uiManager=myFactory.getUIManager();
	myLL2PDaemon=myFactory.getLL2PDaemonObjectReference();
	new listenForUDPPacket().execute(receiveSocket);
	//MAC_IPAddressTable.addEntry(123456,"10.4.240.114");
	//MAC_IPAddressTable.addEntry(128886,"10.4.240.94");
	//MAC_IPAddressTable.addEntry(0xfabce1,"10.4.240.153"); // add an entry for LL2P MAc address 1 & IP shown
	//  MAC_IPAddressTable.addEntry(66051,"192.168.1.120");// add an entry for LL2P MAC address 314159
	//MAC_IPAddressTable.addEntry(010203,"192.168.1.120");
}
/*
 * This method is passed the LL2P address as an integer, along with the associated 
 * IP address of the target Android device.  The method uses the adjacency table 
 * to add this entry into the table.
 * 
 */
public void setAdjacency(Integer fromLL2PMAC,String targetIPaddr){
	MAC_IPAddressTable.addEntry(fromLL2PMAC,targetIPaddr);
	myLL2PDaemon.sendARPUpdate(fromLL2PMAC);
	//uiManager.raiseToast((MAC_IPAddressTable.getIPAddressForMAC(fromLL2PMAC)).toString());
	
}
/* 
 * this method is given an LL2P address and asks the 
 * table to remove this pair from the adjacency table.  Why? Perhaps the target node went down… 
 */
public void removeAdjacency(Integer MACToBeRemoved){
	MAC_IPAddressTable.removeEntry(MACToBeRemoved);
} 
/*
 * this method returns a List object containing all the adjacencies 
 * currently in the router. Fortunately, this is simply the adjacency table. 
 * 
 */
public AdjacencyTable getAdjacencyList(){
	return MAC_IPAddressTable;
}
/*
 *  This method simply grabs the current LL2P Frame in the system (you should have asked the factory
 *  for it in the “getObjectReferences()” method). This method is very simple – it simply takes that 
 *  frame and passes it to the “SendLL2PFrame(LL2P)” method.
 * 
 */
public void  sendLL2PFrame(){
	sendLL2PFrame(ll2PFrame);
} 
public void sendLL2PFrame(LL2P frame){
	
	String frameToSend= new String(frame.toString());
	
	boolean foundValidAddress=true;
	/*
	 * here we get the IP address for the MAC frame from the AdjacencyTable and use it to fetch
	 * the actual Internet address data structure for use in opening the port.
	 */
	InetAddress IPAddress=null;
	try{
		IPAddress=MAC_IPAddressTable.getIPAddressForMAC(frame.getDestinationAddress());
		if (IPAddress==null)
		throw new LabException("NOT FOUND");
	}catch(LabException e){
		foundValidAddress=false;
	}
	//String ipadrr=IPAddress.getHostAddress();
	if(foundValidAddress){
		// create datagram for sending.
		
//		uiManager.raiseToast("Found destination IP: "+IPAddress.toString());
		         // frame.getDestinationAddressHexString());//,Toast.LENGTH_LONG);
		DatagramPacket sendPacket=new DatagramPacket(frameToSend.getBytes(),//string converted to bytes
		                                frameToSend.length(), // number of bytes
		                                IPAddress,//IP address retrieved above
		                                portNumber);// port 9999 as of feb 1,2012
		// send the packet to the remote system. Use the Async task private class for this.
		
		new SendUDPPacket().execute(new PacketInformation(sendSocket,sendPacket));
	}
	else{
		uiManager.raiseToast("Attempt to send unknown LL2P: "+ frame.getDestinationAddressHexString());//,Toast.LENGTH_LONG);
		//Integer.parseInt(frame.getDestinationAddressHexString())
		//MAC_IPAddressTable.addEntry(frame.getDestinationAddress(), "10.4.240.114");
		//sendLL2PFrame(frame);//suspended because i can't find the corresponding IP Address
	}
	
 }
  public void openUDPPort(){
	  /*
	   * Datagram socket opens socket for sending a UDP packet.Here we simply open a UDP port
	   */
	  sendSocket=null; // defined in case the open port method fails
	  // try to open the socket.This is a send socket so we don't declare the port number
	  try{
		  sendSocket= new DatagramSocket();
	    }catch(SocketException e){
	    	e.printStackTrace();
	    }
	  // open the receive port.declare the port number to listen on.
	  receiveSocket=null;
	  try{
		  receiveSocket= new DatagramSocket(receivePort);// receive port defined above
	    }catch(SocketException e){
	    	e.printStackTrace();
	    }
	  }

  /** send UDP Packet
	 * A private Async class to send packets out of the UI thread.
	 */
	private class SendUDPPacket extends AsyncTask<PacketInformation, Void, Void> {
		@Override
		protected Void doInBackground(PacketInformation... arg0) {
			PacketInformation pktInfo = arg0[0];
			try {
				pktInfo.getSocket().send(pktInfo.getData());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}	
		

 }
	
	/*
	 * allow you to create a thread that will be given the socket
	 *  to listen for, and a method for returning the data once the data is received
	 */
	private class listenForUDPPacket extends AsyncTask<DatagramSocket,Void,byte[]>{
		
		@Override
		/*
		 * doInBackground method is the code that runs on a separate thread. Since your read 
		 * is posted on a separate thread, the rest of the router can continue to do its work.
		 * This mothod is not running on the thread that your user interface runs on. 
		 * it returns a byte array and let the main thread do the rest of the work
		 */
		protected byte[] doInBackground(DatagramSocket... socketList) {
			// TODO Auto-generated method stub
			byte[] receiverData = new byte[1024];//1024 is the maximum size 
			DatagramSocket ServerSocket=socketList[0];
			
			DatagramPacket receivePacket =new DatagramPacket(receiverData,receiverData.length);
			boolean timeout=true;
			try{	
				timeout=false;
				ServerSocket.setSoTimeout(100);
				ServerSocket.receive(receivePacket);
				
			}catch(IOException e){
				timeout=true;
				e.printStackTrace();
				
			}
			byte[] rxData;
			if (timeout){
				rxData=new String ("").getBytes();
			}
			else{
				int bytesReceived=receivePacket.getLength();
				rxData = new String(receivePacket.getData()).substring(0, bytesReceived).getBytes();
			}
			return rxData;
			
			}
		
	/*
	 * This method is executed back on the main thread and may access the screen through the UI, 
	 * as well as access other memory locations and objects. 
	 * This method receives the return value from “doInBackground”, which will be your byte array.
	 * You can pass it to the LL2P frame method I showed above to update the LL2P Packet, 
	 * 
	 */
		@Override
	protected void onPostExecute(byte[] rxData){
		
		String temp=new String(rxData);
		if(temp.length()>0){
			ll2PFrame.fillInLL2PFrame(rxData);
			if(ll2PFrame.getType()==NetworkConstants.LL2P_Echo_Reply||ll2PFrame.getType()==NetworkConstants.LL2P_Echo_Request
					||ll2PFrame.getType()==NetworkConstants.ARP_REPLY_TYPE||ll2PFrame.getType()==NetworkConstants.ARP_UPDATE_LL2P_Type
					||ll2PFrame.getType()==NetworkConstants.LRP_Update||ll2PFrame.getType()==NetworkConstants.Lab_Routing_Protocol
					||ll2PFrame.getType()==NetworkConstants.LL3P_packet){
				
				myLL2PDaemon.receiveLL2PFrame(rxData);
			//	uiManager.raiseToast(temp);
				//uiManager.updateLL2PLayout();
			}
		//else	
			//uiManager.raiseToast(temp);		
			
		}
		new listenForUDPPacket().execute(receiveSocket);	
	}
	} 
private class PacketInformation{
	DatagramSocket newSocket;
	DatagramPacket newPacket;
	
	PacketInformation(DatagramSocket sendSocket,DatagramPacket sendPacket){
		newSocket=sendSocket;
		newPacket=sendPacket;
	}
	
	public DatagramSocket getSocket(){
		return newSocket;
	}
	public DatagramPacket getData(){
		return newPacket;
	}
}

}
	

