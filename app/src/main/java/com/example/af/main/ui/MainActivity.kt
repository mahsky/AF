package com.example.af.main.ui

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.af.framework.ui.BaseActivity
import com.example.af.databinding.ActivityMainBinding
import com.example.af.databinding.ItemAppBinding
import com.example.af.main.viewmodel.MainViewModel
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var appAdapter: AppAdapter
    private val apps = mutableListOf<App>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        initView()

        lifecycleScope.launch {
            apps.clear()
            apps.addAll(getApps(baseContext.packageManager))
            apps.forEach {
                println("=: ${it.appName} ${it.letterAppName}")
            }
            appAdapter.setApps(apps.subList(0, 8))
            if (viewBinding.editText.text.toString().isNotEmpty()) {
                findApp(viewBinding.editText.text.toString())
            }
        }

        viewBinding.editText.doOnTextChanged { text, start, before, count ->
            println("text: $text")
            findApp(text)
        }
        viewBinding.editText.requestFocus()
    }

    private fun findApp(text: CharSequence?) {
        text ?: return
        val tempApps = mutableListOf<App>()
        if (text.isEmpty()) {
            appAdapter.setApps(tempApps)
            return
        }
        apps.forEach { app ->
            app.sort = 0
            var isContains = true
            text.toString().uppercase(Locale.ENGLISH).toCharArray().forEach { char ->
                if (!app.letterAppName.contains(char)) {
                    isContains = false
                }
                if (isContains) {
                    val index = app.letterAppName.uppercase(Locale.ENGLISH).indexOf(char)
                    if (index != -1) {
                        app.sort = app.sort + index
                    }
                }
            }
            if (isContains) {
                tempApps.add(app)
            }

            tempApps.sortBy { it.sort }
        }
        appAdapter.setApps(tempApps)
    }

    private fun initView() {
        appAdapter = AppAdapter(mutableListOf()) {
            finish()
        }
        viewBinding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        viewBinding.recyclerView.adapter = appAdapter
    }

    private suspend fun getApps(packageManager: PackageManager): List<App> = withContext(Dispatchers.IO) {
        val apps = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES)
        mutableListOf<App>().apply {
            apps.forEach {
                if (packageManager.getLaunchIntentForPackage(it.packageName) != null) {
                    val appName = it.applicationInfo.loadLabel(packageManager).toString()
                    val pinyin = Pinyin.toPinyin(appName, "")
                    add(App(it, pinyin, appName))
                }
            }
        }
    }
}

class AppAdapter(
    private val orders: MutableList<App>,
    private val launchApp: () -> Unit
) : RecyclerView.Adapter<AppRender>() {

    fun setApps(orders: List<App>) {
        this.orders.clear()
        this.orders.addAll(orders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppRender {
        val viewBinding = ItemAppBinding.inflate(LayoutInflater.from(parent.context))
        return AppRender(viewBinding, launchApp)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: AppRender, position: Int) {
        holder.render(orders[position], position)
    }
}

class AppRender(
    private val viewBinding: ItemAppBinding,
    private val launchApp: () -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun render(app: App, position: Int) {
        viewBinding.name.text = app.appName
        viewBinding.imageView.setImageDrawable(app.packageInfo.applicationInfo.loadIcon(viewBinding.root.context.packageManager))

        viewBinding.root.setOnClickListener {
            viewBinding.root.context.startActivity(
                viewBinding.root.context.packageManager.getLaunchIntentForPackage(app.packageInfo.packageName)
            )
            launchApp.invoke()
        }
    }
}

data class App(val packageInfo: PackageInfo, val letterAppName: String, val appName: String, var sort: Int = 0)