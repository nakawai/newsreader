package com.github.nakawai.newsreader.presentation.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivitySearchBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel.error.observe(this, Observer {
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(it.message)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.menu_item_search_view).actionView as SearchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query == null) {
                    false
                } else {
                    viewModel.onQueryTextSubmit(query)
                    true
                }
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.d(newText)
                return false
            }

        })
        return true
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}
