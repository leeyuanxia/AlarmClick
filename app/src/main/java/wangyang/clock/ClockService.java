package wangyang.clock;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;



public class ClockService extends Service {
    private AlarmManager am;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;

    private List<PendingIntent> pendingIntentList=new ArrayList<>();
    private Vibrator vibrator;
    private ClockBean clockBean;
    private MediaPlayer mMediaPlayer;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("flag")!=null) {
            if (intent.getStringExtra("flag").equals("ClockReceiver")) {
                myDatabaseHelper = new MyDatabaseHelper(this, "Items.db", null, 1);
                db = myDatabaseHelper.getWritableDatabase();
                Cursor cursor2 = db.query("clocks", new String[]{"*"}, "id=?", new String[]{intent.getStringExtra("code")}, null, null, null, null);
                while (cursor2.moveToNext()) {
                    clockBean = new ClockBean(cursor2.getInt(cursor2.getColumnIndex("id")), cursor2.getString(cursor2.getColumnIndex("time")), cursor2.getString(cursor2.getColumnIndex("repeat")), cursor2.getInt(cursor2.getColumnIndex("isSwitchOn")), cursor2.getString(cursor2.getColumnIndex("ifVibrate")), cursor2.getString(cursor2.getColumnIndex("create_time")));
                }
                if (clockBean.getIsSwitchOn() == 1) {
                    startAlertActivity(this, clockBean);

                }
            }
            else {
                am=(AlarmManager)this.getSystemService(this.ALARM_SERVICE);
                myDatabaseHelper=new MyDatabaseHelper(ClockService.this,"Items.db",null,1);
                db=myDatabaseHelper.getWritableDatabase();
                Cursor cursor=db.query("clocks",null,null,null,null,null,null,null);
                while (cursor.moveToNext()) {
                    ClockBean clockBean = new ClockBean(cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("time")),
                            cursor.getString(cursor.getColumnIndex("repeat")),
                            cursor.getInt(cursor.getColumnIndex("isSwitchOn")),
                            cursor.getString(cursor.getColumnIndex("ifVibrate")),
                            cursor.getString(cursor.getColumnIndex("create_time"))
                    );
                    pendingIntentList.add(initRemind(Integer.parseInt(clockBean.getTime().split(":")[0]),
                            Integer.parseInt(clockBean.getTime().split(":")[1]),  clockBean.getId()));
                    Toast.makeText(getApplicationContext(), "开启", Toast.LENGTH_SHORT).show();

                }
                for (PendingIntent p : pendingIntentList) {
                    Log.i("pendingIntentList",p.getCreatorPackage());
                }
            }
        }else {
            am=(AlarmManager)this.getSystemService(this.ALARM_SERVICE);
            myDatabaseHelper=new MyDatabaseHelper(ClockService.this,"Items.db",null,1);
            db=myDatabaseHelper.getWritableDatabase();
            Cursor cursor=db.query("clocks",null,null,null,null,null,null,null);
            while (cursor.moveToNext()) {
                ClockBean clockBean = new ClockBean(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("repeat")),
                        cursor.getInt(cursor.getColumnIndex("isSwitchOn")),
                        cursor.getString(cursor.getColumnIndex("ifVibrate")),
                        cursor.getString(cursor.getColumnIndex("create_time"))
                );
                pendingIntentList.add(initRemind(Integer.parseInt(clockBean.getTime().split(":")[0]),
                        Integer.parseInt(clockBean.getTime().split(":")[1]),  clockBean.getId()));
                Toast.makeText(getApplicationContext(), "开启", Toast.LENGTH_SHORT).show();

            }
            for (PendingIntent p : pendingIntentList) {
                Log.i("pendingIntentList",p.getCreatorPackage());
            }
        }
      return START_STICKY;
    }
    private void startAlertActivity(Context context, ClockBean clockBean) {

        Calendar calendar=Calendar.getInstance();
        int day_of_week=calendar.get(Calendar.DAY_OF_WEEK)-1;
        Toast.makeText(context, "闹钟", Toast.LENGTH_LONG).show();

        if (clockBean.getRepeat().equals("仅一次")) {
            showDialog(clockBean);
            db.delete("clocks", "id=?", new String[]{clockBean.getId() + ""});
        }else if (clockBean.getRepeat().equals("周一到周五")){
            if (day_of_week>=1&&day_of_week<=5){
                showDialog(clockBean);
            }
        }else if (clockBean.getRepeat().equals("每天")){
            showDialog(clockBean);
        }

    }
    private void shake() {
        vibrator = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        //数组的a[0]表示静止的时间，a[1]代表的是震动的时间，
        long[] time = {1000, 2000};
        //震动的毫秒值
        //vibrate的第二参数表示从哪里开始循环
        vibrator.vibrate(time, 0);

    }

    private void showDialog(ClockBean clockBean) {

        if (clockBean.getIfVibrate().equals("是")){
            shake();
        }
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.clock);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                mMediaPlayer.start();
                mMediaPlayer.setLooping(true);
            }
        });
        new CommomDialog(getApplicationContext(), R.style.dialog, "设置的闹钟响了！", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm){
                    mMediaPlayer.stop();
                    vibrator.cancel();
                    dialog.dismiss();
                }else {
                    mMediaPlayer.stop();
                    vibrator.cancel();
                    dialog.dismiss();
                }

            }
        }).setNegativeButton("确定").setWindow().setTitleText("提示").show();
    }


    private PendingIntent initRemind(int huorOfDay, int minute, int requestCode) {
        Intent intent2 = new Intent(this,ClockReceiver.class );
        intent2.putExtra("requestcode",requestCode);
        intent2.setAction("wangyang.clock.RING");
        PendingIntent pi = PendingIntent.getBroadcast(this, requestCode , intent2, PendingIntent.FLAG_CANCEL_CURRENT );
        long systemTime = System.currentTimeMillis();//java.lang.System.currentTimeMillis()，它返回从 UTC 1970 年 1 月 1 日午夜开始经过的毫秒数。
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, huorOfDay);//
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);            //选择的定时时间
        long selectTime = calendar.getTimeInMillis();    //计算出设定的时间
        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }// 进行闹铃注册
        if (Build.VERSION.SDK_INT<19) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, am.INTERVAL_DAY, pi);
        }else if (Build.VERSION.SDK_INT>=19&&Build.VERSION.SDK_INT<23){
            am.setExact(AlarmManager.RTC_WAKEUP,
                    selectTime, pi);
        }else if (Build.VERSION.SDK_INT>=23){
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    selectTime, pi);
        }
        return pi;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        for (PendingIntent p : pendingIntentList) {
            am.cancel(p);
        }
        db.close();
    }
}


