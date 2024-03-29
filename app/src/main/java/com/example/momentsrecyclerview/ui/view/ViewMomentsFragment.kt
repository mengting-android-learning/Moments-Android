package com.example.momentsrecyclerview.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.databinding.ViewFragmentMomentsBinding
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.MomentsViewModelFactory

class ViewMomentsFragment : Fragment() {
    private val momentsViewModel by activityViewModels<MomentsViewModel> {
        MomentsViewModelFactory(
            requireActivity().application
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ViewFragmentMomentsBinding>(
            inflater,
            R.layout.view_fragment_moments,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = momentsViewModel
        binding.tweetsList.adapter = TweetsAdapter()
        binding.viewSwipeRefreshLayout.setOnRefreshListener {
            momentsViewModel.refreshData()
            binding.viewSwipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }
}
