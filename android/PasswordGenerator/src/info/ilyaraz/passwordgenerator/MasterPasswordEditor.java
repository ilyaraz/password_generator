package info.ilyaraz.passwordgenerator;

import info.ilyaraz.passwordgenerator.util.Closure;
import info.ilyaraz.passwordgenerator.util.Constants;
import info.ilyaraz.passwordgenerator.util.HashCalculator;
import info.ilyaraz.passwordgenerator.util.StringCallback;

import java.security.MessageDigest;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.EditText;

public class MasterPasswordEditor {
	public static void editMasterPassword(final ContextWrapper context, String caption, String text, final StringCallback onSuccess, final Closure onFailure) {
		final SharedPreferences settings = context.getSharedPreferences(Constants.STORAGE_NAMESPACE, 0);
		
		AlertDialog.Builder masterPasswordDialog = new AlertDialog.Builder(context);
    	masterPasswordDialog.setTitle(caption);
    	masterPasswordDialog.setMessage(text);
    	masterPasswordDialog.setCancelable(false);
    	
    	final EditText input = new EditText(context);
    	masterPasswordDialog.setView(input);
    	
    	masterPasswordDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String value = input.getText().toString();
				value = HashCalculator.base64SHA512(value);
				
				settings.edit().putString(Constants.MASTER_HASH, value).commit();
				onSuccess.Run(value);
			}
		});
    	
    	masterPasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onFailure.Run();
			}
		});
    	
    	masterPasswordDialog.show();
	}
}
