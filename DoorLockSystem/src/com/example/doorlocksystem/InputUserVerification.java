package com.example.doorlocksystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputUserVerification {
	private String verifychar;
	private String devicename;
	public void showInputDialogBox(final boolean isFirstTime, Context context, final Handler mhandler){
		View view = (LayoutInflater.from(context)).inflate(R.layout.user_input_box, null);
		final EditText inputverifychar = (EditText) view.findViewById(R.id.inputverifychar);
		final EditText inputdevicename = (EditText) view.findViewById(R.id.inputdevicename);
		final AlertDialog alertBuilder = new AlertDialog.Builder(context)
		.setView(view)
		.setCancelable(!isFirstTime)
		.setPositiveButton("OK", null)
		.setNegativeButton("Cancel", null)
		.create();
		
		alertBuilder.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface d) {
				inputdevicename.setText(MainActivity.strdeviceName);
				Button a = alertBuilder.getButton(alertBuilder.BUTTON_POSITIVE);
				a.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(!inputverifychar.getText().toString().isEmpty()){
							verifychar =inputverifychar.getText().toString();
							devicename = inputdevicename.getText().toString();
							//Send to mainactivity or devicemanager
							mhandler.obtainMessage(1,verifychar+"\n"+devicename).sendToTarget();
							alertBuilder.dismiss();
						}
						
					}
				});
				
				Button b = alertBuilder.getButton(alertBuilder.BUTTON_NEGATIVE);
				if(isFirstTime){
					b.setEnabled(false);
					inputdevicename.setEnabled(true);
				}
				
			}
		});
		
		alertBuilder.show();		
	}
	
	public String getVerifyChar(){
		return verifychar;
	}
}
