package com.madiwist.twitch.core.presentation.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.CropSquare
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun ImageCropper(
    state: ImageCropperState,
    onCropComplete: (Bitmap) -> Unit,
    onDismiss: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = state.isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Dialog(
            onDismissRequest = {
                state.dismiss()
                onDismiss()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
        ) {
            ImageCropperContent(
                state = state,
                onCropComplete = { bmp ->
                    state.croppedBitmap = bmp
                    state.isVisible = false
                    onCropComplete(bmp)
                },
                onDismiss = {
                    state.dismiss()
                    onDismiss()
                },
            )
        }
    }
}

@Composable
fun rememberImageCropperLauncher(
    state: ImageCropperState,
    onCropComplete: (Bitmap) -> Unit,
    onDismiss: () -> Unit = {},
): () -> Unit {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { state.open(it) }
    }

    ImageCropper(
        state = state,
        onCropComplete = onCropComplete,
        onDismiss = onDismiss,
    )

    return { galleryLauncher.launch("image/*") }
}

@Composable
private fun ImageCropperContent(
    state: ImageCropperState,
    onCropComplete: (Bitmap) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Load bitmap from URI
    var sourceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(state.imageUri) {
        sourceBitmap = withContext(Dispatchers.IO) {
            state.imageUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }
        }
    }

    // Colors
    val overlayColor   = Color(0x99000000)
    val handleColor    = Color(0xFF03B100)   // app green
    val borderColor    = Color(0xFFFFFFFF)
    val gridLineColor  = Color(0x55FFFFFF)
    val handleSize     = 12.dp
    val borderWidth    = 2.dp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            CropperTopBar(
                onClose = onDismiss,
                onConfirm = {
                    val bmp = sourceBitmap ?: return@CropperTopBar
                    scope.launch {
                        val cropped = withContext(Dispatchers.Default) {
                            cropBitmap(bmp, state)
                        }
                        onCropComplete(cropped)
                    }
                },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                sourceBitmap?.let { bmp ->
                    CropCanvas(
                        bitmap = bmp,
                        state = state,
                        overlayColor = overlayColor,
                        handleColor = handleColor,
                        borderColor = borderColor,
                        gridLineColor = gridLineColor,
                        handleSize = handleSize,
                        borderWidth = borderWidth,
                    )
                }
            }

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                CropperBottomBar(state = state)
            }
        }
    }
}

@Composable
private fun CropperTopBar(
    onClose: () -> Unit,
    onConfirm: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClose,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFF2C2C2C),
                contentColor = Color.White,
            ),
        ) {
            Icon(Icons.Outlined.Close, contentDescription = "Cancel crop")
        }

        Text(
            text = "Crop Image",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )

        IconButton(
            onClick = onConfirm,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color(0xFF03B100),
                contentColor = Color.White,
            ),
        ) {
            Icon(Icons.Outlined.Check, contentDescription = "Confirm crop")
        }
    }
}

@Composable
private fun CropperBottomBar(state: ImageCropperState) {
    val ratios = listOf(
        CropAspectRatio.Free,
        CropAspectRatio.Square,
        CropAspectRatio.Ratio4x3,
        CropAspectRatio.Ratio16x9,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .navigationBarsPadding()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Shape toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            ShapeToggleButton(
                icon = {
                    Icon(
                        Icons.Outlined.CropSquare,
                        contentDescription = "Rectangle",
                        tint = if (state.cropShape == CropShape.RECTANGLE) Color(0xFF03B100)
                        else Color.Gray,
                    )
                },
                selected = state.cropShape == CropShape.RECTANGLE,
                onClick = { state.cropShape = CropShape.RECTANGLE },
                label = "Rect",
            )
            Spacer(Modifier.width(16.dp))
            ShapeToggleButton(
                icon = {
                    Icon(
                        Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Circle",
                        tint = if (state.cropShape == CropShape.CIRCLE) Color(0xFF03B100)
                        else Color.Gray,
                    )
                },
                selected = state.cropShape == CropShape.CIRCLE,
                onClick = {
                    state.cropShape = CropShape.CIRCLE
                    // Circle forces 1:1
                    state.aspectRatio = CropAspectRatio.Square
                },
                label = "Circle",
            )
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ratios.forEach { ratio ->
                val isSelected = state.aspectRatio == ratio
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        state.aspectRatio = ratio
                        // Reset crop rect so it recalculates for the new ratio
                        state.cropRect = Rect.Zero
                    },
                    label = {
                        Text(
                            text = ratio.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Outlined.Crop, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF03B100),
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = Color(0xFF2C2C2C),
                        labelColor = Color.LightGray,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ShapeToggleButton(
    icon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Color(0xFF2A3D2A) else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(36.dp)) { icon() }
        Text(label, color = if (selected) Color(0xFF03B100) else Color.Gray, fontSize = 11.sp)
    }
}

@Composable
private fun CropCanvas(
    bitmap: Bitmap,
    state: ImageCropperState,
    overlayColor: Color,
    handleColor: Color,
    borderColor: Color,
    gridLineColor: Color,
    handleSize: Dp,
    borderWidth: Dp,
) {
    // Which crop-rect handle (or body) is being dragged
    var dragTarget by remember { mutableStateOf(DragTarget.NONE) }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(bitmap, state.aspectRatio, state.cropShape) {
                detectTransformGestures(panZoomLock = false) { centroid, pan, _, _ ->
                    val canvasSize = state.canvasSize
                    val cropRect   = state.cropRect

                    // Initialise crop rect on very first gesture if draw() hasn't fired yet
                    if (cropRect == Rect.Zero && canvasSize != Size.Zero) {
                        state.cropRect = defaultCropRect(canvasSize, state.aspectRatio)
                        return@detectTransformGestures
                    }

                    // Latch which target we're dragging at the start of each gesture sequence
                    if (dragTarget == DragTarget.NONE) {
                        dragTarget = pickDragTarget(centroid, cropRect, handleSize.toPx())
                    }

                    when (dragTarget) {
                        DragTarget.MOVE -> {
                            val newLeft = (cropRect.left + pan.x).coerceIn(0f, canvasSize.width  - cropRect.width)
                            val newTop  = (cropRect.top  + pan.y).coerceIn(0f, canvasSize.height - cropRect.height)
                            state.cropRect = Rect(
                                offset = Offset(newLeft, newTop),
                                size   = cropRect.size,
                            )
                        }
                        DragTarget.TL -> state.cropRect = resizeCropRect(cropRect, pan, DragTarget.TL, state.aspectRatio, canvasSize)
                        DragTarget.TR -> state.cropRect = resizeCropRect(cropRect, pan, DragTarget.TR, state.aspectRatio, canvasSize)
                        DragTarget.BL -> state.cropRect = resizeCropRect(cropRect, pan, DragTarget.BL, state.aspectRatio, canvasSize)
                        DragTarget.BR -> state.cropRect = resizeCropRect(cropRect, pan, DragTarget.BR, state.aspectRatio, canvasSize)
                        DragTarget.NONE -> Unit
                    }
                }
            }
            .pointerInput(Unit) {
                // Reset drag target when all fingers lift
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.all { !it.pressed }) {
                            dragTarget = DragTarget.NONE
                        }
                    }
                }
            },
    ) {
        if (state.canvasSize != size) state.canvasSize = size

        if (state.cropRect == Rect.Zero) {
            state.cropRect = defaultCropRect(size, state.aspectRatio)
        }

        val cropRect = state.cropRect

        val imgBmp   = bitmap.asImageBitmap()
        val imgW     = bitmap.width.toFloat()
        val imgH     = bitmap.height.toFloat()
        val fitScale = min(size.width / imgW, size.height / imgH)
        val drawW    = imgW * fitScale
        val drawH    = imgH * fitScale
        val drawLeft = (size.width  - drawW) / 2f
        val drawTop  = (size.height - drawH) / 2f

        drawImage(
            image     = imgBmp,
            dstOffset = androidx.compose.ui.unit.IntOffset(drawLeft.roundToInt(), drawTop.roundToInt()),
            dstSize   = androidx.compose.ui.unit.IntSize(drawW.roundToInt(), drawH.roundToInt()),
        )

        drawCropOverlay(
            cropRect     = cropRect,
            overlayColor = overlayColor,
            shape        = state.cropShape,
        )

        drawGrid(cropRect = cropRect, lineColor = gridLineColor, strokeWidth = 1.dp.toPx())

        val strokePx = borderWidth.toPx()
        if (state.cropShape == CropShape.CIRCLE) {
            drawCircle(
                color  = borderColor,
                radius = cropRect.width / 2f,
                center = cropRect.center,
                style  = Stroke(width = strokePx),
            )
        } else {
            drawRect(
                color   = borderColor,
                topLeft = cropRect.topLeft,
                size    = cropRect.size,
                style   = Stroke(width = strokePx),
            )
        }

        if (state.cropShape == CropShape.RECTANGLE) {
            drawCornerHandles(
                cropRect    = cropRect,
                color       = handleColor,
                handleSize  = handleSize.toPx(),
                strokeWidth = strokePx * 2,
            )
        }
    }
}
private fun DrawScope.drawCropOverlay(
    cropRect: Rect,
    overlayColor: Color,
    shape: CropShape,
) {
    if (shape == CropShape.CIRCLE) {
        val path = Path().apply {
            fillType = PathFillType.EvenOdd
            addRect(Rect(Offset.Zero, size))
            addOval(cropRect)
        }
        drawPath(path, overlayColor)
    } else {
        // Top band
        drawRect(
            color   = overlayColor,
            topLeft = Offset.Zero,
            size    = Size(size.width, cropRect.top),
        )
        // Bottom band
        drawRect(
            color   = overlayColor,
            topLeft = Offset(0f, cropRect.bottom),
            size    = Size(size.width, size.height - cropRect.bottom),
        )
        // Left strip (between top and bottom bands)
        drawRect(
            color   = overlayColor,
            topLeft = Offset(0f, cropRect.top),
            size    = Size(cropRect.left, cropRect.height),
        )
        // Right strip (between top and bottom bands)
        drawRect(
            color   = overlayColor,
            topLeft = Offset(cropRect.right, cropRect.top),
            size    = Size(size.width - cropRect.right, cropRect.height),
        )
    }
}

private fun DrawScope.drawGrid(cropRect: Rect, lineColor: Color, strokeWidth: Float) {
    val thirds = 3
    for (i in 1 until thirds) {
        val x = cropRect.left + cropRect.width  * i / thirds
        val y = cropRect.top  + cropRect.height * i / thirds
        drawLine(lineColor, Offset(x, cropRect.top),  Offset(x, cropRect.bottom), strokeWidth)
        drawLine(lineColor, Offset(cropRect.left, y), Offset(cropRect.right, y),  strokeWidth)
    }
}

private fun DrawScope.drawCornerHandles(
    cropRect: Rect,
    color: Color,
    handleSize: Float,
    strokeWidth: Float,
) {
    val corners = listOf(
        cropRect.topLeft, cropRect.topRight,
        cropRect.bottomLeft, cropRect.bottomRight,
    )
    val isTop    = listOf(true, true, false, false)
    val isLeft   = listOf(true, false, true, false)

    corners.forEachIndexed { i, corner ->
        val hx = if (isLeft[i]) 1f else -1f
        val hy = if (isTop[i]) 1f else -1f
        // horizontal arm
        drawLine(color, corner, Offset(corner.x + hx * handleSize, corner.y), strokeWidth)
        // vertical arm
        drawLine(color, corner, Offset(corner.x, corner.y + hy * handleSize), strokeWidth)
    }
}

private enum class DragTarget { NONE, MOVE, TL, TR, BL, BR }

private fun pickDragTarget(point: Offset, rect: Rect, threshold: Float): DragTarget {
    fun near(a: Offset, b: Offset) = (a - b).getDistance() < threshold * 2.5f
    return when {
        near(point, rect.topLeft)     -> DragTarget.TL
        near(point, rect.topRight)    -> DragTarget.TR
        near(point, rect.bottomLeft)  -> DragTarget.BL
        near(point, rect.bottomRight) -> DragTarget.BR
        rect.contains(point)          -> DragTarget.MOVE
        else                          -> DragTarget.NONE
    }
}

private fun defaultCropRect(canvasSize: Size, ratio: CropAspectRatio): Rect {
    val padding = canvasSize.width * 0.1f
    val maxW    = canvasSize.width  - padding * 2
    val maxH    = canvasSize.height - padding * 2
    val r       = ratio.ratio()
    val (w, h)  = if (r == null) {
        maxW to maxH
    } else {
        if (maxW / r <= maxH) maxW to maxW / r else maxH * r to maxH
    }
    val left = (canvasSize.width  - w) / 2f
    val top  = (canvasSize.height - h) / 2f
    return Rect(Offset(left, top), Size(w, h))
}

private fun resizeCropRect(
    rect: Rect,
    pan: Offset,
    target: DragTarget,
    ratio: CropAspectRatio,
    canvasSize: Size,
    minSize: Float = 80f,
): Rect {
    var l = rect.left
    var t = rect.top
    var r = rect.right
    var b = rect.bottom

    when (target) {
        DragTarget.TL -> { l += pan.x; t += pan.y }
        DragTarget.TR -> { r += pan.x; t += pan.y }
        DragTarget.BL -> { l += pan.x; b += pan.y }
        DragTarget.BR -> { r += pan.x; b += pan.y }
        else -> {}
    }

    // Clamp to canvas
    l = l.coerceIn(0f, r - minSize)
    t = t.coerceIn(0f, b - minSize)
    r = r.coerceIn(l + minSize, canvasSize.width)
    b = b.coerceIn(t + minSize, canvasSize.height)

    // Enforce aspect ratio (drive off width)
    val ar = ratio.ratio()
    if (ar != null) {
        val w = r - l
        val h = w / ar
        when (target) {
            DragTarget.TL, DragTarget.BL -> b = t + h
            else -> b = t + h
        }
        b = b.coerceAtMost(canvasSize.height)
    }

    return Rect(Offset(l, t), Size(r - l, b - t))
}
private fun cropBitmap(source: Bitmap, state: ImageCropperState): Bitmap {
    val canvasSize = state.canvasSize
    val cropRect   = state.cropRect

    if (canvasSize == Size.Zero || cropRect == Rect.Zero) return source

    val imgW     = source.width.toFloat()
    val imgH     = source.height.toFloat()
    // Image is always statically fitted — no imageScale / imageOffset
    val fitScale = min(canvasSize.width / imgW, canvasSize.height / imgH)
    val drawW    = imgW * fitScale
    val drawH    = imgH * fitScale
    val drawLeft = (canvasSize.width  - drawW) / 2f
    val drawTop  = (canvasSize.height - drawH) / 2f

    // Map canvas crop rect → source bitmap pixel coordinates
    val srcLeft  = ((cropRect.left   - drawLeft) / fitScale).coerceIn(0f, imgW).roundToInt()
    val srcTop   = ((cropRect.top    - drawTop)  / fitScale).coerceIn(0f, imgH).roundToInt()
    val srcRight = ((cropRect.right  - drawLeft) / fitScale).coerceIn(0f, imgW).roundToInt()
    val srcBot   = ((cropRect.bottom - drawTop)  / fitScale).coerceIn(0f, imgH).roundToInt()

    val srcW = (srcRight - srcLeft).coerceAtLeast(1)
    val srcH = (srcBot   - srcTop).coerceAtLeast(1)

    val cropped = Bitmap.createBitmap(source, srcLeft, srcTop, srcW, srcH)
    return if (state.cropShape == CropShape.CIRCLE) applyCircleMask(cropped) else cropped
}

private fun applyCircleMask(src: Bitmap): Bitmap {
    val size   = min(src.width, src.height)
    val output = createBitmap(size, size)
    val canvas = android.graphics.Canvas(output)
    val paint  = Paint(Paint.ANTI_ALIAS_FLAG)
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(src, ((size - src.width) / 2f), ((size - src.height) / 2f), paint)
    return output
}
