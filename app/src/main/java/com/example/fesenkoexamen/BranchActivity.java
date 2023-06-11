package com.example.fesenkoexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

public class BranchActivity extends AppCompatActivity {
    private static final String TAG = "Branch";
    private ArrayList<String> lst;
    private long rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        ListView lv = findViewById(R.id.lv_branch);
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
        new GetBranchRowsTasks(this).execute();
    }

    public void add_btn_clicked(View view) {
        String name = ((EditText) findViewById(R.id.name_branch)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();
        String quantityMeds = ((EditText) findViewById(R.id.quantity_meds)).getText().toString();

        DatabaseConnector databaseConnector = new DatabaseConnector(this);
        databaseConnector.insertBranchRow(name, address, quantityMeds);

        refresh_screen();

        // Вывод сообщения на экран о выполненном действии
        Toast.makeText(this, "List updated", Toast.LENGTH_SHORT).show();
    }

    final int MENU_CONTEXT_DELETE_ID = 123;
    final int MENU_CONTEXT_EDIT_ID = 124;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId() == R.id.lv_branch){
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
            case MENU_CONTEXT_EDIT_ID: { // Редактирование компонента БД
                Log.d(TAG, "edit item pos=" + info.position); // Запись информации в журнал

                String name = str.split(", ")[1];
                String address = str.split(", ")[2];
                String quantityMeds = str.split(", ")[3];

                // Отображение полей текста для редактирования
                ((EditText) findViewById(R.id.name_branch)).setText(name);
                ((EditText) findViewById(R.id.address)).setText(address);
                ((EditText) findViewById(R.id.quantity_meds)).setText(quantityMeds);

                rowId = Long.parseLong(str.split(",")[0].substring(3)); // Получение ID элемента

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

        ListView lv = findViewById(R.id.lv_branch);
        lv.setAdapter(listAdapter);
    }
}