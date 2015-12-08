package com.example.doorlocksystem;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.widget.Toast;

public class ConnectToDevice extends Thread{
	private BluetoothAdapter mBluetoothAdapter;
	private DeviceConnected mConnectedThread;
	private Handler mhandler;
	private int privateKey;
	private int publicKey;
	private boolean isSendingPrivateKey;
	private GenerateKey genKey;
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
	public ConnectToDevice(BluetoothDevice device, BluetoothAdapter bt, boolean isSendingPrivateKey, Handler mhandler) {
		this.mhandler = mhandler;
		this.isSendingPrivateKey = isSendingPrivateKey;
		mBluetoothAdapter = bt;
		genKey = new GenerateKey();
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
		sendData(isSendingPrivateKey);
	}
		
		//Stop Connection With Bluetooth Device
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) { }
	}
	
	public void sendData(boolean sendPrivateKey){
		if(sendPrivateKey){
			privateKey = genKey.getPrivateKey();
			String msg = (privateKey + "\n");
			mConnectedThread.write(msg.getBytes());
		}
	}
}
