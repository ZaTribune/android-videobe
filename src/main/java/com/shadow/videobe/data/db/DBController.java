package com.shadow.videobe.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.shadow.videobe.data.entities.Tool;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private SQLiteDatabase database;
    private static DBController dbcontroller;

    private DBController(Context context) {
        DBHelper helper=new DBHelper(context);
        database=helper.getWritableDatabase();
    }

    private static DBController getDBController(Context context) {
        if(dbcontroller==null)
        dbcontroller=new DBController(context);
        return dbcontroller;
    }


    public static boolean addTool(Context context, String name,byte[] bytes,String details){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBContract.ToolTable.COL_NAME,name);
        contentValues.put(DBContract.ToolTable.COL_DETAILS,details);
        if(bytes!=null)
        contentValues.put(DBContract.ToolTable.COL_PIC, bytes);
        long id=getDBController(context).database.insert(DBContract.ToolTable.TABLE_NAME,null, contentValues);
        if(id!=0) Log.i("addTool","id: "+id+"\t,name: "+name);
        return id != 0;
    }

    public static List<Tool> getTools(Context context){
        String[]projection=new String[]{DBContract.ToolTable._ID, DBContract.ToolTable.COL_NAME, DBContract.ToolTable.COL_PIC,DBContract.ToolTable.COL_DETAILS};
        Cursor cursor=getDBController(context).database.query(DBContract.ToolTable.TABLE_NAME,projection,null,null,null,null,null);
        List<Tool> tools =new ArrayList<>();
        Tool tool;
        while (cursor.moveToNext()){
            tool =new Tool();
            tool.setId(cursor.getLong(cursor.getColumnIndex(DBContract.ToolTable._ID)));
            tool.setName(cursor.getString(cursor.getColumnIndex(DBContract.ToolTable.COL_NAME)));
            tool.setPic(cursor.getBlob(cursor.getColumnIndex(DBContract.ToolTable.COL_PIC)));
            tool.setDetails(cursor.getString(cursor.getColumnIndex(DBContract.ToolTable.COL_DETAILS)));
            tools.add(tool);
        }
        cursor.close();
        return tools;
    }

    public static long removeToolById(Context context, long idTool){

        return (long) getDBController(context).database.delete(DBContract.ToolTable.TABLE_NAME,
                DBContract.ToolTable._ID+"=?",new String[]{""+idTool}
        );
    }
    public static Tool getToolById(Context context, long idTool){
        Tool tool=new Tool();
        Cursor cursor=getDBController(context).database.query(DBContract.ToolTable.TABLE_NAME,null,
                DBContract.ToolTable._ID+"=?",new String[]{""+idTool},null,null,null);
        while (cursor.moveToNext()) {
            tool.setId(cursor.getLong(cursor.getColumnIndex(DBContract.ToolTable._ID)));
            tool.setName(cursor.getString(cursor.getColumnIndex(DBContract.ToolTable.COL_NAME)));
            tool.setPic(cursor.getBlob(cursor.getColumnIndex(DBContract.ToolTable.COL_PIC)));
            tool.setDetails(cursor.getString(cursor.getColumnIndex(DBContract.ToolTable.COL_DETAILS)));
        }
        cursor.close();
        return tool;
    }



}
