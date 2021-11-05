package com.android.Jumble;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class GameLogic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int NOVICE = 3;
	public static final int MEDIUM = 4;
	public static final int EXPERT = 5;
	
	private final int INVALID_VALUE = 99;
	
	private Context mContext;	
	private Button mButtons[][];
	
	private boolean mVisible[][];
	private int mKeyValues[][];
	private int mNumKeys;		
	
	private int mNumClicks;
	private Date mStartTime;
	
	private SaveGameState mSavedGameState;
	
	
	private final String TAG = "GameLogic";
	
	private enum MAKE_MOVE {
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT
	};
	
	//public GameLogic(Context context, int diff_level) { }
	public GameLogic(Context context )
	{
		mContext = context;		
	}
	
	public GameLogic(Context context, Button keys[][], int diff_level) {
		
		mContext = context;		
		mNumKeys = diff_level;
		mButtons = keys;
			
		mVisible = new boolean[mNumKeys][mNumKeys];
		mKeyValues = new int[mNumKeys][mNumKeys];
		
		for (int i = 0; i < mNumKeys; i++) {
			for (int j = 0; j < mNumKeys; j++) {
				mVisible[i][j] = false;
				mKeyValues[i][j] = INVALID_VALUE;
			}
		}
		
		mStartTime = new Date();
	}
	
	public void startPuzzle() {
		
		mNumClicks = 0;
		
		boolean isUsed[] = new boolean[mNumKeys*mNumKeys];
		for ( int i = 0 ; i < mNumKeys*mNumKeys; i++ ) {
			isUsed[i] = false;
		}
		
		Random r = new Random();
		boolean bDone = false;
		int index_i=0, index_j = 0;
		
		while ( !bDone ) {
			int nextVal = r.nextInt(mNumKeys * mNumKeys - 1 );
			//Log.v(TAG,"nextVal = " + nextVal);
			
			if ( !isUsed[nextVal] ) {
				mKeyValues[index_i][index_j] = nextVal + 1;
				mVisible[index_i][index_j] = true;
				isUsed[nextVal] = true;
				index_j++;
				if ( index_j >= mNumKeys ) {
					index_j = 0;
					index_i++;
				}					
			}
			if (( index_i >= mNumKeys - 1) && (index_j >= mNumKeys - 1))
				bDone = true;
		}
		
		
		for (int i = 0; i < mNumKeys; i++) {
			for (int j = 0; j < mNumKeys; j++) {

				// Set Text to Buttons				
				mButtons[i][j].setText("  " + String.valueOf(mKeyValues[i][j]) + " ");

				// Set Visibility to Buttons
				if (mVisible[i][j])
					mButtons[i][j].setVisibility(Button.VISIBLE);
				else
					mButtons[i][j].setVisibility(Button.INVISIBLE);

			}
		}
	}
	
	public void resumePuzzle (Button buttons[][]) {
		
		mButtons = buttons;		
		mSavedGameState.loadButtons(mContext, mButtons);
		
		/*
		
		for (int i = 0; i < mNumKeys; i++) {
			for (int j = 0; j < mNumKeys; j++) {

				// Set Text to Buttons				
				mButtons[i][j].setText("  " + String.valueOf(mKeyValues[i][j]) + " ");

				// Set Visibility to Buttons
				if (mVisible[i][j])
					mButtons[i][j].setVisibility(Button.VISIBLE);
				else
					mButtons[i][j].setVisibility(Button.INVISIBLE);

			}
		}		
		*/
	}

	//private void onClick(int button)
	public void onClick(int resId)
	{
		// Increment the counter
		mNumClicks++;
		
		// Check whether the button can make a move
		MAKE_MOVE makeMove = MAKE_MOVE.NONE;
		
		for (int row = 0; row < mNumKeys && makeMove == MAKE_MOVE.NONE; row++) {
			for (int col = 0; col < mNumKeys; col++) {
				if (resId == mButtons[row][col].getId()) {
					
					//String text = "row = " + row + ", col = " + col;
					//Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();

					try {
						// Move Up
						if (mVisible[row - 1][col] == false) {
							makeMove = MAKE_MOVE.UP;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

					try {
						// Move Down
						if (mVisible[row + 1][col] == false) {
							makeMove = MAKE_MOVE.DOWN;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

					try {
						// Move Left
						if (mVisible[row][col - 1] == false) {
							makeMove = MAKE_MOVE.LEFT;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
					}

					try {
						// Move Right
						if (mVisible[row][col + 1] == false) {
							makeMove = MAKE_MOVE.RIGHT;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// Toast.makeText(mContext, e.toString(),
						// Toast.LENGTH_SHORT).show();
					}

					switch (makeMove) {
					case UP:
						// Toast.makeText(mContext, "Can move up",
						// Toast.LENGTH_SHORT).show();
						updateValues(row,col, row-1, col);

						break;

					case DOWN:
						// Toast.makeText(mContext, "Can move Down",
						// Toast.LENGTH_SHORT).show();
						updateValues(row,col, row+1,col);

						break;

					case LEFT:
						// Toast.makeText(mContext, "Can move Left",
						// Toast.LENGTH_SHORT).show();
						updateValues(row,col, row, col-1);						
						ByPassResult();						
						break;

					case RIGHT:
						// Toast.makeText(mContext, "Can move Right",
						// Toast.LENGTH_SHORT).show();
						updateValues(row,col, row, col+1);
						break;
					default:
						Toast.makeText(mContext, "Can't move",Toast.LENGTH_SHORT).show();
					}

					break;
				}
			}
		}
/*		
		if ( makeMove != MAKE_MOVE.NONE )
			CheckResult();*/
	}

	private void updateValues(int oldRow, int oldCol, int newRow, int newCol) {
		mVisible[oldRow][oldCol] = false;
		mButtons[oldRow][oldCol].setVisibility(Button.INVISIBLE);
		mKeyValues[oldRow][oldCol] = 0;

		mVisible[newRow][newCol] = true;
		mButtons[newRow][newCol].setVisibility(Button.VISIBLE);
		mButtons[newRow][newCol].setText(mButtons[oldRow][oldCol].getText());
		mKeyValues[newRow][newCol] = Integer.parseInt(mButtons[oldRow][oldCol].getText().toString().trim());
		mButtons[oldRow][oldCol].setText(" " + INVALID_VALUE + " ");		
	}
	
	public boolean CheckResult()
	{
		boolean bPass = true;
		for (int i = 0; i < mNumKeys && bPass; i++) {
			for (int j = 0; j < mNumKeys; j++) {
				if ((mKeyValues[i][j] != i * mNumKeys + j + 1) && (i != mNumKeys - 1 && j != mNumKeys - 2)) {
					// Toast.makeText(mContext, "i = " + i + ", j=" +j + " : " +
					// mKeyValues[i][j], Toast.LENGTH_SHORT).show();
					bPass = false;
					break;
				}
			}
		}
		return bPass;
		/*
		if ( bPass) {
			Toast.makeText(mContext, "Congrats !!", Toast.LENGTH_SHORT).show();
			DataManager dm = new DataManager(mContext);
			dm.writeData(new Record("NAME", mNumKeys , mNumClicks, 0));
		}
		*/
	}
	

	
	// ByPass Result
	private void ByPassResult() {
		int i,j=0;
		for (i = 0; i < mNumKeys ; i++) {
			for (j = 0; j < mNumKeys; j++) {
				mKeyValues[i][j] = i*mNumKeys + j + 1;
				mVisible[i][j] = true;
				mButtons[i][j].setVisibility(Button.VISIBLE);
				mButtons[i][j].setText(" " + (mNumKeys* i + j + 1) + " ");
			}
		}
		
		mKeyValues[mNumKeys-1][mNumKeys-1] = INVALID_VALUE;
		mVisible  [mNumKeys-1][mNumKeys-1] = false;
		mButtons  [mNumKeys-1][mNumKeys-1].setVisibility(Button.INVISIBLE);
		mButtons  [mNumKeys-1][mNumKeys-1].setText(" " + INVALID_VALUE + " ");
					
	}

	public int getClick() {
		// TODO Auto-generated method stub
		return mNumClicks ;
	}

	public int getLevel() {
		// TODO Auto-generated method stub
		return mNumKeys;
	}

	public void saveGame() {
		// TODO Auto-generated method stub

		// Write to disk with FileOutputStream
		FileOutputStream f_out = null;
		ObjectOutputStream obj_out = null;
		try {
			
			f_out = mContext.openFileOutput( "myobject.data", Context.MODE_PRIVATE);
			// Write object with ObjectOutputStream
			obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			SaveGameState game_state = new SaveGameState();
			game_state.setButtons(this.mButtons);
			game_state.mVisible = this.mVisible;
			game_state.mKeyValues = this.mKeyValues;
			game_state.mNumKeys = this.mNumKeys;					
			game_state.mNumClicks = this.mNumClicks;
			game_state.mStartTime = this.mStartTime;			

			obj_out.writeObject(game_state);
			Log.v(TAG, "Game Saved Succesfull!!");
			Toast.makeText(mContext, "Game Saved Successfully!!", Toast.LENGTH_SHORT).show();
			
		}catch(IOException e) {
			Log.e(TAG, "saveGame : " + e.toString());
		}	finally {
				// Close the ObjectOutputStream
				try {
					if (obj_out != null) {
						obj_out.flush();
						obj_out.close();
						f_out.flush();
						f_out.close();
					}									
				} catch (IOException ex) {
					ex.printStackTrace();
					Log.e(TAG, ex.toString());
				}
			}		
	}

	public static boolean checkSavedGame (Context context) {
		
		FileInputStream f_in = null;
		ObjectInputStream obj_in = null;
		try {
			
			f_in = context.openFileInput("myobject.data");

			// Read object with ObjectOutputStream
			obj_in = new ObjectInputStream(f_in);

			// Write object out to disk
			SaveGameState temp = (SaveGameState) obj_in.readObject();

			if (temp == null) return false;

		} catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (obj_in != null) {
				try {
					obj_in.close();
					f_in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}		

	public boolean loadGame() {
		
		FileInputStream f_in = null;
		ObjectInputStream obj_in = null;
		try {
			f_in = mContext.openFileInput("myobject.data");

			// Write object with ObjectOutputStream
			obj_in = new ObjectInputStream (f_in);

			// Write object out to disk
			SaveGameState temp = (SaveGameState)obj_in.readObject();
			
			if ( temp == null ) return false;
			
			mSavedGameState = temp;
			
			this.mNumKeys = temp.mNumKeys;
			
			// this.mButtons = new Button[this.mNumKeys][this.mNumKeys];
			// temp.loadButtons(mContext, mButtons);
			this.mVisible = new boolean[this.mNumKeys][this.mNumKeys];
			this.mVisible = temp.mVisible;
			this.mKeyValues = new int [this.mNumKeys][this.mNumKeys];
			this.mKeyValues = temp.mKeyValues;
			this.mNumClicks = temp.mNumClicks;
			this.mStartTime = temp.mStartTime;
						
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (obj_in != null ) {
				try {
					obj_in.close();
					f_in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
