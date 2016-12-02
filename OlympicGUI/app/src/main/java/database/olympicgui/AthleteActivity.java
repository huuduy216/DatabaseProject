package database.olympicgui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AthleteActivity extends AppCompatActivity {

    private static EditText name;
    private static Spinner sport;
    private static Spinner gender;
    private static Spinner country;
    private static SQLiteDatabase db;
    private static ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        lv = (ListView) findViewById(R.id.listView_athlete);
        lv.setVisibility(View.INVISIBLE);//hide the listview that show the result

        name = (EditText)findViewById(R.id.editText_athleteName);

        db = MainActivity.dbHelper.getReadableDatabase();

        initSpinners();

        Button accept = (Button)findViewById(R.id.button_athlete_accept);
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
        String[] projection = {"_id","name","country","gender","date_of_birth","weight","height","sport"};
        int[] to = {R.id.athlete_id,R.id.athlete_name,R.id.athlete_country,R.id.athlete_gender,R.id.athlete_dob,R.id.athlete_weight,R.id.athlete_height,R.id.athlete_sport};
        String[] argument = {"%"+ name.getText().toString() +"%", gender.getSelectedItem().toString() ,"%"+ sport.getSelectedItem().toString()+"%","%"+ country.getSelectedItem().toString()+"%"};
        Cursor c = db.query("Athlete",projection," name LIKE ? AND gender LIKE ? AND sport LIKE ? AND country LIKE ?",argument,null,null,null);
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.layout_athlete,
                c,
                projection,
                to,0);

        lv.setAdapter(adapter);
        Toast.makeText(this,"Fun fact: we found " + c.getCount() +" athlete(s)",Toast.LENGTH_LONG).show();
    }

    private void initSpinners()
    {
        sport = (Spinner)findViewById(R.id.spinner_sport);
        gender = (Spinner)findViewById(R.id.spinner_stadium);
        country = (Spinner)findViewById(R.id.spinner_country);

        initSport();
        initGender();
        initCountry();
    }

    private void initSport() {
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

    private void initGender() {
        List<String> list = new ArrayList<String>();
        String[] projection = {"gender"};
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
        gender.setAdapter(adapter);
    }

    private void initCountry() {
        List<String> list = new ArrayList<String>();
        String[] projection = {"country"};
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
        country.setAdapter(adapter);
    }
}
