package com.example.momentsrecyclerview.ui.compose.screen.bottom.sheet

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.momentsrecyclerview.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun CameraTab(
    hideBottomSheet: () -> Unit,
    setLocalImage: (List<String>) -> Unit,
    navigateToNewTweetScreen: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    var uri: Uri? by remember { mutableStateOf(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                setLocalImage(listOf(uri.toString()))
                navigateToNewTweetScreen()
            }
        }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            uri = activity.createImageFileUri()
            cameraLauncher.launch(uri)
        } else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(activity, "You have denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            hideBottomSheet()
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                uri = activity.createImageFileUri()
                cameraLauncher.launch(uri)
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.CAMERA
                )
            ) {
                AlertDialog.Builder(activity)
                    .setMessage("Camera Needed")
                    .setPositiveButton("OK") { _, _ -> launcher.launch(Manifest.permission.CAMERA) }
                    .setNegativeButton("No") { _, _ -> }
                    .create().show()
            } else {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    ) {
        Space()
        Text(text = "Camera")
        Space()
    }
}

fun Context.createImageFileUri(): Uri {
    val imageFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val photoFile = File.createTempFile(
        imageFileName,
        ".png",
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            externalCacheDir
        } else {
            cacheDir
        }
    )
    return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", photoFile)
}
