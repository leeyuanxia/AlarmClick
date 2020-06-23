package wangyang.clock;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StopwatchActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private ListView stopwatchItems;
    private TextView doRecord;
    private TextView start_stopwatch;
    private TextView pause_stopwatch;
    private ArrayList<String> timArr=new ArrayList<>();
    private MyAdapter myAdapter;
    private long recordingTime = 0;// 记录下来的总时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏ActionBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏

        setContentView(R.layout.activity_stopwatch);

        chronometer=findViewById(R.id.Chronometer);
        stopwatchItems=findViewById(R.id.stopwatchItems);
        doRecord=findViewById(R.id.doRecord);
        start_stopwatch=findViewById(R.id.start_stopwatch);
        pause_stopwatch=findViewById(R.id.pause_stopwatch);


        start_stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                doRecord.setVisibility(View.VISIBLE);
                start_stopwatch.setVisibility(View.GONE);
                pause_stopwatch.setVisibility(View.VISIBLE);
            }
        });
        myAdapter=new MyAdapter(getApplicationContext(),timArr);
        stopwatchItems.setAdapter(myAdapter);
        doRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doRecord.getText().toString().equals("计次")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    timArr.add(sdf.format(new Date(SystemClock.elapsedRealtime() - chronometer.getBase())));
                    myAdapter.notifyDataSetChanged();
                }else if (doRecord.getText().toString().equals("复位")){
                    recordingTime=0;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timArr.clear();

                    doRecord.setText("计次");
                    pause_stopwatch.setText("暂停");
                    doRecord.setVisibility(View.GONE);
                    start_stopwatch.setVisibility(View.VISIBLE);
                    pause_stopwatch.setVisibility(View.GONE);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
        pause_stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pause_stopwatch.getText().toString().equals("暂停")){
                    chronometer.stop();
                    recordingTime = SystemClock.elapsedRealtime()- chronometer.getBase();// 保存这次记录了的时间
                    doRecord.setText("复位");
                    pause_stopwatch.setText("继续");
                }else {
                    chronometer.setBase(SystemClock.elapsedRealtime() - recordingTime);
                    chronometer.start();
                    doRecord.setText("计次");
                    pause_stopwatch.setText("暂停");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(StopwatchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
