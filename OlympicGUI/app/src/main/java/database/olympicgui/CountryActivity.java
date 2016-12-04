package database.olympicgui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryActivity extends AppCompatActivity {

    private static Spinner sport;
    private static SQLiteDatabase db;
    private static ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        lv = (ListView) findViewById(R.id.listView_country);
        lv.setVisibility(View.INVISIBLE);//hide the listview that show the result


        db = MainActivity.dbHelper.getReadableDatabase();

        initSpinners();

        Button accept = (Button)findViewById(R.id.button_country_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryData();
                findViewById(R.id.option).setVisibility(View.INVISIBLE);//Hide the selecting
                lv.setVisibility(View.VISIBLE);//show the listview that show the result
            }
        });

    }

    private void queryData()
    {
        String[] projection = {"_id","gold","silver","bronze"};
        int[] to = {R.id.textView_country,R.id.textView_gold,R.id.textView_silver,R.id.textView_bronze};//textview_id is hiddent
        String[] argument = { sport.getSelectedItem().toString() };
        CheckBox sortByMedal = (CheckBox)findViewById(R.id.checkBox_sortByMedal);
        String sortBy = null;
        if(sortByMedal.isChecked())
        {
            sortBy = "gold DESC, silver DESC, bronze DESC";
        }
        else
        {
            sortBy = "country";
        }
        final Cursor c = db.rawQuery("SELECT country as _id, COUNT(t1.GoldWinnerId) AS gold , COUNT(t2.SilverWinnerId) AS silver, COUNT(t3.BronzeWinnerId) AS bronze " +
                "FROM Athlete LEFT JOIN Winner_Of t1 ON Athlete.id = t1.GoldWinnerId "+
                "LEFT JOIN Winner_Of t2 ON Athlete.id = t2.SilverWinnerId "+
                "LEFT JOIN Winner_Of t3 ON Athlete.id = t3.BronzeWinnerId "+
                "WHERE country LIKE ? "+
                "GROUP BY country "+
                "ORDER BY "+ sortBy
                ,argument);
        final ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.layout_country,
                c,
                projection,
                to,0);

        lv.setAdapter(adapter);

        Toast.makeText(this,"Fun fact: there are 208 countries participated in the Olympics!",Toast.LENGTH_LONG).show();
    }

    private void initSpinners()
    {
        sport = (Spinner)findViewById(R.id.spinner_sport);

        initSport();
    }

    private void initSport()
    {
        List<String> list = new ArrayList<String>();
        String[] projection = {"sport"};
        list.add("");

        Cursor c = db.query(true,"Athlete",projection,null,null,null,null,null,null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            list.add(c.getString(0));
            c.moveToNext();
        }
        Collections.sort(list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sport.setAdapter(adapter);
    }
}
