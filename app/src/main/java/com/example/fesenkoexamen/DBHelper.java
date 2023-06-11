// DBHelper.java
package com.example.fesenkoexamen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_BRANCH = "Branch";
    public static final String BRANCH_COLUMN_id = "_id";
    public static final String BRANCH_COLUMN_name = "name_branch";
    public static final String BRANCH_COLUMN_address = "address";
    public static final String BRANCH_COLUMN_quantity_meds = "quantity_meds";

    public static final String TABLE_WORKERS = "Workers";
    public static final String WORKERS_COLUMN_id = "_id";
    public static final String WORKERS_COLUMN_name = "name_worker";
    public static final String WORKERS_COLUMN_salary = "salary";


    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, databaseName, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BRANCH + "( "
                + BRANCH_COLUMN_id + " integer primary key autoincrement, "
                + BRANCH_COLUMN_name + " TEXT, "
                + BRANCH_COLUMN_address + " TEXT, "
                + BRANCH_COLUMN_quantity_meds + " INTEGER "
                + " )"
        );

        db.execSQL("CREATE TABLE " + TABLE_WORKERS + "( "
                + WORKERS_COLUMN_id + " integer primary key autoincrement, "
                + WORKERS_COLUMN_name + " TEXT, "
                + WORKERS_COLUMN_salary + " INTEGER "
                + " )"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
