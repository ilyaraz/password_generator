package info.ilyaraz.passwordgenerator;

import info.ilyaraz.passwordgenerator.domain.ClueData;
import info.ilyaraz.passwordgenerator.util.Callback1;
import info.ilyaraz.passwordgenerator.util.Closure;
import info.ilyaraz.passwordgenerator.util.Constants;
import info.ilyaraz.passwordgenerator.util.HashCalculator;
import info.ilyaraz.passwordgenerator.util.ObjectSerializer;
import info.ilyaraz.passwordgenerator.util.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String MASTER_HASH = "MASTER_HASH";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final SharedPreferences settings = this.getSharedPreferences("UnpredictablePasswordGenerator", 0);
        settings.edit().clear().commit();
        String masterPasswordHash = settings.getString(MASTER_HASH, null);
        if (masterPasswordHash == null) {
        	MasterPasswordEditor.editMasterPassword(this, "Set Master Password", "Please set master password.", 
        			new StringCallback() {
						@Override
						public void Run(String value) {
							finishCreation();
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
        
        finishCreation();
 

    }
	
	private void finishCreation() {
		setContentView(R.layout.activity_main);
	    EditText masterPasswordField = (EditText)findViewById(R.id.master_password);
	    final Activity parent = this;
	    final ArrayList<ClueData> clues = this.clues;
        masterPasswordField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				EditText masterPasswordField = (EditText)parent.findViewById(R.id.master_password);
				String masterPassword = masterPasswordField.getText().toString();
				Spinner clueSpinner = (Spinner)parent.findViewById(R.id.clue);
				long position = clueSpinner.getSelectedItemPosition();
				if (position < 0 || position >= clues.size()) {
					return;
				}
				ClueData clueData = clues.get((int)position);
				String clue = clueData.getClueName();
				int passwordLength = clueData.getPasswordLength();
				Set<Character> alphabet = clueData.getAlphabet();
				String password = HashCalculator.getPassword(masterPassword, clue, passwordLength, alphabet);
				TextView passwordField = (TextView)parent.findViewById(R.id.password);
				passwordField.setText(password);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
        	
        });
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    void changeMasterPasswordMenuItem() {
    	MasterPasswordEditor.editMasterPassword(this, "Change Master Password", "Please set new master password.", 
    			new StringCallback() {
					@Override
					public void Run(String value) {
						
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
    
    private ArrayList<ClueData> clues = new ArrayList<ClueData>();
    
    private void addCluesToSpinner() {
    	final SharedPreferences settings = getSharedPreferences(Constants.STORAGE_NAMESPACE, 0);
    	Map<String, ?> settingsMap = settings.getAll();
    	clues.clear();
    	try {
	    	for (String key: settingsMap.keySet()) {
	    		if (key.startsWith(Constants.CLUES_PREFIX)) {
	    			clues.add((ClueData)ObjectSerializer.deserialize((String)settingsMap.get(key)));
	    		}
	    	}
    	}
    	catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    	Collections.sort(clues, new Comparator<ClueData>() {

			@Override
			public int compare(ClueData arg0, ClueData arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
    		
    	});
    	
    	String[] cluesNames = new String[clues.size()];
    	for (int i = 0; i < clues.size(); ++i) {
    		cluesNames[i] = clues.get(i).getClueName();
    	}
    	SpinnerAdapter newSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cluesNames);
    	Spinner cluesSpinner = (Spinner)findViewById(R.id.clue);
    	cluesSpinner.setAdapter(newSpinnerAdapter);
    }
}
