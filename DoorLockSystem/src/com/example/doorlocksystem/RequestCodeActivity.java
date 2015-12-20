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
    TextView pubKey, privKey;
	Button gotoLockUnlock;
	ProgressBar progBar;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		progBar = (ProgressBar) findViewById(R.id.progBar);
		gotoLockUnlock = (Button) findViewById(R.id.gotoLockUnlock);
		gotoLockUnlock.setEnabled(false);
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
			GenerateKey.genprivateNum();
			mConnectThread = new ConnectToDevice(btModule, 
					mBluetoothAdapter, SignalToArduino.SEND_PRIVKEY + GenerateKey.getPrivateKey(), mHandler);
			//Connect to Bluetooth Module
			mConnectThread.start();
			Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getApplicationContext(), ErrorCode.E70, Toast.LENGTH_LONG).show();
		}
	}
	
	public void gotoLockUnlockActivity(View view){
		Intent intent = new Intent(this, LockUnlockActivity.class);
		closeAll();
		startActivity(intent);
	}
	
	//BT Connection ceases when user
	//presses back button
	@Override
    public void onBackPressed(){
		closeAll();
	}
	
	public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
		          String data = (String) msg.obj;
		          pubKey.setText(data);
		          progBar.setVisibility(View.GONE);
		          try{
		        	  gotoLockUnlock.setEnabled(true);
		          }catch(Exception e){}
		    }
	  };
	  
	  
	  public void closeAll(){
		  try{
				mConnectThread.cancel();
			}catch(Exception e){}		
			finish();
	  }

}

