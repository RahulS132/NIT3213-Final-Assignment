package com.example.nit3213app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nit3213app.databinding.ActivityLoginBinding
import com.example.nit3213app.ui.dashboard.DashboardActivity
import com.example.nit3213app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text?.toString().orEmpty()
            val password = binding.passwordInput.text?.toString().orEmpty()
            viewModel.login(username, password)
        }

        observeLoginState()
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is Resource.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                        }
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                            binding.loginButton.isEnabled = false
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            navigateToDashboard(state.data)
                            viewModel.resetState()
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.loginButton.isEnabled = true
                            binding.errorText.text = state.message
                            binding.errorText.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDashboard(keypass: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            putExtra(DashboardActivity.EXTRA_KEYPASS, keypass)
        }
        startActivity(intent)
    }
}
