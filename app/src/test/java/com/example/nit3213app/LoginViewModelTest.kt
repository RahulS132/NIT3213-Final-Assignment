package com.example.nit3213app

import com.example.nit3213app.data.api.models.LoginResponse
import com.example.nit3213app.data.repository.AppRepository
import com.example.nit3213app.ui.login.LoginViewModel
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
 * Unit tests for LoginViewModel. Verifies validation, loading state, and the success
 * and error paths against a mocked AppRepository.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: AppRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        repository = mock()
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `blank credentials produce Error state`() = runTest {
        viewModel.login("", "")
        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("Username and password cannot be empty", (state as Resource.Error).message)
    }

    @Test
    fun `successful login emits Success with keypass`() = runTest {
        whenever(repository.login("sydney", "Rahul", "s8114019"))
            .thenReturn(LoginResponse(keypass = "topicABC"))

        viewModel.login("Rahul", "s8114019")
        advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Success)
        assertEquals("topicABC", (state as Resource.Success).data)
    }

    @Test
    fun `failed login emits Error with message`() = runTest {
        whenever(repository.login("sydney", "Rahul", "wrong"))
            .thenThrow(RuntimeException("401 unauthorized"))

        viewModel.login("Rahul", "wrong")
        advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("401 unauthorized", (state as Resource.Error).message)
    }

    @Test
    fun `resetState returns to Idle`() {
        viewModel.login("", "")
        viewModel.resetState()
        assertTrue(viewModel.loginState.value is Resource.Idle)
    }
}
