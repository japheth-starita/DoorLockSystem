package com.example.doorlocksystem;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

public class AddOwnDevice {
	AlertDialog alertBuilder;
	public void showInputDialogBox(Context context, final Handler mhandler){
		View view = (LayoutInflater.from(context)).inflate(R.layout.add_own_device, null);
		alertBuilder = new AlertDialog.Builder(context)
		.setView(view)
		.setCancelable(false)
		.create();
		alertBuilder.show();
	}
	
	public void removeDialog(){
		alertBuilder.dismiss();
	}
}
