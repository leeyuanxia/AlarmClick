package wangyang.clock;

public class ClockBean {
    private int id;
    private String time;
    private  String repeat;
    private int isSwitchOn;
    private String ifVibrate;
    private String create_time;

    public String getIfVibrate() {
        return ifVibrate;
    }

    public void setIfVibrate(String ifVibrate) {
        this.ifVibrate = ifVibrate;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getIsSwitchOn() {
        return isSwitchOn;
    }

    public void setIsSwitchOn(int isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
    }

    @Override
    public String toString() {
        return "ClockBean{" + "id=" + id + ", time='" + time + '\'' + ", repeat='" + repeat + '\'' + ", isSwitchOn=" + isSwitchOn + ", ifVibrate='" + ifVibrate + '\'' + ", create_time='" + create_time + '\'' + '}';
    }

    public ClockBean(int id, String time, String repeat, int isSwitchOn, String ifVibrate, String create_time) {
        this.id = id;
        this.time = time;
        this.repeat = repeat;
        this.isSwitchOn = isSwitchOn;
        this.ifVibrate = ifVibrate;
        this.create_time = create_time;
    }

    public ClockBean() {
    }
}
