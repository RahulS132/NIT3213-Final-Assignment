package com.example.nit3213app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.ItemEntityBinding

// Adapter for Entity list
class EntityAdapter(
    private val onItemClick: (Entity) -> Unit
) : ListAdapter<Entity, EntityAdapter.EntityViewHolder>(DIFF_CALLBACK) {

    // Inflate layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntityViewHolder(binding)
    }

    // Bind data to holder
    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // Bind entity data to views
        fun bind(entity: Entity) {
            val summary = entity.summaryFields
            binding.primaryText.text = summary.firstOrNull()?.second ?: "(unnamed)"

            binding.fieldsContainer.removeAllViews()
            summary.drop(1).forEach { (key, value) ->
                val tv = TextView(binding.root.context).apply {
                    text = "$key: $value"
                    setTextAppearance(android.R.style.TextAppearance_Material_Body2)
                }
                binding.fieldsContainer.addView(tv)
            }
            binding.root.setOnClickListener { onItemClick(entity) }
        }
    }

    companion object {
        // Callback for item differences
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Entity>() {
            override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean =
                oldItem == newItem
        }
    }
}
