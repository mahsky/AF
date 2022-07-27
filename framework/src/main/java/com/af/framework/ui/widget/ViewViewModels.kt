package com.af.framework.ui.widget

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
public inline fun <reified VM : ViewModel> View.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = try {
    findFragment<Fragment>()
} catch (e: IllegalStateException) {
    null
}?.viewModels() ?: getActivity(context)?.viewModels() ?: throw RuntimeException("not found activity")

@MainThread
public inline fun <reified VM : ViewModel> View.activityViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = getActivity(context)?.viewModels() ?: throw RuntimeException("not found activity")

fun getActivity(c: Context): ComponentActivity? {
    var context: Context? = c
    while (context is ContextWrapper) {
        if (context is ComponentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}