package com.github.nakawai.newsreader.ui.articles

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.databinding.ActivityArticlesBinding
import com.github.nakawai.newsreader.model.NewsReaderAppService
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.ui.details.DetailsActivity

class ArticlesActivity : AppCompatActivity(), ArticleListAdapter.OnItemClickListener {
    private lateinit var section: Section
    private val viewModel: ArticlesViewModel by viewModels {
        // TODO use DI Container
        ArticlesViewModel.Factory(NewsReaderAppService.instance!!, section)
    }
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
        section = Section.valueOf(intent.extras!!.getString(EXTRA_SECTION)!!)
        supportActionBar?.title = section.label

        observeViewModel()

        viewModel.loadData(force = false)

    }

    private fun onRefresh() {
        viewModel.loadData(force = true)
    }

    override fun onItemClick(story: Article) {
        val intent: Intent = DetailsActivity.getIntent(this, story)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.storiesData.observe(this, Observer {
            if (!initialized) {
                initialized = true
                return@Observer
            }

            if (it.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.refreshView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.refreshView.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        })

        viewModel.isLoading.observe(this, Observer {
            binding.refreshView.isRefreshing = it
            binding.emptyView.isRefreshing = it
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
