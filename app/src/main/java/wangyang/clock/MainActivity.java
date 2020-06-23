package wangyang.clock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.time.Clock;

public class MainActivity extends AppCompatActivity {

    private ImageView clock,sandglass,stopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏ActionBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏

        setContentView(R.layout.activity_main);

        clock=findViewById(R.id.clock);
        sandglass=findViewById(R.id.sandglass);
        stopwatch=findViewById(R.id.stopwatch);

        if (ServiceUtil.isRunning(getApplication(),"wangyang.clock.ClockService")){
        }else {
            startService(new Intent(MainActivity.this,ClockService.class).putExtra("flag","ClockActivity"));
            Toast.makeText(getApplicationContext(),"服务开启成功",Toast.LENGTH_SHORT).show();
        }
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ClockActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sandglass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SandglassActivity.class);
                startActivity(intent);
                finish();
            }
        });

        stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, StopwatchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
