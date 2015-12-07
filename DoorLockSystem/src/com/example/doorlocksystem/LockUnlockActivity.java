package com.example.doorlocksystem;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LockUnlockActivity extends Activity {
	BluetoothAdapter adapt;
	EditText pw1;
	EditText pw2;
	EditText pw3;
	EditText pw4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_unlock);
		adapt = BluetoothAdapter.getDefaultAdapter();
		adapt.enable();
		pw1 = (EditText) findViewById(R.id.pw1);
		pw2 = (EditText) findViewById(R.id.pw2);
		pw3 = (EditText) findViewById(R.id.pw3);
		pw4 = (EditText) findViewById(R.id.pw4);
		
		
		Set<BluetoothDevice> pairedDevices = adapt.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
			BluetoothDevice mDevice = device;
			}
		}
		
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
		
		pw4.setOnEditorActionListener(new OnEditorActionListener() {

		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_GO) {
		            checkPublicKey();
		            handled = true;
		        }
		        return handled;
		    }
		});
		

	}
	
	public void checkPass(View view){
		checkPublicKey();
	}
	
	public void checkPublicKey(){
		if (checkEditText()){
			int pass = Integer.parseInt((pw1.getText().toString()+pw2.getText().toString()+
					pw3.getText().toString()+pw4.getText().toString()));
			if (pass == 1234){
				TextView status = (TextView) findViewById(R.id.status);
						status.setText("Status: Unlocked");
			}
			else{
				clearEditText();
			}
		}
		else{
			clearEditText();
		}
	}
	
	public boolean checkEditText(){
		try {
			Integer.parseInt((pw1.getText().toString()+pw2.getText().toString()+
					pw3.getText().toString()+pw4.getText().toString()));
			return true;
		} catch (Exception e) {
			Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT ).show();
			return false;
		}
	}
	
	public void clearEditText(){
		pw1.setText("");
		pw2.setText("");
		pw3.setText("");
		pw4.setText("");
		pw1.requestFocus();
	}
}

