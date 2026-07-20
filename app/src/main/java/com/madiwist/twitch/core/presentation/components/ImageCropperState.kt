package com.madiwist.twitch.core.presentation.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

enum class CropShape { RECTANGLE, CIRCLE }
sealed class CropAspectRatio(val label: String) {
    data object Free       : CropAspectRatio("Free")
    data object Square     : CropAspectRatio("1 : 1")
    data object Ratio4x3   : CropAspectRatio("4 : 3")
    data object Ratio16x9  : CropAspectRatio("16 : 9")
    data class Custom(val w: Float, val h: Float) : CropAspectRatio("$w : $h")

    fun ratio(): Float? = when (this) {
        is Free      -> null
        is Square    -> 1f
        is Ratio4x3  -> 4f / 3f
        is Ratio16x9 -> 16f / 9f
        is Custom    -> w / h
    }
}
@Stable
class ImageCropperState(
    initialAspectRatio: CropAspectRatio = CropAspectRatio.Free,
    initialShape: CropShape = CropShape.RECTANGLE,
) {
    var imageUri: Uri? by mutableStateOf(null)

    var croppedBitmap: Bitmap? by mutableStateOf(null)

    var isVisible: Boolean by mutableStateOf(false)

    var aspectRatio: CropAspectRatio by mutableStateOf(initialAspectRatio)
    var cropShape: CropShape by mutableStateOf(initialShape)

    internal var cropRect   by mutableStateOf(Rect.Zero)
    internal var canvasSize by mutableStateOf(Size.Zero)

    fun open(uri: Uri) {
        imageUri      = uri
        croppedBitmap = null
        isVisible     = true
        cropRect      = Rect.Zero
    }

    fun dismiss() {
        isVisible = false
    }
}
@Composable
fun rememberImageCropperState(
    initialAspectRatio: CropAspectRatio = CropAspectRatio.Free,
    initialShape: CropShape = CropShape.RECTANGLE,
): ImageCropperState = remember {
    ImageCropperState(
        initialAspectRatio = initialAspectRatio,
        initialShape       = initialShape,
    )
}
