package com.alierdemalkoc.artbooktest.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.alierdemalkoc.artbooktest.R
import com.alierdemalkoc.artbooktest.getOrAwaitValueTest
import com.alierdemalkoc.artbooktest.launchFragmentInHiltContainer
import com.alierdemalkoc.artbooktest.model.Art
import com.alierdemalkoc.artbooktest.repo.FakeArtRepositoryTest
import com.alierdemalkoc.artbooktest.viewModel.ArtViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun navigationFromDetailsToAPI(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(click())
        Mockito.verify(navController).navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageAPI())

    }

    @Test
    fun testOnBackPressed(){
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            Navigation.setViewNavController(requireView(),navController)
        }
        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun testSave(){
        val testViewModel = ArtViewModel(FakeArtRepositoryTest())
        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            viewModel = testViewModel
        }

        Espresso.onView(withId(R.id.nameText)).perform(ViewActions.replaceText("Mona Lisa"))
        Espresso.onView(withId(R.id.artistNameText)).perform(ViewActions.replaceText("Da Vinci"))
        Espresso.onView(withId(R.id.yearText)).perform(ViewActions.replaceText("1537"))
        Espresso.onView(withId(R.id.saveButton)).perform(click())

        assertThat(testViewModel.artList.getOrAwaitValueTest()).contains(
            Art("Mona Lisa","Da Vinci",1537,""))

    }

}