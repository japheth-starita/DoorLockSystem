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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RequestCodeActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice btModule;
	ConnectToDevice mConnectThread;
    TextView pubKey;
	TextView privKey;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mBluetoothAdapter.enable();
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
		
		
		mConnectThread = new ConnectToDevice(btModule);
		//Connect to Bluetooth Module
		mConnectThread.start();
		
	    //generatingPrivateCode countingTask = new generatingPrivateCode(); // create a new task
	    //countingTask.execute(values);
	}
	
	public void gotoLockUnlockActivity(View view){
		//Close Connection with Bluetooth Module
		mConnectThread.cancel();
		Intent intent = new Intent(this, LockUnlockActivity.class);
		startActivity(intent);
	}
	
	private class ConnectToDevice extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
		public ConnectToDevice(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			mmDevice = device;
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) { }
			
			mmSocket = tmp;
		}
		
		//mConnectThread.start();
		//run() will be called instead of start()
		public void run() {
			mBluetoothAdapter.cancelDiscovery();
			
			try {
			mmSocket.connect();
			} catch (IOException connectException) {
				try {
					mmSocket.close();
				} catch (IOException closeException) { }
				return;
			}
		}
		
		//Stop Connection With Bluetooth Device
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}
}

