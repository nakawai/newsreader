package com.github.nakawai.newsreader.presentation.sections

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivitySectionsBinding
import com.github.nakawai.newsreader.presentation.ItemListDialogFragment
import com.github.nakawai.newsreader.presentation.articles.TopStoriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SectionsActivity : AppCompatActivity() {
    private val viewModel: SectionsViewModel by viewModels()
    private lateinit var adapter: SectionListAdapter
    private lateinit var binding: ActivitySectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivitySectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = SectionListAdapter(onItemClick = { section ->
            TopStoriesActivity.start(this, section)
        })

        binding.listView.adapter = adapter
        binding.listView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        observeViewModel()

        viewModel.start()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sections, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_item_search -> {
                //startActivity(Intent(this, SearchActivity::class.java))
                ItemListDialogFragment.newInstance(5).show(supportFragmentManager, "hoge")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.sections.observe(this, {
            adapter.submitList(it)
        })

    }

}
