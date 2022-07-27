package com.example.af.main.ui

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.af.R
import com.example.af.main.viewmodel.MainViewModel

/**
 * Created by mah on 2022/7/26.
 */
class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel: MainViewModel by activityViewModels()
    private val viewModel2: MainViewModel by viewModels()
}