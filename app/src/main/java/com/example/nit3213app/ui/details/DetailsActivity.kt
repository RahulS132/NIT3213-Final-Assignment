package com.example.nit3213app.ui.details

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ENTITY = "extra_entity"
    }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_ENTITY, Entity::class.java)
        } else {
            intent.getSerializableExtra(EXTRA_ENTITY) as? Entity
        }

        if (entity == null) {
            finish()
            return
        }

        binding.property1Value.text = entity.property1.orEmpty()
        binding.property2Value.text = entity.property2.orEmpty()
        binding.descriptionValue.text = entity.description.orEmpty()
    }
}
