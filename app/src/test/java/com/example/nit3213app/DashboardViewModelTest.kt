package com.example.nit3213app

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

    private fun entity(vararg pairs: Pair<String, String>): Entity {
        val m = LinkedHashMap<String, String>()
        pairs.forEach { (k, v) -> m[k] = v }
        return Entity(m)
    }

    @Test
    fun `loadDashboard emits Success with entity list`() = runTest {
        val entities = listOf(
            entity("name" to "Cheetah", "speed" to "fast", "description" to "Big cat"),
            entity("name" to "Sloth", "speed" to "slow", "description" to "Slow mover")
        )
        whenever(repository.getDashboard("topicABC")).thenReturn(entities)

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
