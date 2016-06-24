package com.example.zhengjin.funsettingsuitest.testcasedemos;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.zhengjin.funsettingsuitest.MainActivity;
import com.example.zhengjin.funsettingsuitest.R;
import com.example.zhengjin.funsettingsuitest.testcategory.CategoryDemoTests;
import com.example.zhengjin.funsettingsuitest.testutils.ShellUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.SHORT_WAIT;
import static com.example.zhengjin.funsettingsuitest.testutils.TestConstants.WAIT;

/**
 * Created by zhengjin on 2016/6/1.
 *
 * Include test cases for Main activity.
 * Use both tools Espresso and Uiautomator.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class TestMainActivity extends ActivityInstrumentationTestCase2 {

    private final static String TAG = TestMainActivity.class.getSimpleName();
//    private UiDevice mDevice;

    public TestMainActivity() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() {

        Log.d(TAG, String.format("***** Test %s start.", TAG));
//        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        this.getActivity();  // launcher and open specified APP
        ShellUtils.systemWait(WAIT);
    }

    @After
    public void clearUp() {

        ViewInteraction btnBack = Espresso.onView(ViewMatchers.withId(R.id.buttonBack));
        btnBack.perform(ViewActions.click());
        ShellUtils.systemWait(SHORT_WAIT);
        Log.d(TAG, String.format("***** Test %s finished.", TAG));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test1MainActEditWithEmpty() {

        ViewInteraction edit = Espresso.onView(ViewMatchers.withId(R.id.edit1));
        edit.perform(ViewActions.clearText());

        ViewInteraction btnOk = Espresso.onView(ViewMatchers.withId(R.id.buttonOK));
        btnOk.perform(ViewActions.click());

        String result = "Pls enter your name in edit text.";
        ViewInteraction text = Espresso.onView(ViewMatchers.withId(R.id.text1));
        text.check(ViewAssertions.matches(ViewMatchers.withText(result)));
    }

    @Test
    @Category(CategoryDemoTests.class)
    public void test2MainActEditWithString() {

        String input = "ZhengJin";
        String result = "Hello, ZhengJin";

        ViewInteraction edit = Espresso.onView(ViewMatchers.withId(R.id.edit1));
        edit.perform(ViewActions.clearText());
        edit.perform(ViewActions.typeText(input));

        ViewInteraction btnOk = Espresso.onView(ViewMatchers.withId(R.id.buttonOK));
        btnOk.perform(ViewActions.click());

        ViewInteraction text = Espresso.onView(ViewMatchers.withId(R.id.text1));
        text.check(ViewAssertions.matches(ViewMatchers.withText(result)));
    }
}
