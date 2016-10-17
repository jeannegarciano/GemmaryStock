package navigation.garciano.com.navigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by admin on 8/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "BranchId.db";
    public static final String TABLE_NAME = "branch_table";
    public static final String ID = "ID";
    public static final String BranchID = "BranchID";
    public static final String BranchName = "BranchName";
    private static final String CREATE_Branch = "CREATE TABLE " + TABLE_NAME + " ("+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            BranchName +" TEXT);";

//    private static final String SQL_CREATE_ENTRIES =
//            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
//                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
//                    FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
//                    FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP + " )";


    public DatabaseHelper(Context context)
    {
       super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_Branch);
        Log.d("Create",""+CREATE_Branch);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS" +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(DatabaseHelper db, String branch)
    {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, 0);
        cv.put(BranchName, branch);
        long result = sql.insert(TABLE_NAME, null, cv);
        if(result == -1)
        {
            return false;
        }
        else{
            return true;
        }
    }


    public ArrayList<String> getBranches(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();


        try
        {

            String selectQuery = "SELECT * FROM "+ TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)

            {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String branch= cursor.getString(cursor.getColumnIndex("BranchName"));
                    list.add(branch);

                }


            }
            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;


    }
}
