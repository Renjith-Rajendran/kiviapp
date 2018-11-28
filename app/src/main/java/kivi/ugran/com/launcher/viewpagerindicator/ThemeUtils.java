package kivi.ugran.com.launcher.viewpagerindicator;

import android.content.Context;
import android.util.TypedValue;

import kivi.ugran.com.launcher.R;

public class ThemeUtils {
    static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    //public static Snackbar getSnackBar(View view, String textToShow, int color, int backgroundColor) {
    //    Snackbar snackbar;
    //    snackbar = Snackbar.make(view, textToShow, Snackbar.LENGTH_LONG);
    //    View snackBarView = snackbar.getView();
    //    snackBarView.setBackgroundColor(backgroundColor);
    //    TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
    //    textView.setTextColor(color);
    //    return snackbar;
    //}
}
