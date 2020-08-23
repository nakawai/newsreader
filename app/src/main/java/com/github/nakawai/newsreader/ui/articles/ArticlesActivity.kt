/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.ui.details.DetailsActivity

class ArticlesActivity : AppCompatActivity(), ArticleListAdapter.OnItemClickListener {
    private val viewModel: ArticlesViewModel by viewModels { ArticlesViewModel.Factory(Model.instance!!) }
    private lateinit var adapter: ArticleListAdapter
    private lateinit var binding: ActivityArticlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = ArticleListAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.refreshView.setOnRefreshListener(this::onRefresh)
        binding.emptyView.setOnRefreshListener(this::onRefresh)

        binding.progressBar.visibility = View.INVISIBLE

        observeViewModel()

        // After setup, notify presenter
        val section = Section.valueOf(intent.extras!!.getString(EXTRA_SECTION)!!)
        viewModel.sectionSelected(section)
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
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.progress.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        viewModel.isRefreshing.observe(this, Observer {
            binding.refreshView.isRefreshing = it
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
