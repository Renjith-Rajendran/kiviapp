package kivi.ugran.com.launcher.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

public class ActivityChangeMenuCommand implements MenuCommand {

    private final Context context;
    private Intent intent;

    public ActivityChangeMenuCommand(Context ctx, Intent intent) {
        this.context = ctx;
        this.intent = intent;
    }

    @Override
    public void execute(@NonNull Context context) {
        this.context.startActivity(intent);
    }
}
