package com.example.asdas;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.example.asdas.CustomHttpClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class mapwork extends FragmentActivity {
	
	EditText etLng;
	EditText etLat;
	GoogleMap googleMap;
	Double lat,lng;
	Button b1,b2;
	TextView tv;
	String returnString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_work);
		
		// Reference to the EditText et_lat of the layout activity_main.xml
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
		
		// Reference to the EditText et_lng of the layout activity_main.xml
		etLng = (EditText)findViewById(R.id.et_lng);
		tv = (TextView) findViewById(R.id.textView1);
		b1=(Button)findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					mappp();
		}
			});
		b2=(Button)findViewById(R.id.button2);
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					mappp();
		}
			});
		
	}
	
	private void mappp(){
		 // declare parameters that are passed to PHP script i.e. the name "birthyear" and its value submitted by user   
        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        
        // define the parameter
        postParameters.add(new BasicNameValuePair("busnum",
    etLng.getText().toString()));
        String response = null;
        
        // call executeHttpPost method passing necessary parameters 
        try {
   response = CustomHttpClient.executeHttpPost(
     //"http://129.107.187.135/CSE5324/jsonscript.php", // your ip address if using localhost server
     "http://banarga.com/get.php",  // in case of a remote server
     postParameters);
   
   // store the result returned by PHP script that runs MySQL query
   String result = response.toString();  
            
    //parse json data
       try{
         returnString="";
         JSONArray jArray = new JSONArray(result);
               for(int i=0;i<jArray.length();i++){
                       JSONObject json_data = jArray.getJSONObject(i);
                       Log.i("log_tag","BUS: "+json_data.getInt("id")+
                               ", latitude: "+json_data.getString("lati")+
                               ", longitude: "+json_data.getString("longi")
                       );
                       //Get an output to the screen
                    
                       lat=Double.parseDouble((json_data.getString("lati")));
                      lng=Double.parseDouble((json_data.getString("longi")));
                      returnString += "Lat"+ json_data.getString("lati") + " \n "+ "Lon: "+ json_data.getString("longi");
                      
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
          }   }
       catch (Exception e) {
           Log.e("log_tag","Error in http connection!!" + e.toString());     
          }
	/*	// Getting the entered latitude
		String lat = etLat.getText().toString();
		
		// Getting the entered longitude
		String lng = etLng.getText().toString();*/
		
		 // Getting Google Play availability status
      int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

              // Showing status
      if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

          int requestCode = 10;
          Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getParent(), requestCode);
          dialog.show();

      }else { // Google Play Services are available           

          // Getting reference to the SupportMapFragment of activity_main.xml
          SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

          // Getting GoogleMap object from the fragment
          googleMap = fm.getMap();

          // Enabling MyLocation Layer of Google Map
          googleMap.setMyLocationEnabled(true);
          
          // LatLng object to store user input coordinates
          LatLng point = new LatLng(lat, lng);
          
          // Drawing the marker at the coordinates
          drawMarker(point);   
      }
}
	
	
	private void drawMarker(LatLng point){
		// Clears all the existing coordinates
		googleMap.clear();		
		
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);       
        
        // Setting title for the InfoWindow
        markerOptions.title("Position");
        
        // Setting InfoWindow contents
        markerOptions.snippet("Latitude:"+point.latitude+",Longitude"+point.longitude);
       // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.));

        // Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
        
        // Moving CameraPosition to the user input coordinates
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

        // Setting the zoom level
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));        
        
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

