package com.alierdemalkoc.artbooktest.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alierdemalkoc.artbooktest.getOrAwaitValueTest
import com.alierdemalkoc.artbooktest.repo.FakeArtRepository
import com.alierdemalkoc.artbooktest.util.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup(){
       viewModel = ArtViewModel(FakeArtRepository())

    }

    @Test
    fun `insert art without year returns error`(){
        viewModel.makeArt("ali","ali","")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)

    }

    @Test
    fun `insert art without name returns error`(){

    }

    @Test
    fun `insert art without artistName returns error`(){

    }
}