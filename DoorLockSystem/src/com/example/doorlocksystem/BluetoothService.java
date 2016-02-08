package com.example.doorlocksystem;

import android.bluetooth.BluetoothAdapter;

public class BluetoothService {

	public boolean isthereBluetooth(BluetoothAdapter adapt){
		if(adapt == null){
			return false;
		}
		else{
			while(!adapt.isEnabled()){
				adapt.enable();
			}
			return true;
		}
	}
}
