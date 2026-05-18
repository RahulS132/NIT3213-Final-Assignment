package com.example.nit3213app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.nit3213app.R
import com.example.nit3213app.databinding.FragmentLoginBinding
import com.example.nit3213app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Fragment for user login
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val campusOptions = listOf("sydney", "footscray", "ort")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCampusDropdown()
        setupLoginButton()
        observeLoginState()
    }

    private fun setupCampusDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            campusOptions
        )
        binding.campusDropdown.setAdapter(adapter)
        binding.campusDropdown.setText(campusOptions[0], false)
    }

    private fun setupLoginButton() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text?.toString().orEmpty()
            val password = binding.passwordInput.text?.toString().orEmpty()
            val campus = binding.campusDropdown.text?.toString()?.lowercase()?.trim().orEmpty()
            viewModel.login(campus, username, password)
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDashboard(keypass: String) {
        val args = Bundle().apply { putString("keypass", keypass) }
        findNavController().navigate(R.id.action_login_to_dashboard, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
