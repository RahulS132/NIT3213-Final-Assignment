package com.example.nit3213app.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.ActivityDashboardBinding
import com.example.nit3213app.ui.details.DetailsActivity
import com.example.nit3213app.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()

    private val adapter by lazy {
        EntityAdapter(onItemClick = ::openDetails)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeState()

        val keypass = intent.getStringExtra(EXTRA_KEYPASS).orEmpty()
        if (keypass.isBlank()) {
            binding.errorText.text = getString(com.example.nit3213app.R.string.error_missing_keypass)
            binding.errorText.visibility = View.VISIBLE
        } else {
            viewModel.loadDashboard(keypass)
        }
    }

    private fun setupRecyclerView() {
        binding.entitiesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.entitiesRecyclerView.adapter = adapter
        binding.entitiesRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                                com.example.nit3213app.R.string.entity_total_format,
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
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_ENTITY, entity)
        }
        startActivity(intent)
    }
}
