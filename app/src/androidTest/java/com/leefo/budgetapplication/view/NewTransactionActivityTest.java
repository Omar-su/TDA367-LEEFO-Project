package com.leefo.budgetapplication.view;
import com.leefo.budgetapplication.R;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.core.app.ActivityScenario;
import org.junit.Test;

public class NewTransactionActivityTest {

    @Test
    public void isActivityInView(){
        ActivityScenario activityScenario = ActivityScenario.launch(NewTransactionActivity.class);

        onView(withId(R.id.new_transaction_activity))
                .check(matches(isDisplayed()));
    }
}