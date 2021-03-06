package com.example.doorlocksystem;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LockUnlockActivity extends Activity {
	
	EditText pw1, pw2, pw3, pw4, pw5;
	TextView lockstatus;
	Button submitCode;
	BluetoothAdapter mBluetoothAdapter,adapt;
	BluetoothDevice btModule;
	ConnectToDevice mConnectThread;
	int productKey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_unlock);
		adapt = BluetoothAdapter.getDefaultAdapter();
		adapt.enable();
		submitCode = (Button) findViewById(R.id.sendPW);
		pw1 = (EditText) findViewById(R.id.pw1);
		pw2 = (EditText) findViewById(R.id.pw2);
		pw3 = (EditText) findViewById(R.id.pw3);
		pw4 = (EditText) findViewById(R.id.pw4);
		pw5 = (EditText) findViewById(R.id.pw5);
		lockstatus = (TextView) findViewById(R.id.status);
		
		pw1.addTextChangedListener(new TextWatcher() {	
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					((EditText) findViewById(R.id.pw2)).requestFocus();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		pw2.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					((EditText) findViewById(R.id.pw3)).requestFocus();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
	
			}
		});
	
		pw3.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					((EditText) findViewById(R.id.pw4)).requestFocus();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		
		pw4.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(count>0){
					((EditText) findViewById(R.id.pw5)).requestFocus();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void checkPass(View view){
		sendProductKey();
	}
	
	public void sendProductKey(){
		disableWidget();
		if(checkPublicKey()){
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
				GenerateKey.resetKey();
				//Connect to Bluetooth Module
				mConnectThread.start();
			}
			else{
				Toast.makeText(getApplicationContext(), ErrorCode.E70, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void disableWidget(){
		pw1.setEnabled(false);
		pw2.setEnabled(false);
		pw3.setEnabled(false);
		pw4.setEnabled(false);
		pw5.setEnabled(false);
		submitCode.setEnabled(false);
	}
	
	public boolean checkPublicKey(){
		if(pw5.getText().toString().equals(
				(getSharedPreferences(MainActivity.PREFS_NAME,MODE_PRIVATE).getString("verifychar","0")))){
			if (checkEditText()){
						int passcode = (Integer.parseInt((pw1.getText().toString()+pw2.getText().toString()+
						pw3.getText().toString()+pw4.getText().toString())));
						Log.d("Int", passcode+"");
						Log.d("Int", GenerateKey.getPrivateKey()*passcode+"");
						productKey = GenerateKey.getPrivateKey()*passcode;
				return true;
			}
			else{
				clearEditText();
				return false;
				
			}
		}
		else{
			Toast.makeText(LockUnlockActivity.this, ErrorCode.E60, Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public boolean checkEditText(){
		try {
			Integer.parseInt((pw1.getText().toString()+pw2.getText().toString()+
					pw3.getText().toString()+pw4.getText().toString()));
			return true;
		} catch (Exception e) {
			Log.d("Check", "Invalid");
			Toast.makeText(this, ErrorCode.E60, Toast.LENGTH_SHORT ).show();
			return false;
		}
	}
	
	public void clearEditText(){
		pw1.setText("");
		pw2.setText("");
		pw3.setText("");
		pw4.setText("");
		pw5.setText("");
		pw1.requestFocus();
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
			  Log.d("Ard", data);
			  if (data.trim().equals("1")){
				  Toast.makeText(LockUnlockActivity.this, "Door Unlocked", Toast.LENGTH_LONG).show();
			  }
			  else if(data.equals("OK")){
				  mConnectThread.sendData(SignalToArduino.SEND_PRODKEY + productKey+"");
	          }
			  else{
				  Toast.makeText(LockUnlockActivity.this, ErrorCode.E60, Toast.LENGTH_LONG).show();
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

