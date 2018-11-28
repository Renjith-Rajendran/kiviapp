package kivi.ugran.com.core;

import androidx.annotation.NonNull;
import android.util.Log;

public class LogUtils {
    private static boolean enableLogs = false;
    public static void d(@NonNull String tag,@NonNull String text){
      if(enableLogs){
          Log.d(tag,text);
      }
    }

    public static void w(@NonNull String tag,@NonNull String text){
        if(enableLogs){
            Log.w(tag,text);
        }
    }

    public static void e(@NonNull String tag,@NonNull String text){
        if(enableLogs){
            Log.e(tag,text);
        }
    }

    public static void i(@NonNull String tag,@NonNull String text){
        if(enableLogs){
            Log.i(tag,text);
        }
    }

    public static void v(@NonNull String tag,@NonNull String text){
        if(enableLogs){
            Log.v(tag,text);
        }
    }
}
