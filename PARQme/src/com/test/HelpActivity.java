package com.test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity {
	private Button testphp;
	public static final String SAVED_INFO = "ParqMeInfo";
	private TextView phpre;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.myhelp);
        testphp = (Button) findViewById(R.id.testbutton);
        phpre = (TextView) findViewById(R.id.phpresponse);
        
        testphp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				final SharedPreferences check = getSharedPreferences(SAVED_INFO,0);
				final SharedPreferences.Editor editor = check.edit();
				editor.putBoolean("parkState", false);
				editor.commit();
			}
		});
	}
	public String hitPhp(){
		String outstring ="";
		
		try {
			URL url = new URL("http://parqme.com/writedb");
			URLConnection servletConnection = url.openConnection();

			// inform the connection that we will send output and accept input
			servletConnection.setDoInput(true);
			servletConnection.setDoOutput(true);

			// Don't use a cached version of URL connection.
			servletConnection.setUseCaches(false);
			servletConnection.setDefaultUseCaches(false);

			// Specify the content type that we will send binary data
			servletConnection.setRequestProperty("Content-Type", "application/x-java-serialized-object");

			// get input and output streams on servlet
			ObjectOutputStream os = new ObjectOutputStream(servletConnection.getOutputStream());

			// send your data to the servlet
			os.writeObject("testing");
			os.flush();
			os.close();

			ObjectInputStream iStream = new ObjectInputStream(servletConnection.getInputStream());
			outstring+=iStream.readObject();
			

			/*read response code and interprit*/

		}catch (Exception e){
			e.printStackTrace();
		}
		
		return outstring;
	}

}
