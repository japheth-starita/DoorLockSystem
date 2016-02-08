package com.example.doorlocksystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.provider.Telephony.MmsSms;
import android.util.Log;

public class DeviceConnected extends Thread {
	private Handler mHandler;
	private BluetoothSocket mmSocket;
	private InputStream mmInStream;
	private  OutputStream mmOutStream;
	public DeviceConnected(BluetoothSocket socket, Handler mhandler) {
		this.mHandler = mhandler;
		mmSocket = socket;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) { }
		
		mmInStream = tmpIn;
		mmOutStream = tmpOut;
	}
	
		
	public void run() {
        byte delimiter = 10; //This is the ASCII code for a newline character
        
        boolean stopWorker = false;
        int readBufferPosition = 0;
        byte [] readBuffer = new byte[1024];
        while(!Thread.currentThread().isInterrupted() && !stopWorker){
        	try {
        		int bytesAvailable = mmInStream.available();                        
        		if(bytesAvailable > 0){
        			byte[] packetBytes = new byte[bytesAvailable];
        			mmInStream.read(packetBytes);
        			for(int i=0;i<bytesAvailable;i++){
        				byte b = packetBytes[i];
        				if(b == delimiter){
        					byte[] encodedBytes = new byte[readBufferPosition];
        					System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
        					String data = new String(encodedBytes, "US-ASCII");
        					mHandler.obtainMessage(1,data).sendToTarget();
        					readBufferPosition = 0;        
        				}
        				else{
                                    readBuffer[readBufferPosition++] = b;
                        }
        			}
        		}
        	} catch (IOException ex) {stopWorker = true;}
        }
    }
		
	public void write(byte[] bytes) {
		try {
			mmOutStream.write(bytes);
		} catch (IOException e) { }
	}
		
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

