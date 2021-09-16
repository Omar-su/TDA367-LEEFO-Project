package com.leefo.budgetapplication.view;
import com.leefo.budgetapplication.R;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Test;

public class MainActivityTest {

    @Test
    public void isActivityInView(){
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.main))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test_visibility_new_transaction_button() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));

        /* alternative 1
        onView(withId(R.id.button_new_transaction))
            .check(matches(isDisplayed()));
        */

        // alternative 2
        onView(withId(R.id.button_new_transaction))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

}