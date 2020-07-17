package com.example.renaultrx


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testCommunes() {
        // Créer idlingResource de l'activity
        val idlingResource = CountingIdlingResource("search resource")
        activityRule.activity.idlingResource = idlingResource

        // Enregistrer la resource auprès de Espresso
        IdlingRegistry.getInstance().register(idlingResource)

        // Saisir "Toulouse" dans le champ de recherche
        onView(withId(R.id.editTextTextCommune))
            .perform(typeText("Toulouse"), closeSoftKeyboard())

        // Vérifier qu'un élément avec l'id textView_communeNom
        // et contenant le texte Toulouse est affiché
        onView(allOf(withId(R.id.textView_communeNom), withText("Toulouse")))
            .check(matches(isDisplayed()))

        Thread.sleep(1000)
    }
}