package com.example.nit3213app

import com.example.nit3213app.data.api.models.DashboardResponse
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.data.repository.AppRepository
import com.example.nit3213app.ui.dashboard.DashboardViewModel
import com.example.nit3213app.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for DashboardViewModel. Confirms the success and error paths emit the
 * right Resource state.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: AppRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        repository = mock()
        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun `loadDashboard emits Success with entity list`() = runTest {
        val entities = listOf(
            Entity("p1", "p2", "desc"),
            Entity("p1b", "p2b", "desc b")
        )
        whenever(repository.getDashboard("topicABC"))
            .thenReturn(DashboardResponse(entities = entities, entityTotal = 2))

        viewModel.loadDashboard("topicABC")
        advanceUntilIdle()

        val state = viewModel.entitiesState.value
        assertTrue(state is Resource.Success)
        assertEquals(entities, (state as Resource.Success).data)
    }

    @Test
    fun `loadDashboard emits Error on failure`() = runTest {
        whenever(repository.getDashboard("badpass"))
            .thenThrow(RuntimeException("Network down"))

        viewModel.loadDashboard("badpass")
        advanceUntilIdle()

        val state = viewModel.entitiesState.value
        assertTrue(state is Resource.Error)
        assertEquals("Network down", (state as Resource.Error).message)
    }
}
