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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityMainBinding
import com.github.nakawai.newsreader.databinding.ListItemBinding
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.ui.details.DetailsActivity

class MainActivity : AppCompatActivity() {
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

        adapter = NewsListAdapter(this@MainActivity, emptyList())
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            adapter.getItem(position)?.let {
                val intent: Intent = DetailsActivity.getIntent(this, it)
                startActivity(intent)
            }
        }
        binding.listView.emptyView =
            layoutInflater.inflate(R.layout.common_emptylist, binding.listView, false)
        binding.refreshView.setOnRefreshListener { viewModel.refreshList() }
        binding.progressBar.visibility = View.INVISIBLE

        observeViewModel()

        // After setup, notify presenter
        viewModel.onCreate()

    }

    private fun observeViewModel() {
        viewModel.sectionList.observe(this, Observer {
            configureToolbar(it)
        })

        viewModel.storiesData.observe(this, Observer {
            adapter.clear()
            adapter.addAll(it)
            adapter.notifyDataSetChanged()
        })

        viewModel.isNetworkInUse.observe(this, Observer {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })

        viewModel.isRefreshing.observe(this, Observer {
            binding.refreshView.isRefreshing = it
        })
    }

    /**
     * Setup the toolbar spinner with the available sections
     */
    private fun configureToolbar(sections: List<String>) {
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
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    // ListView adapter class
    class NewsListAdapter(context: Context, initialData: List<NYTimesStory>) : ArrayAdapter<NYTimesStory?>(context, 0) {
        private val inflater: LayoutInflater

        @ColorInt
        private val readColor: Int

        @ColorInt
        private val unreadColor: Int
        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            var view = convertView
            if (view == null) {
                val binding = ListItemBinding.inflate(inflater)
                view = binding.root
                val holder = ViewHolder(binding)
                view.tag = holder
            }
            val holder = view.tag as ViewHolder
            val story = getItem(position)
            holder.binding.text.text = story?.title
            holder.binding.text.setTextColor(if (story!!.isRead) readColor else unreadColor)
            return view
        }

        internal class ViewHolder(var binding: ListItemBinding)

        init {
            setNotifyOnChange(false)
            addAll(initialData)
            inflater = LayoutInflater.from(context)
            readColor = ContextCompat.getColor(context, android.R.color.darker_gray)
            unreadColor = ContextCompat.getColor(context, android.R.color.primary_text_light)
        }
    }
}
