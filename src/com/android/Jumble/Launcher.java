package com.android.Jumble;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Launcher extends Activity {

	private final int NEW = 0;
	private final int RESUME = 1;
	private final int RECORDS = 2;
	private final int QUIT = 3;
	private final int TEST = 4;
	private final int MAX_BUTTONS = 5;

	Button mButtons[];
	
	ChooseDifficulty mDifficulty;
	
	private final String TAG = "Launcher";
	private Bundle mSavedInstanceState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);
		
		Log.v(TAG, "OnCreate");

		setContentView(R.layout.main);

		if ( mSavedInstanceState == null )
			mSavedInstanceState = savedInstanceState;

		initButtons();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		// TODO Auto-generated method stub
		Log.v(TAG, "onKeyDown");
		if (mDifficulty != null) {
			boolean ret = mDifficulty.onKeyDown(keyCode, event);
			if (ret == true) {
				mDifficulty = null;
				onCreate(mSavedInstanceState);
				return true; 
			}
		}
		return super.onKeyDown(keyCode, event);
	}
		
	private void initButtons() {
		// TODO Auto-generated method stub
		if ( mSavedInstanceState == null ) {
			mButtons = new Button[MAX_BUTTONS];
			mButtons[NEW] = (Button) findViewById(R.id.New);
			mButtons[RESUME] = (Button) findViewById(R.id.Resume);
			mButtons[RECORDS] = (Button) findViewById(R.id.Records);
			mButtons[QUIT] = (Button) findViewById(R.id.Quit);
			mButtons[TEST] = (Button) findViewById(R.id.test);
		}
		
		// Set onClickListerners
		mButtons[NEW].setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Button : NEW CLICKED");
				mDifficulty = new ChooseDifficulty(Launcher.this);
			}
		});

		mButtons[RESUME].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Button : RESUME CLICKED");
				if ( true == GameLogic.checkSavedGame(Launcher.this) )
				{
					Intent intent = new Intent(Launcher.this, GameUI.class);
					intent.putExtra("RESUME", true);
					startActivity(intent);
					Log.v(Launcher.this.TAG, "Game Resumed.");					
				}
				else Toast.makeText(Launcher.this, "No Previos Game Saved", Toast.LENGTH_SHORT).show();
			}
		});

		mButtons[RECORDS].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Button : RECORDS CLICKED");
				Intent intent = new Intent(Launcher.this, View_Records.class);
				startActivity(intent);
			}
		});

		mButtons[QUIT].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Button : QUIT CLICKED");
				Launcher.this.finish();
			}
		});
		
		mButtons[TEST].setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Button : TEST CLICKED");
				
				AlertDialog.Builder alert = new AlertDialog.Builder(Launcher.this);

				alert.setTitle("Congratulation !! New Record !!");
				alert.setMessage("Please Enter your name");

				// Set an EditText view to get user input 
				final EditText input = new EditText(Launcher.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				  String value = input.getText().toString();
				  // Do something with value!
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();				
			}
		});		
	}
}
