package wangyang.clock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClockActivity extends AppCompatActivity {
    private ArrayList<Integer> isSwitchOnArray=new ArrayList<>();
    private ArrayList<String> timeArray=new ArrayList<>(),repeatArray=new ArrayList<>();
    private ListView clockItmesList;
    private MySimpleAdaptey mySimpleAdaptey;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    private List<ClockBean> clockBeanList=new ArrayList<>();
    private TextView addclock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏ActionBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_clock);

        myDatabaseHelper=new MyDatabaseHelper(getApplicationContext(),"Items.db",null,1);
        db=myDatabaseHelper.getWritableDatabase();
        clockItmesList=findViewById(R.id.clockItmesList);
        initDatabase();
        mySimpleAdaptey=new MySimpleAdaptey(getApplicationContext(),clockBeanList);
        clockItmesList.setAdapter(mySimpleAdaptey);

        addclock=findViewById(R.id.addclock);
        addclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClockActivity.this,AddclockActivity.class);
                startActivity(intent);
                finish();
            }
        });
        clockItmesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new CommomDialog(ClockActivity.this, R.style.dialog, "您确定删除吗", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            if (deleteItem(clockBeanList.get(position))==1){
                                Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        } else {

                        }
                    }
                }).setTitleText("提示").show();
                return true;
            }
        });
        Intent jumpType=getIntent();
        if (jumpType.getStringExtra("addType")!=null){
            if (jumpType.getStringExtra("addType").equals("yes")){
                initDatabase();
                mySimpleAdaptey.notifyDataSetChanged();
                stopService(new Intent(ClockActivity.this,ClockService.class));
                startService(new Intent(ClockActivity.this,ClockService.class).putExtra("flag","ClockActivity"));
            }else if (jumpType.getStringExtra("addType").equals("cancel")){

            }
        }
    }

    private int deleteItem(ClockBean clockBean) {
        db.delete("clocks","id=?",new String[]{clockBean.getId()+""});
        initDatabase();
        mySimpleAdaptey.notifyDataSetChanged();
        return 1;
    }

    private void initDatabase() {
        clockBeanList.clear();

        Cursor cursor=db.query("clocks",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            ClockBean clockBean=new ClockBean(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("repeat")),
                    cursor.getInt(cursor.getColumnIndex("isSwitchOn")),
                    cursor.getString(cursor.getColumnIndex("ifVibrate")),
                    cursor.getString(cursor.getColumnIndex("create_time"))
            );
            clockBeanList.add(clockBean);
            Log.i("ClockActivity",clockBeanList.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatabase();
        mySimpleAdaptey.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ClockActivity.this,MainActivity.class));
        finish();
    }
}
