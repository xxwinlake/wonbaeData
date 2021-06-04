package com.example.wonbaeteamtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "shelter3.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;



    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }
    public boolean updateColumn(long id,byte[] byteArray, int subject, String name, String address ,String provider,String audio,String video) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGE, byteArray);
        values.put(DataBases.CreateDB.SUBJECT, subject);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.ADDRESS, address);
        values.put(DataBases.CreateDB.PROVIDER, provider);
        values.put(DataBases.CreateDB.RECORD, audio);
        values.put(DataBases.CreateDB.VIDEO, video);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, "_id="+id, null) > 0;
    }
    public long insertColumn( byte[] byteArray,int subject, String name, String address ,String provider,String audio,String video) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.IMAGE, byteArray);
        values.put(DataBases.CreateDB.SUBJECT, subject);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.ADDRESS, address);
        values.put(DataBases.CreateDB.PROVIDER, provider);
        values.put(DataBases.CreateDB.RECORD, audio);
        values.put(DataBases.CreateDB.VIDEO, video);
        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
    }
    public boolean deleteColumn(long id) {
        return mDB.delete(DataBases.CreateDB._TABLENAME, "_id=" + id, null) > 0;
    }

    public Cursor getAllColumns() {
        return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
    }
}
