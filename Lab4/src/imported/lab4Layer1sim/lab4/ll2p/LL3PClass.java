/*
 * StudName: Desire AHEZA
 * Class: Network SYSTEM DESIGN
 * CLASS: LL3P
 * Date:4/3/2014
 * 
 * 
 */

package imported.lab4Layer1sim.lab4.ll2p;

public class LL3PClass {

	Integer sourceLL3P;//store LL3P source
	Integer destLL3P;//store LL3P destination 
	Integer packetTYPE;//store LL3P type
	Integer id;//Packet Identifier
	Integer timeTTL;//Time To Leave of a Packet
	byte[] payload;//store LL3P payload
	
	//offSET DEFINITION
	
	Integer srcLL3POffSet=0;//offSET DEFINITION 
	Integer srcLL3PLength=4;//offSET DEFINITION for the source field
	Integer dstLL3PLength=4;
	Integer typeLength=4;
	Integer idLength=4;
	Integer lengthTTL=2;
	Integer checksumLen=4;
	
	Integer dstLL3pOffst=srcLL3PLength+srcLL3POffSet;//offSET DEFINITION for the destination field
	Integer typeOffst=dstLL3pOffst+dstLL3PLength;
	Integer idOffst=typeOffst+typeLength;
	Integer ttlOffst=idOffst+idLength;
	Integer payloadOffst=ttlOffst+lengthTTL;
	
	public LL3PClass(){//default CONSTRUCTOR to INITIALIZE ALL FIELDs to ZERO
		 	createLL3();
	}
	
	public void createLL3(){//to initialize all variable to zero
		sourceLL3P=0;
		destLL3P=0;
		packetTYPE=0;
		id=0;//Packet Identifier
		timeTTL=0;//Time To Leave of a Packet
		payload="".getBytes();
	}
	
	public LL3PClass(String srcAddr, String destAddr, String type, String ID, String TTL, String payload){// used to construct the LL3P packet
		createLL3();
		setSourceLL3P(srcAddr);
		setDstLL3P(destAddr);
		setTYPE(type);
		setID(ID);
		setTTL(TTL);
		setPayload(payload);
		//NEED TO DO CHECKSUMMMMMMMMM
	}
	
	public LL3PClass(byte[] newLL3Ppacket){//used to construct ll3p packet based on a byte ARRAY
		String newLL3Ppckt=new String (newLL3Ppacket);
		setSourceLL3P(newLL3Ppckt.substring(0, dstLL3pOffst));
		setDstLL3P(newLL3Ppckt.substring(dstLL3pOffst,typeOffst));
		setTYPE(newLL3Ppckt.substring(typeOffst, idOffst));
		setID(newLL3Ppckt.substring(idOffst, ttlOffst));
		String ttlvalue=newLL3Ppckt.substring(ttlOffst, payloadOffst);
		setTTL(newLL3Ppckt.substring(ttlOffst, payloadOffst));
		setPayload(newLL3Ppckt.substring(payloadOffst, newLL3Ppckt.length()-4));//the 4 length is reserved for the CHECKSUMq
		setCHECKSUM(newLL3Ppckt.substring(newLL3Ppckt.length()-4, newLL3Ppckt.length()));
		
	}
		/*
		 * Void set[field] ](String inputValue)
		 * this method is passed a string value (usually a string of Hex characters) which is then converted to the appropriate object for the class.
		 */
	public void setSourceLL3P(String newHexSRC){//set Source LL3P
		sourceLL3P=Integer.valueOf(newHexSRC, 16);
	}
	
	public void setDstLL3P(String newHexDST){//set Destination LL3P
		destLL3P=Integer.valueOf(newHexDST, 16);
	}
	public void setTYPE(String newHextype){//Set Type LL3P
		packetTYPE=Integer.valueOf(newHextype, 16);
	}
	public void setID(String newHexID){//set ID of LL3p
		id=Integer.valueOf(newHexID, 16);
	}
	public void setTTL(String newHexTTL){//Set TTL of LL3P
		timeTTL=Integer.valueOf(newHexTTL, 16);
	}
	//SET a BYTE IN PAYLOAD USING an HEXSTRING
	public void setPayload(String payloadInHex){//set payload of LL3P
		payload=payloadInHex.getBytes();
	}
	
	/*
	 *  void set[field](int inputValue) : 
	 *	this method is passed a primitive data type “int”.  This set method is only required for fields stored as integers.
	 * 
	 */
	public void setSourceLL3P(Integer newSRC){
		sourceLL3P=newSRC;
	}
	
	public void setDstLL3P(Integer newDST){
		destLL3P=newDST;
	}
	public void setTYPE(Integer newtype){
		packetTYPE=newtype;
	}
	public void setID(Integer newID){
		id=newID;
	}
	public void setTTL(Integer newTTL){
		timeTTL=newTTL;
	}
	public void setCHECKSUM(String hexCHECKSYM){//here i'm trying to build up the check sum!...BUT IM NOT DONE
		//String ll3Ppacket=srcAddr+destAddr+type+ID+TTL+payload;
		byte[] myLL3pInByte=hexCHECKSYM.getBytes();
		StringBuilder binary = new StringBuilder();
		for(byte b:myLL3pInByte){
			int value=b;
			
			for(int i=0;i<8;i++){
				
			        binary.append((value & 128) == 0 ? 0 : 1);
			        value <<= 1;//shift the number
			     }
			     binary.append(' ');  
		}
		//binary.append("00000000000000000000000000000000");
	//NEED to CALCULATE 1'COMPLEMENT and put the value in the CHECKSUM
		
	}
	
	//set BYTE IN PAYLOAD with a BYTE ARRAY
	public void setPayload(byte[] newPayload){//SET PAYLOAD BASED ON A byte ARRAY
		payload=newPayload;
	}
	//void get[field]
	public Integer getIntSRCLL3P(){
		return sourceLL3P;
	}
	public Integer getIntDSTLL3P(){
		return destLL3P;
	}
	public Integer getIntType(){
		return packetTYPE;
	}
	public Integer getIntID(){
		return id;
	}
	public Integer getTTLvalue(){
		return timeTTL;
	}
	
	public byte[] getBytePayload(){
		return payload;
	}
	public String getStringPayload(){
		String stringPAYLOAD=new String(payload);
		return stringPAYLOAD;
	}
	
	//String get[field]HexString() – all of these methods each returns a hex string for the corresponding field.

	public String getHexSRCLL3P(){
		
		return Utilities.padHexString(Integer.toHexString(sourceLL3P),2);
	}
	public String getHexDSTLL3P(){
		
		return Utilities.padHexString(Integer.toHexString(destLL3P),2);
	}
	public String getHexPckType(){
		return  Utilities.padHexString(Integer.toHexString(packetTYPE),2);
	}
	public String getHexID(){
		return  Utilities.padHexString(Integer.toHexString(id),2);
	}
	public String getTTLinHex(){
		return Utilities.padHexString(Integer.toHexString(timeTTL),1);
	}
	public String getCHECKSUM(){
		String checksum=new String("0000"); 
		return checksum;
	}
	
//	 This method returns a string containing the contents of the field requested. for display
		
	public String toString(){
		String ll3pPacket=new String (getHexSRCLL3P());
		return ll3pPacket=ll3pPacket+getHexDSTLL3P()+getHexPckType()+getHexID()+
				getTTLinHex()+getStringPayload();
	}
	
	public byte[] getByteLL3P(){//return LL3P PACKET IN BYTE
		return (this.toString()+getCHECKSUM()).getBytes();
	}
}