package wangyang.clock;

import java.util.Calendar;

public class CalTime {
    private Calendar mCalendar;
    private int mHour,mMinuts;

    public String cal(int hour, int minute){
      final long time=System.currentTimeMillis();
      int Calhour=0;
        int Calminute=0;
      mCalendar= Calendar.getInstance();
      mCalendar.setTimeInMillis(time);
      mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
      mMinuts=mCalendar.get(Calendar.MINUTE);
      if (hour<mHour){
          Calhour=24-mHour+hour;
          if (minute>mMinuts){
              Calminute=minute-mMinuts;
          }else {
              Calhour=Calhour-1;
              Calminute=60-mMinuts+minute;
          }
      }else if (hour>mHour){
          Calhour=hour-mHour;
          if (minute>mMinuts){
              Calminute=minute-mMinuts;
          }else {
              Calhour=Calhour-1;
              Calminute=60-mMinuts+minute;
          }
      }else {
          Calhour=0;
          if (minute>mMinuts){
              Calminute=minute-mMinuts;
          }else {
              Calhour=23;
              Calminute=60-mMinuts+minute;
          }
      }
      if (Calminute==60){
          Calminute=0;
          Calhour+=1;
      }

      return Calhour+"小时"+Calminute+"分钟后响铃";
    }
}
