package com.example.doorlocksystem;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeleteDeviceActivity extends Activity {

	private ListView listofdevices;
	private BluetoothAdapter mBluetoothAdapter;
	private ConnectToDevice mConnectThread;
	private BluetoothDevice btModule;
	private ArrayAdapter<String> optionArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_device);
		
		listofdevices = (ListView) findViewById(R.id.listofdevices);
		optionArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listofdevices.setAdapter(optionArrayAdapter);
		
        listofdevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try{
					deleteDevice(position+1);
				}catch(Exception e){}
			}
        });
        
        getDevices();

	}
	
	public void deleteDevice(int device){
		Toast.makeText(getApplicationContext(), device, Toast.LENGTH_SHORT).show();
		mConnectThread.sendData(SignalToArduino.SEND_DEVICE_TO_DELETE+device);
	}
	
	public void getDevices(){
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothService bs = new BluetoothService();
		if(bs.isthereBluetooth(mBluetoothAdapter)){
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					 if(device.getName().equals("HC-05")) 
		                {
		                    btModule = device;
		                    break;
		                }
				}
			}

			//Parameters are btModule - device to be connected
			//mBluetoothAdapter - BluetoothAdapter
			mConnectThread = new ConnectToDevice(btModule, 
					mBluetoothAdapter, mHandler);
			//Connect to Bluetooth Module
			mConnectThread.start();
		}
		else{
			Toast.makeText(getApplicationContext(), ErrorCode.E70, Toast.LENGTH_LONG).show();
		}
	}
	
	 public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  String data = ((String) msg.obj).trim();
			  if(data.equals("OK")){
				  //Get list of mac addresses
				  mConnectThread.sendData(SignalToArduino.REQUEST_LIST_OF_DEVICES+"");
				  Log.d("BT", "Sending data");
			  }
			  else if(isInteger(data)){
				  optionArrayAdapter.add("user"+data);
				  optionArrayAdapter.notifyDataSetChanged();
			  }
			  else if(data.equals("Success")){
				  Toast.makeText(getApplicationContext(), "Successfully Deleted Device", Toast.LENGTH_LONG).show();
			  }
		  }
		  
		  public boolean isInteger( String input ) {
			    try {
			        Integer.parseInt( input );
			        return true;
			    }
			    catch( Exception e ) {
			        return false;
			    }
			}
	  };
	  
	  public void closeAll(){
		  try{
			  mBluetoothAdapter.disable();
			}catch(Exception e){}
		  finish();
	  }
	  
	  @Override
	    public void onBackPressed(){
			closeAll();
		}

}
