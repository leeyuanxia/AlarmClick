package wangyang.clock;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static SharedPreferences sp;
    /*写入
    * @pram context  上下文环境
    * key  储存节点的名称
    * value 节点的值，Boolean
    * */
    //写
    public static void putBoolean(Context context, String key, boolean value) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }
    //读
    /*
     * @pram context  上下文环境
     * key  储存节点的名称
     * defvalue 没有节点的值
     * */
    public static boolean getBoolean(Context context, String key, boolean defvalue) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defvalue);
    }

    //密码方法
    public static void putString(Context context, String key, String value) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }
    //读
    /*
     * @pram context  上下文环境
     * key  储存节点的名称
     * defvalue 没有节点的值
     * */
    public static String getString(Context context, String key, String defvalue) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defvalue);
    }

    public static void remove(Context ctx, String key) {
        if (sp==null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();

    }



    public static void putInt(Context context, String key, int value) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }
    //读
    /*
     * @pram context  上下文环境
     * key  储存节点的名称
     * defvalue 没有节点的值
     * */
    public static int getInt(Context context, String key, int defvalue) {
        //（存储节点文件的名称，读写方式）
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key,defvalue);
    }

}
