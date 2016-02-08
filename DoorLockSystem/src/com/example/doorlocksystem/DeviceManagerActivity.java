package com.example.doorlocksystem;


import java.util.Arrays;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
	private SharedPreferences.Editor editor;
	private AddOwnDevice addOwnDevice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_manager);
		
		ListView option = (ListView)findViewById(R.id.optiondm);
		
        ArrayAdapter<String> optionArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        option.setAdapter(optionArrayAdapter);
        optionArrayAdapter.add("Add This Device");
        optionArrayAdapter.add("Delete Device");
        optionArrayAdapter.add("Change Verification Character");
        optionArrayAdapter.add("Change Username and Password");
        option.setAdapter(optionArrayAdapter);
        option.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch( position ){
					case 0:
						addOwnDevice = new AddOwnDevice();
				    	addOwnDevice.showInputDialogBox( DeviceManagerActivity.this, mHandler);
				    	sendDeviceAddress();
				    	new CountDownTimer(80000, 1000) {

				    	     public void onTick(long millisUntilFinished) {
				    	         
				    	     }

				    	     public void onFinish() {
				    	    	 Log.d("Error", "Adding Device");
				    				Toast.makeText(getApplicationContext(), ErrorCode.E40, Toast.LENGTH_LONG).show();
				    				addOwnDevice.removeDialog();
				    				closeAll();
				    	     }
				    	  }.start();
				    	break;
					case 1:
						intent = new Intent(DeviceManagerActivity.this, DeleteDeviceActivity.class);
						startActivity(intent);
						break;
					case 2:
						InputUserVerification userverify = new InputUserVerification();
						userverify.showInputDialogBox(false, DeviceManagerActivity.this, mHandler);
						break;
					case 3:
						intent = new Intent(DeviceManagerActivity.this, ChangeUserPassActivity.class);     
						startActivity(intent);
				}
			}
        }); 

	}
	
	public void sendDeviceAddress(){
		if(noDeviceName()){
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			BluetoothService bs = new BluetoothService();
			if(bs.isthereBluetooth(mBluetoothAdapter)){
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						if(device.getName().equals(SignalToArduino.NAME)) {
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
		}else{
			Log.d("Error", "Adding Device");
			Toast.makeText(getApplicationContext(), ErrorCode.E40, Toast.LENGTH_LONG).show();
			addOwnDevice.removeDialog();
			closeAll();
		}
	}
	
	//handler for alertdialog
	public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  switch (msg.what) {
		        case 1: 
		          editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
			      editor.putString("verifychar", (String) msg.obj);
			      editor.commit();
			      Log.d("Verification Char", "Received");
		      }
		    }
	  };	  
	  
	  //Handler for bluetooth
	  public Handler mHandler2 = new Handler() {
		  public void handleMessage(Message msg) {
			  String data = (String) msg.obj;
			  if(data.equals("OK")){
				  //Get list of mac addresses
				  //Send signal 4 and the mac address
				  mConnectThread.sendData(SignalToArduino.SEND_DEVICE_TO_ADD+mBluetoothAdapter.getAddress());
				  Log.d("BT", "Sending data");
			  }
			  else if(data.trim().equals("Error")){
				  Log.d("Error", "Adding Device");
				  Toast.makeText(getApplicationContext(), ErrorCode.E40, Toast.LENGTH_LONG).show();
			  }
			  else if(data.trim().contains("user")){
				  Log.d("Success", "Adding Device");
				  setDeviceName(data.trim());
				  addOwnDevice.removeDialog();
				  closeAll();
			  }
		  }
	  };
	  
	  public void closeAll(){
		  try{
			  mBluetoothAdapter.disable();
			}catch(Exception e){}		
	  }
	  
	  public boolean noDeviceName(){
		  String devicename = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).getString("devicename", "");
		  if(devicename.isEmpty()){
			  return true;
		  }
		  return false;
	  }
	  
	  public void setDeviceName(String name){
		  editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
	      editor.putString("devicename", name);
	      editor.commit();
	      Log.d("Device Name", "Received");
	  }
}
