package com.example.zhengjin.funsettingsuitest.testuitasks;

import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;

import com.example.zhengjin.funsettingsuitest.testrunner.RunnerProfile;
import com.example.zhengjin.funsettingsuitest.testuiactions.DeviceActionMoveDown;
import com.example.zhengjin.funsettingsuitest.testuiactions.UiActionsManager;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;
import com.example.zhengjin.funsettingsuitest.testutils.TestHelper;

import junit.framework.Assert;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_SWITCHER;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.CLASS_TEXT_VIEW;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_IMAGE_AND_SOUND_ACT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SETTINGS_PKG_NAME;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/12/14.
 * <p>
 * Include the UI selectors and tasks for settings module.
 */

public final class TaskImageAndSound {

    private static TaskImageAndSound instance = null;
    private UiDevice device;
    private UiActionsManager action;

    public final String IMAGE_AND_SOUND_TEXT = "图像与声音";

    private TaskImageAndSound() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        action = UiActionsManager.getInstance();
    }

    public static synchronized TaskImageAndSound getInstance() {
        if (instance == null) {
            instance = new TaskImageAndSound();
        }
        return instance;
    }

    public void destroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public BySelector getTitleOfImageAndSoundSettingsSelector() {
        return By.res("tv.fun.settings:id/setting_title");
    }

    public BySelector getImageParamsSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_pic_params");
    }

    public BySelector getPressKeySoundSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_sound_effect_enable");
    }

    public BySelector getAudioAroundSettingItemSelector() {
        return By.res("tv.fun.settings:id/setting_item_around_audio");
    }

    public BySelector getImageAndSoundSettingItemTitleSelector() {
        return By.res("tv.fun.settings:id/item_title");
    }

    public BySelector getImageAndSoundSettingItemValueSelector() {
        return By.res("tv.fun.settings:id/item_value");
    }

    public BySelector getColorTmpSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_temp");
    }

    public BySelector getTitleOfColorTmpSettingSelector() {
        return By.res("tv.fun.settings:id/custom_scene_temp_title");
    }

    public BySelector getBackLightSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_backlight");
    }

    public BySelector getBrightnessSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_brightness");
    }

    public BySelector getContrastSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_contrast");
    }

    public BySelector getSaturationSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/custom_scene_saturation");
    }

    public BySelector getTitleOfImageSettingsOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_title");
    }

    public BySelector getValueOfImageSettingsOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_value");
    }

    private BySelector getValueSeekBarOfSettingItemOnImageParamsSelector() {
        return By.res("tv.fun.settings:id/scene_seekbar");
    }

    public BySelector getResetToDefaultSettingItemOfImageParamsSelector() {
        return By.res("tv.fun.settings:id/set_to_default");
    }

    public BySelector getTitleOfResetToDefaultSettingItemSelector() {
        return By.res("tv.fun.settings:id/set_to_default_title");
    }

    public UiObject2 getSwitcherValueOfColorTmpSetting(UiObject2 container) {
        UiObject2 switcher = container.findObject(By.clazz(CLASS_TEXT_SWITCHER));
        return switcher.findObject(By.clazz(CLASS_TEXT_VIEW));
    }

    public void openImageAndSoundSettingsPage() {
        if (RunnerProfile.isPlatform938) {
            this.openImageAndSoundSettingsPageByShellCmd();
        } else {  // platform 638
            TaskLauncher.openSpecifiedCardFromSettingsTab(IMAGE_AND_SOUND_TEXT);
        }

        Assert.assertTrue("openImageAndSoundSettingsPage, open failed!",
                TestHelper.waitForActivityOpenedByShellCmd(
                        SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT));
    }

    private void openImageAndSoundSettingsPageByShellCmd() {
        ShellUtils.startSpecifiedActivity(SETTINGS_PKG_NAME, SETTINGS_IMAGE_AND_SOUND_ACT);
        ShellUtils.systemWaitByMillis(WAIT);
    }

    public void focusOnSpecifiedImageAndSoundSettingsItem(String itemText) {
        for (int i = 0, maxMoves = 5; i < maxMoves; i++) {
            UiObject2 item = device.findObject(By.text(itemText));
            if (item.getParent().isFocused()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedImageAndSoundSettingsItem, error open setting item "
                + itemText, false);
    }

    public void focusOnSpecifiedImageParamsSettingsItem(String itemText) {
        UiObject2 item = device.findObject(By.text(itemText));
        for (int i = 0, maxMoves = 5; i < maxMoves; i++) {
            UiObject2 seekBar = item.getParent().findObject(
                    this.getValueSeekBarOfSettingItemOnImageParamsSelector());
            if (seekBar != null && seekBar.isEnabled()) {
                return;
            }
            action.doDeviceActionAndWait(new DeviceActionMoveDown());
        }

        Assert.assertTrue("focusOnSpecifiedImageParamsSettingsItem, error open setting item "
                + itemText, false);
    }

    public void focusOnResetToDefaultImageParamsSettingsItem() {
        action.doRepeatDeviceActionAndWait(new DeviceActionMoveDown(), 6);
        UiObject2 resetItem =
                device.findObject(this.getResetToDefaultSettingItemOfImageParamsSelector());
        Assert.assertTrue("focusOnResetToDefaultImageParamsSettingsItem, failed to focus."
                , resetItem.isFocused());
    }

}
