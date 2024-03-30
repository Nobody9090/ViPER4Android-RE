package com.aam.viper4android.ui.component

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewRootForInspector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.popup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.launch

private class BottomSheetWindow(
    private val composeView: View,
    parentCompositionContext: CompositionContext,
    private var onDismissRequest: () -> Unit,
    private val content: @Composable () -> Unit
) : AbstractComposeView(composeView.context), ViewTreeObserver.OnGlobalLayoutListener, ViewRootForInspector {

    private var backCallback: Any? = null

    init {
        id = android.R.id.content
        // Set up view owners
        setViewTreeLifecycleOwner(composeView.findViewTreeLifecycleOwner())
        setViewTreeViewModelStoreOwner(composeView.findViewTreeViewModelStoreOwner())
        setViewTreeSavedStateRegistryOwner(composeView.findViewTreeSavedStateRegistryOwner())
//        setTag(androidx.compose.ui.R.id.compose_view_saveable_id_tag, "Popup:$saveId")
        // Enable children to draw their shadow by not clipping them
        clipChildren = false
    }

    private val windowManager = composeView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val displayWidth: Int
        get() = context.resources.displayMetrics.widthPixels

    private val params: WindowManager.LayoutParams =
        WindowManager.LayoutParams().apply {
            // Position bottom sheet from the bottom of the screen
            gravity = Gravity.BOTTOM or Gravity.START
            // Application panel window
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            // Fill up the entire app view
            width = displayWidth
            height = WindowManager.LayoutParams.MATCH_PARENT

            // Format of screen pixels
            format = PixelFormat.TRANSLUCENT
            // Title used as fallback for a11y services
            // TODO: Provide bottom sheet window resource
            title = composeView.context.resources.getString(
                androidx.compose.ui.R.string.default_popup_window_title
            )
            // Get the Window token from the parent view
            token = composeView.applicationWindowToken

            // Flags specific to modal bottom sheet.
            flags = flags and (
                    WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES or
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                    ).inv()

            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            // Security flag
            flags = flags and (WindowManager.LayoutParams.FLAG_SECURE.inv())

            // Focusable
            flags = flags and (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv())
        }

    init {
        setParentCompositionContext(parentCompositionContext)
    }

    @Composable
    override fun Content() {
        content()
    }

    fun show() {
        windowManager.addView(this, params)
    }

    fun dismiss() {
        setViewTreeLifecycleOwner(null)
        setViewTreeSavedStateRegistryOwner(null)
        composeView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        windowManager.removeViewImmediate(this)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyDispatcherState == null) {
                return super.dispatchKeyEvent(event)
            }
            if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) {
                val state = keyDispatcherState
                state?.startTracking(event, this)
                return true
            } else if (event.action == KeyEvent.ACTION_UP) {
                val state = keyDispatcherState
                if (state != null && state.isTracking(event) && !event.isCanceled) {
                    onDismissRequest()
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        maybeRegisterBackCallback()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        maybeUnregisterBackCallback()
    }

    private fun maybeRegisterBackCallback() {
        if (Build.VERSION.SDK_INT < 33) {
            return
        }
        if (backCallback == null) {
            backCallback = OnBackInvokedCallback(onDismissRequest)
        }
        if (backCallback is OnBackInvokedCallback) {
            findOnBackInvokedDispatcher()?.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_OVERLAY,
                backCallback as OnBackInvokedCallback
            )
        }
    }

    private fun maybeUnregisterBackCallback() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (backCallback is OnBackInvokedCallback) {
                findOnBackInvokedDispatcher()?.unregisterOnBackInvokedCallback(backCallback as OnBackInvokedCallback)
            }
        }
        backCallback = null
    }

    override fun onGlobalLayout() {
        // No-op
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        // Do nothing. ViewRootImpl will call this method attempting to set the layout direction
        // from the context's locale, but we have one already from the parent composition.
    }

    // Sets the "real" layout direction for our content that we obtain from the parent composition.
    fun superSetLayoutDirection(layoutDirection: LayoutDirection) {
        val direction = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
        super.setLayoutDirection(direction)
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismissRequest: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissSheet = if (visible) {
            Modifier
                .pointerInput(onDismissRequest) {
                    detectTapGestures {
                        onDismissRequest()
                    }
                }
                .clearAndSetSemantics {}
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissSheet)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}

class BottomSheetState(
    val density: Density,
) {

    val isVisible: Boolean
        get() = true

    val shouldBeVisible: Boolean
        get() = true

    var offset = 0f

    suspend fun show() {

    }

    suspend fun hide() {

    }
}

@Composable
@ExperimentalMaterial3Api
internal fun rememberBottomSheetState(): BottomSheetState {
    val density = LocalDensity.current
    return remember(
//        skipPartiallyExpanded, confirmValueChange,
//        saver = SheetState.Saver(
//            skipPartiallyExpanded = skipPartiallyExpanded,
//            confirmValueChange = confirmValueChange,
//            density = density
//        )
    ) {
        BottomSheetState(
            density
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: BottomSheetState = rememberBottomSheetState(),
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    val view = LocalView.current
    val layoutDirection = LocalLayoutDirection.current

    val compositionContext = rememberCompositionContext()

    val scope = rememberCoroutineScope()
    val animateToDismiss: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest() }
    }

    val bottomSheetWindow = remember(view, compositionContext) {
        BottomSheetWindow(view, compositionContext, onDismissRequest = animateToDismiss) {
            Box(
                Modifier
                    .semantics { this.popup() }
                    .windowInsetsPadding(windowInsets)
                    .then(
                        // TODO(b/290893168): Figure out a solution for APIs < 30.
                        if (Build.VERSION.SDK_INT >= 33)
                            Modifier.imePadding()
                        else Modifier
                    )
            ) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val fullHeight = constraints.maxHeight
                    var sheetHeight by remember { mutableIntStateOf(fullHeight) }

                    LaunchedEffect(sheetState, fullHeight, sheetHeight) {
                        println("fullHeight: $fullHeight, sheetHeight: $sheetHeight")
                        sheetState.offset = (fullHeight - sheetHeight).toFloat()
                    }

                    Scrim(
                        color = scrimColor,
                        onDismissRequest = animateToDismiss,
                        visible = sheetState.shouldBeVisible
                    )
                    Surface(
                        modifier = modifier
                            .fillMaxWidth()
                            .offset { IntOffset(0, sheetState.offset.toInt()) }
                            .onSizeChanged { newSize -> sheetHeight = newSize.height },
                        shape = shape,
                        color = containerColor,
                        contentColor = contentColor,
                        tonalElevation = tonalElevation,
                    ) {
                        Column(Modifier.fillMaxWidth()) {
                            content()
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(bottomSheetWindow) {
        bottomSheetWindow.show()
        bottomSheetWindow.superSetLayoutDirection(layoutDirection)
        onDispose {
            bottomSheetWindow.disposeComposition()
            bottomSheetWindow.dismiss()
        }
    }

    LaunchedEffect(sheetState) {
        sheetState.show()
    }
}