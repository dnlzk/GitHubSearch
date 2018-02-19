package pl.nalazek.githubsearch.search;

import pl.nalazek.githubsearch.R;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Daniel Nalazek
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchActivityTest {

    @Rule
    public ActivityTestRule<SearchActivity> searchActivityActivityTestRule =
            new ActivityTestRule<>(SearchActivity.class, true, false);



    @Before
    public void before() {
        searchActivityActivityTestRule.launchActivity(new Intent());
    }



    @Test
    public void whenStartScreenThenListVisible() throws Exception {
        onView(withId(R.id.list_view)).check(matches(isDisplayed()));
    }



    @Test
    public void whenStartScreenThenBusyInvisible() throws Exception {
        onView(withId(R.id.include_progress))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
}