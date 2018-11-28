package kivi.ugran.com.launcher.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import kivi.ugran.com.core.Constants;
import kivi.ugran.com.launcher.KiviHomeActivity;
import kivi.ugran.com.launcher.R;
import kivi.ugran.com.launcher.TutorialActivity;

public class MenuCommandImpl implements MenuCommand {
    int id;
    public MenuCommandImpl(int id) {
        this.id = id;
    }
    @Override
    public void execute(@NonNull Context context) {
        //All actions are handled on activity
        if(context instanceof DrawerMenuFragment.OnFragmentInteractionListener){
            DrawerMenuFragment.OnFragmentInteractionListener listener = (DrawerMenuFragment.OnFragmentInteractionListener)context;
            listener.onFragmentInteraction(id);
        }

    }

    private void launchTutorialFlow(@NonNull Context context) {

        Intent intent = new Intent(context, TutorialActivity.class);
        intent.putExtra(Constants.KiviTutorial.KEY_LAUNCHED_BY_MENU,true);
        context.startActivity(intent);
        if(context instanceof Activity){
            KiviHomeActivity activity = (KiviHomeActivity) context;
            activity.closeDrawer();
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

    }
}
