package com.af.framework.ui.widget

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.lifecycle.*

/**
 * 能感知当前activity生命周期的FrameLayout
 */
open class LinearLayoutLifecycle @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {
    private var layoutLifecycleDelegate = LayoutLifecycleDelegate()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutLifecycleDelegate.onAttachedToWindow(context, this, this)
    }
}