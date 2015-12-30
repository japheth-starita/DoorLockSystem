package com.example.doorlocksystem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
public class MainActivity extends Activity {
	private SystemMethods sm;
	private BluetoothAdapter adapt;
	private ToggleButton onoff;
	private TextView devicename;
	public static final String PREFS_NAME = "PREFERENCES";
	public static String strdeviceName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = new SystemMethods();
        adapt = BluetoothAdapter.getDefaultAdapter();
        onoff = (ToggleButton) findViewById(R.id.btSwitch);
        devicename = (TextView) findViewById(R.id.txtDevicename);
		checkBluetooth();
		boolean isFirstRun = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).getBoolean("isfirstrun", true);
		Toast.makeText(MainActivity.this,isFirstRun+"", Toast.LENGTH_SHORT).show();
		InputUserVerification userverify = new InputUserVerification();
		if(isFirstRun || getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString("verifychar", "").isEmpty()){
			userverify.showInputDialogBox(true, MainActivity.this, mHandler);
		      SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
		      editor.putString("verifychar", userverify.getVerifyChar());
		      editor.putBoolean("isfirstrun", false);
		      editor.commit();
		}
		devicename.setText(getSharedPreferences(PREFS_NAME,  MODE_PRIVATE).getString("devicename", ""));
	      strdeviceName = devicename.getText().toString();
		
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	checkBluetooth();
    }
    
    @Override
    public void onBackPressed(){
    	adapt.disable();
    	finish();
    }
    
    public void openDeviceManager(View view) {
    	Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
    
	public void openLockUnlock(View view) {
		//Intent intent = new Intent(this, LockUnlockActivity.class);
		//startActivity(intent);
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
    
    public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  switch (msg.what) {
		        case 1: 
		          String [] data = ((String) msg.obj).split("\n");
		          SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
			      editor.putString("verifychar", data[0]);
			      editor.putString("devicename", data[1]);
			      editor.commit();
			      devicename.setText(getSharedPreferences(PREFS_NAME,  MODE_PRIVATE).getString("devicename", ""));
			      strdeviceName = devicename.getText().toString();
			      Log.d("YEhey", data[0]);
			      Log.d("YEhey", data[1]);
			      Log.d("Received", "OK");
		      }
		    }
	  };
    
    
    
}
