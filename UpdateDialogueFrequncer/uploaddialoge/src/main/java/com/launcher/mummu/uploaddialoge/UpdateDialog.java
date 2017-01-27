package com.launcher.mummu.uploaddialoge;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Date;

import static com.launcher.mummu.uploaddialoge.DialogManager.create;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.getInstallDate;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.getIsAgreeShowDialog;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.getLaunchTimes;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.getRemindInterval;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.isFirstLaunch;
import static com.launcher.mummu.uploaddialoge.PreferenceHelper.setInstallDate;

/**
 * Created by muhammed on 1/27/2017.
 */

public final class UpdateDialog {
    private static UpdateDialog singleton;

    private final Context context;

    private final DialogOptions options = new DialogOptions();

    private int installDate = 10;

    private int launchTimes = 10;

    private int remindInterval = 1;

    private boolean isDebug = false;

    private UpdateDialog(Context context) {
        this.context = context.getApplicationContext();
    }

    public static UpdateDialog with(Context context) {
        if (singleton == null) {
            synchronized (UpdateDialog.class) {
                if (singleton == null) {
                    singleton = new UpdateDialog(context);
                }
            }
        }
        return singleton;
    }

    public static boolean showRateDialogIfMeetsConditions(Activity activity) {
        boolean isMeetsConditions = singleton.isDebug || singleton.shouldShowRateDialog();
        if (isMeetsConditions) {
            singleton.showRateDialog(activity);
        }
        return isMeetsConditions;
    }

    private static boolean isOverDate(long targetDate, int threshold) {
        return new Date().getTime() - targetDate >= threshold * 24 * 60 * 60 * 1000;
    }

    public UpdateDialog setLaunchTimes(int launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public UpdateDialog setInstallDays(int installDate) {
        this.installDate = installDate;
        return this;
    }

    public UpdateDialog setRemindInterval(int remindInterval) {
        this.remindInterval = remindInterval;
        return this;
    }

    public UpdateDialog setShowLaterButton(boolean isShowNeutralButton) {
        options.setShowNeutralButton(isShowNeutralButton);
        return this;
    }

    public UpdateDialog setShowNeverButton(boolean isShowNeverButton) {
        options.setShowNegativeButton(isShowNeverButton);
        return this;
    }

    public UpdateDialog setShowTitle(boolean isShowTitle) {
        options.setShowTitle(isShowTitle);
        return this;
    }

    public UpdateDialog clearAgreeShowDialog() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        return this;
    }

    public UpdateDialog clearSettingsParam() {
        PreferenceHelper.setAgreeShowDialog(context, true);
        PreferenceHelper.clearSharedPreferences(context);
        return this;
    }

    public UpdateDialog setAgreeShowDialog(boolean clear) {
        PreferenceHelper.setAgreeShowDialog(context, clear);
        return this;
    }

    public UpdateDialog setView(View view) {
        options.setView(view);
        return this;
    }

    public UpdateDialog setOnClickButtonListener(OnClickButtonListener listener) {
        options.setListener(listener);
        return this;
    }

    public UpdateDialog setTitle(int resourceId) {
        options.setTitleResId(resourceId);
        return this;
    }

    public UpdateDialog setTitle(String title) {
        options.setTitleText(title);
        return this;
    }

    public UpdateDialog setMessage(int resourceId) {
        options.setMessageResId(resourceId);
        return this;
    }

    public UpdateDialog setMessage(String message) {
        options.setMessageText(message);
        return this;
    }

    public UpdateDialog setTextRateNow(int resourceId) {
        options.setTextPositiveResId(resourceId);
        return this;
    }

    public UpdateDialog setTextRateNow(String positiveText) {
        options.setPositiveText(positiveText);
        return this;
    }

    public UpdateDialog setTextLater(int resourceId) {
        options.setTextNeutralResId(resourceId);
        return this;
    }

    public UpdateDialog setTextLater(String neutralText) {
        options.setNeutralText(neutralText);
        return this;
    }

    public UpdateDialog setTextNever(int resourceId) {
        options.setTextNegativeResId(resourceId);
        return this;
    }

    public UpdateDialog setTextNever(String negativeText) {
        options.setNegativeText(negativeText);
        return this;
    }

    public UpdateDialog setCancelable(boolean cancelable) {
        options.setCancelable(cancelable);
        return this;
    }

    public UpdateDialog setStoreType(StoreType appstore) {
        options.setStoreType(appstore);
        return this;
    }

    public void monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context);
        }
        PreferenceHelper.setLaunchTimes(context, getLaunchTimes(context) + 1);
    }

    public void showRateDialog(Activity activity) {
        if (!activity.isFinishing()) {
            create(activity, options).show();
        }
    }

    public boolean shouldShowRateDialog() {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate();
    }

    private boolean isOverLaunchTimes() {
        return getLaunchTimes(context) >= launchTimes;
    }

    private boolean isOverInstallDate() {
        return isOverDate(getInstallDate(context), installDate);
    }

    private boolean isOverRemindDate() {
        return isOverDate(getRemindInterval(context), remindInterval);
    }

    public boolean isDebug() {
        return isDebug;
    }

    public UpdateDialog setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }
}
