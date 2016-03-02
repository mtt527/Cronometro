package mx.itesm.cronometro;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Handler myHandler = new Handler();

    private ListView listView;

    private TextView millisLabel;
    private TextView secondsLabel;
    private TextView minutesLabel;
    private TextView hoursLabel;

    private Button startButton;
    private Button stopButton;

    private boolean isTimerStarted = false;
    private boolean isTimerStoped = false;

    private  long millis;
    private  long seconds;
    private  long minutes;
    private  long hours;

    private long startTime;
    private long timeCounter;
    private final int REFRESHAT = 10;
    private List<String> timeList;
    ArrayAdapter<String> myAdapter;

    private Runnable startTimerThread = new Runnable() {
        @Override
        public void run() {
            timeCounter = System.currentTimeMillis()-startTime;
            updateTimer(timeCounter);
            myHandler.postDelayed(this,REFRESHAT);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        timeList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.listView);
        myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,timeList );
        listView.setAdapter(myAdapter);

        millisLabel = (TextView) findViewById(R.id.millisLabel);
        secondsLabel = (TextView) findViewById(R.id.secondsLabel);
        minutesLabel = (TextView) findViewById(R.id.minutesLabel);
        hoursLabel = (TextView) findViewById(R.id.hoursLabel);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void starTimer (View view)
    {
        if(isTimerStarted)
        {
            saveTimeToList();
            clearTimer();
            myHandler.removeCallbacks(startTimerThread);
            isTimerStarted=false;
        }
        else
        {
            isTimerStarted=true;
            startTime = System.currentTimeMillis();
            myHandler.removeCallbacks(startTimerThread);
            myHandler.postDelayed(startTimerThread,0);
        }
        toggleStartButton();
    }

    public void stopTimer (View view)
    {
        if(isTimerStarted)
        {
            if(isTimerStoped)
            {
                startTime = System.currentTimeMillis()- timeCounter;
                myHandler.postDelayed(startTimerThread,0);
                isTimerStoped=false;
            }
            else
            {
                myHandler.removeCallbacks(startTimerThread);
                isTimerStoped=true;
            }
            toogleStopButton();
        }
    }

    private void toggleStartButton() {
        if(isTimerStarted) {
            startButton.setText("Reset");
        }else {
            startButton.setText("Start");
        }
    }

    private void toogleStopButton() {
        if(isTimerStoped) {
            stopButton.setText("Resume");
        }else {
            stopButton.setText("Stop");
        }
    }


    private void saveTimeToList()
    {

        String itemResult = itemConstructor();
        timeList.add(itemResult);
        myAdapter.notifyDataSetChanged();
    }

    private String itemConstructor()
    {
        StringBuilder itemBuilder = new StringBuilder();
        String itemResult;

        itemBuilder.append(hoursLabel.getText().toString());
        itemBuilder.append(":");
        itemBuilder.append(minutesLabel.getText().toString());
        itemBuilder.append(":");
        itemBuilder.append(secondsLabel.getText().toString());
        itemBuilder.append(".");
        itemBuilder.append(millisLabel.getText().toString());

        itemResult = new String(itemBuilder);
        return itemResult;
    }

    private void clearTimer()
    {
        updateTimer(0);
    }

    public void updateTimer(long valueTime)
    {
        millis = (long)(valueTime % 1000);
        seconds = (long)(valueTime/1000);
        minutes = (long)((valueTime/1000)/60);
        hours = (long) (((valueTime/1000)/60)/60);

        if(millis == 0) {
            millisLabel.setText("000");
        }else if(millis < 100 && millis > 0){
            millisLabel.setText("0" + Long.toString(millis));
        }else {
            millisLabel.setText(Long.toString(millis));
        }
        seconds = seconds % 60;
        if(seconds == 0) {
            secondsLabel.setText("00");
        }else if(seconds < 10 && seconds > 0) {
            secondsLabel.setText("0" + Long.toString(seconds));
        }else {
            secondsLabel.setText(Long.toString(seconds));
        }

        minutes = minutes % 60;
        if(minutes == 0) {
            minutesLabel.setText("00");
        }else if(minutes < 10 && minutes > 0){
            minutesLabel.setText("0" + Long.toString(minutes));
        }else {
            minutesLabel.setText(Long.toString(minutes));
        }
    }
}
