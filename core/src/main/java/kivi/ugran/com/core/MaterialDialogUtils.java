package kivi.ugran.com.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A utility to help dimiss dialogs
 **/
public final class MaterialDialogUtils {

    public static Snackbar getSnackBar(View view, String textToShow, int color, int backgroundColor) {
        Snackbar snackbar;
        snackbar = Snackbar.make(view, textToShow, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(backgroundColor);
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(color);
        return snackbar;
    }

    private MaterialDialogUtils() {
        throw new AssertionError();
    }

    /**
     * Hides a dialog that may currently be displayed.
     * Returns true if dialog actually dismissed.
     */
    @TargetApi(17) public static boolean hideDialogWithResult(Dialog dialog) {
        if (null != dialog && dialog.isShowing()) {
            if (isSafeToOperate(dialog)) {
                dialog.dismiss();
                return true;
            }
        }
        return false;
    }

    /**
     * Hides a dialog that is currently displayed.
     */
    @TargetApi(17) public static void hideDialog(Dialog dialog) {
        if (null != dialog && dialog.isShowing()) {
            if (isSafeToOperate(dialog)) {
                dialog.dismiss();
            }
        }
    }

    public static void hideDialog(android.app.Fragment fragment, Dialog dialog) {
        if (null != dialog && dialog.isShowing()) {
            //we may have a fragment attached to an activity but not added to its view hierarchy
            if (fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving()) {
                dialog.dismiss();
            }
        }
    }

    public static void hideDialog(Fragment fragment, Dialog dialog) {
        if (null != dialog && dialog.isShowing()) {
            if (!fragment.isDetached() && !fragment.isRemoving()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * Checks to see if the context to the dialog is valid
     */
    private static boolean isSafeToOperate(Dialog dialog) {
        /** Get the context of the Dialog **/
        Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

        /** Is our context an Activity? **/
        if (context instanceof ContextThemeWrapper) {
            Log.d("Dialog", "isSafeToOperate: Context = ContextThemeWrapper");
            if (isSafeToOperate(context)) {
                return true;
            } else if (null != dialog.getOwnerActivity()) {
                Log.d("Dialog", "isSafeToOperate: Context = ContextThemeWrapper != Activity");
                return isSafeToOperate(dialog.getOwnerActivity());
            } else {
                Log.d("Dialog", "isSafeToOperate: Context = ContextThemeWrapper != Activity  && !ownerActivity");
                return true;
            }
        }
        return false;
    }

    public static boolean isSafeToOperate(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing() && !activity.isDestroyed()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSafeToOperate(Context context) {
        if (context instanceof Activity) {
            return isSafeToOperate((Activity) context);
        } else if (null != context && ((ContextWrapper) context).getBaseContext() instanceof Activity) {
            return isSafeToOperate((Activity) ((ContextWrapper) context).getBaseContext());
        }
        return false;
    }
}