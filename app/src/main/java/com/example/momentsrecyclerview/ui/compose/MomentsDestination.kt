package com.example.momentsrecyclerview.ui.compose

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface MomentsDestination {
    val route: String
}

object MomentsScreen : MomentsDestination {
    override val route = "moments"
}

object SingleTweetImageScreen : MomentsDestination {
    override val route = "single_image"
    const val imageUrlArg = "image_url"
    val routeWithArgs = "$route/{$imageUrlArg}"
    val arguments = listOf(
        navArgument(imageUrlArg) { type = NavType.StringType }
    )
    val deepLinks = listOf(
        navDeepLink {
            uriPattern =
                "{$imageUrlArg}"
        }
    )
}
