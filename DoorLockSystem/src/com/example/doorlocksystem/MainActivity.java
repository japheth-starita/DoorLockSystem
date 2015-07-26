package com.example.doorlocksystem;

import com.example.doorlocksystem.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;
import android.widget.Button;
import android.content.BroadcastReceiver;

import java.util.Set;
public class MainActivity extends ActionBarActivity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	final BluetoothAdapter adapt = BluetoothAdapter.getDefaultAdapter();
	final ArrayAdapter btArray = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
	final Set<BluetoothDevice> pairedDevices = adapt.getBondedDevices();
	final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	final ToggleButton onoff = (ToggleButton) findViewById(R.id.btSwitch);
	
	public void turnOn() {
		if (!adapt.isEnabled()){
			startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
		}
	}
	
	public void discovDiv() {
		Intent discoverableIntent = new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);
	}
	
	public void findDiv(){
		if (pairedDevices.size() > 0){
			for (BluetoothDevice device : pairedDevices){
				btArray.add(device.getName() + "\n" + device.getAddress());
			}
		}
	}
	
	public void onRecieve(Context context, Intent intent) {
		String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			btArray.add(device.getName() + "\n" + device.getAddress());
		}
	}
	
	public void openTurnOn(View view) {
		turnOn();
		discovDiv();
	}

	public void openDeviceManager(View view) {
		findDiv();
	}
	public void openLockUnlock(View view) {
		turnOn();

		discovDiv();

		Intent intent = new Intent(this, LockUnlockActivity.class);
		startActivity(intent);

	}
	public void openRequestCode(View view) {
		turnOn();
		discovDiv();
	}
	public void SwitchBT(View view){
		if (onoff.isChecked()){
			startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
		}
		else {
			adapt.disable();
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
