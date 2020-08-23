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
package com.github.nakawai.newsreader.ui.sections

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.databinding.ActivitySectionsBinding
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.ui.articles.ArticlesActivity

class SectionsActivity : AppCompatActivity(), SectionListAdapter.OnItemClickListener {
    private val viewModel: SectionsViewModel by viewModels { SectionsViewModel.Factory() }
    private lateinit var adapter: SectionListAdapter
    private lateinit var binding: ActivitySectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivitySectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = SectionListAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        observeViewModel()

        viewModel.start()


    }

    private fun observeViewModel() {
        viewModel.sections.observe(this, Observer {
            adapter.submitList(it)
        })

    }


    override fun onItemClick(section: Section) {
        ArticlesActivity.start(this, section)
    }

}
