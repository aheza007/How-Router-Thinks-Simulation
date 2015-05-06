package imported.lab4Layer1sim.lab4.ll2p;


import lab4Layer1sim.lab4.LL2PDaemon;
import lab4Layer1sim.lab4.LabLayer1Daemon;
import lab4Layer1sim.lab4.NetworkDistancePairClass;
import android.app.Activity;


	/*
	 * this is used to create all objects and provide their reference 
	 * 
	 */
	public class Factory extends Activity {
		Activity parentActivity;//Activity: the layout(main screen) of my app
		UIManager uiManager;//
		NetworkConstants myNetworkConstants;
		LL2P ll2pobject;
		LabLayer1Daemon Layer1DaemonObject;
		LL2PDaemon myLL2PDaemon;
		ARPDaemon myARPDaemon;
		ARPTable myARPTable;
		Scheduler myScheduler;
		NetworkDistancePairClass myPair;
		RouteTable myRoutingTable;
		ForwardingTable myForwardingTable;
		LRPDaemonShell myLRPDaemonShell;
		LRPClass myLRP;
		LL3PClass myLL3P;
		LL3PDaemon myLL3Daemon;
		SoundPlayer mySounds;
		
		public Factory(Activity callingActivity){
			parentActivity=callingActivity;//keep track of MainActivity as the calledActivity
			myNetworkConstants=new NetworkConstants(parentActivity);//initialize a NetworkConstants object "passes parentActivity from MainActivity"
			mySounds = new SoundPlayer(parentActivity);
			uiManager=new UIManager(parentActivity);//initialize a UIManager object "passes parentActivity from MainActivity"
			ll2pobject=new LL2P("8", "CECAFA", "FCCA", "Desire, Thank you for choosing NETWORK SYSTEM ENGINNERING");//initialize LL2P frame to zeros "DE0077"
			//ll2pobject=new LL2P();
			
			Layer1DaemonObject=new LabLayer1Daemon();
			myLL2PDaemon=new LL2PDaemon(Integer.parseInt((NetworkConstants.MY_LL2P_ADDRESS),16));
			myARPDaemon=new ARPDaemon();
			myARPTable=new ARPTable();
			myScheduler=new Scheduler();
			myForwardingTable=new ForwardingTable();
			myPair=new NetworkDistancePairClass();
			myRoutingTable=new RouteTable();
			
			myLRP=new LRPClass();
			myLRPDaemonShell=new LRPDaemonShell();
			
			myLL3P=new LL3PClass();
			myLL3Daemon=new LL3PDaemon();
			
			//myMessenger
			
			
			//Layer1DaemonObject.setAdjacency(123456,"10.73.192.33");
			//myARPTable.addrOrUpdate(0x0301, 0x00a0a0);
		//	myARPTable.addrOrUpdate(0x0c07, 128886);
     		//myARPTable.addrOrUpdate(0x0f0a, 123456);
     		//myARPTable.addrOrUpdate(0x0501, 0xfabce1);
//			myARPTable.addrOrUpdate(0x0404, 121234);
			//myARPTable.addEntry(301013, 310101);
//			myRoutingTable.AddEntry(4, 1, 2, 4);
//			//myRoutingTable.AddEntry(3, 1, 1, 3);
//			myRoutingTable.AddEntry(4, 2, 2, 4);
//			myRoutingTable.AddEntry(1, 5, 1, 1);
			//myScheduler.getObjectReferences(this);
			//uiManager.
			
			myLRP.getObjectReferences(this);
			myLRPDaemonShell.getObjectReferences(this);
			
			myLL3Daemon.getObjectReferences(this);
			
			myARPDaemon.getObjectReferences(this);
			myARPTable.getObjectReference(this);
			ll2pobject.getObjectReferences(this);//provide reference to ll2p
			uiManager.getObjectReferences(this); //this is to provide Factory Reference to uiManager   
			myNetworkConstants.getObjectReferences(this);//this is to provide Factory Reference to to myNetworkConstants
			Layer1DaemonObject.getObjectReferences(this);
			myLL2PDaemon.getObjectReferences(this);
			myPair.getObjectReferences(this);
			myRoutingTable.getObjectReference(this);
		    myForwardingTable.getObjectReference(this);
		    
		    myScheduler.getObjectReferences(this);
		}
		public LRPClass getLRPObjectReference(){
			return myLRP;
		}
		public LRPDaemonShell getLRPDaemonShellReference(){
			return myLRPDaemonShell;
		}
		public RouteTable getRouteTableObjectReference(){
			return myRoutingTable;
		}
		public ForwardingTable getForwardingTableObjectReference(){
			return myForwardingTable;
		}
		public NetworkDistancePairClass getNetworkDistancePairObjectReference(){
			return myPair;
		}
		public ARPTable getARTableObjectReference(){
			return myARPTable;
		}
		public ARPDaemon getARPDaemonObjectReference(){
			return myARPDaemon;
		}
		public Scheduler getSchedulerObjectReference(){
			return myScheduler;
		}
		public Activity getParentActivityReference(){
			return parentActivity;
		}
		public UIManager getUIManager(){
			return uiManager;
		}
		public NetworkConstants getNetworkConstants(){
			return myNetworkConstants;
		}
		public LL2P getLL2PObjectReference(){
			return ll2pobject;
		}
		public LL2PDaemon getLL2PDaemonObjectReference(){
			return myLL2PDaemon;
		}
		public LabLayer1Daemon getLL1DObjectReference(){
			/*other class will call Factory looking for Layer1Daemon and Factory will use this 
			 * method to respond them by providing it
			 * 
			 */
			return Layer1DaemonObject;
		}
		public LL3PClass getLL3PObjectReference(){
			return myLL3P;
		}
		public LL3PDaemon getLL3PDaemonObjectReference(){
			return myLL3Daemon;
		}
		public SoundPlayer getSoundPlayer(){
			return mySounds;
		}
		
		
	}


