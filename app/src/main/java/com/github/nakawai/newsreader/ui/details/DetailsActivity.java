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

package com.github.nakawai.newsreader.ui.details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.nakawai.newsreader.R;
import com.github.nakawai.newsreader.databinding.ActivityDetailsBinding;
import com.github.nakawai.newsreader.model.Model;
import com.github.nakawai.newsreader.model.entity.NYTimesStory;


public class DetailsActivity extends AppCompatActivity {

    private static final String KEY_STORY_ID = "key.storyId";


    private Toolbar toolbar;
    private DetailsPresenter presenter;
    private ActivityDetailsBinding binding;

    public static Intent getIntent(Context context, NYTimesStory story) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_STORY_ID, story.getUrl());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup initial views
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        binding.loaderView.setVisibility(View.VISIBLE);

        // After setup, notify presenter
        String storyId = getIntent().getExtras().getString(KEY_STORY_ID);
        presenter = new DetailsPresenter(this, Model.getInstance(), storyId);
        presenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    public void showLoader() {
        binding.loaderView.setAlpha(1.0f);
        binding.loaderView.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        if (binding.loaderView.getVisibility() != View.GONE) {
            binding.loaderView.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.loaderView.setVisibility(View.GONE);
                }
            });
        }
    }

    public void showStory(NYTimesStory story) {
        toolbar.setTitle(story.getTitle());
        binding.detailsView.setText(story.getStoryAbstract());
        binding.dateView.setText(story.getPublishedDate());
    }

    public void setRead(boolean read) {
        if (read) {
            binding.readView.setText(R.string.read);
            binding.readView.animate().alpha(1.0f);
        } else {
            binding.readView.setText("");
            binding.readView.animate().alpha(0f);
        }
    }
}
