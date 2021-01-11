package com.github.nakawai.newsreader.presentation.search.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.nakawai.newsreader.databinding.FragmentPlaceholderBinding
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.presentation.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class PlaceholderFragment : Fragment() {

    private lateinit var adapter: SearchResultAdapter
    private var _binding: FragmentPlaceholderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val viewModel: PlaceholderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceholderBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchResultAdapter(this::onItemClick)
        binding.recyclerView.adapter = adapter
    }

    private fun onItemClick(url: ArticleUrl) {
        Toast.makeText(requireContext(), "url:${url.value}", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchViewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        when (requireArguments().getInt(ARG_SECTION_NUMBER)) {
            0 -> {
                searchViewModel.articles.observe(viewLifecycleOwner, { stories ->
                    adapter.submitList(stories)
                })
            }
            1 -> {
                searchViewModel.articles2.observe(viewLifecycleOwner, { stories ->
                    adapter.submitList(stories)
                })
            }
            2 -> {
                searchViewModel.articles3.observe(viewLifecycleOwner, { stories ->
                    adapter.submitList(stories)
                })
            }
        }

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = bundleOf(
                    ARG_SECTION_NUMBER to sectionNumber
                )
            }
        }
    }
}
