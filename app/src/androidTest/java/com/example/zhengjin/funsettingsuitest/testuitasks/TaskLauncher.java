package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionCenter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionEnter;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionHome;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveLeft;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveRight;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveUp;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testuiobjects.UiObjectsLauncher;
import com.example.zhengjin.funsettingsuitest.testutils.TestConstants;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import java.util.List;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_HOME_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LAUNCHER_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.LONG_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WEATHER_PKG_NAME;

/**
 * Created by zhengjin on 2016/6/1.
 * <p>
 * Include the UI tasks on Launcher.
 * This task is used by each module, so keep static.
 */
public final class TaskLauncher {

    private static final String TAG = TaskLauncher.class.getSimpleName();

    private static final UiDevice DEVICE = TestConstants.GetUiDeviceInstance();
    private static final UiActionsManager ACTION = UiActionsManager.getInstance();
    private static final UiObjectsLauncher UI_OBJECTS = UiObjectsLauncher.getInstance();

    public static final String[] LAUNCHER_HOME_TABS =
            {"电视", "视频", "体育", "少儿", "应用", "设置", "设置icon"};

    private TaskLauncher() {
    }

    public static void backToLauncher() {
        if (RunnerProfile.isPlatform938) {
            backToLauncherByShell();
        } else {
            backToLauncherByDevice();
        }
    }

    private static void backToLauncherByDevice() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);

        final String pkgName = DEVICE.getLauncherPackageName();
        Log.d(TAG, TestConstants.LOG_KEYWORD + "current package name: " + pkgName);
        if ("android".equals(pkgName)) {
            return;
        }
        Assert.assertTrue("backToLauncherByDevice, failed to back to the launcher home.",
                TestHelper.waitForAppOpenedByUntil(pkgName));
    }

    private static void backToLauncherByPm() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);

        final String pkgName = DEVICE.getLauncherPackageName();
        Log.d(TAG, TestConstants.LOG_KEYWORD + "current package name: " + pkgName);
        if ("android".equals(pkgName)) {
            return;
        }
        Assert.assertEquals("backToLauncherByPm, failed to back to the launcher home.",
                getLauncherPackageName(), pkgName);
    }

    private static void backToLauncherByShell() {
        ACTION.doDeviceActionAndWait(new DeviceActionHome(), WAIT);
        Assert.assertTrue("backToLauncherByShell, failed to back to the launcher home.",
                TestHelper.waitForActivityOpenedByShellCmd(LAUNCHER_PKG_NAME, LAUNCHER_HOME_ACT));
    }

    public static String getLauncherPackageName() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo.activityInfo.packageName;
    }

    public static void navigateToSpecifiedMainTab(String tabText) {
        navigateToVideoTab();

        UiObject2 uiTab;
        if (RunnerProfile.isVersion30 && LAUNCHER_HOME_TABS[6].equals(tabText)) {
            uiTab = DEVICE.findObject(UI_OBJECTS.getSettingsEntrySelector());
        } else {
            uiTab = getSpecifiedTab(tabText);
        }

        if (LAUNCHER_HOME_TABS[0].equals(tabText)) {
            ACTION.doDeviceActionAndWait(new DeviceActionMoveLeft());
            if (uiTab != null && (uiTab.isFocused() || uiTab.isSelected())) {
                return;
            }
        } else {
            for (int i = 0, moveTimes = 9; i < moveTimes; i++) {
                ACTION.doDeviceActionAndWait(new DeviceActionMoveRight());
                if (uiTab != null) {
                    if (uiTab.isFocused() || uiTab.isSelected()) {
                        return;
                    }
                }
            }
        }

        Assert.assertTrue("navigateToSpecifiedMainTab, failed to focus on tab: " + tabText, false);
    }

    public static void navigateToVideoTab() {
        backToLauncherByPm();
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());

        UiObject2 tabVideo = getSpecifiedTab(LAUNCHER_HOME_TABS[1]);
        Assert.assertNotNull("navigateToVideoTab, video tab is NOT found!", tabVideo);
        Assert.assertTrue("navigateToVideoTab, video is NOT focused!",
                (tabVideo.isFocused() || tabVideo.isSelected()));
    }

    @Nullable
    private static UiObject2 getSpecifiedTab(String tabName) {
        List<UiObject2> tabs = DEVICE.findObjects(UI_OBJECTS.getAllLauncherTabsSelector());
        if (tabs.size() == 0) {
            Log.e(TAG, "getSpecifiedTab, no tabs found on launcher!");
            return null;
        }

        for (UiObject2 tab : tabs) {
            if (tabName.equals(tab.getText())) {
                return tab.getParent();
            }
        }
        return null;
    }

    public static void openSpecifiedAppFromAppTab(String appName) {
        openSpecifiedCardFromMainTab(LAUNCHER_HOME_TABS[4], appName);
    }

    static void openSpecifiedCardFromSettingsTab(String cardText) {
        openSpecifiedCardFromMainTab(LAUNCHER_HOME_TABS[5], cardText);
    }

    private static void openSpecifiedCardFromMainTab(String tabText, String cardText) {
        navigateToSpecifiedMainTab(tabText);
        ACTION.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);

        UiObject2 appCard = DEVICE.findObject(By.text(cardText));
        Assert.assertNotNull("OpenSpecifiedCardFromTopTab, tab NOT found: " + cardText, appCard);
        ACTION.doClickActionAndWait(appCard.getParent());  // set focus
        ACTION.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT);
    }

    private static void enterOnTabFromTopQuickAccessBar(BySelector selector) {
        showLauncherTopBar();
        UiObject2 quickAccessBtn = DEVICE.findObject(selector);
        Assert.assertNotNull("enterOnTabFromTopQuickAccessBar, " +
                "the tab from top quick access bar is NOT found.", quickAccessBtn);

        if (!quickAccessBtn.isFocused()) {
            ACTION.doClickActionAndWait(quickAccessBtn);
        }
        ACTION.doDeviceActionAndWait(new DeviceActionEnter(), LONG_WAIT);
    }

    private static void showLauncherTopBar() {
        backToLauncher();
        ACTION.doRepeatDeviceActionAndWait(new DeviceActionMoveUp(), 2);

        UiObject2 bar = DEVICE.findObject(UI_OBJECTS.getLauncherTopBarSelector());
        Assert.assertNotNull(bar);
        Assert.assertTrue("showLauncherTopBar, top bar is NOT enabled.", bar.isEnabled());
    }

    public static void openSettingsFromLauncherQuickAccessBar() {
        enterOnTabFromTopQuickAccessBar(UI_OBJECTS.getQuickAccessTabSettingsSelector());

        if (RunnerProfile.isVersion30) {
            UiObject2 settingsCard = TestHelper.waitForUiObjectExistAndReturn(
                    By.text(TestConstants.TEXT_COMMON_SETTINGS));
            ACTION.doClickActionAndWait(settingsCard);
            ACTION.doDeviceActionAndWait(new DeviceActionCenter(), WAIT);
        }

        Assert.assertTrue("openSettingsFromLauncherQuickAccessBar, open failed!",
                TestHelper.waitForAppOpenedByUntil(SETTINGS_PKG_NAME));
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    public static void openNetworkConfigFromLauncherQuickAccessBar() {
        enterOnTabFromTopQuickAccessBar(UI_OBJECTS.getQuickAccessTabNetworkSelector());
        Assert.assertTrue("openNetworkConfigFromLauncherQuickAccessBar, open failed!",
                TestHelper.waitForAppOpenedByUntil(SETTINGS_PKG_NAME));
        ACTION.doDeviceActionAndWait(new DeviceActionMoveUp());  // request focus
    }

    public static void openWeatherFromLauncherQuickAccessBar() {
        enterOnTabFromTopQuickAccessBar(UI_OBJECTS.getQuickAccessTabWeatherSelector());
        Assert.assertTrue("openWeatherFromLauncherQuickAccessBar, open failed!",
                TestHelper.waitForAppOpenedByUntil(WEATHER_PKG_NAME));
    }

}
