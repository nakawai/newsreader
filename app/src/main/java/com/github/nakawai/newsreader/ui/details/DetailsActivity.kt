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
package com.github.nakawai.newsreader.ui.details

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivityDetailsBinding
import com.github.nakawai.newsreader.model.Model
import com.github.nakawai.newsreader.model.entity.NYTimesStory

class DetailsActivity : AppCompatActivity() {
    private val viewModel: DetailsViewModel by viewModels { DetailsViewModel.Factory(Model.instance!!) }
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup initial views
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.loaderView.visibility = View.VISIBLE

        observeViewModel()

        // After setup, notify presenter
        val storyId = intent.extras?.getString(KEY_STORY_ID).orEmpty()

        viewModel.onCreate(storyId)
    }

    private fun observeViewModel() {
        viewModel.story.observe(this, Observer {
            showStory(it)
            setRead(it.isRead)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            if(isLoading) {
                binding.loaderView.alpha = 1.0f
                binding.loaderView.visibility = View.VISIBLE
            } else {
                if (binding.loaderView.visibility != View.GONE) {
                    binding.loaderView.animate().alpha(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.loaderView.visibility = View.GONE
                            }
                        })
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    fun showStory(story: NYTimesStory) {
        binding.toolbar.title = story.title
        binding.detailsView.text = story.storyAbstract
        binding.dateView.text = story.publishedDate
    }

    fun setRead(read: Boolean) {
        if (read) {
            binding.readView.setText(R.string.read)
            binding.readView.animate().alpha(1.0f)
        } else {
            binding.readView.text = ""
            binding.readView.animate().alpha(0f)
        }
    }

    companion object {
        private const val KEY_STORY_ID = "key.storyId"
        fun getIntent(context: Context, story: NYTimesStory): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(KEY_STORY_ID, story.url)
            return intent
        }
    }
}
