package com.example.fesenkoexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class WorkersActivity extends AppCompatActivity {
    private static final String TAG = "Workers";
    private ArrayList<String> lst;
    private long rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        ListView lv = findViewById(R.id.lv_workers);
        registerForContextMenu(lv);
        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh_screen();
    }

    // старт оновлення екрану інформацією з бази даних у фоновому потоці
    void refresh_screen(){
        new GetWorkersRowsTask(this).execute();
    }

    public void add_btn_clicked(View view){

        // одержання посилання на екземпляр компонентів
        String name = ((EditText) findViewById(R.id.name_worker)).getText().toString();
        String salary = ((EditText) findViewById(R.id.salary)).getText().toString();

        // підключення до БД
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        if (rowId > 0) { // редагування
            databaseConnector.editWorkersRow(rowId, name, salary);
            rowId = 0;
        } else { // додавання
            databaseConnector.insertWorkersRow(name, salary);
        }
        refresh_screen();

        // виведення на екран повідомлення про виконану дію
        Toast.makeText(this, "List updated", Toast.LENGTH_SHORT).show();
    }
    final int MENU_CONTEXT_DELETE_ID = 123;
    final int MENU_CONTEXT_EDIT_ID = 124;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.lv_workers){
            ListView lv = (ListView) v;
            // створюємо дві опції контекстного меню з видаленням та редагуванням елементу
            menu.add(Menu.NONE, MENU_CONTEXT_DELETE_ID, Menu.NONE, "Remove item");
            menu.add(Menu.NONE, MENU_CONTEXT_EDIT_ID, Menu.NONE, "Edit item");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String str = lst.get(info.position);

        switch (item.getItemId()) {
            case MENU_CONTEXT_DELETE_ID: { // видалення елемента з БД
                Log.d(TAG, "removing item pos=" + info.position); // запис інфо в журнал

                long rid = Long.parseLong(str.split(",")[0].substring(3)); // одержання id рядка
                DatabaseConnector databaseConnector = new DatabaseConnector(this);
                databaseConnector.deleteWorkersRow(rid); //

                refresh_screen();
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                return true;
            }
            case MENU_CONTEXT_EDIT_ID:{ // редагування компонента БД
                Log.d(TAG, "edit item pos=" + info.position); // запис інфо в журнал

                String txt = str.split(", ")[1];
                ((EditText) findViewById(R.id.name_worker)).setText(txt); // відображення поля тексту

                String num = str.split(", ")[2];
                ((EditText) findViewById(R.id.salary)).setText(num); // відображення поля числа

                rowId = Long.parseLong(str.split(",")[0].substring(3)); // одержання ID елемента

                // оновлюємо текстові поля для редагування
                ((EditText) findViewById(R.id.name_worker)).setText(txt);
                ((EditText) findViewById(R.id.salary)).setText(num);
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void update_list(ArrayList<String> lst) {
        this.lst = lst;
        ListAdapter listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lst);

        ListView lv = findViewById(R.id.lv_workers);
        lv.setAdapter(listAdapter);
    }
}