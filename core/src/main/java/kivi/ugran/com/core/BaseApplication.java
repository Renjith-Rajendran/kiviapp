package kivi.ugran.com.core;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;

public class BaseApplication extends Application {

    protected static BaseApplication mInstance;

    public BaseApplication() {
    }

    public BaseApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    public BaseApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    public static void setInstance(BaseApplication instance) {
        mInstance = instance;
    }

    /**
     * Gets an Instance of the Application
     */
    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new LifeCycleCallback());
        setInstance(this);
    }

    /**
     * Provides Application Context
     */
    public static Context provideAppContext() {
        return getInstance().getApplicationContext();
    }

    private static class LifeCycleCallback implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
