package com.android.Jumble;

import java.util.Vector;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class View_Records extends Activity {

	private Spinner mSpinner;
	private Button mView;
	private Button mDelete;
	private DataManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_records);

		mSpinner = (Spinner) findViewById(R.id.spinner1);
		final String levels[] = { "NOVICE", "MEDIUM", "EXPERT" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levels);
		mSpinner.setAdapter(adapter);

		// Initialize Buttons
		mView = (Button) findViewById(R.id.View);
		mDelete = (Button) findViewById(R.id.Delete);

		mManager = new DataManager(this);

		mView.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Get Records If Available
				String s_level = (String) mSpinner.getSelectedItem().toString();
				int level = 0;
				if (s_level.equals(levels[0]))
					level = GameLogic.NOVICE;
				else if (s_level.equals(levels[1]))
					level = GameLogic.MEDIUM;
				else if (s_level.equals(levels[2]))
					level = GameLogic.EXPERT;

				Vector<Record> recs = mManager.LoadData(level);

				createTable(recs);
			}
		});

		mDelete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String s_level = (String) mSpinner.getSelectedItem().toString();
				int level = 0;
				if (s_level.equals(levels[0]))
					level = GameLogic.NOVICE;
				else if (s_level.equals(levels[1]))
					level = GameLogic.MEDIUM;
				else if (s_level.equals(levels[2]))
					level = GameLogic.EXPERT;

				mManager.deleteFile(level);
			}
		});

	}

	private void createTable(Vector<Record> recs) {

		if (recs == null) {
			Toast.makeText(this, "No Records Found!!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		TableLayout table = new TableLayout(this);
		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		int max_row = 1 + recs.size();
		TableRow rows[] = new TableRow[max_row];
		for (int i = 0; i < max_row; i++) {
			rows[i] = new TableRow(this);
		}

		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.span = 6;

		// Header Columns
		TextView tv1 = new TextView(this);
		tv1.setText("Name");
		tv1.setTypeface(Typeface.SERIF, Typeface.BOLD);
		rows[0].addView(tv1);

		TextView tv2 = new TextView(this);
		tv2.setText("Click");
		tv2.setTypeface(Typeface.SERIF, Typeface.BOLD);
		rows[0].addView(tv2);

		TextView tv3 = new TextView(this);
		tv3.setText("Time");
		tv3.setTypeface(Typeface.SERIF, Typeface.BOLD);
		rows[0].addView(tv3);

		TextView tv4 = new TextView(this);
		tv4.setText("Date");
		tv4.setTypeface(Typeface.SERIF, Typeface.BOLD);
		rows[0].addView(tv4);

		table.addView(rows[0]);

		for (int i = 1; i <= recs.size(); i++) {
			Record rec = recs.get(i - 1);
			TextView tv[] = new TextView[4];

			// Name
			tv[0] = new TextView(this);
			tv[0].setText(rec.getName());
			tv[0].setTypeface(Typeface.SERIF, Typeface.BOLD);
			rows[i].addView(tv[0]);

			tv[1] = new TextView(this);
			tv[1].setText(String.valueOf(rec.getNumClicks()));
			tv[1].setTypeface(Typeface.SERIF, Typeface.BOLD);
			rows[i].addView(tv[1]);

			tv[2] = new TextView(this);
			tv[2].setText(String.valueOf(rec.getTimeTaken()));
			tv[2].setTypeface(Typeface.SERIF, Typeface.BOLD);
			rows[i].addView(tv[2]);

			tv[3] = new TextView(this);
			tv[3].setText(String.valueOf(rec.getDate()));
			tv[3].setTypeface(Typeface.SERIF, Typeface.BOLD);
			rows[i].addView(tv[3]);

			table.addView(rows[i]);
		}
		setContentView(table);

	}

}
