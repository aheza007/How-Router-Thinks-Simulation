/*
 * Student Name: Desire AHEZA
 * Lab4:MainActivity
 * Version 1
 * Course: NETWORK SYSTEM DESIGN
 * Professor SMITH 
 */

package lab4Layer1sim.lab4;


import imported.lab4Layer1sim.lab4.ll2p.Factory;
import imported.lab4Layer1sim.lab4.ll2p.NetworkConstants;
import imported.lab4Layer1sim.lab4.ll2p.Scheduler;
import imported.lab4Layer1sim.lab4.ll2p.UIManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	Factory myFactory;//this one is used by the MainActivity to access to the object from Factory
	UIManager uiManager;//receive UIManager object through Factory 
	LabLayer1Daemon Layer1Daemon;
	Scheduler myScheduler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myFactory= new Factory(this);//initialize the Factory object associated to the 
		uiManager=myFactory.getUIManager();//keep track of UIManager
		uiManager.raiseToast("all done!!");//pop-up toast "all done!!"
		uiManager.raiseToast(NetworkConstants.IP_ADDRESS);
		uiManager.updateLL2PLayout();
		Layer1Daemon=myFactory.getLL1DObjectReference();
		//Layer1Daemon.sendLL2PFrame();
		//myScheduler=myFactory.getSchedulerObjectReference();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu); 
		return true;
	} 
	/**
	 *   Here we respond to the menu selection.  The menu is created
     *   in res/menu.  Any item created there can be checked for here
	 *   this method will then handle all of these. 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.showIPAddress)
		{
			uiManager.raiseToast(NetworkConstants.IP_ADDRESS);
			// The next lines are optional, thus commented out
			//uiManager.setIPAddressTextView(NetworkConstants.IP_ADDRESS);
		}
		else if(item.getItemId() == R.id.TransmitUdpPacket)
		{
			Layer1Daemon.sendLL2PFrame();
			
		}
		else if(item.getItemId() == R.id.OpenMessenger ){
			uiManager.getMessenger().openMessengerWindow();
		}

		return true;
	}

}
