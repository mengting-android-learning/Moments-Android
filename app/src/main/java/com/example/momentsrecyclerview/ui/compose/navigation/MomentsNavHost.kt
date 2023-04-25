package com.example.momentsrecyclerview.ui.compose.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.momentsrecyclerview.ui.MomentsViewModel
import com.example.momentsrecyclerview.ui.compose.screen.add.NewTextTweet
import com.example.momentsrecyclerview.ui.compose.screen.add.NewTweet
import com.example.momentsrecyclerview.ui.compose.screen.home.MomentsDescription
import com.example.momentsrecyclerview.ui.compose.screen.single.SingleTweetImage

@Composable
fun MomentsNavHost(
    navController: NavHostController,
    viewModel: MomentsViewModel,
    activity: Activity,
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
                navController::navigateToSingleImage,
                { navController.navigate(NewTextTweetScreen.route) },
                { navController.navigate(NewTweetScreen.route) },
                activity
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
                { navController.navigateUp() },
                viewModel::persistAccess,
                navController::navigateToSingleImage
            )
        }
    }
}

private fun NavHostController.navigateToSingleImage(accountType: String) {
    this.navigate("${SingleTweetImageScreen.route}/$accountType")
}
