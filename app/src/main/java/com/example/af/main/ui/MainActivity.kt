package com.example.af.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.af.framework.ui.BaseActivity
import com.af.framework.utils.AppUtils
import com.af.model.AppItem
import com.example.af.databinding.ActivityMainBinding
import com.example.af.databinding.ItemAppBinding
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
            finishAndRemoveTask()
        }
        viewBinding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        viewBinding.recyclerView.adapter = appAdapter

        viewBinding.editText.doOnTextChanged { text, _, _, _ ->
            println("text: $text")
            viewModel.findApp(text.toString())
            viewBinding.clear.isVisible = text?.isNotEmpty() ?: false
        }
        viewBinding.editText.requestFocus()
        viewBinding.clear.setOnClickListener {
            viewBinding.editText.setText("")
        }
        viewBinding.exitBg.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    private fun initObserver() {
        viewModel.apps.observe(this) {
            if (viewBinding.editText.text.toString().isNotEmpty()) {
                viewModel.findApp(viewBinding.editText.text.toString())
//            } else {
//                appAdapter.setApps(it.subList(0, it.size.coerceAtMost(4)))
            }
        }
        viewModel.findApps.observe(this) {
            appAdapter.setApps(it.subList(0, it.size.coerceAtMost(4)))
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
        viewBinding.imageView.setImageDrawable(AppUtils.getAppIcon(appItem.packageName, viewBinding.root.context.packageManager))

        viewBinding.root.setOnClickListener {
            viewBinding.root.context.startActivity(
                viewBinding.root.context.packageManager.getLaunchIntentForPackage(appItem.packageName)
            )
            launchApp.invoke()
        }
    }
}