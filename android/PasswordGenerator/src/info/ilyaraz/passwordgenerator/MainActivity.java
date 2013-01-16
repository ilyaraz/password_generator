package info.ilyaraz.passwordgenerator;

import info.ilyaraz.passwordgenerator.domain.ClueData;
import info.ilyaraz.passwordgenerator.util.Callback1;
import info.ilyaraz.passwordgenerator.util.Closure;
import info.ilyaraz.passwordgenerator.util.Constants;
import info.ilyaraz.passwordgenerator.util.ObjectSerializer;
import info.ilyaraz.passwordgenerator.util.StringCallback;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class MainActivity extends Activity {

    private static final String MASTER_HASH = "MASTER_HASH";
    
    private String masterPasswordHash = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final SharedPreferences settings = this.getSharedPreferences("UnpredictablePasswordGenerator", 0);
        settings.edit().clear().commit();
        String masterPasswordHash = settings.getString(MASTER_HASH, null);
        if (masterPasswordHash == null) {
        	final MainActivity activity = this;
        	
        	MasterPasswordEditor.editMasterPassword(this, "Set Master Password", "Please set master password.", 
        			new StringCallback() {
						@Override
						public void Run(String value) {
							activity.masterPasswordHash = value;
							setContentView(R.layout.activity_main);
						}
					}, 
        			new Closure() {
						@Override
						public void Run() {
							moveTaskToBack(true);
						}
					});
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
    
    
    void changeMasterPasswordMenuItem() {
    	final MainActivity activity = this;
    	
    	MasterPasswordEditor.editMasterPassword(this, "Change Master Password", "Please set new master password.", 
    			new StringCallback() {
					@Override
					public void Run(String value) {
						activity.masterPasswordHash = value;
						setContentView(R.layout.activity_main);
					}
				}, 
    			new Closure() {
					@Override
					public void Run() {
					}
				});
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.changeMasterItem:
    			changeMasterPasswordMenuItem();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    public void onAddClue(View view) {
    	ClueEditor.editClue(null, this, 
    			new Callback1<ClueData>() {
					@Override
					public void Run(ClueData value) {
						addCluesToSpinner();
					}
				}, 
				new Closure() {
					@Override
					public void Run() {
						
					}
				});
    }
    
    private void addCluesToSpinner() {
    	final SharedPreferences settings = getSharedPreferences(Constants.STORAGE_NAMESPACE, 0);
    	Map<String, ?> settingsMap = settings.getAll();
    	List<ClueData> clues = new ArrayList<ClueData>();
    	try {
	    	for (String key: settingsMap.keySet()) {
	    		if (key.startsWith(Constants.CLUES_PREFIX)) {
	    			Log.d("pizdec", (String)settingsMap.get(key));
	    			clues.add((ClueData)ObjectSerializer.deserialize((String)settingsMap.get(key)));
	    		}
	    	}
    	}
    	catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    	String[] cluesNames = new String[clues.size()];
    	int ind = 0;
    	for (ClueData clue: clues) {
    		cluesNames[ind++] = clue.toString();
    	}
    	Arrays.sort(cluesNames);
    	SpinnerAdapter newSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cluesNames);
    	Spinner cluesSpinner = (Spinner)findViewById(R.id.clue);
    	Log.d("epta", Arrays.toString(cluesNames));
    	cluesSpinner.setAdapter(newSpinnerAdapter);
    }
}
