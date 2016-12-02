package database.olympicgui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        findViewById(R.id.layout_searchChoice).setVisibility(View.INVISIBLE);//hide the buttons

        final TextView hello = (TextView) findViewById(R.id.hello) ;
        hello.setText("Hello, we are just setting a few things up. Please wait a moment");
        dbHelper = new DatabaseHelper(this);
        //After hello prompt
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                hello.setText("Done!");
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                hello.setText("What do you want to look for?");
                findViewById(R.id.layout_searchChoice).setVisibility(View.VISIBLE);
            }
        }, 1500);


    }

    private void initButtons()
    {
        Button athlete = (Button) findViewById(R.id.button_athlete);
        Button sport = (Button)findViewById(R.id.button_sport);

        athlete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AthleteActivity.class);
                startActivity(intent);
            }
        });

        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventActivity.class);
                startActivity(intent);
            }
        });
    }
}
