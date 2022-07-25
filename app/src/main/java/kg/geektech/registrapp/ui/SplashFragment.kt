package kg.geektech.registrapp.ui

import android.view.LayoutInflater
import androidx.navigation.Navigation
import kg.geektech.registrapp.App
import kg.geektech.registrapp.R
import kg.geektech.registrapp.base.BaseFragment
import kg.geektech.registrapp.databinding.FragmentSplashBinding


class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override fun inflateVB(inflater: LayoutInflater): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater)
    }

    override fun initView() {
        val currentUser = App().getFirebaseAuth().currentUser
        if (currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.mainFragment)
        }
    }

    override fun initListener() {
        binding.loginBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.registerFragment)
        }
    }


}