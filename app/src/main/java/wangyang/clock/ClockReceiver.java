package wangyang.clock;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

public class ClockReceiver extends BroadcastReceiver {

    private int code;

    @Override
    public void onReceive(Context context, Intent intent) {
            code = intent.getIntExtra("requestcode", 0);
            Intent intent1=new Intent(context,ClockService.class);
            intent1.putExtra("flag","ClockReceiver");
            intent1.putExtra("code",code+"");
            context.startService(intent1);

    }



}
