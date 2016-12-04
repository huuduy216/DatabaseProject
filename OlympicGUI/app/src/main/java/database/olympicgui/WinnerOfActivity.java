package database.olympicgui;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class WinnerOfActivity extends AppCompatActivity {

    private static ListView goldWinner;
    private static ListView silverWinner;
    private static ListView bronzeWinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_of);

        initListviews();
    }

    private void initListviews()
    {
        initGoldWinner();
        initSilverWinner();
        initBronzeWinner();
    }

    private void initBronzeWinner()
    {
        bronzeWinner = (ListView)findViewById(R.id.listView_bronzeWinner);
        bronzeWinner.setAdapter(getWinnerAdapter(3));
    }

    private void initSilverWinner()
    {
        silverWinner = (ListView)findViewById(R.id.listView_silverWinner);
        silverWinner.setAdapter(getWinnerAdapter(2));
    }

    private void initGoldWinner()
    {
        goldWinner = (ListView)findViewById(R.id.listView_goldWinner);
        goldWinner.setAdapter(getWinnerAdapter(1));
    }

    private ListAdapter getWinnerAdapter(int medal)
    {
        String column = null;
        if(medal == 1)
        {
            column = "GoldWinnerId";
        }
        else if(medal == 2)
        {
            column = "SilverWinnerId";
        }
        else if(medal == 3)
        {
            column = "BronzeWinnerId";
        }
        final Cursor c = MainActivity.dbHelper.getReadableDatabase().rawQuery("SELECT EventID AS _id, Athlete.name "+
                "FROM Winner_Of " +
                "INNER JOIN Athlete ON Athlete.Id = Winner_Of." + column+" " +
                "WHERE EventId = " +getIntent().getStringExtra("eventId"),null);
        String[] projection = {"_id","name"};
        int[] to = {R.id.textView_hidden_winner,R.id.textView_winner_name};
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.layout_winner_of,
                c,
                projection,
                to,0);
        return adapter;
    }
}
