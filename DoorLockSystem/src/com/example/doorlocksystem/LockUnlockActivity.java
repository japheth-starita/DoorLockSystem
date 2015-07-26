package com.example.doorlocksystem;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LockUnlockActivity extends Activity {
	BluetoothAdapter adapt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_unlock);
		adapt = BluetoothAdapter.getDefaultAdapter();
		adapt.enable();
	}
	
	public void checkPW(View view){
		EditText pw = (EditText) findViewById(R.id.pw);
		int pass = Integer.parseInt(pw.getText().toString());
		if (pass == 1234){
			TextView status = (TextView) findViewById(R.id.status);
					status.setText("Status: Unlocked");
		}
		else{
			pw.setText("");
		}
		
	}
	
}
