package com.github.nakawai.newsreader.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityDetailsBinding
import com.github.nakawai.newsreader.domain.story.Story
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {
    private val outputDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    private val viewModel: DetailsViewModel by viewModel()
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeViewModel()

        viewModel.start(intent.getParcelableExtra(KEY_STORY_URL)!!)
    }

    private fun observeViewModel() {
        viewModel.story.observe(this, Observer { article ->
            binding.toolbar.title = article.title
            binding.detailsView.text = article.storyAbstract
            binding.dateView.text = article.publishedDate?.let { outputDateFormat.format(it) }

            if (article.isRead) {
                binding.readView.setText(R.string.read)
                binding.readView.animate().alpha(1.0f)
            } else {
                binding.readView.text = ""
                binding.readView.animate().alpha(0f)
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.loaderView.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    companion object {
        private const val KEY_STORY_URL = "KEY_STORY_URL"
        fun getIntent(context: Context, story: Story): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(KEY_STORY_URL, story.url)
            return intent
        }
    }
}
