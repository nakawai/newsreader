package com.github.nakawai.newsreader.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.nakawai.newsreader.R
import com.github.nakawai.newsreader.databinding.ActivitySectionsBinding
import com.github.nakawai.newsreader.presentation.articles.TopStoriesActivity
import com.github.nakawai.newsreader.presentation.sections.SectionListAdapter
import com.github.nakawai.newsreader.presentation.sections.SectionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SectionsFragment : Fragment() {

    private var _binding: ActivitySectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_sections, container, false)
        _binding = ActivitySectionsBinding.bind(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.listView.adapter = SectionListAdapter(onItemClick = { section ->
            TopStoriesActivity.start(requireActivity(), section)
        })
        binding.listView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        observeViewModel()

        viewModel.start()
    }

    private fun observeViewModel() {
        viewModel.sections.observe(viewLifecycleOwner, {
            (binding.listView.adapter as SectionListAdapter).submitList(it)
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
