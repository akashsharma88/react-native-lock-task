
package com.rnlocktask;

import android.app.Activity;
import android.view.WindowManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNLockTaskModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNLockTaskModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNLockTask";
  }

  @ReactMethod
  public  void clearDeviceOwnerApp() {
    try {
      Activity mActivity = reactContext.getCurrentActivity();
      if (mActivity != null) {
        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        myDevicePolicyManager.clearDeviceOwnerApp(mActivity.getPackageName());
      }
    } catch (Exception e) {
    }
  }

  @ReactMethod
  public void startLockTask() {
    try {
      Activity mActivity = reactContext.getCurrentActivity();
      if (mActivity != null) {
        DevicePolicyManager myDevicePolicyManager = (DevicePolicyManager) mActivity.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mDPM = new ComponentName(mActivity, MyAdmin.class);

        if (myDevicePolicyManager.isDeviceOwnerApp(mActivity.getPackageName())) {
          String[] packages = {mActivity.getPackageName()};
          myDevicePolicyManager.setLockTaskPackages(mDPM, packages);
          mActivity.startLockTask();
        } else {
          mActivity.startLockTask();
        }
      }
    } catch (Exception e) {
    }
  }

  @ReactMethod
  public  void stopLockTask() {
    try {
      Activity mActivity = reactContext.getCurrentActivity();
      if (mActivity != null) {
        mActivity.stopLockTask();
      }
    } catch (Exception e) {
    }
  }
  
  @ReactMethod
  public void isInLockTask(Promise promise) {
    try {
      Activity mActivity = reactContext.getCurrentActivity();
      boolean isLockTaskModeRunning = false;

      ActivityManager activityManager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        isLockTaskModeRunning = activityManager.getLockTaskModeState() != ActivityManager.LOCK_TASK_MODE_NONE;
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // Deprecated in API level 23.
        isLockTaskModeRunning = activityManager.isInLockTaskMode();
      }
      promise.resolve(isLockTaskModeRunning);

    } catch (Exception e) {
      promise.reject(E_LOCK_TASK, e);
    }
  }
}
