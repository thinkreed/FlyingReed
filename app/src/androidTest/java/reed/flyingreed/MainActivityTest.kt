package reed.flyingreed

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import reed.flyingreed.common.activity.MainActivity
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*

/**
 * Created by thinkreed on 2017/7/1.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @Rule @JvmField
    val mActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun listGoesOverTheFold(): Unit {
        onView(withId(R.id.container)).check(matches(isDisplayed()))
        onView(withId(R.id.navigation)).check(matches(isDisplayed()))
    }

}