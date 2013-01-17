package info.ilyaraz.passwordgenerator.ui;

import info.ilyaraz.passwordgenerator.R;
import info.ilyaraz.passwordgenerator.util.Callback1;
import info.ilyaraz.passwordgenerator.util.StringCallback;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ClueAdapter extends BaseAdapter implements SpinnerAdapter {
	private ArrayList<String> items = new ArrayList<String>();
	private ArrayList<String> itemIds = new ArrayList<String>();
	private Activity activity;
	private final StringCallback onEditClue;
	private final StringCallback onRemoveClue;
	private final Callback1<Integer> onClick;
	
	public ClueAdapter(ArrayList<String> items, ArrayList<String> itemIds, Activity activity, StringCallback editClue, StringCallback removeClue, Callback1<Integer> onClick) {
		this.items = new ArrayList<String>(items);
		this.itemIds = new ArrayList<String>(itemIds);
		this.activity = activity;
		this.onEditClue = editClue;
		this.onRemoveClue = removeClue;
		this.onClick = onClick;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final String clueId = itemIds.get(arg0);
		
		final LayoutInflater inflater = activity.getLayoutInflater();
		View entry = arg1;
		if (entry == null) {
			entry = inflater.inflate(R.layout.clue_spinner_entry, null);
		}
		final View spinnerEntry = entry;
		
		final TextView clueName = (TextView) spinnerEntry.findViewById(R.id.spinnerEntryClueName);
		final ImageButton editClue = (ImageButton) spinnerEntry.findViewById(R.id.spinnerEntryEdit);		
		final ImageButton removeClue = (ImageButton) spinnerEntry.findViewById(R.id.spinnerEntryRemove);
		final int index = arg0;
		
		clueName.setText(items.get(arg0));
		
		editClue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onEditClue.Run(clueId);
			}
		});
		
		removeClue.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				onRemoveClue.Run(clueId);
			}
		});
		
//		spinnerEntry.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onClick.Run(index);
//			}
//		});
		
		return spinnerEntry;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);				
	}

	
}
