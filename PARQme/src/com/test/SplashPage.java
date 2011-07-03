package com.test;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 

/*	HEY DEVELOPER, THIS IS A HORRIBLE WAY TO IMPLIMENT A SPLASH SCREEN.  YOU BASICALLY STALL THE MACHINE FOR 5 SECONDS.
 * 
 * FIND A BETTER WAY YOU NOOB.  you want to load parqactivity in the back as well.  */

public class SplashPage extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash);
      Thread splashThread = new Thread() {
         @Override
         public void run() {
            try {
               int waited = 0;
               while (waited < 5000) {
                  sleep(100);
                  waited += 100;
               }
            } catch (InterruptedException e) {
               // do nothing
            } finally {
               finish();
               Intent i = new Intent();
               i.setClassName("main.java", "main.java.MainPage");
               
               startActivity(i);
            }
         }
      };
      splashThread.start();
   }
}