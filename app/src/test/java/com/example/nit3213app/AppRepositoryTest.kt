package com.example.nit3213app

import com.example.nit3213app.data.api.ApiService
import com.example.nit3213app.data.api.models.DashboardResponse
import com.example.nit3213app.data.api.models.LoginRequest
import com.example.nit3213app.data.api.models.LoginResponse
import com.example.nit3213app.data.repository.AppRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

// Unit tests for AppRepository
class AppRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: AppRepository

    @Before
    fun setUp() {
        apiService = mock()
        repository = AppRepository(apiService)
    }

    @Test
    fun `login forwards request to ApiService`() = runTest {
        whenever(apiService.login(eq("sydney"), eq(LoginRequest("s8114019", "Rahul"))))
            .thenReturn(LoginResponse("topicXYZ"))

        val result = repository.login("sydney", "s8114019", "Rahul")

        assertEquals("topicXYZ", result.keypass)
        verify(apiService).login("sydney", LoginRequest("s8114019", "Rahul"))
    }

    @Test
    fun `getDashboard converts Map list to Entity list`() = runTest {
        val rawEntities = listOf(
            mapOf("name" to "Cheetah", "speed" to "fast", "description" to "Big cat"),
            mapOf("name" to "Sloth", "speed" to "slow", "description" to "Slow mover")
        )
        whenever(apiService.getDashboard("topicXYZ"))
            .thenReturn(DashboardResponse(entities = rawEntities, entityTotal = 2))

        val result = repository.getDashboard("topicXYZ")

        assertEquals(2, result.size)
        assertEquals("Cheetah", result[0].fields["name"])
        assertEquals("Big cat", result[0].description)
        assertEquals("Cheetah", result[0].primaryLabel)
        verify(apiService).getDashboard("topicXYZ")
    }
}
