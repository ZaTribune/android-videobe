package com.shadow.videobe.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME="videobe_db";
    private final static int DB_VERSION=1;
    //
    private final String TYPE_TEXT=" text ";
    private final String TYPE_INT=" integer ";
    private final String TYPE_DOUBLE=" double ";
    private final String TYPE_BOOLEAN=" numeric ";
    private final String TYPE_BLOB=" blob ";
    private final String CM=" , ";
    private final String LP=" ( ";
    private final String RP=" ) ";
    private final String NOT_NULL=" not null ";
    private final String CREATE_TABLE_TOOL ="create table "+ DBContract.ToolTable.TABLE_NAME+LP+ DBContract.ToolTable._ID
            +TYPE_INT+NOT_NULL+"PRIMARY KEY AUTOINCREMENT"+CM+ DBContract.ToolTable.COL_NAME +TYPE_TEXT+CM
            + DBContract.ToolTable.COL_PIC+TYPE_BLOB+CM+DBContract.ToolTable.COL_DETAILS+TYPE_TEXT+RP+" ;";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //todo: add a mechanism to update the tool of db
        updateMyDatabase(db,0,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db,oldVersion,newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db,int oldVersion,int newVersion) {
        if (newVersion == 1) {
            db.execSQL(CREATE_TABLE_TOOL);
            insertTool(db, "Watermark",new byte[0],"Insert a watermark."
            );
            insertTool(db, "Crop",new byte[0],"Crop a Video."
            );
        }
    }
    private void insertTool(SQLiteDatabase db,String name,byte[] pic, String details){
        ContentValues cv=new ContentValues();
        cv.put(DBContract.ToolTable.COL_NAME,name);
        cv.put(DBContract.ToolTable.COL_PIC,pic);
        cv.put(DBContract.ToolTable.COL_DETAILS,details);
        db.insert(DBContract.ToolTable.TABLE_NAME,null,cv);

    }
}
