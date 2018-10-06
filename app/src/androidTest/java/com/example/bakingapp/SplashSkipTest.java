package com.example.bakingapp;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.bakingapp.activities.HomeActivity;
import com.example.bakingapp.activities.SplashActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class SplashSkipTest {

    @Rule public ActivityTestRule<SplashActivity> activityTestRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Before public void setUp() {
        Intents.init();
    }

    @Test
    public void splashScreenSelect_launchHomeActivity(){
        onView(ViewMatchers.withId(R.id.splash_screen)).perform(click());
        intended(hasComponent(HomeActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}
