package com.example.af.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.af.framework.ui.BaseActivity
import com.example.af.databinding.ActivityMainBinding
import com.example.af.databinding.ItemAppBinding
import com.example.af.main.viewmodel.AppItem
import com.example.af.main.viewmodel.MainViewModel

class MainActivity : BaseActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        initView()
        initObserver()
        viewModel.load(baseContext.packageManager)
    }

    private fun initView() {
        appAdapter = AppAdapter(mutableListOf()) {
            finish()
        }
        viewBinding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        viewBinding.recyclerView.adapter = appAdapter

        viewBinding.editText.doOnTextChanged { text, _, _, _ ->
            println("text: $text")
            viewModel.findApp(text.toString())
        }
        viewBinding.editText.requestFocus()
    }

    private fun initObserver() {
        viewModel.apps.observe(this) {
            if (viewBinding.editText.text.toString().isNotEmpty()) {
                viewModel.findApp(viewBinding.editText.text.toString())
            } else {
                appAdapter.setApps(it.subList(0, it.size.coerceAtMost(8)))
            }
        }
        viewModel.findApps.observe(this) {
            appAdapter.setApps(it.subList(0, it.size.coerceAtMost(8)))
        }
    }
}

class AppAdapter(
    private val orders: MutableList<AppItem>,
    private val launchApp: () -> Unit
) : RecyclerView.Adapter<AppRender>() {

    fun setApps(orders: List<AppItem>) {
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
    fun render(appItem: AppItem, position: Int) {
        viewBinding.name.text = appItem.appName
        viewBinding.imageView.setImageDrawable(appItem.packageInfo.applicationInfo.loadIcon(viewBinding.root.context.packageManager))

        viewBinding.root.setOnClickListener {
            viewBinding.root.context.startActivity(
                viewBinding.root.context.packageManager.getLaunchIntentForPackage(appItem.packageInfo.packageName)
            )
            launchApp.invoke()
        }
    }
}