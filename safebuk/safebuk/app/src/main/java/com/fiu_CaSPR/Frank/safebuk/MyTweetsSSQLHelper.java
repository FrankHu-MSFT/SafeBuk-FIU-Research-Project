package com.fiu_CaSPR.Frank.safebuk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public class MyTweetsSSQLHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "MyTweets.db";

    private static final int VERSION = 1;


    public MyTweetsSSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(MyTweetsContract.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(MyTweetsContract.DELETE_STATEMENT);
        onCreate(db);
    }

}
