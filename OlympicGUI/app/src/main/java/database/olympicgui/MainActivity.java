package database.olympicgui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView hello = (TextView) findViewById(R.id.hello) ;
        hello.setText("Hello, we are just setting a few things up. Please wait a moment");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {

            dbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            dbHelper.openDataBase();

        }catch(SQLException sqle){


        }
        hello.setText("Done!");
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        ListView lv = (ListView) findViewById(R.id.listView);
        dbHelper.close();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM Stadium",null);
        while(c.isAfterLast() == false){
            c.moveToNext();
        }

        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.activity_list_item,
                c,
                new String[] {"Name"},
                new int[] {android.R.id.text1},0);

        lv.setAdapter(adapter);
    }
}
