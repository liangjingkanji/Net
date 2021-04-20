package com.drake.net.sample.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drake.net.sample.R
import com.drake.net.sample.databinding.FragmentViewModelRequestBinding
import com.drake.net.sample.model.UserViewModel
import kotlinx.android.synthetic.main.fragment_view_model_request.*

class ViewModelRequestFragment : Fragment(R.layout.fragment_view_model_request) {


    val userViewModel: UserViewModel by viewModels() // 创建ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // 绑定DataBinding
        val bind = DataBindingUtil.bind<FragmentViewModelRequestBinding>(view)!!
        bind.lifecycleOwner = this
        bind.m = userViewModel

        // 动作开始拉取服务器数据
        btn_fetch_userinfo.setOnClickListener {
            userViewModel.fetchUserInfo()
        }
    }
}