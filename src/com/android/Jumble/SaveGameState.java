package com.android.Jumble;

import java.io.Serializable;
import java.util.Date;

import android.content.Context;
import android.widget.Button;

public class SaveGameState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//public Button mButtons[][];
	private boolean s_ButtonState[][];
	private String s_ButtonText[][];
		
	public boolean mVisible[][];
	public int mKeyValues[][];
	public int mNumKeys;		
	
	public int mNumClicks;
	public Date mStartTime;
	
	public void setButtons( Button buttons[][] ) {
		int r = buttons.length;
		int c = buttons[r-1].length;
		
		s_ButtonText = new String[r][c];
		s_ButtonState = new boolean[r][c];
		
		for ( int i = 0; i < r; i++ ) {
			for ( int j = 0; j < c; j++ ) {
				if ( Button.VISIBLE == buttons[i][j].getVisibility() )
					s_ButtonState[i][j] = true;
				else s_ButtonState[i][j] = false;
				
				s_ButtonText[i][j] = (String)buttons[i][j].getText();
			}
		}
	}
	
	public int diff_level ()
	{
		return mNumKeys;
	}
	
	public void loadButtons(Context context, Button buttons[][]) {
		
		int r = mNumKeys;
		int c = mNumKeys;
		
		//Button buttons[][] =  new Button[r][c];
				
		for ( int i = 0; i < r; i++ ) {
			for ( int j = 0; j < c; j++ ) {
				//buttons[i][j] = new Button(context);
				// Load Visiblity
				if (s_ButtonState[i][j] == true)
					buttons[i][j].setVisibility(Button.VISIBLE);
				else buttons[i][j].setVisibility(Button.INVISIBLE);
				// Load Text
				buttons[i][j].setText(s_ButtonText[i][j]);
			}
		}
		// return buttons;
	}
		
}
