package com.example.doorlocksystem;

import java.io.IOException;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectToDevice extends Thread{
	private BluetoothAdapter mBluetoothAdapter;
	private DeviceConnected mConnectedThread;
	private Handler mhandler;
	private String data;
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
	public ConnectToDevice(BluetoothDevice device, BluetoothAdapter bt, String dataToBeSent, Handler mhandler) {
		this.data = dataToBeSent;
		this.mhandler = mhandler;
		mBluetoothAdapter = bt;
		BluetoothSocket tmp = null;
		mmDevice = device;
		try {
			tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) { }
			mmSocket = tmp;
	}
		
	//mConnectThread.start();
	//run() will be called instead of start()
	public void run() {
		mBluetoothAdapter.cancelDiscovery();
		try {
			mmSocket.connect();
		} catch (IOException connectException) {
			try {
				mmSocket.close();
			} catch (IOException closeException) { }
			return;
		}
		
		mConnectedThread = new DeviceConnected(mmSocket, mhandler);
		mConnectedThread.start();
		sendData();
	}
		
		//Stop Connection With Bluetooth Device
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) { }
	}
	
	public void sendData(){
		mConnectedThread.write((data+"\n").getBytes());
	}
}
