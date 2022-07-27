package com.example.af.main.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.postDelayed
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.af.framework.ui.BaseActivity
import com.example.af.R
import com.example.af.databinding.ActivityMainBinding
import com.example.af.main.viewmodel.MainViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    val mainFragment  = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                /**
                 * UiState diffing：UiState 对象中的字段越多，数据流就越有可能因为其中一个字段被更新而发出。
                 * 由于视图没有 diffing 机制来了解连续发出的数据流是否相同，因此每次发出都会导致视图更新。
                 * 这意味着，可能必须要对 LiveData 使用 Flow API 或 distinctUntilChanged() 等方法来缓解这个问题。
                 */
                viewModel.uiState.map { it.name }
                    .distinctUntilChanged()
                    .collectLatest {
                        appendText(it)
                    }

                viewModel.uiState.collectLatest {
                    appendText(it.name)
                }
            }
        }

        viewBinding.textView.setOnClickListener {
            viewModel.load()
        }

        testLifecycleLayout()

    }

    private fun testLifecycleLayout() {
        viewBinding.textView.postDelayed({
            supportFragmentManager.commit {
                remove(mainFragment)
            }
        }, 3000)

        supportFragmentManager.commit {
            add(R.id.fragment_container_view, mainFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    fun appendText(text: String) {
        viewBinding.textView.text = "${viewBinding.textView.text} + $text"
    }
}