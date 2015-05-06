package imported.lab4Layer1sim.lab4.ll2p;

public class CRC16 {
	private Integer CRC;	//Object to store the CRC value
	private int polynom = 0x11021;// polynomial to be used in the CRC: x^16 + x^12 + x^5 + 1 
	private int length = 4;	// length of the CRC(string of HEX number)
	
	public CRC16 (){// Constructor that initializes the CRC value to 0
		CRC = new Integer(0);
	}
	
	public Integer getCRC (){// methods that returns the value of CRC
		return CRC;
	}
	
	// getCRCHexString() returns a 4 characters string representing the CRC value in hexadecimal 
	public String getCRCHexString(){
		return padHexString(Integer.toHexString(CRC));
	}
	
	//resetCRC() method resets the CRC to zero i.e be ready for a new calculation
	public void resetCRC(){
		CRC = 0;
	}
	// Method used to set the CRC when copying one frame to another
	public void setCRC( Integer newCRC){
		CRC = newCRC;
	}
	/* Method that allows providing a new byte to the CRC object 
	   This method calculate the value of the CRC for every byte in the input string 
	*/
	 public void update (byte iInputByte){
		 //XOR the current input byte at inputstring[i] position with the current value of the CRC
		 CRC = CRC ^ (((int) iInputByte) << 8);
		 // Doing the modulo-2 division on the current byte
		 for (int i = 0; i < 8; i++){
			 if ((CRC & 0x8000) == 0x8000){//test the most significant bit if it is equal to 1
					CRC = (CRC << 1) ^ polynom;
				}		
				else{
					CRC = CRC << 1;
				}
			 
		 }
	 }
	
	// This method is passed a byte array and updates the CRC value by processing this array as a byte stream
	public void Update(byte [] inputString){
		// First reset the CRC before counting the new one
		resetCRC();
		for (int i = 0; i < inputString.length; i++){
			update(inputString[i]);
		}
	}
	
	// Method that returns the CRC as a string
	public String toString(){
		return CRC.toString();
	}

	/*
	 * PadHexString is a method to add leading zeros and simulate the return of
	 * an actual byte/hex display of a field
	 */
	private String padHexString(String inputHexString){
		for (int i = inputHexString.length(); i < length; i++){
			inputHexString = "0" + inputHexString.toUpperCase();
		}
		
		return inputHexString;
	}
}
