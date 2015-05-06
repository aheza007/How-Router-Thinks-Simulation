package imported.lab4Layer1sim.lab4.ll2p;



public class LL2P {

	private Integer destinationAddress; // 3 byte destination address field
	private Integer sourceAddress; 		// 3 byte source address field
	private Integer type; 				// 2 byte type field
	private byte[] payload; 			// variable length field containing LL2P payload
	public CRC16 CRC;					//store CRC
	
	// These are pointers and length 'constants' for use during packet processing
	private int addressLength = 3;		// address is 3 bytes long
	private int typeLength = 2;			// type field is 2 bytes long
	private int CRCLength = 2;			// CRC is a two byte field
	private int destinationAddressOffset = 0;  // offset of the destination address
	private int sourceAddressOffset = destinationAddressOffset + addressLength;	// offset of source address
	private int typeOffset = sourceAddressOffset + addressLength;	// type offset
	private int payloadOffset = typeOffset + typeLength;	// payload offset
	
	

	
	private UIManager uiManager;
	private Factory parentLL2PFactory;
	/*
	 * The default constructor. It creates an LL2P frame that is filled with zeros 
	 * and has no data in the payload
	 */
	public LL2P (){
		createFields();
		setDestinationAddress(0);
		setSourceAddress(0);
		setType(0);
		setPayload("");
		calculateCRC();
	}
	
	/*
	 * This is a constructor that take an array of bytes
	 * substring method is used to handle frame component 
	 * NOTE THAT 1 Byte is 2 HEX Character    
	 */
	public LL2P(byte[] frameBytes){
		createFields();
		String frameString = new String(frameBytes);
		setDestinationAddress(frameString.substring(2*destinationAddressOffset, 2*destinationAddressOffset + (2*addressLength)));
		setSourceAddress(frameString.substring(2*sourceAddressOffset, 2*sourceAddressOffset + (2*addressLength)));
		setType(frameString.substring(2*typeOffset, 2*typeOffset + (2*typeLength)));
		setPayload(frameString.substring(2*payloadOffset,( frameString.length()- CRCLength *2)));
		calculateCRC();//called internally when the frame changes.  
	
	}
	/*
	 * This is method to allow the layer 1 Deamon to update the LL2P frame object in use by router
	 */
	public void fillInLL2PFrame(byte[] frame){
			String frameChars = new String(frame);
			setDestinationAddress(frameChars.substring(2*destinationAddressOffset, 2*destinationAddressOffset + (2*addressLength)));
			setSourceAddress(frameChars.substring(2*sourceAddressOffset, 2*sourceAddressOffset + (2*addressLength)));
			setType(frameChars.substring(2*typeOffset, 2*typeOffset + (2*typeLength)));
			setPayload(frameChars.substring(2*payloadOffset,( frameChars.length()- CRCLength *2)));
			setCRC(Integer.parseInt(frameChars.substring((frameChars.length()- CRCLength *2),frameChars.length()),16));
			//calculateCRC();
		    uiManager.updateLL2PLayout();
			
		}
	public byte[] helpCalculateCRC(byte[] frame){
		String frameChars = new String(frame);
		frameChars=frameChars.substring(0, frameChars.length()- CRCLength *2);
		return frameChars.getBytes();
	}
	
	/*
	 * This is a constructor that take four strings
	 * it is passed individual strings for each field in the new object except the CRC when we begin using the CRC 
	 * then the CRC will always be calculated. 
	 */
	public LL2P ( String destnationAddress, String sourceAddress, String typeStr, String payloadStr){
		createFields();
		setDestinationAddress(destnationAddress);
		setSourceAddress(sourceAddress);
		setType(typeStr);
		setPayload(payloadStr);
		calculateCRC();
		
	}
	
	/*
	public void registerUIManager(UIManager uiMANAGER){// Allows the LL2P Packet to notice UI manager of updates
		uiManager = uiMANAGER;
	}
	*/
	
	/*
	 * Here are the set of methods;
	 */
	
	/*
	 * Method to create all objects
	 * It initializes them to zero, empty byte arrays, blank CRC objects,
	 *  and empty strings.
	 */
	private void createFields(){
		CRC = new CRC16();
		destinationAddress = 0;
		sourceAddress = new Integer(0);
		type = new Integer(0);
		payload = new String("").getBytes();
	}

	/*
	 *  This method is passed a string value (usually a string of Hex characters)
	 *  which is then converted to the appropriate object for the class(integer in this case).
	 */
	public void setDestinationAddress( String hexDestAddressString){
		destinationAddress = Integer.valueOf(hexDestAddressString.toUpperCase(), 16);//provide Integer value of provided Hex string of the destination address
	}
	
	/*
	 * Method to set the destination address with integer
	 */
	public void setDestinationAddress(int newdestAddress){
		destinationAddress = newdestAddress;
	//	calculateCRC();
	}
	
	/*
	 * Method to set the source address with integer
	 */
	public void setSourceAddress(int newSrcAddress){
		sourceAddress = new Integer(newSrcAddress);
	//	calculateCRC();
	}
	
	/*
	 * Method to set the source address with string
	 */
	public void setSourceAddress(String hexAddressString){
		sourceAddress = (Integer.valueOf(hexAddressString.toUpperCase(), 16));
		
	}
	
	/*
	 * Method to set the type with integer
	 */
	public void setType(int newType){
		type = new Integer(newType);
		//calculateCRC();//calculate CRC
	}
	
	/*
	 * Method to set the type with string
	 */
	public void setType(String newType){
		type = Integer.valueOf(newType, 16);
	}
	
	/*
	 * Method to set the payload with string
	 */
	public void setPayload(String newPayload){
		payload = newPayload.getBytes();
	}
	
	/*
	 * Method used to set the contents of the payload
	 */
	public void setPayload(byte [] payloadByte){
		payload = payloadByte;
	}
	
	/*
	 * Method to set the type with an arrary of bytes
	 */
	public void setCRC(Integer newCRC){
		CRC.setCRC(newCRC);
	}
	
	/*
	 * Get Methods. Most of them return a primitive "int" or a Hex String
	 * 
	 */
	
	public int getDestinationAddress(){
		return destinationAddress;
	}
	
	public int getSourceAddress(){
		return sourceAddress;
	}
	
	public int getType(){
		return type;
	}
	
	public int getCRC(){
		return CRC.getCRC();
	}
	
	public String getDestinationAddressHexString(){//return the hex string representing the value of this field
		return padHexString(Integer.toHexString(destinationAddress), addressLength*2);
	}
	
	public String getSourceAddressHexString(){//return the hex string representing the value of this field
		return padHexString(Integer.toHexString(sourceAddress), addressLength*2);
	}
	
	public String getTypeHexString(){
		return padHexString(Integer.toHexString(type), typeLength*2);
	}
	public String getCRCHexString(){
		return padHexString(CRC.getCRCHexString(), CRCLength*2);
	}
	
	public byte[] getPayloadByte(){
		return payload;
	}
	
	public String getPayloadString(){
		String payloadString = new String(payload);
		return payloadString;
	}
	
	/*
	 * The CRC method is private is called every time the frame changes
	 */
	public void calculateCRC(){
		CRC.Update(getFrameByte());
	}
	
	/*
	 * Pad Hex String is a method to add leading zeros and simulate the return of
	 * an actual byte/hex display of a field
	 */
	private String padHexString(String inputHexString, int length){
		for (int i = inputHexString.length(); i < length; i++){
			inputHexString = "0" + inputHexString;
		}
		
		return inputHexString;
	}
	
	// Method to verify the CRC of incoming packets
	public boolean checkCRC(){
		// First calculate the new CRC
		Integer currentCRC = this.getCRC();
		//
		//uiManager.raiseToast("the current CRC in HEX :"+this.getCRCHexString());
		CRC16 receivedCRC=new CRC16();
		//this.setCRC(0);
		receivedCRC.Update(helpCalculateCRC(getFrameByte()));
		
		if(receivedCRC.getCRC()== currentCRC)
			return true;
		else
		 {	
			//uiManager.raiseToast("the calcutated CRC :"+ CRC.toString());
			return false;
			
		 }
		}
		
	

	
	/**
	 * toString()
	 * This method returns a string containing all elements of the class, appended together
	 * as a string, This is useful for displaying things on the screen
	 */
	public String toString(){
		String returnString = new String(getDestinationAddressHexString());
		returnString = returnString + getSourceAddressHexString() + getTypeHexString() 
				+ getPayloadString() + getCRCHexString();
		return returnString;
	}
	
	/*
	 * Method to get the frame's bytes
	 */
	public byte[] getFrameByte(){
		String frameString= new String(this.toString());
		return frameString.getBytes();
	}
	
	
	public void getObjectReferences(Factory myFactory){//used to get Reference of the object created in Factory  
	parentLL2PFactory=myFactory;//this to store the reference to the factory	
	uiManager=parentLL2PFactory.getUIManager();
	
	}
	

}
