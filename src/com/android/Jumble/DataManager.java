package com.android.Jumble;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DataManager {

	private final String FILENAME_NOVICE = "Records_Novice.dat";
	private final String FILENAME_MEDIUM = "Records_Medium.dat";
	private final String FILENAME_EXPERT = "Records_Expert.dat";

	private final int mNumMaxRecords = 10;
	
	private Vector<Record> mRecords;
	private Record mBestRecord;
	private Record mWorstRecord;
	
	private String mFileName;
	private Context mContext;

	
	private final String TAG = "DataManager";

	public DataManager(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;			
	}
		
	public Vector<Record> LoadData ( int level ) {
		
		SetSourceFile(level);
		LoadRecordsFromFile();
		return mRecords;
	}

	private void SetSourceFile(int level) {
		// TODO Auto-generated method stub
		if (level == GameLogic.NOVICE)
			mFileName = FILENAME_NOVICE;
		else if (level == GameLogic.MEDIUM)
			mFileName = FILENAME_MEDIUM;
		else if (level == GameLogic.EXPERT)
			mFileName = FILENAME_EXPERT;
	}

	public boolean isNewRecord (Record new_record) {
		
		int level = new_record.getDifficultyLevel();

		mFileName = "";
		
		SetSourceFile(level);
		LoadRecordsFromFile();
		
		LoadBestAndWorstRecord();
		
		if ( !( new_record.isBetterThan(mWorstRecord) || mRecords.size() < mNumMaxRecords && mRecords != null )) {
			return false;
		}		
		return true;		
	}
	
	public void writeData(Record new_record) {
		
		int level = new_record.getDifficultyLevel();

		mFileName = "";
		
		SetSourceFile(level);
		LoadRecordsFromFile();
						
		if ( mRecords == null ) {
			// Create New Record File
			Log.v(TAG, "No Records Exists!!, Creating New File....");
			Vector<Record> v = new Vector<Record>();
			v.add(new_record);
			writeRecordToFile(mFileName, v);

		} else {
			int i_rec_count = mRecords.size();
			int i_pos = i_rec_count ;
			for (int i = 0; i < i_rec_count; i++ ) {
				if ( new_record.isBetterThan(mRecords.get(i))) {
					i_pos = i;
					break;
				}
			}

			mRecords.insertElementAt(new_record, i_pos);
			
			if ( mRecords.size() > mNumMaxRecords)
				mRecords.setSize(mNumMaxRecords);
	
			writeRecordToFile(mFileName, mRecords);

		}
	}

	private void writeRecordToFile(String fileName, Vector<Record> v) {
		
		Log.v(TAG, "writeRecordToFile");
		
		FileOutputStream outFile = null;
		ObjectOutputStream outputStream = null;
		try {
			outFile = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream = new ObjectOutputStream(outFile);

			outputStream.writeObject(v);

			Log.v(TAG, "Write Succesfull!!");
			Toast.makeText(mContext, "Records Saved Successfully!!", Toast.LENGTH_SHORT).show();
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			Log.e(TAG, ex.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.e(TAG, ex.toString());
		} finally {
			// Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
					outFile.flush();
					outFile.close();
				}
								
			} catch (IOException ex) {
				ex.printStackTrace();
				Log.e(TAG, ex.toString());
			}
		}		
	}

	//@SuppressWarnings("unchecked")
	private void LoadRecordsFromFile() {
		
		Log.v(TAG, "LoadRecordsFromFile");

		FileInputStream inFile = null;
		ObjectInputStream inputStream = null;
		mRecords = null;

		try {

			inFile = mContext.openFileInput(mFileName);
			inputStream = new ObjectInputStream(inFile);

			mRecords = ((Vector<Record>) inputStream.readObject()); 

		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LoadBestAndWorstRecord() {

		Log.v(TAG, "LoadBestAndWorstRecord");
		
		mBestRecord = null;
		mWorstRecord = null;
		
		if ( mRecords != null) {
			mBestRecord = mRecords.get(0);
			mWorstRecord = mRecords.get(mRecords.size() - 1 );

			for (int i = 0; i < mRecords.size(); i++) {
				if (mRecords.get(i).isBetterThan(mBestRecord))
					mBestRecord = mRecords.get(i);
			}
		}
	}
	
	public void deleteFile(int level) {
		// TODO Auto-generated method stub
		Log.v(TAG, "deleteFile");
		SetSourceFile(level);		
		mContext.deleteFile(mFileName);
		Log.v(TAG, "deleteFile : FileName = " + mFileName);
	}

/*	
	private static int mNumCurrentRecords_Novice = 0;
	private static int mNumCurrentRecords_Medium = 0;
	private static int mNumCurrentRecords_Expert = 0;
	
	public void test(Record new_record) {

		// int level = GameLogic.NOVICE;
		int level = new_record.getDifficultyLevel();

		bIsFileExists = false;
		mFileName = "";

		if (level == GameLogic.NOVICE)
			mFileName = FILENAME_NOVICE;
		else if (level == GameLogic.MEDIUM)
			mFileName = FILENAME_MEDIUM;
		else if (level == GameLogic.EXPERT)
			mFileName = FILENAME_EXPERT;
		
		deleteFile();
		
		for (int i = 0; i < 10; i++) {
			writeData(new Record("SANKHA" + mNumCurrentRecords_Novice,
					GameLogic.NOVICE, 10 + mNumCurrentRecords_Novice, 1000));
			mNumCurrentRecords_Novice += 2;
		}			
	}*/
}
