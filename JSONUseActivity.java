package com.example.asdas;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;

import com.example.asdas.CustomHttpClient;




import android.util.Log;


public class JSONUseActivity extends Activity implements OnInitListener  {
 EditText byear;   // input from user
 Button submit;    
 TextView tv;      // TextView to show the result of MySQL query 
 String lat="", lon="",text="",text1="";
 String returnString;   // to store the result of MySQL query after decoding JSON
 double lat1,lon1,lat2,lon2;
 Float distanceInMeters;
 TextView tv1;
 private int MY_DATA_CHECK_CODE = 0;
	
	private TextToSpeech tts;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
  .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonuse);
        Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		
        byear = (EditText) findViewById(R.id.editText1);
        submit = (Button) findViewById(R.id.submitbutton);
        tv = (TextView) findViewById(R.id.showresult);
       
                
        // define the action when user clicks on submit button
        submit.setOnClickListener(new View.OnClickListener(){        
         public void onClick(View v) {
          // declare parameters that are passed to PHP script i.e. the name "birthyear" and its value submitted by user   
          ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
          
          // define the parameter
          postParameters.add(new BasicNameValuePair("busnum",
      byear.getText().toString()));
          String response = null;
          
          // call executeHttpPost method passing necessary parameters 
          try {
     response = CustomHttpClient.executeHttpPost(
       // your ip address if using localhost server
       "http://banarga.com/get.php",  // in case of a remote server
       postParameters);
     
     // store the result returned by PHP script that runs MySQL query
     String result = response.toString();  
              
      //parse json data
         try{
                 returnString = "";
           JSONArray jArray = new JSONArray(result);
                 for(int i=0;i<jArray.length();i++){
                         JSONObject json_data = jArray.getJSONObject(i);
                         Log.i("log_tag","BUS: "+json_data.getInt("id")+
                                 ", latitude: "+json_data.getString("lati")+
                                 ", longitude: "+json_data.getString("longi")
                         );
                         //Get an output to the screen
                         lat=json_data.getString("lati");
                         lon=json_data.getString("longi");
                         lat1=Double.parseDouble((json_data.getString("lati")));
                         lon1=Double.parseDouble((json_data.getString("longi")));
                         returnString += "\n" + "Latitude"+ json_data.getString("lati") + " \n "+ "Longitude: "+ json_data.getString("longi");
                 }
         }
         catch(JSONException e){
                 Log.e("log_tag", "Error parsing data "+e.toString());
         }
     
         try{
          tv.setText(returnString);
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
        Button btnAdd = (Button)findViewById(R.id.button1);
		btnAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TextView tv = (TextView)findViewById(R.id.address);
				tv.setText(GetAddress(lat, lon));
				text=tv.getText().toString();
				if (text!=null && text.length()>0) {
					Toast.makeText(JSONUseActivity.this, "Saying: " + text, Toast.LENGTH_LONG).show();
					tts.speak("Your bus is in" + text, TextToSpeech.QUEUE_ADD, null);
				}
				
				
				
			}
		});

		
		 Button btnshow = (Button)findViewById(R.id.button3);
			btnshow.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					 Intent mapin= new Intent(JSONUseActivity.this, mapwork.class);
                     startActivity(mapin);
                  	
				}
			});
		
		
		
		 Button btndist = (Button)findViewById(R.id.button2);
			btndist.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					locationn();
				/*	text1=tv1.getText().toString();
					if (text1!=null && text1.length()>0) {
						Toast.makeText(JSONUseActivity.this, "Saying: " + text1, Toast.LENGTH_LONG).show();
						tts.speak("Your bus is at a distance of" + text1, TextToSpeech.QUEUE_ADD, null);
					}*/
					
					
					
			}}); 
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				tts = new TextToSpeech(this, this);
			} 
			else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}

	}
    
    public void locationn()
    {
    	// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) JSONUseActivity.this.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
			
				lat2 =location.getLatitude();
				lon2 = location.getLongitude();
	
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		Location loc1=new Location("");
		loc1.setLatitude(lat1);
		loc1.setLongitude(lon1);
		Location loc2=new Location("");
		loc2.setLatitude(lat2);
		loc2.setLongitude(lon2);
		distanceInMeters=loc1.distanceTo(loc2);
		float distanceInKm=distanceInMeters/1000;
		
		 tv1 = (TextView)findViewById(R.id.textView1);
			tv1.setText(Double.toString(distanceInKm)+"km");
    
    }
    public String GetAddress(String lat, String lon)
	{
    	
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		String ret = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 10);

			//List<Address> addresses = geocoder.getFromLocation(37.423247,-122.085469,1);
			if(addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
				}
				ret = strReturnedAddress.toString();
				
	
				
			}
			else{
				ret = "No Address returned!";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = "Can't get Address!";
		}
		return ret;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			Toast.makeText(JSONUseActivity.this, 
					"Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show();
		}
		else if (status == TextToSpeech.ERROR) {
			Toast.makeText(JSONUseActivity.this, 
					"Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
		}
		
	}
}