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

package com.github.nakawai.newsreader.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.nakawai.newsreader.R;
import com.github.nakawai.newsreader.databinding.ActivityMainBinding;
import com.github.nakawai.newsreader.databinding.ListItemBinding;
import com.github.nakawai.newsreader.model.Model;
import com.github.nakawai.newsreader.model.entity.NYTimesStory;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.refresh_view) SwipeRefreshLayout refreshView;
//    @BindView(R.id.list_view) ListView listView;
//    @BindView(R.id.progressbar) MaterialProgressBar progressBar;
//    @BindView(R.id.spinner) Spinner spinner;

    MainPresenter presenter = new MainPresenter(this, Model.getInstance());
    private ArrayAdapter<NYTimesStory> adapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup initial views
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = null;
        binding.listView.setOnItemClickListener((parent, view, position, id) -> presenter.listItemSelected(position));
        binding.listView.setEmptyView(getLayoutInflater().inflate(R.layout.common_emptylist, binding.listView, false));
        binding.refreshView.setOnRefreshListener(() -> presenter.refreshList());
        binding.progressBar.setVisibility(View.INVISIBLE);

        // After setup, notify presenter
        presenter.onCreate();
    }

    /**
     * Setup the toolbar spinner with the available sections
     */
    public void configureToolbar(List<String> sections) {
        String[] sectionList = sections.toArray(new String[sections.size()]);
        final ArrayAdapter adapter = new ArrayAdapter<CharSequence>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sectionList);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.titleSpinnerSectionSelected((String) adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void hideRefreshing() {
        binding.refreshView.setRefreshing(false);
    }

    public void showList(List<NYTimesStory> items) {
        if (adapter == null) {
            adapter = new NewsListAdapter(MainActivity.this, items);
            binding.listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    public void showNetworkLoading(Boolean networkInUse) {
        binding.progressBar.setVisibility(networkInUse ? View.VISIBLE : View.INVISIBLE);
    }

    // ListView adapter class
    public static class NewsListAdapter extends ArrayAdapter<NYTimesStory> {

        private final LayoutInflater inflater;
        @ColorInt
        private final int readColor;
        @ColorInt
        private final int unreadColor;

        public NewsListAdapter(Context context, List<NYTimesStory> initialData) {
            super(context, android.R.layout.simple_list_item_1);
            setNotifyOnChange(false);
            addAll(initialData);
            inflater = LayoutInflater.from(context);

            readColor = context.getResources().getColor(android.R.color.darker_gray);
            unreadColor = context.getResources().getColor(android.R.color.primary_text_light);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                ListItemBinding binding = ListItemBinding.inflate(inflater);
                view = binding.getRoot();
                ViewHolder holder = new ViewHolder(binding);
                view.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) view.getTag();
            NYTimesStory story = getItem(position);
            holder.binding.text.setText(story.getTitle());
            holder.binding.text.setTextColor(story.isRead() ? readColor : unreadColor);
            return view;
        }

        static class ViewHolder {
            ListItemBinding binding;
            public ViewHolder(ListItemBinding binding) {
                this.binding = binding;
            }
        }
    }
}
