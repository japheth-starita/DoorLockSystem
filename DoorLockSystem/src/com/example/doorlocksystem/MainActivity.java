package com.example.doorlocksystem;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.ToggleButton;
public class MainActivity extends Activity {
	SystemMethods sm;
	BluetoothAdapter adapt;
	ToggleButton onoff;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = new SystemMethods();
        adapt = BluetoothAdapter.getDefaultAdapter();
        onoff = (ToggleButton) findViewById(R.id.btSwitch);
		checkBluetooth();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	checkBluetooth();
    }
    
    @Override
    public void onBackPressed(){
    	adapt.disable();
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	sm.delay(1);
    	startActivity(intent);	
    }
    
    public void openDeviceManager(View view) {
    	Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
    
	public void openLockUnlock(View view) {
		Intent intent = new Intent(this, LockUnlockActivity.class);
		startActivity(intent);
	}
	
	public void openRequestCode(View view) {
		Intent intent = new Intent(this, RequestCodeActivity.class);
		startActivity(intent);
	}
	
	public void SwitchBT(View view){
		ToggleButton onoff = (ToggleButton) findViewById(R.id.btSwitch);
		if (onoff.isChecked()){
			adapt.enable();
		}
		else{
			adapt.disable();
		}
	}
    
    public void checkBluetooth(){
    	if (adapt.isEnabled()){
    		onoff.setChecked(true);;
    	}
    	else{
    		onoff.setChecked(false);
    	}
    }
    
}
