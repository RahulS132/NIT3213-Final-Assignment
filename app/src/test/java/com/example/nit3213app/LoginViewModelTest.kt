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
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

// Unit tests for LoginViewModel
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
        viewModel.login("sydney", "", "")
        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("Username and password cannot be empty", (state as Resource.Error).message)
    }

    @Test
    fun `blank campus produces Error state`() = runTest {
        viewModel.login("", "s8114019", "Rahul")
        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals("Please choose a campus.", (state as Resource.Error).message)
    }

    @Test
    fun `successful login emits Success with keypass`() = runTest {
        whenever(repository.login("sydney", "s8114019", "Rahul"))
            .thenReturn(LoginResponse(keypass = "topicABC"))

        viewModel.login("sydney", "s8114019", "Rahul")
        advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Success)
        assertEquals("topicABC", (state as Resource.Success).data)
    }

    @Test
    fun `404 response produces friendly error`() = runTest {
        val errorBody = "".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(404, errorBody))
        whenever(repository.login("sydney", "s8114019", "wrong"))
            .thenThrow(httpException)

        viewModel.login("sydney", "s8114019", "wrong")
        advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals(
            "Endpoint not found (404). Try a different campus.",
            (state as Resource.Error).message
        )
    }

    @Test
    fun `401 response produces invalid credentials error`() = runTest {
        val errorBody = "".toResponseBody(null)
        val httpException = HttpException(Response.error<Any>(401, errorBody))
        whenever(repository.login("sydney", "s8114019", "wrong"))
            .thenThrow(httpException)

        viewModel.login("sydney", "s8114019", "wrong")
        advanceUntilIdle()

        val state = viewModel.loginState.value
        assertTrue(state is Resource.Error)
        assertEquals(
            "Invalid username or password.",
            (state as Resource.Error).message
        )
    }

    @Test
    fun `resetState returns to Idle`() {
        viewModel.login("sydney", "", "")
        viewModel.resetState()
        assertTrue(viewModel.loginState.value is Resource.Idle)
    }
}
