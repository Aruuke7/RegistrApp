package kg.geektech.registrapp.ui.main

import android.view.LayoutInflater
import kg.geektech.registrapp.base.BaseFragment
import kg.geektech.registrapp.databinding.FragmentMainBinding


class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun inflateVB(inflater: LayoutInflater): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater)
    }

    override fun initView() {
    }

    override fun initListener() {
    }

}