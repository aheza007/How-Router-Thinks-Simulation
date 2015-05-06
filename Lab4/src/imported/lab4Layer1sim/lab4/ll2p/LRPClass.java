/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: LRPClass
 * Date:3/13/2014
 * 
 * 
 */

package imported.lab4Layer1sim.lab4.ll2p;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import lab4Layer1sim.lab4.NetworkDistancePairClass;

/*
 * this class constructs LRP packets for transmission and receive and decode LRP Packets which arrive at Layer 2
 */
public class LRPClass {
	
	private Integer sourceLL3PAddress;//this is the address of the router that sent you this Lab Routing Protocol packet.
	private Integer lrpCOUNTER;//this is a counter of the number of LRPs’ received so far and which one this is.
	private Integer sequenceNumber;// – this is used by the sending router to help you avoid entering duplicate LRP’s in the RouteTable (Created last week).  If you have already received an LRP you can discard one from the same source with the same sequence number.
   // private Integer routeCount;// – this is the number of routes present in this LRP.  
	private NetworkDistancePairClass myPair;
	private ArrayList<NetworkDistancePairClass> NetDistPairList;// – this is an ArrayList or networkDistancePairs – this will be what goes into the LRP packet.
	private ArrayList<RouteTableEntry> myForwardingTablelist;
	//private ForwardingTable myForwardingTable;
//	private Factory myFactory;
	private Integer sourceLL3POffest    =0;
	private Integer sourceLength        =4;
//	private Integer sequenceNumLength   =2;
//	private Integer countNumberLength   =2;
	private Integer sequenceNumLength   =1;
	private Integer countNumberLength   =1;
	private Integer sequenceNumberOffest=sourceLL3POffest+sourceLength;
	private Integer countNumberOffest   =sequenceNumberOffest + sequenceNumLength;
	private Integer myNetworkPairOffest =countNumberOffest+countNumberLength ;
	private Factory myFact;
	
	
	public LRPClass() {//– a constructor to create an empty LRP.
		sourceLL3PAddress=0;
		lrpCOUNTER=0;
		sequenceNumber=0;
		//routeCount=0;
		NetDistPairList = new ArrayList<NetworkDistancePairClass>();
		//NetDistPairList=null;
		
	}
	public void getObjectReferences(Factory myFactory){
		myFact=myFactory;
		myPair=myFactory.getNetworkDistancePairObjectReference();
		
	}
	
	
	public LRPClass(Integer myLL3PAddress, ForwardingTable forwardingTable, Integer destLL3Paddress){// – this is a constructor that is given all the information necessary to create an LRP packet for transmission. ThatWillreceiveThisLRPPacket
		
		sourceLL3PAddress=myLL3PAddress;
		NetDistPairList = new ArrayList<NetworkDistancePairClass>();
		setLRPPacket(forwardingTable, destLL3Paddress);
	}
	public LRPClass(byte[] receivedLRPPacket){//this is a constructor that receives a byte array and deconstructs it to build an LRP object which can be used to update forwarding tables.
		NetDistPairList = new ArrayList<NetworkDistancePairClass>();
		fillInFromBytes(receivedLRPPacket);	
	}
	public void fillInFromBytes(byte[] receivedLRPPacket){// this is a method that is called by the constructor and possibly other methods and can actually do the work of decoding the bytes received into the object, setting member values.
		String newLRPFrame=new String(receivedLRPPacket);
		
		//setSourceLL3P(Integer.parseInt(newLRPFrame.substring(sourceLL3POffest, sourceLength)));
		setSourceLL3P(Integer.parseInt(newLRPFrame.substring(sourceLL3POffest, sourceLength),16));
		setSequenceNumber(Integer.parseInt(newLRPFrame.substring(sequenceNumberOffest, countNumberOffest),16));
		setLRPCOUNTER(Integer.parseInt(newLRPFrame.substring(countNumberOffest,myNetworkPairOffest),16));
		String pairs=newLRPFrame.substring(myNetworkPairOffest,newLRPFrame.length());//this holds the LRP PACKET
		for(int i=0;i<pairs.length();i=i+4){//this will take all my pairs and add it in arraylist
			try{
			setPairs(pairs.substring(i,i+4));// call method to add in Array List
			}
			catch(NumberFormatException e){
				
				Integer nn=Integer.parseInt(pairs.substring(i,i+4),16);
	//		.String nnn=Utilities.padHexString(nn.toString(nn, 16),2);
				setPairs(nn);
			}
		
		}
		
	}
    
	//Setters for the source LL3P and routes (given a forwarding table and the address this packet will be sent to). 
	
	public void setSourceLL3P(Integer sourceLL3P){
		sourceLL3PAddress=sourceLL3P;
	}
	
	public void setLRPPacket(ForwardingTable myForwardingTable, Integer destLLP3){
		//sequenceNumber=1;
		myForwardingTablelist=myForwardingTable.getFIBExcludingLL3PAddress(destLLP3);
		setSourceLL3P(Integer.parseInt(NetworkConstants.LL3P_Address));
		
		setSequenceNumber(0);
		
		setLRPCOUNTER(myForwardingTablelist.size());
		//String LRPString=NetworkConstants.LL3P_Address+"01";
		//String LRPString=NetworkConstants.LL3P_Address+Utilities.padHexString(getSequenceNum().toString(),1)+Utilities.padHexString(getLRPCOUNTER().toString(),1);
		String LRPString=NetworkConstants.LL3P_Address+Integer.toHexString(getSequenceNum())+Integer.toHexString(getLRPCOUNTER());
		String pair=null;
		
		
		for (RouteTableEntry entry : myForwardingTablelist){
			pair=entry.getNetworkDistancePair().toString();
			//01011209010100||0101010209010100
			LRPString=LRPString + Utilities.padHexString(Integer.toHexString(entry.getNetworkDistancePair().getNetworkNumber()),1)+Utilities.padHexString(Integer.toHexString(entry.getNetworkDistancePair().getDistance()),1);
		
		}
		//0101010906
		String pairs=LRPString.substring(6,LRPString.length());//this holds the LRP PACKET
		for(int i=0;i<pairs.length();i=i+4){//this will take all my pairs and add it in arraylist
			
			Integer newPair=Integer.parseInt(pairs.substring(i,i+4),16);
			
			setPairs(newPair);// call method to add in Array List
		}
		
	}
	
	public void setLRPCOUNTER(Integer newLRPCount){
		lrpCOUNTER=newLRPCount;
	}
	
	public void setSequenceNumber(Integer newSeq){
		sequenceNumber=newSeq;
	}
		
	//Getters for the source address, routes as a list of networkDistancePairs
	
	public Integer getLRPCOUNTER(){
		return lrpCOUNTER;
	}
	public Integer getSourceLL3Address(){
		return sourceLL3PAddress;
	}
	
	public void setPairs(String newPair){//this method helps in building an arraylist of NETWORKDISTANCEPAIR
		
		Integer nn=(Integer.parseInt(newPair.substring(0, newPair.length()/2)));
		myPair=new NetworkDistancePairClass((Integer.parseInt(newPair.substring(0, newPair.length()/2),16)),(Integer.parseInt(newPair.substring(newPair.length()/2,newPair.length()),16)));
		NetDistPairList.add(myPair);
		
	}
	
	
    public void setPairs(Integer nPair){//this method helps in building an arraylist of NETWORKDISTANCEPAIR
		String newPair=Integer.toHexString(nPair);
		//Integer nn=(Integer.parseInt(newPair.substring(0, newPair.length()/2)));
		myPair=new NetworkDistancePairClass((Integer.parseInt(newPair.substring(0, newPair.length()/2),16)),(Integer.parseInt(newPair.substring(newPair.length()/2,newPair.length()),16)));
		NetDistPairList.add(myPair);
		
	}
    
	
	public Integer getSequenceNum(){
		return sequenceNumber;
	}
	
	public ArrayList<NetworkDistancePairClass> getNetworkPair(){
		
		return NetDistPairList;
	}
	
	public String getNetworkPairString(){
		String pairSTRING="";
		for(NetworkDistancePairClass entry: NetDistPairList){
			pairSTRING=pairSTRING+entry.toNoNAMEString();
		}
		
		return pairSTRING;
	}
	
	
	
    public byte[] getLRPPacketBytes(){//this returns a byte array properly formatted for an LRP packet to be encapsulated by an LL2P Frame.
    	//String frameString= new String(this.getNetworkPairString());
    	String frameString=new String(Utilities.padHexString(sourceLL3PAddress.toString(),2));
               frameString=frameString+ Integer.toHexString(getSequenceNum()) + getLRPCOUNTER() +this.getNetworkPairString();
               //frameString=frameString+ getSequenceNum().toString()  + getLRPCOUNTER().toString() +this.getNetworkPairString();
    	return frameString.getBytes();
    }
    
//    public Integer getRouteCount(){//a method to return the number of routes in the LRP packet (or this object).
//    		return routeCount;
//    
//    }

    
    public String toString(){//a method to convert the object to a string that can be converted to bytes
		String returnString = new String(sourceLL3PAddress.toString());
		
		returnString = returnString + getSequenceNum() + getLRPCOUNTER() + getNetworkPair();
		return returnString;
	}
}
