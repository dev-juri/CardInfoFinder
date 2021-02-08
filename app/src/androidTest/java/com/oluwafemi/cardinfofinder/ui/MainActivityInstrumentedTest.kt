package com.oluwafemi.cardinfofinder.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.oluwafemi.cardinfofinder.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest {

    private var numberToType by Delegates.notNull<Long>()

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun inputValidNumber() {
        numberToType = 5678123456788908
    }

    @Test
    fun textInputField() {
        Espresso.onView(ViewMatchers.withId(R.id.card_number))
            .perform(ViewActions.typeText(numberToType.toString()), ViewActions.closeSoftKeyboard())
    }

}