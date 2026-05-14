package com.example.nit3213app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.ItemEntityBinding

class EntityAdapter(
    private val onItemClick: (Entity) -> Unit
) : ListAdapter<Entity, EntityAdapter.EntityViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Entity) {
            binding.property1Text.text = entity.property1.orEmpty()
            binding.property2Text.text = entity.property2.orEmpty()
            binding.root.setOnClickListener { onItemClick(entity) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Entity>() {
            override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean =
                oldItem == newItem
        }
    }
}
