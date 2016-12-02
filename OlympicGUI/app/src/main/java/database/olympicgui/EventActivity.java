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

public class EventActivity extends AppCompatActivity
{

    private static EditText name;
    private static Spinner olympic;
    private static Spinner stadium;
    private static SQLiteDatabase db;
    private static ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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
        String[] projection = {"_id","olympic","temperature","humidity","stadiumName","discipline","name","number_of_athlete"};
        int[] to = {R.id.athlete_id,R.id.athlete_name,R.id.athlete_country,R.id.athlete_gender,R.id.athlete_dob,R.id.athlete_weight,R.id.athlete_height,R.id.athlete_sport};
        String[] argument = {"%"+ name.getText().toString() +"%","%"+ stadium.getSelectedItem().toString() +"%","%"+ olympic.getSelectedItem().toString()+"%"};
        Cursor c = db.rawQuery("SELECT Event.Sport, Event.Temperature, Event.Humidity, StadiumName, Discipline, Summer_Olympics.Name , COUNT(AthleteId) AS Number_Of_Athlete " +
                "FROM event INNER JOIN eventAthlete ON Event.Id = EventAthlete.EventId " +
                "INNER JOIN Athlete ON Athlete._id = EventAthlete.AthleteId " +
                "INNER JOIN Summer_Olympics ON Event.OlympicId = Summer_Olympics.ID " +
                "WHERE Athlete.name LIKE ? AND StadiumName LIKE ? AND Summer_Olympics.Name LIKE ?",argument);
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.layout_event,
                c,
                projection,
                to,0);

        lv.setAdapter(adapter);
        Toast.makeText(this,"Fun fact: we found " + c.getCount() +" athlete(s)",Toast.LENGTH_LONG).show();
    }

        private void initSpinners()
        {
            olympic = (Spinner)findViewById(R.id.spinner_sport);
            stadium = (Spinner)findViewById(R.id.spinner_stadium);

            initOlympic();
            initStadium();
        }

        private void initOlympic() {
            List<String> list = new ArrayList<String>();
            String[] projection = {"Name"};
            list.add("");

            Cursor c = db.query(true,"Summer_Olympics",projection,null,null,null,null,null,null);
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
            olympic.setAdapter(adapter);
        }

        private void initStadium() {
            List<String> list = new ArrayList<String>();
            String[] projection = {"name"};
            list.add("");

            Cursor c = db.query(true,"Stadium",projection,null,null,null,null,null,null);
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
            stadium.setAdapter(adapter);
        }
    }