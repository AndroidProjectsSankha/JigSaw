package com.android.Jumble;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ChooseDifficulty /*extends Activity*/ {

	private static final String TAG = "ChooseDifficulty";
	
	private Button mClear, mStart;
	private RadioButton moptNovice, moptMedium, moptExpert;
	private RadioGroup mOptionGroup;

	// GameLogic mGame;
	Context mContext;
	Activity mActivity;
	//GameUI mGameUI;

	public ChooseDifficulty(Context context ) {
		
		mContext = context;
		mActivity = (Activity)context;
	
		mActivity.setContentView(R.layout.difficulty);
		
		// Initialize Radio Group
		mOptionGroup = (RadioGroup)mActivity.findViewById(R.id.option_group);
		
		// Initialize Radio Buttons
		moptNovice = (RadioButton) mActivity.findViewById(R.id.novice);
		moptMedium = (RadioButton) mActivity.findViewById(R.id.medium);
		moptExpert = (RadioButton) mActivity.findViewById(R.id.expert);

		// Initialize Buttons
		mClear = (Button) mActivity.findViewById(R.id.clear);
		mStart = (Button) mActivity.findViewById(R.id.start);

		mClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(ChooseDifficulty.this.mActivity, "Clicked Clear", Toast.LENGTH_SHORT).show();
				mOptionGroup.clearCheck();
			}
		});

		mStart.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// TODO Auto-generated method stub
				//int diff_level = 0;
				Intent intent = new Intent(ChooseDifficulty.this.mActivity, GameUI.class);
				if (moptNovice.isChecked())
					//diff_level = GameLogic.NOVICE;
					intent.putExtra("LEVEL", GameLogic.NOVICE);
				else if (moptMedium.isChecked())
					//diff_level = GameLogic.MEDIUM;
					intent.putExtra("LEVEL", GameLogic.MEDIUM);
				else if (moptExpert.isChecked())
					//diff_level = GameLogic.EXPERT;
					intent.putExtra("LEVEL", GameLogic.EXPERT);
				else {
					Toast.makeText(ChooseDifficulty.this.mActivity, "Please Select a Difficulty Level !!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//mGameUI = new GameUI(mContext, diff_level);
				//mGameUI.start();
				ChooseDifficulty.this.mActivity.startActivity(intent);
				ChooseDifficulty.this.mActivity.finish();
				//ChooseDifficulty.this.mActivity.setContentView(R.layout.main);
				
		    	//ChooseDifficulty.this.finish();

				/*
				 * if (moptNovice.isChecked() || moptMedium.isChecked() ||
				 * moptExpert.isChecked()) {
				 * 
				 * if (moptNovice.isChecked()) initButtons(GameLogic.NOVICE);
				 * else if (moptMedium.isChecked())
				 * initButtons(GameLogic.MEDIUM); else if
				 * (moptExpert.isChecked()) initButtons(GameLogic.EXPERT);
				 * 
				 * 
				 * if (mButtons != null) {
				 * 
				 * new GameLogic(JumbleActivity.this, mButtons).startPuzzle();
				 * //mGame.startPuzzle();
				 * 
				 * } else Toast.makeText(JumbleActivity.this,
				 * "Please Select a Difficulty Level !!",
				 * Toast.LENGTH_SHORT).show(); }
				 */
			}
		});		
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		// TODO Auto-generated method stub
		Log.v(TAG, "onKeyDown");
		
		// Start the main class
		//Intent intent = new Intent(ChooseDifficulty.this.mActivity, Launcher.class);
		//mActivity.startActivity(intent);
		//mActivity.finish();

		return true;

	}
}