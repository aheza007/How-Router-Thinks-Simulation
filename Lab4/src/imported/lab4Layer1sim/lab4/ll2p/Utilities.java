package imported.lab4Layer1sim.lab4.ll2p;

import java.util.Calendar;

public class Utilities {

	/**
	 * This static variable is the value for the number of seconds in the current time since some time back in the 70's.
	 * It's used to calculate the number of seconds since the program began by the method which follows.
	 */
	public static long baseDateSeconds = Calendar.getInstance().getTimeInMillis()/1000;
	
	/**
	 * This method returns the number of seconds since the program began.  
	 * @return
	 */
	public static int getTimeInSeconds(){
		return (int) (Calendar.getInstance().getTimeInMillis()/1000 - baseDateSeconds); 
	}
	public static String padHexString(String hexString, int length){
		String paddedString = new String(hexString);
		/*
		 * here we run the loop starting at the current length (which has two characters for each byte.  
		 * We run to 2*length because length is in bytes, but there are two characters per byte.
		 */
		for (int i = paddedString.length(); i < length*2; i++)
			paddedString = "0" + paddedString;
		return paddedString;
	}
	
	/**
	 * PrependString(String, Int) - given a string, prepend spaces to it and extend
	 *   it to the given length.  
	 * @param s
	 * @param length
	 * @return
	 */
	public static String prependString(String s, int length){
		String paddedString = new String(s);
		for (int i=paddedString.length(); i<length;i++)
			paddedString = " " + paddedString;
		return paddedString;
	}
	
	public static Integer getNetworkNumber(String ll3pAddress){
		Integer networkNumber=0;
		String halfLen=null;
		int halfLenght=ll3pAddress.length()/2;
		halfLen=ll3pAddress.substring(0,halfLenght);
		networkNumber=(Integer.parseInt((halfLen),16));
		return networkNumber;
	}
}
