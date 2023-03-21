package com.example.momentsrecyclerview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.databinding.FragmentMomentsBinding
import com.example.momentsrecyclerview.viewmodels.MomentsViewModel

class MomentsFragment : Fragment() {

    private val momentsViewModel: MomentsViewModel by lazy {
        ViewModelProvider(this).get(MomentsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMomentsBinding>(
            inflater,
            R.layout.fragment_moments,
            container,
            false
        ).apply {
            composeMoments.setContent { MomentsDescription(momentsViewModel) }
        }
        binding.lifecycleOwner = this
        binding.viewModel = momentsViewModel
        binding.swipeRefreshLayout.setOnRefreshListener {
            momentsViewModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }
}
