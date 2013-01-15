package info.ilyaraz.passwordgenerator;

import java.security.MessageDigest;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static final String MASTER_HASH = "MASTER_HASH";
    
    private String masterPasswordHash = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final SharedPreferences settings = this.getSharedPreferences("UnpredictablePasswordGenerator", 0);
        String masterPasswordHash = settings.getString(MASTER_HASH, null);
        if (masterPasswordHash == null) {
        	AlertDialog.Builder masterPasswordDialog = new AlertDialog.Builder(this);
        	masterPasswordDialog.setTitle("Set Master Password");
        	masterPasswordDialog.setMessage("Please set master password.");
        	masterPasswordDialog.setCancelable(false);
        	
        	final MainActivity activity = this;
        	final EditText input = new EditText(this);
        	masterPasswordDialog.setView(input);
        	
        	masterPasswordDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String value = input.getText().toString();
					
					try {
						MessageDigest hasher = MessageDigest.getInstance("SHA-512", "BC");
						byte[] digest = hasher.digest(value.getBytes());
						value = new String(digest);
					} catch (Exception e) {
						e.printStackTrace();
						moveTaskToBack(true);
					}
					
					settings.edit().putString(MASTER_HASH, value).commit();
					activity.masterPasswordHash = value;
					setContentView(R.layout.activity_main);
				}
			});
        	
        	masterPasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					moveTaskToBack(true);
				}
			});
        	
        	masterPasswordDialog.show();
        	return;
        }
        
        this.masterPasswordHash = masterPasswordHash;
        
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
