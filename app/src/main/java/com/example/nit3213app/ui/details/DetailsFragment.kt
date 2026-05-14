package com.example.nit3213app.ui.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("entity", Entity::class.java)
        } else {
            arguments?.getSerializable("entity") as? Entity
        }

        if (entity == null) {
            return
        }

        binding.fieldsContainer.removeAllViews()
        entity.summaryFields.forEach { (key, value) ->
            addFieldBlock(key, value)
        }

        binding.descriptionValue.text = entity.description.orEmpty()
    }

    private fun addFieldBlock(label: String, value: String) {
        val ctx = binding.root.context
        val bottomPaddingPx = (16 * resources.displayMetrics.density).toInt()

        val labelView = TextView(ctx).apply {
            text = label
            setTextAppearance(android.R.style.TextAppearance_Material_Caption)
        }

        val valueView = TextView(ctx).apply {
            text = value
            setTextAppearance(android.R.style.TextAppearance_Material_Body1)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, bottomPaddingPx)
        }

        binding.fieldsContainer.addView(labelView)
        binding.fieldsContainer.addView(valueView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
