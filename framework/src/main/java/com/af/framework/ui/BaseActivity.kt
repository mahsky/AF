package com.af.framework.ui

import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * Created by mah on 2022/3/22.
 */
abstract class BaseActivity : AppCompatActivity() {
}

fun AppCompatActivity.darkUi() = run {
    ImmersionBar.with(this).statusBarDarkFont(true).init()
}

fun AppCompatActivity.lightUi() = run {
    ImmersionBar.with(this).statusBarDarkFont(false).init()
}