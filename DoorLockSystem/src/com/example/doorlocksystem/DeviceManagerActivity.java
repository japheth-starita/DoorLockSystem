package com.example.doorlocksystem;


import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class DeviceManagerActivity extends Activity {
	
	private BluetoothAdapter mBluetoothAdapter;
	private ConnectToDevice mConnectThread;
	private BluetoothDevice btModule;
	AddOwnDevice addOwnDevice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_manager);
		
		ListView option = (ListView)findViewById(R.id.optiondm);
		
        ArrayAdapter<String> optionArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        option.setAdapter(optionArrayAdapter);
        optionArrayAdapter.add("Add This Device");
        optionArrayAdapter.add("Add Other Device");
        optionArrayAdapter.add("Manage Device");
        optionArrayAdapter.add("Change Verification Character");
        option.setAdapter(optionArrayAdapter);
        
        option.setOnItemClickListener(new OnItemClickListener() {
			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch( position ){
					case 0:
						addOwnDevice = new AddOwnDevice();
				    	addOwnDevice.showInputDialogBox( DeviceManagerActivity.this, mHandler);
					case 1:  
						Intent newActivity = new Intent(DeviceManagerActivity.this, AddDeviceActivity.class);     
						startActivity(newActivity);
						break;
					case 2:
						InputUserVerification userverify = new InputUserVerification();
						userverify.showInputDialogBox(false, DeviceManagerActivity.this, mHandler);
				}
			}
        }); 

	}
	
	public void sendDeviceAddress(){
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
						mBluetoothAdapter, mHandler2);
				//Connect to Bluetooth Module
				mConnectThread.start();
			}
			else{
				Toast.makeText(getApplicationContext(), ErrorCode.E70, Toast.LENGTH_LONG).show();
			}
		}
	
	 public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  String data = (String) msg.obj;
			  switch (msg.what) {
		        case 1: 
		          SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
			      editor.putString("verifychar", data);
			      editor.commit();
			      Log.d("Received", "OK");
		      }
		    }
	  };
	  
	  public Handler mHandler2 = new Handler() {
		  public void handleMessage(Message msg) {
			  String data = (String) msg.obj;
			  if(data.equals("OK")){
				  mConnectThread.sendData(SignalToArduino.SEND_ANDROID_MAC_ADD);
			  }
			  else{
				  if(data.length()/17 >= 5){
					  Toast.makeText(getApplicationContext(), ErrorCode.E40, Toast.LENGTH_LONG).show();
				  }
				  else{
					  data+=mBluetoothAdapter.getAddress();
					  mConnectThread.sendData(SignalToArduino.SEND_DEVICE_TO_ADD + data);
					  Toast.makeText(getApplicationContext(), "Successfully Added Device", Toast.LENGTH_LONG).show();
					  addOwnDevice.removeDialog();
				  }
			  }
		    }
	  };
	  
	  public void closeAll(){
		  try{
				mConnectThread.cancel();
			}catch(Exception e){}		
	  }
}
