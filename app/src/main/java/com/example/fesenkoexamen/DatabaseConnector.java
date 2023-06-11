// DatabaseConnector.java
package com.example.fesenkoexamen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseConnector {
    private static final String DATABASE_NAME = "DrugStore";
    private SQLiteDatabase database;
    private DBHelper databaseOpenHelper;

    public DatabaseConnector(Context context) {
        databaseOpenHelper = new DBHelper(context, DATABASE_NAME, null, 1);
    }

    public void open() {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }

    public void insertBranchRow(String name_branch, String address, String quantity_meds) {
        ContentValues row = new ContentValues();
        row.put(DBHelper.BRANCH_COLUMN_name, name_branch);
        row.put(DBHelper.BRANCH_COLUMN_address, address);
        row.put(DBHelper.BRANCH_COLUMN_quantity_meds, quantity_meds);
        open();
        database.insert(DBHelper.TABLE_BRANCH, null, row);
        close();
    }

    public void insertWorkersRow(String name_worker, String salary) {
        ContentValues row = new ContentValues();
        row.put(DBHelper.WORKERS_COLUMN_name, name_worker);
        row.put(DBHelper.WORKERS_COLUMN_salary, salary);
        open();
        database.insert(DBHelper.TABLE_WORKERS, null, row);
        close();
    }

    public Cursor getBranchAllRows() {
        return database.query(DBHelper.TABLE_BRANCH, new String[]{
                        DBHelper.BRANCH_COLUMN_id, DBHelper.BRANCH_COLUMN_name, DBHelper.BRANCH_COLUMN_address, DBHelper.BRANCH_COLUMN_quantity_meds},
                null, null, null, null,
                DBHelper.BRANCH_COLUMN_name
        );
    }

    public Cursor getWorkersAllRows() {
        return database.query(DBHelper.TABLE_WORKERS, new String[]{
                        DBHelper.WORKERS_COLUMN_id, DBHelper.WORKERS_COLUMN_name, DBHelper.WORKERS_COLUMN_salary},
                null, null, null, null,
                DBHelper.WORKERS_COLUMN_name
        );
    }

    public void deleteBranchRow(long id) {
        open();
        database.delete(DBHelper.TABLE_BRANCH, "_id=" + id, null);
        close();
    }

    public void deleteWorkersRow(long id) {
        open();
        database.delete(DBHelper.TABLE_WORKERS, "_id=" + id, null);
        close();
    }

    public void editBranchRow(long id, String name, String address, int quantityMeds) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.BRANCH_COLUMN_name, name);
        cv.put(DBHelper.BRANCH_COLUMN_address, address);
        cv.put(DBHelper.BRANCH_COLUMN_quantity_meds, quantityMeds);
        String where = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        open();
        database.update(DBHelper.TABLE_BRANCH, cv, where, whereArgs);
        close();
    }
    public void editWorkersRow(long id, String name, String salary) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.WORKERS_COLUMN_name, name);
        cv.put(DBHelper.WORKERS_COLUMN_salary, salary);
        String where = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        open();
        database.update(DBHelper.TABLE_WORKERS, cv, where, whereArgs);
        close();
    }
}