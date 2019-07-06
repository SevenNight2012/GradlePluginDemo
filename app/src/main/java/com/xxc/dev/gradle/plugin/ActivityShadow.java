package com.xxc.dev.gradle.plugin;

import android.app.Activity;
import java.lang.ref.WeakReference;

public class ActivityShadow {

    private WeakReference<Activity> mReference;

    public ActivityShadow(Activity activity) {
        mReference = new WeakReference<>(activity);
    }

    public <T extends Activity> void use(UseCallBack<T> callBack) {
        Activity activity = mReference.get();
        if (callBack != null && null != activity) {
            try {
                callBack.onUse((T) activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends Activity> T get() {
        Activity activity = mReference.get();
        if (null != activity) {
            return null;
        } else {
            try {
                return (T) mReference.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public interface UseCallBack<T> {

        void onUse(T activity);
    }

}
