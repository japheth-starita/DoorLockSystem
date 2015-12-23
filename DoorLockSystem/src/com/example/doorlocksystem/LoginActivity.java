package com.example.doorlocksystem;


import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice btModule;
	ConnectToDevice mConnectThread;
	EditText admin_user;
	EditText admin_pw;
	boolean isConnected;
	boolean isConnecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		admin_user = (EditText) findViewById(R.id.user);
    	admin_pw = (EditText) findViewById(R.id.pw);
    	isConnected =false;
    	isConnecting = false;
	}

	//Check Admin Password and Username
    public void submitAdmin(View view){
    	checkAdmin();
    }
    
    public void checkAdmin(){
    	if(!isConnected){
			BluetoothService bs = new BluetoothService();
			if(bs.isthereBluetooth(mBluetoothAdapter)){
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						if(device.getName().equals("HC-05")){
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
				isConnected = true;
			}
			else{
				Toast.makeText(getApplicationContext(), ErrorCode.E70, Toast.LENGTH_LONG).show();
			}
    	}
    	else{
    		mConnectThread.sendData(SignalToArduino.SEND_USERNAME_PASSWORD+admin_user.getText().toString()+
					  "#"+admin_pw.getText().toString());
    	}
	}

	public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  String data = (String) msg.obj;
			  if (data.trim().equals("1")){
				  gotoDeviceManager();
				  closeAll();
			  }
			  else if(data.equals("OK")){
				  mConnectThread.sendData(SignalToArduino.SEND_USERNAME_PASSWORD+admin_user.getText().toString()+
						  "#"+admin_pw.getText().toString());
	          }
			  else{
				  Toast.makeText(getApplicationContext(), ErrorCode.E30, Toast.LENGTH_LONG).show();
				  admin_user.setText("");
				  admin_user.requestFocus();
				  admin_pw.setText("");
			  }
			  
		  }
	};
	
	@Override
    public void onBackPressed(){
		closeAll();
	}
	
	public void gotoDeviceManager(){
		Intent intent = new Intent(this, DeviceManagerActivity.class);
		startActivity(intent);
	}

	public void closeAll(){
		  try{
				mConnectThread.cancel();
			}catch(Exception e){}		
			finish();
	}
}
