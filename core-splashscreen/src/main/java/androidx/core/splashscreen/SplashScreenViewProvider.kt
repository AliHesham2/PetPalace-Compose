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

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow

/**
 * Contains the splash screen view displayed on top of the application, used to animate from the
 * splash screen to the application.
 *
 * The splashscreen is accessible using [SplashScreenViewProvider.view], the view containing the
 * icon using [SplashScreenViewProvider.iconView] and the branding badge using
 * [SplashScreenViewProvider.brandingView].
 *
 * The application always needs to call [SplashScreenViewProvider.remove] once it's done with it.
 */
@SuppressLint("ViewConstructor")
public class SplashScreenViewProvider internal constructor(activity: Activity) {

    private val splashScreenView: ViewGroup =
        FrameLayout.inflate(activity, R.layout.splash_screen_view, null) as ViewGroup

    private val splashBrandingView: View =
        FrameLayout.inflate(activity, R.layout.splash_screen_branding, null)

    /**
     * The branding badge lives in its own window, so that its entry and exit are window
     * animations. Those are run by the system and not by the main thread of the application,
     * which is busy starting up while the splash screen is displayed: animating the badge as a
     * regular view makes it freeze.
     */
    private val brandingWindow =
        PopupWindow(
                splashBrandingView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            .apply {
                isTouchable = false
                animationStyle = R.style.Animation_SplashScreen_Branding
            }

    init {
        val content = activity.findViewById<ViewGroup>(android.R.id.content)
        (content.rootView as? ViewGroup)?.addView(splashScreenView)
        splashScreenView.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(view: View) {}

                override fun onViewDetachedFromWindow(view: View) {
                    brandingWindow.dismiss()
                }
            }
        )
    }

    /** The splash screen view, on top of the application content. */
    public val view: View
        get() = splashScreenView

    /**
     * The view containing the splashscreen icon as defined by
     * [R.attr.windowSplashScreenAnimatedIcon]
     */
    public val iconView: View
        get() = splashScreenView.findViewById(R.id.splashscreen_icon_view)

    /**
     * The badge at the bottom of the splash screen, holding the two lines defined by
     * [R.attr.splashScreenBrandingLabel] and [R.attr.splashScreenBrandingName]
     */
    public val brandingView: View
        get() = splashBrandingView

    /** Brings in the branding badge, with its entry animation. */
    internal fun showBranding() {
        if (
            splashBrandingView.visibility != View.VISIBLE ||
                !splashScreenView.isAttachedToWindow ||
                brandingWindow.isShowing
        ) {
            return
        }
        brandingWindow.showAtLocation(
            splashScreenView,
            Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
            0,
            splashScreenView.resources.getDimensionPixelSize(
                R.dimen.splashscreen_branding_margin_bottom
            ),
        )
    }

    /** Takes the branding badge away, with its exit animation. */
    internal fun hideBranding() {
        brandingWindow.dismiss()
    }

    /**
     * Remove the SplashScreen's view from the view hierarchy.
     *
     * This always needs to be called when an
     * [androidx.core.splashscreen.SplashScreen.OnExitAnimationListener] is set.
     */
    public fun remove() {
        hideBranding()
        (splashScreenView.parent as? ViewGroup)?.removeView(splashScreenView)
    }
}
