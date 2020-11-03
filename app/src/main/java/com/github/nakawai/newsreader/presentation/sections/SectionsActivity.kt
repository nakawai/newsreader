package com.github.nakawai.newsreader.presentation.sections

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivitySectionsBinding
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.presentation.articles.ArticlesActivity
import com.github.nakawai.newsreader.presentation.search.SearchActivity
import org.koin.android.viewmodel.ext.android.viewModel

class SectionsActivity : AppCompatActivity(), SectionListAdapter.OnItemClickListener {
    private val viewModel: SectionsViewModel by viewModel()
    private lateinit var adapter: SectionListAdapter
    private lateinit var binding: ActivitySectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivitySectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = SectionListAdapter(this)

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        observeViewModel()

        viewModel.start()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_sections, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
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
