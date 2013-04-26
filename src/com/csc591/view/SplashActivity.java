
package com.csc591.view;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

        protected boolean _active = true;
        protected int _splashTime = 3000; // time to display the splash screen in ms

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash);

         // thread for displaying the SplashScreen
            Thread splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while(_active && (waited < _splashTime)) {
                            sleep(100);
                            if(_active) {
                                waited += 100;
                            }
                        }
                    } catch(InterruptedException e) {

                    } finally {

                        startActivity(new Intent(SplashActivity.this, Home.class));
                        finish();
                        //stop();
                    }
                }
            };
            splashTread.start();
        }
    }