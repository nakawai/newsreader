package com.github.nakawai.newsreader.presentation.articles

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityArticlesBinding
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.presentation.ErrorDialogFragment
import com.github.nakawai.newsreader.presentation.details.DetailsActivity
import com.github.nakawai.newsreader.presentation.translate
import org.koin.android.viewmodel.ext.android.viewModel

class ArticlesActivity : AppCompatActivity(), ArticleListAdapter.OnItemClickListener {
    private val viewModel: ArticlesViewModel by viewModel()
    private lateinit var adapter: ArticleListAdapter
    private lateinit var binding: ActivityArticlesBinding

    private var initialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ArticleListAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.refreshView.setOnRefreshListener(this::onRefresh)
        binding.emptyView.setOnRefreshListener(this::onRefresh)

        // After setup, notify presenter
        val section = Section.valueOf(intent.extras!!.getString(EXTRA_SECTION)!!)
        supportActionBar?.title = section.translate().label

        observeViewModel()

        viewModel.start(section)
        viewModel.loadData(force = false)

    }

    private fun onRefresh() {
        viewModel.loadData(force = true)
    }

    override fun onItemClick(story: ArticleUrl) {
        val intent: Intent = DetailsActivity.getIntent(this, story)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.articles.observe(this, Observer { articles ->
            if (!initialized) {
                initialized = true
                return@Observer
            }

            binding.emptyView.visibility = if(articles.isEmpty()) View.VISIBLE  else View.GONE
            binding.refreshView.visibility = if(articles.isNotEmpty()) View.VISIBLE  else View.GONE

            adapter.submitList(articles)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.refreshView.isRefreshing = isLoading
            binding.emptyView.isRefreshing = isLoading
        })

        viewModel.error.observe(this, Observer {
            ErrorDialogFragment.newInstance(getString(R.string.dialog_error_title), it.message.orEmpty())
                .show(supportFragmentManager, ErrorDialogFragment.TAG)
        })
    }

    companion object {
        const val EXTRA_SECTION = "EXTRA_SECTION"

        fun start(activity: Activity, section: Section) {
            activity.startActivity(
                Intent(activity, ArticlesActivity::class.java)
                    .putExtra(EXTRA_SECTION, section.toString())
            )
        }
    }
}
