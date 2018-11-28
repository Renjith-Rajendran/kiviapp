package kivi.ugran.com.core.callbacks;

public interface ActivityCallback {
    void notifyActionSubmitted(int actionID,String data);
    void openDrawerMenu();
    void closeDrawerMenu();
    void openSettings();

    int ACTION_HANDLE_OTP = 0;
    int ACTION_LAUNCH_HOME_ACTIVITY = 1;
    int ACTION_EXECUTE_SEARCH = 2;
}
