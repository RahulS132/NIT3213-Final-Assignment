package com.example.nit3213app

import com.example.nit3213app.data.api.ApiService
import com.example.nit3213app.data.api.models.DashboardResponse
import com.example.nit3213app.data.api.models.Entity
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

/**
 * Light integration test for AppRepository — verifies it forwards calls to the
 * Retrofit ApiService with the right arguments.
 */
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
        whenever(apiService.login(eq("sydney"), eq(LoginRequest("Rahul", "s8114019"))))
            .thenReturn(LoginResponse("topicXYZ"))

        val result = repository.login("sydney", "Rahul", "s8114019")

        assertEquals("topicXYZ", result.keypass)
        verify(apiService).login("sydney", LoginRequest("Rahul", "s8114019"))
    }

    @Test
    fun `getDashboard forwards keypass to ApiService`() = runTest {
        val response = DashboardResponse(
            entities = listOf(Entity("a", "b", "c")),
            entityTotal = 1
        )
        whenever(apiService.getDashboard("topicXYZ")).thenReturn(response)

        val result = repository.getDashboard("topicXYZ")

        assertEquals(response, result)
        verify(apiService).getDashboard("topicXYZ")
    }
}
