package wangyang.clock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    public static final String CREATE_CLOCKS = "create table clocks ("

            + "id integer primary key autoincrement, "

            + "time string,"

            + "repeat string, "

            + "isSwitchOn int, "

            +" ifVibrate String,"

            +"create_time string)";


    private Context mContext;


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);

        mContext = context;

    }


    @Override

    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_CLOCKS);

        /*Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
        Log.i(String.valueOf(mContext), "创建成功");*/

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}