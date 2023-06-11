// WorkersRowsTask.java
package com.example.fesenkoexamen;

import android.database.Cursor;
import android.os.AsyncTask;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class GetWorkersRowsTask extends AsyncTask<Object, Object, Cursor> {
    private WeakReference<WorkersActivity> act;
    private DatabaseConnector databaseConnector;

    GetWorkersRowsTask(WorkersActivity activity) {
        act = new WeakReference<>(activity);
        databaseConnector = new DatabaseConnector(activity);
    }

    @Override
    protected Cursor doInBackground(Object... objects) {
        databaseConnector.open(); // открытие подключения к БД
        return databaseConnector.getWorkersAllRows(); // получение данных
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        ArrayList<String> lst = new ArrayList<>(); // массив строк для вывода на экран в ListView
        int idIndex = cursor.getColumnIndex(DBHelper.WORKERS_COLUMN_id);
        int nameIndex = cursor.getColumnIndex(DBHelper.WORKERS_COLUMN_name);
        int salaryIndex = cursor.getColumnIndex(DBHelper.WORKERS_COLUMN_salary);

        while (cursor.moveToNext()) {
            String id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            int salary = cursor.getInt(salaryIndex);

            lst.add("id=" + id + ", name=" + name + ", salary=" + salary);
        }

        cursor.close(); // закрыть курсор
        databaseConnector.close(); // закрыть подключение
        act.get().update_list(lst); // обновление ListView
    }
}
