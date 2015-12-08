package com.example.doorlocksystem;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RequestCodeActivity extends Activity {
	Handler inputStreamHandler;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice btModule;
	ConnectToDevice mConnectThread;
    TextView pubKey;
	TextView privKey;
	static String receivedata;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
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
			pubKey = (TextView) findViewById(R.id.pubKey);
			privKey = (TextView) findViewById(R.id.privKey);

			//Parameters are btModule - device to be connected
			//mBluetoothAdapter - BluetoothAdapter
			//true - if app is sending its private key
			//false - if app is sending its product key
			mConnectThread = new ConnectToDevice(btModule, mBluetoothAdapter,true,mHandler);
			//Connect to Bluetooth Module
			mConnectThread.start();
			Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
			
		    //generatingPrivateCode countingTask = new generatingPrivateCode(); // create a new task
		    //countingTask.execute(values);
		}
		else{
			Toast.makeText(getApplicationContext(), "No Bluetooth Found", Toast.LENGTH_LONG).show();
		}
	}
	
	public void gotoLockUnlockActivity(View view){
		//when user goes to another activity
		Intent intent = new Intent(this, LockUnlockActivity.class);
		startActivity(intent);
	}
	
	//BT Connection ceases when user
	//presses back button
	@Override
    public void onBackPressed(){
		try{
			mConnectThread.cancel();
		}catch(Exception e){}
		
		finish();
	}
	
	public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  switch (msg.what) {
		        case 1: 
		          String data = (String) msg.obj;
		          pubKey.setText(""+data);
		      }
		    }
	  };

}

