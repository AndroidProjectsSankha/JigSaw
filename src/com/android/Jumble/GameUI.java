package com.android.Jumble;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.TextView;
import android.widget.Toast;

public class GameUI extends Activity {

	private static final String TAG = "GameUI";
		
	private Button mButtons[][];
	private TextView mTV_Counter;
	private TextView mTV_TimeElapsed;
	private long ml_TimeElapsed;

	private GameLogic mGame;
	//private Timer mTimer;
	
	private AlertDialog.Builder mAlert_New_Record;
	private AlertDialog mAlert_Restart_Game;
	private AlertDialog mAlert_SaveGameState;
	private AlertDialog mAlert_QuitGame;
	
	protected boolean bSaveGameState;
	boolean mbResumeGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_ui);
		
		Intent intent = getIntent();
		mbResumeGame = (boolean)intent.getExtras().getBoolean("RESUME");
		
		if ( true == mbResumeGame )
		{
			// Load the Previous Game
			mGame = new GameLogic(this);
			mGame.loadGame();
			initialize(mGame.getLevel());
			// mGame.resumePuzzle();
		}
		else {
			int diff_level = (int)intent.getExtras().getInt("LEVEL");
			initialize(diff_level);
			mGame = new GameLogic(this, mButtons, diff_level);
		}		
	}
	
	private void initialize(int diff_level) {
		
		initViews(diff_level);
		initAlerts();
		
		bSaveGameState = false;

		// mGame = new GameLogic(this, mButtons, diff_level);
		
		for (int i = 0; i < diff_level; i++) {
			for (int j = 0; j < diff_level; j++) {

				// Set onClickListeners to Buttons
				mButtons[i][j].setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mGame.onClick(((Button) v).getId());

						if (mGame.CheckResult()) {
							Toast.makeText(GameUI.this, "Congrats !!", Toast.LENGTH_SHORT).show();
							mAlert_New_Record.show();							
						}						
					}
				});
			}
		}				
	}
	
/*	public GameUI(Context context, int diff_level) {
		mContext = context;
		mActivity = (Activity)context;
		
		bQuitGame = false;
		
		mActivity.setContentView(R.layout.game_ui);
		
		initViews(diff_level);
		initAlerts();

		mGame = new GameLogic(mContext, mButtons, diff_level);
		
		for (int i = 0; i < diff_level; i++) {
			for (int j = 0; j < diff_level; j++) {

				// Set onClickListeners to Buttons
				mButtons[i][j].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mGame.onClick(((Button) v).getId());

						if (mGame.CheckResult()) {
							Toast.makeText(GameUI.this.mActivity, "Congrats !!",Toast.LENGTH_SHORT).show();
							mAlert_New_Record.show();							
						}
						
						//mTV_Counter.setText( "Number of Clicks : " + mGame.getClick() );

						// CharSequence text = ((Button) v).getText();
						// Toast.makeText(mContext, text,
						// Toast.LENGTH_SHORT).show();
						// GameLogic.this.onClick(Integer.parseInt(text.toString().trim()));
					}
				});
			}
		}		
	}
	
	*/
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		//mTimer = new Timer();
		//mTimer.schedule(new Task(mTV_TimeElapsed), 0, 1000);	
		
		if ( false == mbResumeGame ) {
			mGame.startPuzzle();
			mTV_Counter.setText( "Number of Clicks : " + mGame.getClick() );
		}
		else mGame.resumePuzzle(mButtons);
		
		//mTV_TimeElapsed.setText("Time Elapsed : " );
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onKeyDown");
		
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				//Toast.makeText(GameUI.this.mActivity, "Back Key Pressed", Toast.LENGTH_SHORT).show();
				//mTimer.cancel();
				mAlert_QuitGame.show(); 				
				break;
			default:
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy");
		if ( bSaveGameState ) {
			mGame.saveGame();
		}

		//mTimer.cancel();
		mGame = null;

		// Start the main class
		Intent intent = new Intent(GameUI.this, Launcher.class);
		startActivity(intent);
	}
	
	public void pause() {
		// TODO Auto-generated method stub
		mGame.saveGame();
	}
		
	private void initAlerts() {
		// TODO Auto-generated method stub
		mAlert_New_Record = new AlertDialog.Builder(this);

		mAlert_New_Record.setTitle("Congratulation !! New Record !!");
		mAlert_New_Record.setMessage("Please Enter your name");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		mAlert_New_Record.setView(input);

		mAlert_New_Record.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int whichButton) {
						
						String name = input.getText().toString();
						// Do something with value!
						DataManager dm = new DataManager(GameUI.this);
						Record new_record = new Record( name, mGame.getLevel(), mGame.getClick(), ml_TimeElapsed);
						if (dm.isNewRecord(new_record))												
							dm.writeData(new_record);
						
						mAlert_Restart_Game.show();
					}
				});

		mAlert_New_Record.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {						
						// Canceled.
						mAlert_Restart_Game.show();
					}
				});

		// Ask user for a Game Restart
		
		mAlert_Restart_Game = new AlertDialog.Builder(this).create();
		mAlert_Restart_Game.setTitle("Restart Game");
		mAlert_Restart_Game.setMessage("Do you want to play again with same difficulty level ?");
		mAlert_Restart_Game.setButton("Yes", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				GameUI.this.onStart();
			}								
		});
		mAlert_Restart_Game.setButton2("No", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		
		// Ask user whether he wants to save the game
		mAlert_SaveGameState = new AlertDialog.Builder(this).create();
		mAlert_SaveGameState.setTitle("Save Game");
		mAlert_SaveGameState.setMessage("Do you want to Save the Game ?");
		mAlert_SaveGameState.setButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				GameUI.this.bSaveGameState = true;
				finish();
			}			
		});
		mAlert_SaveGameState.setButton2("No", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				finish();			
			}
		});
		
		mAlert_QuitGame = new AlertDialog.Builder(this).create();
		mAlert_QuitGame.setTitle("Quit Game");
		mAlert_QuitGame.setMessage("Do you Really want to quit the game ?");
		mAlert_QuitGame.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mAlert_SaveGameState.show();
			}
		});
		
		mAlert_QuitGame.setButton2("No", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//mTimer.st
			}
		});
	}
	
	private void initViews(int level) {

		int buttonsId[][] = {
				{ R.id.button1, R.id.button2, R.id.button3, R.id.button4,
						R.id.button5, },
				{ R.id.button6, R.id.button7, R.id.button8, R.id.button9,
						R.id.button10, },
				{ R.id.button11, R.id.button12, R.id.button13, R.id.button14,
						R.id.button15, },
				{ R.id.button16, R.id.button17, R.id.button18, R.id.button19,
						R.id.button20, },
				{ R.id.button21, R.id.button22, R.id.button23, R.id.button24,
						R.id.button25 } };

		mButtons = new Button[level][level];

		for (int i = 0; i < level; i++) { 
			for (int j = 0; j < level; j++) {
				mButtons[i][j] = (Button) findViewById(buttonsId[i][j]);
			}
		}

		// Initialize TextViews
		mTV_Counter = (TextView)findViewById(R.id.numClicks);
		mTV_TimeElapsed = (TextView) findViewById(R.id.time_elapsed);
	}
	
	private class Task extends TimerTask {
		TextView mTimeElapsed;
		Date mStartTime ;
		
		public Task ( TextView view ) {
			mTimeElapsed = view;
			mStartTime = new Date();
		}
		public void run() {
			Date currTime = new Date(); 
			long timeElapsed =  currTime.getTime() - mStartTime.getTime();
			//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//Date elapsed = sdf.parse(new Date(timeElapsed));		
			
			mTimeElapsed.setText("Time Elapsed " + timeElapsed/1000);
			//mTimeElapsed.setText("Time Elapsed : 00 ");
			Log.v ( "Task", "Time Elapsed " + timeElapsed/1000);
		}
	}
}
