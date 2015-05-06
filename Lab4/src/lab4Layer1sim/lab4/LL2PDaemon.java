/*
 * Student Name: Desire AHEZA
 * Lab5:LL2PDaemon
 * Version 1
 * Course: NETWORK SYSTEM DESIGN
 * Professor SMITH 
 */

package lab4Layer1sim.lab4;

import imported.lab4Layer1sim.lab4.ll2p.ARPDaemon;
import imported.lab4Layer1sim.lab4.ll2p.CRC16;
import imported.lab4Layer1sim.lab4.ll2p.Factory;
import imported.lab4Layer1sim.lab4.ll2p.LL2P;
import imported.lab4Layer1sim.lab4.ll2p.LL3PDaemon;
import imported.lab4Layer1sim.lab4.ll2p.LRPDaemonShell;
import imported.lab4Layer1sim.lab4.ll2p.NetworkConstants;
import imported.lab4Layer1sim.lab4.ll2p.SoundPlayer;
import imported.lab4Layer1sim.lab4.ll2p.UIManager;

/*
 * The layer 2 Daemon class is a class that receives frames from layer 1 Daemon and dispatches them appropriately 
 * (or replies to LL2P echo requests).  This process also works for upper level protocols by accepting requests 
 * to transmit a payload to an LL2P address specified by the upper level protocol.  
 * 
 */
public class LL2PDaemon {
	private Factory myFactory;
	private LabLayer1Daemon layer1Daemon;
	private UIManager uiManager;
	private LL2P ll2Pframe;
    private String dataEcho,destinationAddress,type;
	private Integer localAddress;
	String mylocalAddress;
	CRC16 receivedCRC;
	private ARPDaemon myARPDaemon;
	private LRPDaemonShell myLRPDaemonShell;
	private LL3PDaemon myLL3PDaemon;
//	private int recCRC,crcCompleteMsge;
	private SoundPlayer mySound;

	
	public LL2PDaemon(int myMAC){

		localAddress=myMAC;
		 
	}
	//	Standard method called by factory. This method calls the factory 
	//	to get references to objects such as the layer 1 Daemon and the uiManager.
	public void getObjectReferences(Factory Factory){
		myFactory=Factory;
		mySound=Factory.getSoundPlayer();
		layer1Daemon=myFactory.getLL1DObjectReference();
		uiManager=myFactory.getUIManager();
		ll2Pframe=myFactory.getLL2PObjectReference();
		myARPDaemon=myFactory.getARPDaemonObjectReference();
		myLRPDaemonShell=myFactory.getLRPDaemonShellReference();
		myLL3PDaemon=myFactory.getLL3PDaemonObjectReference();
		//networkconstants=myFactory.getNetworkConstants();
	}
	//	This method allows the program to change its LL2P address.
	//	We may never use it, but perhaps there will be a time we want to spoof something… 
	public void setLocalLL2PAddress(Integer ll2Paddress){
		ll2Pframe.setSourceAddress(ll2Paddress);
		
	}
	
	//	This is called by higher layers (LL3P, LRP, etc).This method builds an LL2P Frame using the destination 
	//	address,type, and payload.It transmits using the layer 1 Daemon.
    public void	sendLL2PFrame(byte[] payload, Integer destinationLL2PAddress, Integer LL2PType){

		String payloadString=new String(payload);
		
		LL2P myFrame=new LL2P(Integer.toHexString(destinationLL2PAddress),NetworkConstants.MY_LL2P_ADDRESS,Integer.toHexString(LL2PType),payloadString);
		sendLL2PFrame(myFrame);
	}
//    this method receives an LL2P frame passed to the itself 
//    from a higher layer or the daemon itself. This method uses the layer 1 daemon to actually transmit the frame.
  
   public void sendLL2PFrame(LL2P receivedFrame){
	    mySound.playSound(2);
    	layer1Daemon.sendLL2PFrame(receivedFrame);
    }

//     an LL2P Echo request with the payload provided to the LL2P target address.
   public void sendLL2PEchoRequest(String payload, Integer LL2PAddress){
		destinationAddress=Integer.toHexString(LL2PAddress);
		type=Integer.toHexString(NetworkConstants.LL2P_Echo_Request);
	   	ll2Pframe=new LL2P(destinationAddress,NetworkConstants.MY_LL2P_ADDRESS,type,payload);
	    
	   	sendLL2PFrame(ll2Pframe);
   	
   }
//   A private method, which when given the EchoRequest frame,
//   pulls out the appropriate fields,constructs an LL2P echo reply frame, and transmits it to the original sender of the request. 
//   If the original sender is not in the Layer 1 Adjacency table what will you do?
   private void replyToEchoRequest(LL2P echoRequestFRAME){
	   destinationAddress=echoRequestFRAME.getSourceAddressHexString();
	   mylocalAddress=echoRequestFRAME.getDestinationAddressHexString();
	   myFactory.getNetworkConstants();
	   type=Integer.toHexString(NetworkConstants.LL2P_Echo_Reply);
	   dataEcho="Here I am, how about u";
	   ll2Pframe=new LL2P(destinationAddress,mylocalAddress,type,dataEcho);
	  // uiManager.updateLL2PLayout();
	   //send a reply Echo when no destination IP address raise a toast "Attempt to send unknown LL2P"
	   uiManager.raiseToast("THE ECHO REPLY:"+ ll2Pframe.toString());
	   sendLL2PFrame(ll2Pframe);
	   
   }
//   this is called by layer 1.  This method checks CRC, checks destination address to be sure 
//   it’s “us”, and if everything is good then it uses the Type of the frame received to dispatch the frame to the appropriate process.
   public void receiveLL2PFrame(LL2P receiveEcho){
	    
	  if (receiveEcho.checkCRC()==false){
	   Integer type=receiveEcho.getType();
		  //uiManager.raiseToast("BAD CRC PROBABLY ERRORs IN RECEIVED FRAME ");
		  //uiManager.updateLL2PLayout();
	    	if (receiveEcho.getDestinationAddress()==localAddress){
	    		if (receiveEcho.getType()==NetworkConstants.Lab_Routing_Protocol){//treat LRP Packet
	    			
	    			myLRPDaemonShell.receiveNewLRP(receiveEcho.getPayloadString().getBytes(),receiveEcho.getSourceAddress());
	    			//uiManager.raiseToast("RECEIVED LRP!!!: "+ receiveEcho.getPayloadString());
	    			//uiManager.myROUTINGTRICS();
	    		   //myLRPDaemonShell.mytestRUN();
	    		}
		    	if(receiveEcho.getType()==NetworkConstants.LL2P_Echo_Request){//replying an echo request
		    		replyToEchoRequest(receiveEcho);
		    		//uiManager.raiseToast("I have responded to U!!!");
		    	}
		    	else if(receiveEcho.getType()==NetworkConstants.LL2P_Echo_Reply){//receiving an Echo request
		    		String echoReceived=new String(receiveEcho.getFrameByte());
		    		uiManager.raiseToast("RECEIVED ECHO!!!: "+echoReceived);
		    	}
		    	else if(receiveEcho.getType()==NetworkConstants.ARP_UPDATE_LL2P_Type){
		    		//uiManager.raiseToast("RECEIVED ARP UPDATE!!!: "+receiveEcho.toString());
		    		myARPDaemon.addOrUpdate(Integer.parseInt((receiveEcho.getPayloadString()),16), Integer.parseInt(receiveEcho.getSourceAddressHexString(),16));
		    		sendARPReply(Integer.parseInt((receiveEcho.getSourceAddressHexString()),16));
		    		
		    	}
		    	else if (receiveEcho.getType()==NetworkConstants.ARP_REPLY_TYPE){
		    		
		    		uiManager.raiseToast("RECEIVED ARP REPLAY!!!: "+receiveEcho.toString());
		    	}
		    	else if(receiveEcho.getType()==NetworkConstants.LL3P_packet){
		    		String ll3P=receiveEcho.getPayloadString();
		    		
		    		myLL3PDaemon.receiveLL3PPacket(receiveEcho.getPayloadString().getBytes(),receiveEcho.getSourceAddress());
		    	}
		    	else
		    		uiManager.raiseToast("UNKNOWN TYPE ");
		    }
	    else
	    	uiManager.raiseToast("NOT MINE ");
	    	
	    }
	    else
	    	{
	    	mySound.playSound(1);
	    	uiManager.raiseToast("the calcutated CRC :"+ receiveEcho.getCRCHexString());
	    	uiManager.updateLL2PLayout();
	    	}
	   
   }
 //badPacketSound = 1;packetSentSound = 2;buttonPressSound = 3;receiveMessageSound = 4;sendMessageSound = 5;alertSound = 6;
   public void receiveLL2PFrame(byte[] FrameInByte)
		{// to receive frame 
	   mySound.playSound(4);
	   LL2P frame=new LL2P(FrameInByte);
	    //ll2Pframe.fillInLL2PFrame(FrameInByte);
		receiveLL2PFrame(frame);
		
	}
   public void sendARPUpdate(Integer LL2PNode){
	    ll2Pframe=new LL2P(Integer.toHexString(LL2PNode), NetworkConstants.MY_LL2P_ADDRESS, Integer.toHexString(NetworkConstants.ARP_UPDATE_LL2P_Type), NetworkConstants.LL3P_Address); 
	    sendLL2PFrame(ll2Pframe);
   }
   public void sendARPReply(Integer ll2Address){
	   ll2Pframe=new LL2P(Integer.toHexString(ll2Address), NetworkConstants.MY_LL2P_ADDRESS, Integer.toHexString(NetworkConstants.ARP_REPLY_TYPE), NetworkConstants.LL3P_Address); 
	   sendLL2PFrame(ll2Pframe);
	   
   }
   

  
   
}
