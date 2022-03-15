package com.example.af.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.af.R
import com.example.af.databinding.ActivityMainBinding
import com.example.af.db.House
import com.example.af.main.viewmodel.MainViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val houseAdapter: HouseAdapter = HouseAdapter(mutableListOf(), this::delete, this::remark)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    viewBinding.textView.text = it.status
                    viewBinding.textView.isVisible = it.status.isNotEmpty()
                }
            }
        }

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.adapter = houseAdapter

        viewModel.houses.observe(this) {
            houseAdapter.setHouse(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_house, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.add -> {
                showAddDialog()
                true
            }
            R.id.refresh -> {
                viewModel.refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun remark(house: House) {
        val inputServer = TextInputEditText(this)
        inputServer.setText(house.other2 ?: "")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("备注信息").setView(inputServer)
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("更新") { dialog, _ ->
            val text = inputServer.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.remark(house, text)
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun delete(house: House) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("是否删除此房源？")
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("删除") { dialog, _ ->
            viewModel.delete(house)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showAddDialog() {
        val inputServer = TextInputEditText(this)
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("填加房子").setView(inputServer)
            .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("添加") { dialog, _ ->
            val id = inputServer.text.toString().trim()
            if (id.isNotEmpty()) {
                viewModel.insertHouse(id)
            }
            dialog.dismiss()
        }
        builder.show()
    }
}