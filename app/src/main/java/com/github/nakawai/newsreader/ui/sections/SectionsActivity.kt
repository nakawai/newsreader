package com.github.nakawai.newsreader.ui.sections

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.databinding.ActivitySectionsBinding
import com.github.nakawai.newsreader.domain.entity.Section
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
