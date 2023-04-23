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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.momentsrecyclerview.R
import com.example.momentsrecyclerview.databinding.ComposeFragmentMomentsBinding
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.MomentsViewModelFactory

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

@Composable
fun MomentsNavHost(
    navController: NavHostController,
    viewModel: MomentsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = MomentsScreen.route,
        modifier = modifier
    ) {
        composable(route = MomentsScreen.route) {
            MomentsDescription(
                momentsViewModel = viewModel,
                navigateToSingleTweetImage = { url ->
                    navController.navigateToSingleImage(url)
                },
                navigateToNewTextTweetScreen = {
                    navController.navigateSingleTopTo(NewTextTweetScreen.route)
                },
                navigateToNewTweetScreen = {
                    navController.navigateSingleTopTo(NewTweetScreen.route)
                },
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
        composable(route = NewTextTweetScreen.route) {
            NewTextTweet(
                onCancelClick = { navController.navigateSingleTopTo(MomentsScreen.route) },
                onSendClick = { text ->
                    viewModel.saveNewTweet(text)
                },
                onSendClickNavigate = { navController.navigateUp() }
            )
        }
        composable(
            route = NewTweetScreen.route,
        ) {
            NewTweet(viewModel.localImages.value) { navController.navigateUp() }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
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
