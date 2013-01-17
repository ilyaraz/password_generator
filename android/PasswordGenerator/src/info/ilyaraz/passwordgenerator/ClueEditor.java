package info.ilyaraz.passwordgenerator;

import info.ilyaraz.passwordgenerator.domain.ClueData;
import info.ilyaraz.passwordgenerator.util.Callback1;
import info.ilyaraz.passwordgenerator.util.Closure;
import info.ilyaraz.passwordgenerator.util.Constants;
import info.ilyaraz.passwordgenerator.util.ObjectSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class ClueEditor {
	
	public static void editClue(final String clueId, final Activity context, final Callback1<ClueData> onSuccess, final Closure onFailure) {
		final SharedPreferences settings = context.getSharedPreferences(Constants.STORAGE_NAMESPACE, 0);
		
    	LayoutInflater inflater = context.getLayoutInflater();
    	final View dialogLayout = inflater.inflate(R.layout.add_clue, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setView(dialogLayout);

    	final EditText passwordLengthField = (EditText)dialogLayout.findViewById(R.id.password_length);
    	final EditText clueNameField = (EditText)dialogLayout.findViewById(R.id.clue);
    	final Spinner alphabetField = (Spinner)dialogLayout.findViewById(R.id.character_set);
    	
    	if (clueId != null) {
    		String clueString = settings.getString(Constants.CLUES_PREFIX + clueId, null);
    		if (clueString != null) {
    			ClueData data = null;
				try {
					data = (ClueData) ObjectSerializer.deserialize(clueString);
				} catch (Exception e) {
				}
				if (data != null) {
					passwordLengthField.setText(Integer.toString(data.getPasswordLength()));
					clueNameField.setText(data.getClueName());
					
					int index = -1;
					for (int i = 0; i < 2; ++i) {
						HashSet<Character> chars = new HashSet<Character>();
						populateAlphabet(chars, i);
						if (chars.equals(data.getAlphabet())) {
							index = i;
							break;
						}
					}
					if (index == -1) {
						throw new RuntimeException();
					}
					alphabetField.setSelection(index);
				}
    		}
    	}
    	    	
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String clueName = clueNameField.getText().toString();
				int passwordLength = Integer.parseInt(passwordLengthField.getText().toString());
				if (clueName.equals("") || passwordLength > Constants.MAX_PASSWORD_LENGTH) {
					throw new RuntimeException("password length is too large: " + passwordLength);
				}
				HashSet<Character> alphabet = new HashSet<Character>();
				long alphabetID = alphabetField.getSelectedItemId();
				populateAlphabet(alphabet, alphabetID);
				ClueData clue = new ClueData(clueId == null ? getRandomId() : clueId, clueName, passwordLength, alphabet);
				try {
					settings.edit().putString(Constants.CLUES_PREFIX + clue.getId(), ObjectSerializer.serialize(clue)).commit();
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
				onSuccess.Run(clue);			
			}
		});
    	
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onFailure.Run();
			}
		});
    	
    	builder.setCancelable(false);
    	
    	final AlertDialog dialog = builder.create();
    
    	TextWatcher textWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateOkState(dialog, passwordLengthField, clueNameField);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		};
		
		passwordLengthField.addTextChangedListener(textWatcher);
		clueNameField.addTextChangedListener(textWatcher);
		
    	dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				updateOkState(dialog, passwordLengthField, clueNameField);
			}
		});
    	dialog.show();
	}
	
	private static String getRandomId() {
		return UUID.randomUUID().toString();
	}

	private static void updateOkState(final AlertDialog dialog, final EditText passwordLengthField, EditText clueNameField) {
		boolean ok = true;
		try {
			int passwordLength = Integer.parseInt(passwordLengthField.getText().toString());
			if (passwordLength > Constants.MAX_PASSWORD_LENGTH) {
				ok = false;
			}
			if (clueNameField.getText().toString().equals("")) {
				ok = false;
			}
		} catch (Throwable e) {
			ok = false;
		}
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(ok);
	}

	private static void populateAlphabet(HashSet<Character> alphabet,
			long alphabetID) {
		if (alphabetID == 0) {
			for (int c = 33; c < 127; ++c) {
				alphabet.add((char)c);
			}
		}
		else {
			for (char c = 'A'; c <= 'Z'; ++c) {
				alphabet.add(c);
			}
			for (char c = 'a'; c <= 'z'; ++c) {
				alphabet.add(c);
			}
			for (char c = '0'; c <= '9'; ++c) {
				alphabet.add(c);
			}
		}
	}
}
