package wangyang.clock;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {
    /**
     * @param ctx         上下文环境
     * @param serviceName 判断是否正在运行的服务
     * @return true 运行  false  没有运行
     */
    public static boolean isRunning(Context ctx, String serviceName) {
        //获取activity 管理者对象，可以去获取当前手机正在运行的所有的服务
        ActivityManager mAm = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //获取手机正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningService = mAm.getRunningServices(1000);
        //遍历所有服务集合，比对
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningService) {
            //获取名称
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
