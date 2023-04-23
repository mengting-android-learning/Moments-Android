package com.example.momentsrecyclerview.ui.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.rememberNavController
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.databinding.ComposeFragmentMomentsBinding
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.MomentsViewModelFactory
import com.example.momentsrecyclerview.ui.compose.navigation.MomentsNavHost

class ComposeMomentsFragment : Fragment() {
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
        val binding = DataBindingUtil.inflate<ComposeFragmentMomentsBinding>(
            inflater,
            R.layout.compose_fragment_moments,
            container,
            false
        ).apply {
            composeMoments.setContent {
                MomentsFragment(
                    momentsViewModel
                )
            }
        }
        binding.lifecycleOwner = this
        return binding.root
    }
}

@Composable
fun MomentsFragment(
    momentsViewModel: MomentsViewModel
) {
    val navController = rememberNavController()
    MomentsNavHost(
        navController,
        momentsViewModel,
        Modifier.fillMaxSize()
    )
}

