package wangyang.clock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> timArr;
    private TextView record_time;
    private TextView rank;

    public MyAdapter(Context context, ArrayList<String> timeArr){
        this.context=context;
        this.timArr=timeArr;
    }
    @Override
    public int getCount() {
        if (timArr.size()!=0){
            return timArr.size();
        }else {return 0;}

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.stopwatchlistview, null, true);
        record_time=convertView.findViewById(R.id.record_time);
        if (timArr.size()!=0){record_time.setText(timArr.get(position));
            rank=convertView.findViewById(R.id.rank);
            rank.setText((position+1)+"");}

        return convertView;
    }
}
