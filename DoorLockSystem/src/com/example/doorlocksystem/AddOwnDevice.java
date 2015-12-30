package com.example.doorlocksystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddOwnDevice {
	private String verifychar;
	AlertDialog alertBuilder;
	public void showInputDialogBox(Context context, final Handler mhandler){
		View view = (LayoutInflater.from(context)).inflate(R.layout.add_own_device, null);
		final TextView textProg = (TextView) view.findViewById(R.id.tvProg);
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
