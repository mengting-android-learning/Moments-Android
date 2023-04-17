package com.example.momentsrecyclerview.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.data.NetworkTweetsRepository
import com.example.momentsrecyclerview.data.NetworkUserInfoRepository
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import com.example.momentsrecyclerview.databinding.ViewFragmentMomentsBinding
import com.example.momentsrecyclerview.ui.MomentsViewModel

class ViewMomentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application
        val momentsViewModelFactory = MomentsViewModel.MomentsViewModelFactory(
            application,
            NetworkUserInfoRepository(UserInfoNetwork.userInfo),
            NetworkTweetsRepository(TweetsListNetwork.tweets)
        )
        val momentsViewModel =
            ViewModelProvider(
                this,
                momentsViewModelFactory
            )[MomentsViewModel::class.java]
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
