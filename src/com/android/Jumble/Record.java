package com.android.Jumble;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;
	private String mName;
	private int mDifficultyLevel;
	private int mNumClicks;
	private long mTimeTaken;
	private Date mDate;

	public Record(String name, int difficultyLevel, int numClicks, long timeTaken) {
		super();
		this.mName = name;
		this.mDifficultyLevel = difficultyLevel;
		this.mNumClicks = numClicks;
		this.mTimeTaken = timeTaken;
		this.mDate = new Date();
	}

	public String getName() {
		return mName;
	}

	public int getDifficultyLevel() {
		return mDifficultyLevel;
	}

	public int getNumClicks() {
		return mNumClicks;
	}

	public long getTimeTaken() {
		return mTimeTaken;
	}

	public Date getDate() {
		return mDate;
	}

	public boolean isBetterThan(Record rec) {
		if ( rec == null ) 
			return true;
		else if (this.mNumClicks < rec.getNumClicks()) {
			return true;
		} else if (this.mNumClicks == rec.getNumClicks()) {
			if (this.mTimeTaken < rec.mTimeTaken)
				return true;
		}

		return false;
	}

	@Override
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(mName);
		buffer.append("\n");
		buffer.append(mDifficultyLevel);
		buffer.append("\n");
		buffer.append(mNumClicks);
		buffer.append("\n");
		buffer.append(mTimeTaken);
		buffer.append("\n");
		buffer.append(mDate);
		buffer.append("\n");

		return buffer.toString();
	}

}