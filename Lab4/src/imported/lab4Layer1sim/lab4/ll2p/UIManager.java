

package imported.lab4Layer1sim.lab4.ll2p;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lab4Layer1sim.lab4.AdjacencyTableEntry;
import lab4Layer1sim.lab4.LL2PDaemon;
import lab4Layer1sim.lab4.LabLayer1Daemon;
import lab4Layer1sim.lab4.R;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UIManager {
	Activity myActivity;//to be used to get variables associated with the android process
	Context context;//get created object to the screen in the xml file
	Factory ParentFactory;
	TextView IPAddressLabel ;
	TextView IPValue;//this is used to display the Value of the Local IP address
	TextView ll2pCRCTextView;
	TextView sourceAddress, destAddr,type, ll3PSRC,ll3PDST,ll3PType,ll3PId,ll3Ppayload;
	
	TextView payload;
	TextView EchoRequest;
	EditText myPAYLOAD;
    LL2P ll2pObject;
    
    ListView displayAdjacencyTableContent,routingTable,forwardingTable;//used to display the List of AdjacentTableEntries
    EditText captureEnteredMAC;//get User entered MAC
    EditText captureEnteredIP;//get User entered IP
    EditText EchoMessage,ll3pAddress;
    Button add_MAC_IP,sendLL3P;//Add entered MAC and IP into the Adjacency table
    LabLayer1Daemon Layer1DaemonObject;// List of AdjacentTableEntries
    List<AdjacencyTableEntry> myadjacencyList;
    List<RouteTableEntry> myRoutinglist;
    List<RouteTableEntry> myForwardinglist;
    LL2PDaemon myLL2Daemon;
    RouteTable myRoute;
    ForwardingTable myForwarding;
    LRPDaemonShell myLRPDaemon;
    LL3PClass myLL3PClass;
    LL3PDaemon myLL3PDaemon;
    SoundPlayer mySound;
    /*
     * You need an array adapter object to transform underlying objects to the screen printable text widgets
     */
    ArrayAdapter adjacencyListAdapter,routingTableAdapter,forwardingTableAdapter; // used to adapt a table adjacencyTableEnties to a list that can will be displayed on our screen 
    Messenger myMESSENGER;
     
	public UIManager(Activity incomingActivity){
		myActivity=incomingActivity;//keep a reference of the parentActivity from Factory
		context=myActivity.getBaseContext();
		myMESSENGER=new Messenger(myActivity);
		this.setupMainScreenWidgets();
		//IPAddressLabel = new TextView();
		//ll2pObject = getObjectReferences(Factory.getLL2PObjectReference());
	}

	//Factory ParentFactory;
	public void getObjectReferences(Factory myFactory){//used to get Reference of the object created in Factory  
		ParentFactory=myFactory;//this to store the reference to the factory
		this.ll2pObject=myFactory.getLL2PObjectReference();//get LL2P copy
		mySound=myFactory.getSoundPlayer();
		Layer1DaemonObject=myFactory.getLL1DObjectReference();//get Layer1Daemon copy
		myLL2Daemon=myFactory.getLL2PDaemonObjectReference();
		myLRPDaemon=myFactory.getLRPDaemonShellReference();
		
		//myLL3PClass=myFactory.getLL3PObjectReference();
		myLL3PDaemon=myFactory.getLL3PDaemonObjectReference();
		myMESSENGER.getObjectReferences(myFactory);
		
		//myRoute=ParentFactory.getRouteTableObjectReference();
		
		//this.myROUTINGTRICS();
		
	}
	public void raiseToast(String toast2Display){//display Toast 
		Toast.makeText(context, toast2Display, Toast.LENGTH_LONG).show();
	}
	

	public void myROUTINGTRICS(){
		myRoute=ParentFactory.getRouteTableObjectReference();
		myRoutinglist=myRoute.getRouteList();//	
		myRoute.RemoveOldRoutes();
		routingTableAdapter= new ArrayAdapter<RouteTableEntry>(myActivity,android.R.layout.simple_list_item_1,myRoutinglist);//
		routingTable.setAdapter(routingTableAdapter);//
		resetRouteTableListAdapter();
		myForwarding=ParentFactory.getForwardingTableObjectReference();
		myForwarding.AddRouteList(myRoute.getRouteList());		
		myForwardinglist=myForwarding.getRouteList();//	
		myForwarding.RemoveOldRoutes();
		myFORWARDING();
	}
	public void myFORWARDING(){
		//
		forwardingTableAdapter=new ArrayAdapter<RouteTableEntry>(myActivity,android.R.layout.simple_list_item_1,myForwardinglist);
		forwardingTable.setAdapter(forwardingTableAdapter);
		resetforwadingTableListAdapter();
	}
	
	
	private void setupMainScreenWidgets(){ 
		//This method assigns strings from the 
		//LL2P local object to the text values in the LL2P TextView objects.
		ll2pCRCTextView = (TextView) myActivity.findViewById(R.id.textView2CRC);
		sourceAddress=(TextView) myActivity.findViewById(R.id.textView1SRCMAC);
		destAddr=(TextView) myActivity.findViewById(R.id.textView2DestAddValue);
		type=(TextView) myActivity.findViewById(R.id.textView1TypeVal);//textView1TypeVal
		payload=(TextView) myActivity.findViewById(R.id.textView2Payload);	
		//ll3PSRC,ll3PDST,ll3PType,ll3PId,ll3Ppayload;
		ll3PSRC=(TextView) myActivity.findViewById(R.id.SRCLL3PValue);
		ll3PDST=(TextView) myActivity.findViewById(R.id.LL3PDSTValue);
		ll3PType=(TextView) myActivity.findViewById(R.id.LL3PtypeVal);
		ll3PId=(TextView) myActivity.findViewById(R.id.IDvalue);
		ll3Ppayload=(TextView) myActivity.findViewById(R.id.LL3PPayload);
		IPAddressLabel = (TextView) myActivity.findViewById(R.id.textView1);
		//IPAddressLabel.setText("Address -->");
		//IPValue=(TextView) myActivity.findViewById(R.id.textView7);
		displayAdjacencyTableContent=(ListView) myActivity.findViewById(R.id.TablelistView1);
		routingTable=(ListView) myActivity.findViewById(R.id.Routinglist);
		forwardingTable=(ListView) myActivity.findViewById(R.id.Forwardinglist);
		//convertAdjacencyTableEntryTobeDisplayed=(ArrayAdapter) myActivity.findViewById(R.id.AdjacencyTableContentlistView);
		captureEnteredMAC=(EditText) myActivity.findViewById(R.id.MACeditText1);//link UIManager to EditTextMAC created on the screen
		captureEnteredIP=(EditText) myActivity.findViewById(R.id.IPeditText2);//IPeditText2
		EchoRequest=(TextView) myActivity.findViewById(R.id.EchoRequest);
		EchoMessage=(EditText) myActivity.findViewById(R.id.TypeEchoPayload);
		
		//Send LL3P PACKET
		ll3pAddress=(EditText) myActivity.findViewById(R.id.destll3pValue);
		sendLL3P=(Button) myActivity.findViewById(R.id.SendLL3ButtonID);
		sendLL3P.setOnClickListener(sendLL3Ppacket);
		add_MAC_IP=(Button) myActivity.findViewById(R.id.ADDbutton);// link ADD button
		add_MAC_IP.setOnClickListener(addAdjacency);//wait the click! when it available add Entries into the Table of Adjacency Entries
		
	    displayAdjacencyTableContent.setOnItemClickListener(sendToLL2P);//tell the displayAdjacencyTableContent ListView object that you are going to supply an OnClickListener Method for it
		displayAdjacencyTableContent.setOnItemLongClickListener(deleteEntry);//longCLick remove the clicked entry
		
	}
	public View.OnClickListener addAdjacency = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//badPacketSound = 1 ; packetSentSound = 2 ; buttonPressSound = 3 ; receiveMessageSound = 4 ; sendMessageSound = 5 ; alertSound = 6;
			
            Integer MACInString=Integer.parseInt(((captureEnteredMAC.getText()).toString()),16);//convert LL3P entry from screen into a HEX STRING and then into an INTEGER
		    Layer1DaemonObject.setAdjacency(MACInString,(captureEnteredIP.getText()).toString());//Add Adjacency entries through Layer1DaemonObject using the called method "setAdjacency"
		    //Layer1DaemonObject.setAdjacency(10,"10.10.56.43");
           // raiseToast("this is my IP address"+ (captureEnteredIP.getText()).toString() + "MAC ADDress"+MACInString);
            myadjacencyList =  new ArrayList<AdjacencyTableEntry>(Layer1DaemonObject.getAdjacencyList().table);// here used ArrayList to convert from Set to List
            adjacencyListAdapter = new ArrayAdapter<AdjacencyTableEntry>(myActivity,android.R.layout.simple_list_item_1,myadjacencyList); //connect my Adapter to the List of entries
            displayAdjacencyTableContent.setAdapter(adjacencyListAdapter);// link the adapted list to the screen and display it 
		}
		
		
	};

	public View.OnClickListener sendLL3Ppacket = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
            Integer LL3PInString=Integer.parseInt(((ll3pAddress.getText()).toString()),16);//convert MAC entry from screen into a HEX STRING and then into an INTEGER
            String mypayload=EchoMessage.getText().toString();
            myLL3PDaemon.sendPayloadToLL3PDestination(LL3PInString, (EchoMessage.getText()).toString().getBytes());
		}
	};
	private OnItemClickListener sendToLL2P= new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View viewObject, int positionTapped,
				long id) {
			// TODO Auto-generated method stub
			//  The positionTapped is used by the adjacencyList.get(int) method
			//		    so get the adjacency entry.  Then that information is used to
			//		    create a fake LL2P frame…  
				AdjacencyTableEntry target = myadjacencyList.get(positionTapped);//the content on the tapped position
				
				myLL2Daemon.sendLL2PEchoRequest((EchoMessage.getText()).toString(),target.getMACAddress());
				
				
				//Layer1DaemonObject.sendLL2PFrame(newFrame);//send a frame via Layer1DaemonObject using SendLL2Frame
				raiseToast("here is the Destinationa MAC address"+(target.getMACAddress()).toString()+" and the EchoPayload "+(EchoMessage.getText()).toString() );//for troubleshooting
//				new LL2P((target.getMACAddress()).toString(),ll2pObject.getSourceAddressHexString(),Integer.toHexString(NetworkConstants.LL2P_Echo_Request),(EchoMessage.getText()).toString() );
//				updateLL2PLayout();
		} 
	};
	
	private OnItemLongClickListener deleteEntry= new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int positionTapped, long arg3) {
			// TODO Auto-generated method stub
			AdjacencyTableEntry target = myadjacencyList.get(positionTapped);//the content on the tapped position
            Layer1DaemonObject.removeAdjacency(target.getMACAddress());	//get MAC address from Target and used it to delete the corresponding entry
            resetAdjacencyListAdapter();//used to auto-update the screen when there is a change in the Adjacency table
			return false;
		}

	};
	
	public void setIPAddressTextView(String ipAddress){//to show LOCAL IP ADDRESS
		IPValue.setText(ipAddress);
	}
	public void updateLL2PLayout(){
		ll2pCRCTextView.setText(ll2pObject.getCRCHexString());
		sourceAddress.setText(ll2pObject.getSourceAddressHexString());
		destAddr.setText(ll2pObject.getDestinationAddressHexString());
		type.setText(ll2pObject.getTypeHexString());
	    payload.setText(ll2pObject.getPayloadString());	
	    
		
	}
	public void upDATELL3PLayout(){
		ll3PSRC.setText(myLL3PDaemon.myLL3P_Packet.getHexSRCLL3P());
		ll3PDST.setText(myLL3PDaemon.myLL3P_Packet.getHexDSTLL3P());
		ll3PType.setText(myLL3PDaemon.myLL3P_Packet.getHexPckType());
		ll3PId.setText(myLL3PDaemon.myLL3P_Packet.getHexID());
		ll3Ppayload.setText(myLL3PDaemon.myLL3P_Packet.getStringPayload());
	}
	
	private void resetAdjacencyListAdapter(){
		// get the current list
		myadjacencyList =new ArrayList<AdjacencyTableEntry>(Layer1DaemonObject.getAdjacencyList().table);
		// clear the adjacency list
		adjacencyListAdapter.clear();
		// load the list items in the adapter, this updates the screen too.
		Iterator<AdjacencyTableEntry> listIterator = myadjacencyList.iterator();
		while (listIterator.hasNext())
			adjacencyListAdapter.add(listIterator.next());
	}
	
	public void resetRouteTableListAdapter(){
		// get the current list
		myRoutinglist=myLRPDaemon.getRoutingTableAsList();
		
		//myRoutinglist=myRoute.getRouteList();
		
		// clear the adjacency list
		routingTableAdapter.clear();
		// load the list items in the adapter, this updates the screen too.
		Iterator<RouteTableEntry> listIterator = myRoutinglist.iterator();
		while (listIterator.hasNext())
			routingTableAdapter.add(listIterator.next());
	}
	
	public void resetforwadingTableListAdapter(){
		// get the current list
		// myForwardinglist=myForwarding.getRouteList();
		
		myForwardinglist=myLRPDaemon.getForwardingTableAsList();
		
		// clear the adjacency list
		forwardingTableAdapter.clear();
		// load the list items in the adapter, this updates the screen too.
		Iterator<RouteTableEntry> listIterator = myForwardinglist.iterator();
		while (listIterator.hasNext())
			forwardingTableAdapter.add(listIterator.next());
	}
	public Messenger getMessenger(){
		return myMESSENGER;
	}
	
	public void receiveChat(Integer address,String message){//to receive the message 
		myMESSENGER.receiveMessage(address, message);
	}
	
}
