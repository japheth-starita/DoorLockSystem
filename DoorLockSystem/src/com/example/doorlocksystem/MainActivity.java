package com.example.doorlocksystem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.Button;
import android.widget.ToggleButton;
public class MainActivity extends Activity {
	final BluetoothAdapter adapt = BluetoothAdapter.getDefaultAdapter();
	
	public void turnOn() {
		if (!adapt.isEnabled()){
			startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
		}
	}
	
	public void openDeviceManager(View view) {
		turnOn();
	}
	public void openLockUnlock(View view) {
		turnOn();
		Intent intent = new Intent(this, LockUnlockActivity.class);
		startActivity(intent);
	}
	public void openRequestCode(View view) {
		turnOn();
	}
	public void SwitchBT(View view){
		ToggleButton switchbt = (ToggleButton) findViewById(R.id.btSwitch);
		if (switchbt.isChecked()){
			//insert off code
		}
		else{
			turnOn();
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
