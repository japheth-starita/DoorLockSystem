package com.example.doorlocksystem;

import android.os.Handler;

public class SystemMethods {
	private final Handler handler = new Handler();
	public void delay(int delay){
		delay*=1000;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
			}
		 
		}, delay);
	}
	
	public boolean CheckAdminPassword(String user, String pw){
		if ((user.equals("admin")) && (pw.equals("admin"))){
			return true;
		}
		else{
			return false;
		}
	}
	

	
}
