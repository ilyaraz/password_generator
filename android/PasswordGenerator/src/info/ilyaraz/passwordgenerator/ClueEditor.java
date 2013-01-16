package info.ilyaraz.passwordgenerator;

import info.ilyaraz.passwordgenerator.domain.ClueData;
import info.ilyaraz.passwordgenerator.util.Callback1;
import info.ilyaraz.passwordgenerator.util.Closure;
import info.ilyaraz.passwordgenerator.util.Constants;
import info.ilyaraz.passwordgenerator.util.ObjectSerializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class ClueEditor {
	public static void editClue(String clueId, final Activity context, final Callback1<ClueData> onSuccess, final Closure onFailure) {
		final SharedPreferences settings = context.getSharedPreferences(Constants.STORAGE_NAMESPACE, 0);
		
    	LayoutInflater inflater = context.getLayoutInflater();
    	final View dialogLayout = inflater.inflate(R.layout.add_clue, null);
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setView(dialogLayout);
    	    	
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText clueNameField = (EditText)dialogLayout.findViewById(R.id.clue);
				String clueName = clueNameField.getText().toString();
				EditText passwordLengthField = (EditText)dialogLayout.findViewById(R.id.password_length);
				int passwordLength = Integer.parseInt(passwordLengthField.getText().toString());
				if (passwordLength > Constants.MAX_PASSWORD_LENGTH) {
					throw new RuntimeException("password length is too large: " + passwordLength);
				}
				HashSet<Character> alphabet = new HashSet<Character>();
				Spinner alphabetField = (Spinner)dialogLayout.findViewById(R.id.character_set);
				long alphabetID = alphabetField.getSelectedItemId();
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
				ClueData clue = new ClueData(getRandomId(), clueName, passwordLength, alphabet);
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
    	
    	builder.show();
	}
	
	private static String getRandomId() {
		return UUID.randomUUID().toString();
	}
}
