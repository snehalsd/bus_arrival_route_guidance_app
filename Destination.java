package com.example.asdas;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.asdas.CustomHttpClient;



import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class Destination extends Activity {
	
	 EditText source,destination;   //  input from user
	 Button submit;    
	 TextView tv;      // TextView to show the result of MySQL query 
	 
	 String returnString;   // to store the result of MySQL query after decoding JSON

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destination);
		 source = (EditText) findViewById(R.id.editText1);
	        destination = (EditText) findViewById(R.id.editText2);
	        submit = (Button) findViewById(R.id.submitbutton);
	        tv = (TextView) findViewById(R.id.showresult);
	        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
	                
	        // define the action when user clicks on submit button
	        submit.setOnClickListener(new View.OnClickListener(){        
	         public void onClick(View v) {
	          // declare parameters that are passed to PHP script i.e. the name "birthyear" and its value submitted by user   
	          ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	          
	          // define the parameter
	          postParameters.add(new BasicNameValuePair("source",
	      source.getText().toString()));
	          postParameters.add(new BasicNameValuePair("destination",destination.getText().toString()));
	          String response = null;
	          
	          // call executeHttpPost method passing necessary parameters 
	          try {
	     response = CustomHttpClient.executeHttpPost(
	       // your ip address if using localhost server
	       "http://banarga.com/test.php",  // in case of a remote server
	       postParameters);
	     
	     // store the result returned by PHP script that runs MySQL query
	     String result = response.toString();  
	              
	      //parse json data
	         try{
	                 returnString = "";
	           JSONArray jArray = new JSONArray(result);
	                 for(int i=0;i<jArray.length();i++){
	                         JSONObject json_data = jArray.getJSONObject(i);
	                         Log.i("log_tag","bus_id: "+json_data.getInt("bus_unique_id")+
	                                 ", registration_number: "+json_data.getString("registration_number")+
	                                 ", route_no: "+json_data.getString("route_no")+
	                                 ", color: "+json_data.getString("color")+
	                                 ", class: "+json_data.getString("class")
	                         );
	                         //Get an output to the screen
	                         returnString += "\n" + "BUS_ID: "+ json_data.getInt("bus_unique_id") + "\n"+"REGISTRATION NUMBER: "+ json_data.getString("registration_number")+"\n"+"ROUTE NUMBER: "+ json_data.getString("route_no")+"\n"+"Color: "+ json_data.getString("color")+ "\n"+"CLASS: " + json_data.getString("class")+"\n";
	                 }
	         }
	         catch(JSONException e){
	                 Log.e("log_tag", "Error parsing data "+e.toString());
	         }
	     
	         try{
	          tv.setText(returnString);
	          if(returnString=="")
	        	  tv.setText("No direct route exists");
	         }
	         catch(Exception e){
	          Log.e("log_tag","Error in Display!" + e.toString());;          
	         }   
	    }
	          catch (Exception e) {
	     Log.e("log_tag","Error in http connection!!" + e.toString());     
	    }
	         }         
	        });
	    }
	}

	


