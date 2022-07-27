package com.af.framework.ui.widget

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 带有生命周期感知的Layout
 * Created by mah on 2022/7/26.
 */
class LayoutLifecycleDelegate {
    private var _lifecycleOwner: LifecycleOwner? = null

    fun onAttachedToWindow(context: Context, view: View, defaultLifecycleObserver: DefaultLifecycleObserver) {
        val fragment = try {
            view.findFragment<Fragment>()
        } catch (e: IllegalStateException) {
            null
        }
        _lifecycleOwner = fragment?.viewLifecycleOwner ?: getLifecycleOwner(context)
                ?: throw RuntimeException("current context is not LifecycleOwner")
        _lifecycleOwner?.lifecycle?.addObserver(defaultLifecycleObserver)
        println("ActivityLifecycleFrameLayout fragment: $fragment")
    }

    fun removeLifecycleObserver(defaultLifecycleObserver: DefaultLifecycleObserver) {
        _lifecycleOwner?.lifecycle?.removeObserver(defaultLifecycleObserver)
    }

    private fun getLifecycleOwner(context: Context): LifecycleOwner? {
        if (context is LifecycleOwner) return context
        if (context is ContextWrapper) {
            val c = context.baseContext
            if (c is LifecycleOwner) {
                return c
            }
        }
        return null
    }
}