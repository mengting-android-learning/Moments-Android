package com.example.momentsrecyclerview.ui.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.compose.MomentsDescription
import com.example.momentsrecyclerview.ui.compose.NewTextTweet
import com.example.momentsrecyclerview.ui.compose.NewTweet
import com.example.momentsrecyclerview.ui.compose.SingleTweetImage

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
                viewModel,
                { url -> navController.navigateToSingleImage(url) },
                { navController.navigateSingleTopTo(NewTextTweetScreen.route) },
                { navController.navigateSingleTopTo(NewTweetScreen.route) },
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
            val content by viewModel.localContent.observeAsState("")
            NewTextTweet(
                content,
                viewModel::setLocalContent,
                viewModel::createAndSaveTweet
            ) { navController.navigateUp() }
        }
        composable(
            route = NewTweetScreen.route,
        ) {
            val images by viewModel.localImages.observeAsState(emptyList())
            val content by viewModel.localContent.observeAsState("")
            NewTweet(
                images,
                viewModel::setLocalImages,
                content,
                viewModel::setLocalContent,
                viewModel::createAndSaveTweet,
            ) { navController.navigateUp() }
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
