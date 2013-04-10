package com.csc591.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Home extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Removes title Bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Uses below to Remove notification bar but not required in our application. Helpful in games
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		ViewServer.get(this).addWindow(this);
	}
	
	public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
   }

    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
   }

}
