package com.example.nit3213app.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nit3213app.R
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.FragmentDashboardBinding
import com.example.nit3213app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()

    private val adapter by lazy {
        EntityAdapter(onItemClick = ::openDetails)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()

        val keypass = arguments?.getString("keypass").orEmpty()
        if (keypass.isBlank()) {
            binding.errorText.text = getString(R.string.error_missing_keypass)
            binding.errorText.visibility = View.VISIBLE
        } else {
            binding.topicText.text = getString(R.string.dashboard_topic_format, keypass)
            viewModel.loadDashboard(keypass)
        }
    }

    private fun setupRecyclerView() {
        binding.entitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.entitiesRecyclerView.adapter = adapter
        binding.entitiesRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.entitiesState.collect { state ->
                    when (state) {
                        is Resource.Idle -> Unit
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.errorText.visibility = View.GONE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                            adapter.submitList(state.data)
                            binding.totalText.text = getString(
                                R.string.entity_total_format,
                                state.data.size
                            )
                            binding.totalText.visibility = View.VISIBLE
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorText.text = state.message
                            binding.errorText.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun openDetails(entity: Entity) {
        val args = Bundle().apply {
            putSerializable("entity", entity)
        }
        findNavController().navigate(R.id.action_dashboard_to_details, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
