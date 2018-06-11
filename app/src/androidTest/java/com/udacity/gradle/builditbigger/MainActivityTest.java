package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.jokestore.JokeFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private SimpleIdlingResource mIdlingResource;
    private String myJoke;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingInstance();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void clickTellJokeButton_displaysJoke() {
        onView(withId(R.id.tellJoke_button)).perform(click());
        onView(withId(R.id.joke_tv)).check(matches(withText(JokeFactory.generateJoke())));
    }

    @Test
    public void testBackend() {
        final CountDownLatch signal = new CountDownLatch(1);
        EndpointsAsyncTask task = new EndpointsAsyncTask(new EndpointsAsyncTask.JokeFetchedListener() {
            @Override
            public void onJokeFetched(String joke) {
                myJoke = joke;
                signal.countDown();
            }
        });
        task.execute(InstrumentationRegistry.getTargetContext());
        try {
            signal.await();// wait for callback
            Assert.assertEquals(JokeFactory.generateJoke(), myJoke);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
