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
	private String strlistofmacaddress;

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
					deleteDevice(listofdevices.getItemAtPosition(position).toString());
				}catch(Exception e){}
			}
        });
        
        getDevices();

	}
	
	public void deleteDevice(String device){
		Toast.makeText(getApplicationContext(), device, Toast.LENGTH_SHORT).show();
		strlistofmacaddress = strlistofmacaddress.replaceFirst("device", "");
		mConnectThread.sendData(SignalToArduino.SEND_DEVICE_TO_ADD+strlistofmacaddress);
		Toast.makeText(getApplicationContext(), strlistofmacaddress, Toast.LENGTH_LONG).show();
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
			  String data = strlistofmacaddress = (String) msg.obj;
			  if(data.equals("OK")){
				  //Get list of mac addresses
				  mConnectThread.sendData(SignalToArduino.SEND_ANDROID_MAC_ADD+"");
				  Log.d("BT", "Sending data");
			  }
			  else{
				  data = data.trim();
				  int len= 17;
				  int k=(data.length()/len);
				  int m=0;
				   for(int i=0;i<(k+1);i++){
					   if(i!=0) m=m+len;
				    
					   if(data.length()>=m+len){
						   optionArrayAdapter.add((data.substring(m,m+len)));
					   }
					   else if(data.length()<m+len){
						   optionArrayAdapter.add((data.substring(m,data.length())));
					   }
				   }
				   optionArrayAdapter.notifyDataSetChanged();
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
