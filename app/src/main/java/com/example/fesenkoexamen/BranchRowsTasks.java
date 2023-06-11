// BranchRowsTask.java
package com.example.fesenkoexamen;

import android.database.Cursor;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class GetBranchRowsTasks extends AsyncTask<Object, Object, Cursor> {
    private WeakReference<BranchActivity> act;
    private DatabaseConnector databaseConnector;

    GetBranchRowsTasks(BranchActivity activity) {
        act = new WeakReference<>(activity);
        databaseConnector = new DatabaseConnector(activity);
    }

    @Override
    protected Cursor doInBackground(Object... objects) {
        databaseConnector.open(); // открытие подключения к БД
        return databaseConnector.getBranchAllRows(); // получение данных
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        ArrayList<String> lst = new ArrayList<>(); // массив строк для вывода на экран в ListView
        int idIndex = cursor.getColumnIndex(DBHelper.BRANCH_COLUMN_id);
        int nameIndex = cursor.getColumnIndex(DBHelper.BRANCH_COLUMN_name);
        int addressIndex = cursor.getColumnIndex(DBHelper.BRANCH_COLUMN_address);
        int quantityMedsIndex = cursor.getColumnIndex(DBHelper.BRANCH_COLUMN_quantity_meds);

        while (cursor.moveToNext()) {
            String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String address = cursor.getString(addressIndex);
            int quantityMeds = cursor.getInt(quantityMedsIndex);

            lst.add("id=" + id + ", name=" + name + ", address=" + address + ", quantity_meds=" + quantityMeds);
        }

        cursor.close(); // закрыть курсор
        databaseConnector.close(); // закрыть подключение
        act.get().update_list(lst); // обновление ListView
    }
}
