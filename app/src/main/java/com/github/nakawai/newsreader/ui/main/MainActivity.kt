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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityMainBinding
import com.github.nakawai.newsreader.databinding.ListItemBinding
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import com.github.nakawai.newsreader.ui.main.MainActivity

class MainActivity : AppCompatActivity() {
    lateinit var presenter: MainPresenter
    private lateinit var adapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = MainPresenter(this, Model.instance!!)
        // Setup initial views
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        adapter = NewsListAdapter(this@MainActivity, emptyList())
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                presenter.listItemSelected(position)
            }
        binding.listView.emptyView = layoutInflater.inflate(R.layout.common_emptylist, binding.listView,false)
        binding.refreshView.setOnRefreshListener { presenter.refreshList() }
        binding.progressBar.visibility = View.INVISIBLE

        // After setup, notify presenter
        presenter.onCreate()
    }

    /**
     * Setup the toolbar spinner with the available sections
     */
    fun configureToolbar(sections: List<String?>) {
        val sectionList = sections.toTypedArray()
        val adapter: ArrayAdapter<*> = ArrayAdapter<CharSequence?>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            sectionList
        )
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                presenter.titleSpinnerSectionSelected((adapter.getItem(position) as String?)!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    fun hideRefreshing() {
        binding.refreshView.isRefreshing = false
    }

    fun showList(items: List<NYTimesStory>) {

            adapter.clear()
            adapter.addAll(items)
            adapter.notifyDataSetChanged()

    }

    fun showNetworkLoading(networkInUse: Boolean?) {
        binding.progressBar.visibility = if (networkInUse!!) View.VISIBLE else View.INVISIBLE
    }

    // ListView adapter class
    class NewsListAdapter(
        context: Context,
        initialData: List<NYTimesStory?>?
    ) : ArrayAdapter<NYTimesStory?>(context, 0) {
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
                val binding =
                    ListItemBinding.inflate(inflater)
                view = binding.root
                val holder =
                    ViewHolder(
                        binding
                    )
                view.tag = holder
            }
            val holder =
                view.tag as ViewHolder
            val story = getItem(position)
            holder.binding.text.text = story?.title
            holder.binding.text.setTextColor(if (story!!.isRead) readColor else unreadColor)
            return view
        }

        internal class ViewHolder(var binding: ListItemBinding)

        init {
            setNotifyOnChange(false)
            addAll(initialData!!)
            inflater = LayoutInflater.from(context)
            readColor = ContextCompat.getColor(context, android.R.color.darker_gray)
            unreadColor = ContextCompat.getColor(context, android.R.color.primary_text_light)
        }
    }
}