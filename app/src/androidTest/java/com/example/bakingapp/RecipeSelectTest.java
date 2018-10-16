package com.example.bakingapp;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.bakingapp.activities.DetailActivity;
import com.example.bakingapp.activities.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipeSelectTest {

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void clickRecipe_opensDetailActivity(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        onData(anything()).inAdapterView(withId(R.id.rv_recipe_list)).atPosition(0).perform(click());
                        intended(hasComponent(DetailActivity.class.getName()));
                    }
                }, 10000);
            }
        }).run();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

}
