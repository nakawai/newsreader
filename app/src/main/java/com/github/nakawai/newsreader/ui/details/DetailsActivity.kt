package com.github.nakawai.newsreader.ui.details

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityDetailsBinding
import com.github.nakawai.newsreader.model.NewsReaderAppService
import com.github.nakawai.newsreader.model.entity.Article
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {
    private val outputDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Factory(NewsReaderAppService.instance!!, intent.extras?.getString(KEY_STORY_ID).orEmpty())
    }
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.loaderView.visibility = View.VISIBLE

        observeViewModel()
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
            if (isLoading) {
                binding.loaderView.alpha = 1.0f
                binding.loaderView.visibility = View.VISIBLE
            } else {
                if (binding.loaderView.visibility != View.GONE) {
                    binding.loaderView.animate().alpha(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.loaderView.visibility = View.GONE
                            }
                        })
                }
            }

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
        private const val KEY_STORY_ID = "KEY_STORY_ID"
        fun getIntent(context: Context, story: Article): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(KEY_STORY_ID, story.url)
            return intent
        }
    }
}
