package com.example.commercialdirector.myitschool;

import android.content.Context;

import com.example.commercialdirector.myitschool.Activity.LoginActivity;
import com.example.commercialdirector.myitschool.Activity.RegisterActivity;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.example.commercialdirector.myitschool", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    //Проверка успешного входа
    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.email_editText)).perform(typeText("mb@yandex.ru"), closeSoftKeyboard());
        onView(withId(R.id.password_editText)).perform(typeText("020302"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        intended(hasComponent(HomeActivity.class.getName()));
    }

    //Проверка доступности поля email
    @Test
    public void CheckViewIsEnabled() {
        onView(withId(R.id.email)).check(matches(isEnabled()));
    }

    //Проверка некорректных данных пользователя
    @Test
    public void testIncorrectCredentials() {
        onView(withId(R.id.email_editText)).perform(typeText("wrongemail@gmail.com"));
        onView(withId(R.id.password_editText)).perform(typeText("wrongpassword"), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText("Пользователь с данным email не найден")).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //Проверка пустых полей email и password
    @Test
    public void testEmptyFields() {
        onView(withId(R.id.email_editText)).perform(typeText(""));
        onView(withId(R.id.password_editText)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.btnLogin)).perform(click());
        onView(withText("Необходимые данные не введены")).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    //Проверка перехода на экран регистрации
    @Test
    public void testRegisterScreen() {
        onView(withId(R.id.btnLinkToRegisterScreen)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
    }
}
