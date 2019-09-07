package com.java.tamhou.ui.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                              int version)
    {super(context, "my.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE newsTable(myNewsId INTEGER PRIMARY KEY AUTOINCREMENT,content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("Alter Table person ADD phone VARCHAR(12)");
    }
}
