/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.core.splashscreen

import android.R.attr
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.SystemClock
import android.util.TypedValue
import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.window.SplashScreenView
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources

/**
 * Provides control over the splash screen once the application is started.
 *
 * The system splash screen (window background below API 31, platform splash screen on API 31+)
 * only shows a still frame. Once the application has drawn its first frame, an identical view
 * owned by this class takes over and plays the intro animation (the
 * `splashScreenIntroAnimatedIcon` and the branding badge), then the exit animation.
 *
 * # Usage of the `core-splashscreen` library:
 *
 * To replicate the splash screen behavior from Android 12 on older APIs the following steps need to
 * be taken:
 * 1. Create a new Theme (e.g `Theme.App.Starting`) and set its parent to `Theme.SplashScreen` or
 *    `Theme.SplashScreen.IconBackground`
 * 2. In your manifest, set the `theme` attribute of the whole `<application>` or just the starting
 *    `<activity>` to `Theme.App.Starting`
 * 3. In the `onCreate` method the starting activity, call [installSplashScreen] just before
 *    `super.onCreate()`. You also need to make sure that `postSplashScreenTheme` is set to the
 *    application's theme. Alternatively, this call can be replaced by [Activity#setTheme] if a
 *    [SplashScreen] instance isn't needed.
 *
 * ## Themes
 *
 * The library provides two themes: [R.style.Theme_SplashScreen] and
 * [R.style.Theme_SplashScreen_IconBackground]. If you wish to display a background right under your
 * icon, the later needs to be used. This ensure that the scale and masking of the icon are similar
 * to the Android 12 Splash Screen.
 *
 * `windowSplashScreenAnimatedIcon`: The splash screen icon. On API 31+ it can be an animated vector
 * drawable.
 *
 * `windowSplashScreenAnimationDuration`: Duration of the Animated Icon Animation. The value needs
 * to be > 0 if the icon is animated.
 *
 * **Note:** This has no impact on the time during which the splash screen is displayed and is only
 * used in [SplashScreenViewProvider.iconAnimationDurationMillis]. If you need to display the splash
 * screen for a longer time, you can use [SplashScreen.setKeepOnScreenCondition]
 *
 * `windowSplashScreenIconBackgroundColor`: _To be used in with
 * `Theme.SplashScreen.IconBackground`_. Sets a background color under the splash screen icon.
 *
 * `windowSplashScreenBackground`: Background color of the splash screen. Defaults to the theme's
 * `?attr/colorBackground`.
 *
 * `postSplashScreenTheme`* Theme to apply to the Activity when [installSplashScreen] is called.
 *
 * **Known incompatibilities:**
 * - On API < 31, `windowSplashScreenAnimatedIcon` cannot be animated. If you want to provide an
 *   animated icon for API 31+ and a still icon for API <31, you can do so by overriding the still
 *   icon with an animated vector drawable in `res/drawable-v31`.
 * - On API < 31, if the value of `windowSplashScreenAnimatedIcon` is an
 *   [adaptive icon](http://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)
 *   , it will be cropped and scaled. The workaround is to respectively assign
 *   `windowSplashScreenAnimatedIcon` and `windowSplashScreenIconBackgroundColor` to the values of
 *   the adaptive icon `foreground` and `background`.
 *
 * # Design
 * The splash screen icon uses the same specifications as
 * [Adaptive Icons](https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)
 * . This means that the icon needs to fit within a circle whose diameter is 2/3 the size of the
 * icon. The actual values don't really matter if you use a vector icon.
 *
 * ## Specs
 * - With icon background (`Theme.SplashScreen.IconBackground`)
 *     + Image Size: 240x240 dp
 *     + Inner Circle diameter: 160 dp
 * - Without icon background (`Theme.SplashScreen`)
 *         + Image size: 288x288 dp
 *         + Inner circle diameter: 192 dp
 *
 * _Example:_ if the full size of the image is 300dp*300dp, the icon needs to fit within a circle
 * with a diameter of 200dp. Everything outside the circle will be invisible (masked).
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreen private constructor(private val activity: Activity) {

    private var backgroundResId: Int? = null
    private var backgroundColor: Int? = null
    private var icon: Drawable? = null
    private var introIcon: Drawable? = null
    private var brandingLabel: CharSequence? = null
    private var brandingName: CharSequence? = null
    private var hasBackground: Boolean = false

    private var splashScreenWaitPredicate = KeepOnScreenCondition { false }
    private var minDurationMs = DEFAULT_MIN_DURATION_MS
    private var animationListener: OnExitAnimationListener? = null
    private var mSplashScreenViewProvider: SplashScreenViewProvider? = null
    private var introRequested = false
    private var introStarted = false
    private var introStartTime = 0L

    // A recreated activity (rotation, theme or locale change) is not a launch, so it gets no
    // splash screen.
    private val isRelaunch = activity.lastNonConfigurationInstance != null

    public companion object {

        private const val MASK_FACTOR = 2 / 3f

        private const val DEFAULT_MIN_DURATION_MS = 1500L

        /**
         * On API 31+, how long to wait for the platform splash screen to hand over before
         * starting the intro animation anyway (some launches never show a platform splash).
         */
        private const val PLATFORM_HANDOFF_TIMEOUT_MS = 500L

        /** Number of consecutive on-time frames after which the app is considered settled. */
        private const val INTRO_SETTLE_FRAMES = 5

        /** How long to wait for the app to settle before starting the intro animation anyway. */
        private const val INTRO_SETTLE_TIMEOUT_MS = 1500L

        /**
         * Creates a [SplashScreen] instance associated with this [Activity] and handles setting the
         * theme to [R.attr.postSplashScreenTheme].
         *
         * This needs to be called before [Activity.setContentView] or other view operations on the
         * root view (e.g setting flags).
         *
         * Alternatively, if a [SplashScreen] instance is not required, the theme can manually be
         * set using [Activity.setTheme].
         */
        @JvmStatic
        public fun Activity.installSplashScreen(): SplashScreen {
            val splashScreen = SplashScreen(this)
            splashScreen.install()
            return splashScreen
        }
    }

    /**
     * Sets the condition to keep the splash screen visible.
     *
     * The splash will stay visible until the intro animation is done, [minDurationMs] has elapsed
     * and the condition isn't met anymore. The condition is evaluated on every frame, so it needs
     * to be fast to avoid blocking the UI.
     *
     * @param minDurationMs Minimum duration in milliseconds to keep the splash screen visible,
     *   counted from the start of the intro animation.
     * @param condition The condition evaluated to decide whether to keep the splash screen on
     *   screen.
     */
    public fun setKeepOnScreenCondition(
        minDurationMs: Long = DEFAULT_MIN_DURATION_MS,
        condition: KeepOnScreenCondition
    ) {
        this.minDurationMs = minDurationMs
        splashScreenWaitPredicate = condition
        setupSplashScreenView()
    }

    /**
     * Sets the condition to keep the splash screen visible with the default minimum duration (1500ms).
     */
    public fun setKeepOnScreenCondition(condition: KeepOnScreenCondition) {
        setKeepOnScreenCondition(minDurationMs = DEFAULT_MIN_DURATION_MS, condition = condition)
    }

    /**
     * Sets a listener that will be called when the splashscreen is ready to be removed.
     *
     * If a listener is set, the splashscreen won't be automatically removed and the application
     * needs to manually call [SplashScreenViewProvider.remove].
     *
     * IF no listener is set, the splashscreen fades out once the intro animation is done and the
     * app is ready.
     *
     * The listener will be called on the ui thread.
     *
     * @param listener The [OnExitAnimationListener] that will be called when the splash screen is
     *   ready to be dismissed.
     * @see setKeepOnScreenCondition
     * @see OnExitAnimationListener
     * @see SplashScreenViewProvider
     */
    @SuppressWarnings("ExecutorRegistration") // Always runs on the MainThread
    public fun setOnExitAnimationListener(listener: OnExitAnimationListener) {
        animationListener = listener
        setupSplashScreenView()
    }

    private fun install() {
        val typedValue = TypedValue()
        val currentTheme = activity.theme
        if (currentTheme.resolveAttribute(R.attr.windowSplashScreenBackground, typedValue, true)) {
            backgroundResId = typedValue.resourceId
            backgroundColor = typedValue.data
        }
        if (
            currentTheme.resolveAttribute(R.attr.windowSplashScreenAnimatedIcon, typedValue, true)
        ) {
            icon = AppCompatResources.getDrawable(activity, typedValue.resourceId)
        }
        if (
            currentTheme.resolveAttribute(R.attr.splashScreenIntroAnimatedIcon, typedValue, true)
        ) {
            introIcon = AppCompatResources.getDrawable(activity, typedValue.resourceId)
        }
        if (currentTheme.resolveAttribute(R.attr.splashScreenIconSize, typedValue, true)) {
            hasBackground =
                typedValue.resourceId == R.dimen.splashscreen_icon_size_with_background
        }

        val brandingAttrs =
            currentTheme.obtainStyledAttributes(
                intArrayOf(R.attr.splashScreenBrandingLabel, R.attr.splashScreenBrandingName)
            )
        brandingLabel = brandingAttrs.getText(0)
        brandingName = brandingAttrs.getText(1)
        brandingAttrs.recycle()

        if (currentTheme.resolveAttribute(R.attr.postSplashScreenTheme, typedValue, true)) {
            val finalThemeId = typedValue.resourceId
            if (finalThemeId != 0) {
                activity.setTheme(finalThemeId)
            }
        }
    }

    /**
     * Adds our own copy of the splash screen on top of the activity content. It looks exactly
     * like the system one (window background below API 31, platform splash screen on 31+), so the
     * system can hand over to it without any visible change, and it is ours to animate.
     */
    private fun setupSplashScreenView() {
        if (isRelaunch || mSplashScreenViewProvider != null) {
            return
        }
        val splashScreenViewProvider = SplashScreenViewProvider(activity)
        mSplashScreenViewProvider = splashScreenViewProvider
        val finalBackgroundResId = backgroundResId
        val finalBackgroundColor = backgroundColor
        val splashScreenView = splashScreenViewProvider.view

        // Don't let touches reach the content below while the splash is up
        splashScreenView.isClickable = true

        if (finalBackgroundResId != null && finalBackgroundResId != Resources.ID_NULL) {
            splashScreenView.setBackgroundResource(finalBackgroundResId)
        } else if (finalBackgroundColor != null) {
            splashScreenView.setBackgroundColor(finalBackgroundColor)
        } else {
            splashScreenView.background = activity.window.decorView.background
        }

        icon?.let { displaySplashScreenIcon(splashScreenView, it) }
        displayBranding(splashScreenViewProvider.brandingView)

        if (SDK_INT >= 31) {
            PlatformHandoff31(activity) { startIntroAnimationWhenSettled(splashScreenViewProvider) }
                .install()
        }
        splashScreenView.viewTreeObserver.addOnPreDrawListener(
            object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    splashScreenView.viewTreeObserver.removeOnPreDrawListener(this)
                    // Below API 31 our view is visible from the first frame. On 31+ the platform
                    // splash still covers it until it hands over, so this is only a fallback.
                    splashScreenView.postDelayed(
                        { startIntroAnimationWhenSettled(splashScreenViewProvider) },
                        if (SDK_INT >= 31) PLATFORM_HANDOFF_TIMEOUT_MS else 0L,
                    )
                    return true
                }
            }
        )
    }

    private fun displaySplashScreenIcon(splashScreenView: View, icon: Drawable) {
        val iconView = splashScreenView.findViewById<ImageView>(R.id.splashscreen_icon_view)
        iconView.apply {
            val maskSize: Float
            if (hasBackground) {
                // If the splash screen has an icon background we need to mask both the
                // background and foreground.
                val iconBackgroundDrawable =
                    AppCompatResources.getDrawable(context, R.drawable.icon_background)

                val iconSize =
                    resources.getDimension(R.dimen.splashscreen_icon_size_with_background)
                maskSize = iconSize * MASK_FACTOR

                if (iconBackgroundDrawable != null) {
                    background = MaskedDrawable(iconBackgroundDrawable, maskSize)
                }
            } else {
                val iconSize = resources.getDimension(R.dimen.splashscreen_icon_size_no_background)
                maskSize = iconSize * MASK_FACTOR
            }
            // The intro icon replaces the still one: it looks the same until it starts
            setImageDrawable(
                if (introIcon is Animatable2) introIcon else MaskedDrawable(icon, maskSize)
            )
        }
    }

    private fun displayBranding(brandingView: View) {
        val labelView = brandingView.findViewById<TextView>(R.id.splashscreen_branding_label)
        val nameView = brandingView.findViewById<TextView>(R.id.splashscreen_branding_name)

        labelView.text = brandingLabel
        nameView.text = brandingName
        labelView.visibility = if (brandingLabel.isNullOrEmpty()) View.GONE else View.VISIBLE
        nameView.visibility = if (brandingName.isNullOrEmpty()) View.GONE else View.VISIBLE
        if (brandingLabel.isNullOrEmpty() && brandingName.isNullOrEmpty()) {
            brandingView.visibility = View.GONE
        }
    }

    /**
     * Without a [R.attr.splashScreenIntroAnimatedIcon], the intro animation runs on the main
     * thread, so it stutters if it plays while the app is still loading and laying out its first
     * screen. In that case we hold the still splash until the keep on screen condition is over and
     * a few frames in a row were delivered on time.
     */
    private fun startIntroAnimationWhenSettled(splashScreenViewProvider: SplashScreenViewProvider) {
        if (introRequested) {
            return
        }
        introRequested = true

        // An animated vector runs on the render thread, it doesn't need a calm main thread
        if (introIcon is Animatable2) {
            startIntroAnimation(splashScreenViewProvider)
            return
        }

        val refreshRate = splashScreenViewProvider.view.display?.refreshRate ?: 60f
        val frameBudgetNanos = (1.5 * 1_000_000_000L / refreshRate).toLong()
        val deadline = SystemClock.uptimeMillis() + INTRO_SETTLE_TIMEOUT_MS
        val choreographer = Choreographer.getInstance()
        choreographer.postFrameCallback(
            object : Choreographer.FrameCallback {
                var lastFrameTimeNanos = 0L
                var onTimeFrames = 0

                override fun doFrame(frameTimeNanos: Long) {
                    val isOnTime =
                        lastFrameTimeNanos != 0L &&
                            frameTimeNanos - lastFrameTimeNanos <= frameBudgetNanos
                    lastFrameTimeNanos = frameTimeNanos
                    onTimeFrames =
                        if (isOnTime && !splashScreenWaitPredicate.shouldKeepOnScreen()) {
                            onTimeFrames + 1
                        } else {
                            0
                        }
                    if (
                        onTimeFrames >= INTRO_SETTLE_FRAMES ||
                            SystemClock.uptimeMillis() >= deadline
                    ) {
                        startIntroAnimation(splashScreenViewProvider)
                    } else {
                        choreographer.postFrameCallback(this)
                    }
                }
            }
        )
    }

    private fun startIntroAnimation(splashScreenViewProvider: SplashScreenViewProvider) {
        if (introStarted) {
            return
        }
        introStarted = true
        introStartTime = SystemClock.uptimeMillis()

        // The system splash has no branding, it comes in together with the icon
        splashScreenViewProvider.showBranding()

        val animatedIcon = introIcon as? Animatable2
        if (animatedIcon != null) {
            animatedIcon.registerAnimationCallback(
                object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        animatedIcon.unregisterAnimationCallback(this)
                        dispatchOnExitAnimationWhenReady(splashScreenViewProvider)
                    }
                }
            )
            animatedIcon.start()
            return
        }

        val iconView = splashScreenViewProvider.iconView
        // Rotate and scale a cached texture instead of redrawing the masked icon every frame
        iconView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        createIconIntroAnimator(iconView).apply {
            addListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        iconView.setLayerType(View.LAYER_TYPE_NONE, null)
                        dispatchOnExitAnimationWhenReady(splashScreenViewProvider)
                    }
                }
            )
            start()
        }
    }

    private fun dispatchOnExitAnimationWhenReady(
        splashScreenViewProvider: SplashScreenViewProvider
    ) {
        val splashScreenView = splashScreenViewProvider.view
        val isMinDurationMet = SystemClock.uptimeMillis() - introStartTime >= minDurationMs
        if (splashScreenWaitPredicate.shouldKeepOnScreen() || !isMinDurationMet) {
            splashScreenView.postOnAnimation {
                dispatchOnExitAnimationWhenReady(splashScreenViewProvider)
            }
            return
        }

        val finalListener = animationListener
        animationListener = null
        splashScreenView.bringToFront()
        if (finalListener != null) {
            finalListener.onSplashScreenExit(splashScreenViewProvider)
        } else {
            defaultSplashExitAnimation(splashScreenViewProvider)
        }
    }

    /**
     * Listener to be passed in [SplashScreen.setOnExitAnimationListener].
     *
     * The listener will be called once the splash screen is ready to be removed and provides a
     * reference to a [SplashScreenViewProvider] that can be used to customize the exit animation of
     * the splash screen.
     */
    public fun interface OnExitAnimationListener {

        /**
         * Callback called when the splash screen is ready to be dismissed. The caller is
         * responsible for animating and removing splash screen using the provided
         * [splashScreenViewProvider].
         *
         * The caller **must** call [SplashScreenViewProvider.remove] once it's done with the splash
         * screen.
         *
         * @param splashScreenViewProvider An object holding a reference to the displayed splash
         *   screen.
         */
        @MainThread
        public fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider)
    }

    /**
     * Condition evaluated to check if the splash screen should remain on screen
     *
     * The splash screen will stay visible until the condition isn't met anymore. The condition is
     * evaluated on every frame, so it needs to be fast to avoid blocking the UI.
     */
    public fun interface KeepOnScreenCondition {

        /**
         * Callback evaluated on every frame once the intro animation is done. If it returns
         * `true`, the splash screen will be kept visible to hide the Activity below.
         *
         * This callback is evaluated in the main thread.
         */
        @MainThread public fun shouldKeepOnScreen(): Boolean
    }

    /**
     * On API 31+ the system draws its own splash screen. As soon as the app has drawn its first
     * frame, we remove the platform view without any animation, which reveals our identical view
     * right below it.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private class PlatformHandoff31(
        private val activity: Activity,
        private val onHandoff: () -> Unit,
    ) {
        var mDecorFitWindowInsets = true

        val hierarchyListener =
            object : ViewGroup.OnHierarchyChangeListener {
                override fun onChildViewAdded(parent: View?, child: View?) {

                    if (child is SplashScreenView) {
                        /*
                         * On API 31, the SplashScreenView sets window.setDecorFitsSystemWindows(false)
                         * when an OnExitAnimationListener is used. This also affects the application
                         * content that will be pushed up under the status bar even though it didn't
                         * requested it. And once the SplashScreenView is removed, the whole layout
                         * jumps back below the status bar. Fortunately, this happens only after the
                         * view is attached, so we have time to record the value of
                         * window.setDecorFitsSystemWindows() before the splash screen modifies it and
                         * reapply the correct value to the window.
                         */
                        mDecorFitWindowInsets = computeDecorFitsWindow(child)
                        (activity.window.decorView as ViewGroup).setOnHierarchyChangeListener(null)
                    }
                }

                override fun onChildViewRemoved(parent: View?, child: View?) {
                    // no-op
                }
            }

        fun computeDecorFitsWindow(child: SplashScreenView): Boolean {
            val inWindowInsets = WindowInsets.Builder().build()
            val outLocalInsets = Rect(Int.MIN_VALUE, Int.MIN_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

            // If setDecorFitWindowInsets is set to false, computeSystemWindowInsets
            // will return the same instance of WindowInsets passed in its parameter and
            // will set outLocalInsets to empty, so we check that both conditions are
            // filled to extrapolate the value of setDecorFitWindowInsets
            return !(inWindowInsets ===
                child.rootView.computeSystemWindowInsets(inWindowInsets, outLocalInsets) &&
                outLocalInsets.isEmpty)
        }

        fun install() {
            (activity.window.decorView as ViewGroup).setOnHierarchyChangeListener(
                hierarchyListener
            )
            activity.splashScreen.setOnExitAnimationListener { splashScreenView ->
                if (SDK_INT < 33) {
                    applyAppSystemUiTheme()
                }
                splashScreenView.remove()
                if (SDK_INT < 33) {
                    ThemeUtils.Api31.applyThemesSystemBarAppearance(
                        activity.theme,
                        activity.window.decorView,
                    )
                }
                onHandoff()
            }
        }

        /**
         * Apply the system ui related theme attribute defined in the application to override the
         * ones set on the [SplashScreenView]
         *
         * On API 31, if an OnExitAnimationListener is set, the Window layout params are only
         * applied only when the [SplashScreenView] is removed. This lead to some flickers.
         *
         * To fix this, we apply these attributes as soon as the [SplashScreenView] is visible.
         */
        @Suppress("DEPRECATION")
        private fun applyAppSystemUiTheme() {
            val tv = TypedValue()
            val theme = activity.theme
            val window = activity.window

            if (theme.resolveAttribute(attr.statusBarColor, tv, true)) {
                window.statusBarColor = tv.data
            }

            if (theme.resolveAttribute(attr.navigationBarColor, tv, true)) {
                window.navigationBarColor = tv.data
            }

            if (theme.resolveAttribute(attr.windowDrawsSystemBarBackgrounds, tv, true)) {
                if (tv.data != 0) {
                    window.addFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                } else {
                    window.clearFlags(LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                }
            }

            val decorView = window.decorView as ViewGroup
            ThemeUtils.Api31.applyThemesSystemBarAppearance(theme, decorView, tv)

            // Fix setDecorFitsSystemWindows being overridden by the SplashScreenView
            decorView.setOnHierarchyChangeListener(null)
            window.setDecorFitsSystemWindows(mDecorFitWindowInsets)
        }
    }
}

private fun defaultSplashExitAnimation(splashScreenViewProvider: SplashScreenViewProvider) {
    val splashView = splashScreenViewProvider.view

    // Clean, swift exit: no icon exit scaling, just smooth fade-out
    splashScreenViewProvider.hideBranding()
    splashView.animate()
        .alpha(0f)
        .setDuration(220L)
        .setInterpolator(DecelerateInterpolator())
        .withEndAction {
            splashScreenViewProvider.remove()
        }
        .start()
}

// Intro animation tuning, for themes without a splashScreenIntroAnimatedIcon (this fallback runs
// on the main thread, so it is not as smooth). The icon has to start and end at scale 1 /
// rotation 0, because that is how the system shows it before handing over to us.
private const val ICON_INTRO_DURATION_MS = 700L
private const val ICON_INTRO_PEAK_SCALE = 1.25f
private const val ICON_INTRO_ROTATION_DEGREES = 360f

/** Scale + rotate: the icon grows while doing a full spin, then settles back in place. */
private fun createIconIntroAnimator(icon: View): Animator {
    val scale =
        ObjectAnimator.ofPropertyValuesHolder(
                icon,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, ICON_INTRO_PEAK_SCALE, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, ICON_INTRO_PEAK_SCALE, 1f),
            )
            .apply { interpolator = AccelerateDecelerateInterpolator() }
    val rotation =
        ObjectAnimator.ofFloat(icon, View.ROTATION, 0f, ICON_INTRO_ROTATION_DEGREES).apply {
            interpolator = OvershootInterpolator(1.2f)
        }
    return AnimatorSet().apply {
        playTogether(scale, rotation)
        duration = ICON_INTRO_DURATION_MS
    }
}
