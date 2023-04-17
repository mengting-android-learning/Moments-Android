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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.data.NetworkTweetsRepository
import com.example.momentsrecyclerview.data.NetworkUserInfoRepository
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import com.example.momentsrecyclerview.databinding.ComposeFragmentMomentsBinding
import com.example.momentsrecyclerview.ui.MomentsViewModel

class ComposeMomentsFragment : Fragment() {

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
        val binding = DataBindingUtil.inflate<ComposeFragmentMomentsBinding>(
            inflater,
            R.layout.compose_fragment_moments,
            container,
            false
        ).apply {
            composeMoments.setContent { MomentsFragment(momentsViewModel) }
        }
        binding.lifecycleOwner = this
        return binding.root
    }
}

@Composable
fun MomentsFragment(momentsViewModel: MomentsViewModel) {
    val navController = rememberNavController()
    MomentsNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        viewModel = momentsViewModel
    )
}

@Composable
fun MomentsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MomentsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = MomentsScreen.route,
        modifier = modifier
    ) {
        composable(route = MomentsScreen.route) {
            MomentsDescription(
                momentsViewModel = viewModel,
                onImageClick = { url ->
                    navController.navigateToSingleImage(url)
                }
            )
        }
        composable(
            route = SingleTweetImageScreen.routeWithArgs,
            arguments = SingleTweetImageScreen.arguments,
            deepLinks = SingleTweetImageScreen.deepLinks
        ) { navBackStackEntry ->
            val imageUrl =
                navBackStackEntry.arguments?.getString(SingleTweetImageScreen.imageUrlArg)
            SingleTweetImage(imageUrl) { navController.navigateUp() }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSingleImage(accountType: String) {
    this.navigateSingleTopTo("${SingleTweetImageScreen.route}/$accountType")
}
