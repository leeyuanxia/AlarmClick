package wangyang.clock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class SandglassActivity extends AppCompatActivity {

    private RelativeLayout sandglassChooser;
    private PickerView sg_hour_pv;
    private PickerView sg_minute_pv;
    private PickerView sg_second_pv;
    private RelativeLayout sandglassView;
    private TextView sandglass_tv;
    private Button sandglass_start;
    private TextView sandglass_cancel;
    private TextView sandglass_stop;
    private String hour,minute,second;
    private SimpleDateFormat mFormatter;
    private long countDownInterval=0;
    private CountDownUtil mCountDownUtil=new CountDownUtil();
    private TextView sandglass_reset;
    private boolean pauseOn;
    private MediaPlayer mMediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏
        setContentView(R.layout.activity_sandglass);

        pauseOn=true;
        sandglassChooser=findViewById(R.id.sandglassChooser);
        sg_hour_pv=findViewById(R.id.sg_hour_pv);
        sg_minute_pv=findViewById(R.id.sg_minute_pv);
        sg_second_pv=findViewById(R.id.sg_second_pv);
        sandglass_reset=findViewById(R.id.sandglass_reset);
        sandglassView=findViewById(R.id.sandglassView);
        sandglass_tv=findViewById(R.id.sandglass_tv);
        sandglass_start=findViewById(R.id.sandglass_start);
        sandglass_cancel=findViewById(R.id.sandglass_cancel);
        sandglass_stop=findViewById(R.id.sandglass_stop);

        mFormatter = new SimpleDateFormat("HH:mm:ss");
        // 格式化时间 从0时开始而不是从8时开始
        mFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));

        List<String> hourList = new ArrayList<String>();
        List<String> minuteList = new ArrayList<String>();
        List<String> secondList = new ArrayList<String>();

        for (int i=0;i<24;i++){
            if (i<10) {
                hourList.add("0" + i);
            }else {
                hourList.add(i+"");
            }
        }
        for (int i=0;i<60;i++){
            if (i<10) {
                minuteList.add("0" + i);
                secondList.add("0" + i);
            }else {
                minuteList.add(i+"");
                secondList.add(i+"");
            }
        }
        sg_hour_pv.setData(hourList);
        sg_minute_pv.setData(minuteList);
        sg_second_pv.setData(minuteList);
        sg_hour_pv.setSelected(0);
        sg_minute_pv.setSelected(0);
        sg_second_pv.setSelected(0);
        hour=00+"";
        minute=00+"";
        second=00+"";
        sg_hour_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                hour=text;
            }
        });
        sg_minute_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                minute=text;
            }
        });
        sg_second_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                second=text;
            }
        });
        sandglass_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar c=Calendar.getInstance();
                    c.setTime(mFormatter.parse(hour+":"+minute+":"+second));
                    long time=c.getTimeInMillis();
                    countDownInterval = 1000;
                    exchangeView();
                    countDown(time , countDownInterval);
                    mCountDownUtil.start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        sandglass_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownUtil.cancel();
                sandglass_tv.setText("00:00:00");
            }
        });
        sandglass_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownUtil.cancel();
                if (pauseOn){
                    pauseOn = false;
                    sandglass_stop.setText("继续");

                }else {
                    pauseOn = true;
                    sandglass_stop.setText("暂停");
                    String now = (String) sandglass_tv.getText();
                    long time = 0;
                    try {
                        time = mFormatter.parse(now).getTime();
                        // 如果倒计时时间大于0
                        if (time > 0) {
                            // 继续倒计时
                            countDown(time, countDownInterval);
                            mCountDownUtil.start();

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        sandglass_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownUtil.cancel();
                sg_hour_pv.setSelected(0);
                sg_minute_pv.setSelected(0);
                sg_second_pv.setSelected(0);
                exchangeView2();
            }
        });

    }
    private void showDialog() {


        vibrator = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        //数组的a[0]表示静止的时间，a[1]代表的是震动的时间，
        long[] time = {0, 2000};
        //震动的毫秒值
        //vibrate的第二参数表示从哪里开始循环,-1表示不循环，
        vibrator.vibrate(time, -1);

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.clock);
        mMediaPlayer.start();

        new CommomDialog(getApplicationContext(), R.style.dialog, "倒计时结束！", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm){
                    mMediaPlayer.stop();
                    dialog.dismiss();
                }else {
                   ;mMediaPlayer.stop();
                    dialog.dismiss();
                }

            }
        }).setNegativeButton("确定").setWindow().setTitleText("提示").show();
    }
    private void exchangeView() {
        sandglassChooser.setVisibility(View.GONE);
        sandglassView.setVisibility(View.VISIBLE);
        sandglass_start.setVisibility(View.GONE);
        sandglass_cancel.setVisibility(View.VISIBLE);
        sandglass_stop.setVisibility(View.VISIBLE);
        sandglass_reset.setVisibility(View.VISIBLE);
    }
    private void exchangeView2() {
        sandglassChooser.setVisibility(View.VISIBLE);
        sandglassView.setVisibility(View.GONE);
        sandglass_start.setVisibility(View.VISIBLE);
        sandglass_cancel.setVisibility(View.GONE);
        sandglass_stop.setVisibility(View.GONE);
        sandglass_reset.setVisibility(View.GONE);
    }
    /**
     * 开始倒计时,传入总时间和每一秒要倒计时的时间
     */
    private void countDown(final long millisInFuture,long countDownInterval) {
        mCountDownUtil = CountDownUtil.getCountDownTimer()
                // 倒计时总时间
                .setMillisInFuture(millisInFuture)
                // 每隔多久回调一次onTick
                .setCountDownInterval(countDownInterval)
                // 每回调一次onTick执行
                .setTickDelegate(new CountDownUtil.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        String mHms = mFormatter.format(pMillisUntilFinished);
                        sandglass_tv.setText(mHms);
                    }
                })
                // 结束倒计时执行
                .setFinishDelegate(new CountDownUtil.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        sandglass_tv.setText("00:00:00");
                        showDialog();

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SandglassActivity.this,MainActivity.class));
        finish();
    }
}
