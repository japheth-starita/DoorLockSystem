package com.example.doorlocksystem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends Activity {

	TextView admin_user;
	TextView admin_pw;
	EditText pw;
	EditText user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		pw = (EditText) findViewById(R.id.pw);
		pw.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_GO) {
		            checkAdmin();
		            handled = true;
		        }
		        return handled;
		    }
		});
		
	}

	//Check Admin Password and Username
    public void submitAdmin(View view){
    	checkAdmin();
    }
    
    public void checkAdmin(){
    	SystemMethods sm = new SystemMethods();
    	admin_user = (TextView) findViewById(R.id.user);
    	admin_pw = (TextView) findViewById(R.id.pw);
    	if (sm.CheckAdminPassword(admin_user.getText().toString(),
    			admin_pw.getText().toString())){
    		Intent intent = new Intent(this, DeviceManagerActivity.class);
    		startActivity(intent);
    	}
    	else{
    		Toast.makeText(getApplicationContext(), "Username and Password don't match!", Toast.LENGTH_SHORT).show();
    		admin_user.setText("");
    		admin_user.requestFocus();
    		admin_pw.setText("");
    		
    	}
    }
}