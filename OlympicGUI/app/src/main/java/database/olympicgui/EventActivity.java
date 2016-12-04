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
    private static Spinner sport;
    private static Spinner olympic;
    private static Spinner stadium;
    private static SQLiteDatabase db;
    private static ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        lv = (ListView) findViewById(R.id.listView_event);
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
        String[] projection = {"_id","temp","humidity","StadiumName","Discipline","Summer_Olympics.Name","Number_Of_Athlete","Event.Id"};
        int[] to = {R.id.event_sport,R.id.event_temp,R.id.event_humid,R.id.event_stadium,R.id.event_discipline,R.id.event_olympic,R.id.event_numOfAthlete,R.id.textView_id};//textview_id is hiddent
        String[] argument = {"%"+ name.getText().toString() +"%",  sport.getSelectedItem().toString() ,"%"+ stadium.getSelectedItem().toString() +"%","%"+ olympic.getSelectedItem().toString()+"%"};
        final Cursor c = db.rawQuery("SELECT Event.Sport AS _id, (Event.Temperature || \" °C\") AS temp, (Event.Humidity ||'%') as humidity, StadiumName, Discipline, Summer_Olympics.Name , COUNT(AthleteId) AS Number_Of_Athlete, Event.Id " +
                "FROM event INNER JOIN eventAthlete ON Event.Id = EventAthlete.EventId " +
                "INNER JOIN Athlete ON Athlete.Id = EventAthlete.AthleteId " +
                "INNER JOIN Summer_Olympics ON Event.OlympicId = Summer_Olympics.ID " +
                "WHERE Athlete.name LIKE ? AND _id LIKE ? AND StadiumName LIKE ? AND Summer_Olympics.Name LIKE ?" +
                "GROUP BY Event.Id",argument);
        final ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.layout_event,
                c,
                projection,
                to,0);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Iterate through cursor to the clicked row
                c.moveToFirst();
                for(int i = 0 ;i < position; i ++)
                {
                    c.moveToNext();
                }
                Intent intent = new Intent(EventActivity.this,WinnerOfActivity.class).putExtra("eventId",c.getString(7));
                startActivity(intent);
            }
        });
        Toast.makeText(this,"Fun fact: we found " + c.getCount() +" event(s)",Toast.LENGTH_LONG).show();
    }

        private void initSpinners()
        {
            sport = (Spinner)findViewById(R.id.spinner_sport);
            olympic = (Spinner)findViewById(R.id.spinner_olympic);
            stadium = (Spinner)findViewById(R.id.spinner_stadium);

            initSport();
            initOlympic();
            initStadium();
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