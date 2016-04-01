package com.example.asdas;

import com.example.asdas.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";


	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = (Button) findViewById(R.id.scanner);   
        b1.setOnClickListener(this);
        Button b3 = (Button) findViewById(R.id.button3);   
        b3.setOnClickListener(this);

        Button b2 = (Button) findViewById(R.id.button1);   
        b2.setOnClickListener(this);
        Button b4 = (Button) findViewById(R.id.button2);   
        b4.setOnClickListener(this);
    }
            public void onClick(View v) {
            	
            	
            	 switch(v.getId()) {
                 case R.id.scanner:
                	 try {
             			Intent intent = new Intent(ACTION_SCAN);
             			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
             			startActivityForResult(intent, 0);
             		} catch (ActivityNotFoundException anfe) {
             			showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
             		}
                   break;
                 case R.id.button1:
               
                  Intent i= new Intent(MainActivity.this, Destination.class);
                  startActivity(i);
               
                  break;
                  
                 case R.id.button3:
                     
                     Intent innnntent= new Intent(MainActivity.this, menu.class);
                     startActivity(innnntent);
                  
                     break;
                 	case R.id.button2:
                     
                     Intent indb= new Intent(MainActivity.this, JSONUseActivity.class);
                     startActivity(indb);
                  
                     break;
                     
            }
      
            }
       
    

    	
    	

    public void scanQR(View v) {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}
  
	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);
		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {

				}
			}
		});
	
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});
		return downloadDialog.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

				Toast toast = Toast.makeText(this,  contents , Toast.LENGTH_LONG);
				toast.show();
			}
		}
	}
}	
  
      