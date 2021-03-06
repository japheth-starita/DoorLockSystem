package com.example.doorlocksystem;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeUserPassActivity extends Activity {
	TextView lblpass;
	TextView lbluser;
	EditText txtuser;
	EditText txtpass;
	String newUserPass;
	RelativeLayout layout;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice btModule;
	private ConnectToDevice mConnectThread;
	private Button submit;
	private Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		lblpass = (TextView) findViewById(R.id.Password);
		lbluser = (TextView) findViewById(R.id.Username);
		txtuser = (EditText) findViewById(R.id.user);
		txtpass = (EditText) findViewById(R.id.pw);
    	submit = (Button) findViewById(R.id.submit);
    	cancel = (Button) findViewById(R.id.cancel);
		lblpass.setText("*New " + lblpass.getText().toString());
		lbluser.setText("*New " + lbluser.getText().toString());
	}
	
	public void cancelActivity(View v){
		closeAll();
	}
	
	public void submitAdmin(View v){
		String user = txtuser.getText().toString();
		String pass = txtpass.getText().toString();
		if(user.isEmpty()){
			Toast.makeText(getApplicationContext(),"Username is empty.", Toast.LENGTH_SHORT).show();
		}
		else if(pass.isEmpty()){
			Toast.makeText(getApplicationContext(),"Password is empty.", Toast.LENGTH_SHORT).show();
		}
		else{
			newUserPass = user+"#"+pass;
			sendNewUserPass();
		}
	}
	public void sendNewUserPass(){
    	submit.setEnabled(false);
    	cancel.setEnabled(false);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothService bs = new BluetoothService();
		if(bs.isthereBluetooth(mBluetoothAdapter)){
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					 if(device.getName().equals(SignalToArduino.NAME)) 
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
			closeAll();
		}
	}
  
  public Handler mHandler = new Handler() {
	  public void handleMessage(Message msg) {
		  String data = (String) msg.obj;
		  data = data.trim();
		  if(data.equals("OK")){
			  mConnectThread.sendData(SignalToArduino.SEND_NEW_USERNAME_PASSWORD + newUserPass);
			  Log.d("BT", "Sending data");
		  }
		  else if(data.equals("1")){
			  Toast.makeText(getApplicationContext(), "Username and Password changed successfully.", Toast.LENGTH_LONG)
			  .show();
			  closeAll();
			  }
		  }
	    
  };
  
  public void closeAll(){
	  try{
		  mConnectThread.cancel();
		}catch(Exception e){}
	  finish();
  }
	
	

	
}
