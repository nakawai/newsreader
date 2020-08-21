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
package com.github.nakawai.newsreader.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.databinding.ActivityMainBinding
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.ui.details.DetailsActivity

class MainActivity : AppCompatActivity(), NewsListAdapter.OnItemClickListener {
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory(Model.instance!!) }
    private lateinit var adapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = NewsListAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.refreshView.setOnRefreshListener { viewModel.refreshList() }
        binding.progressBar.visibility = View.INVISIBLE

        observeViewModel()

        // After setup, notify presenter
        viewModel.onCreate()

    }

    override fun onItemClick(story: Article) {
        val intent: Intent = DetailsActivity.getIntent(this, story)
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.sectionList.observe(this, Observer { sections ->
            val sectionList = sections.toTypedArray()
            val adapter: ArrayAdapter<*> = ArrayAdapter<CharSequence?>(this, android.R.layout.simple_spinner_dropdown_item, sectionList)
            binding.spinner.adapter = adapter
            binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    viewModel.titleSpinnerSectionSelected((adapter.getItem(position) as String?)!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // NOP
                }
            }
        })

        viewModel.storiesData.observe(this, Observer {
            if (it.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.listView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.listView.visibility = View.VISIBLE
            }
            adapter.submitList(it)
        })

        viewModel.isLoading.observe(this, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        viewModel.isRefreshing.observe(this, Observer {
            binding.refreshView.isRefreshing = it
        })
    }
}
