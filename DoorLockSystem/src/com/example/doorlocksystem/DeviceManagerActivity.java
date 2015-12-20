package com.example.doorlocksystem;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class DeviceManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_manager);
		
		ListView option = (ListView)findViewById(R.id.optiondm);
		
		
        ArrayAdapter<String> optionArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        option.setAdapter(optionArrayAdapter);
        optionArrayAdapter.add("Add Device");
        optionArrayAdapter.add("Manage Device");
        optionArrayAdapter.add("Change Verification Character");
        option.setAdapter(optionArrayAdapter);
        
        option.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					 switch( position ){
					       case 0:  Intent newActivity = new Intent(DeviceManagerActivity.this, AddDeviceActivity.class);     
					                startActivity(newActivity);
					                break;
					       case 2:
					    	   InputUserVerification userverify = new InputUserVerification();
					    	   userverify.showInputDialogBox(false, DeviceManagerActivity.this, mHandler);
					    	   
					}
			}
        }); 

	}
	
	 public Handler mHandler = new Handler() {
		  public void handleMessage(Message msg) {
			  switch (msg.what) {
		        case 1: 
		          String data = (String) msg.obj;
		          SharedPreferences.Editor editor = getSharedPreferences(MainActivity.PREFS_NAME, 0).edit();
			      editor.putString("verifychar", data);
			      editor.commit();
			      Log.d("Received", "OK");
		      }
		    }
	  };

	
}
