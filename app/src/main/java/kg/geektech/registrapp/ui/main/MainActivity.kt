package kg.geektech.registrapp.ui.main

import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kg.geektech.registrapp.App
import kg.geektech.registrapp.R
import kg.geektech.registrapp.base.BaseActivity
import kg.geektech.registrapp.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }
    override fun initView() {
        super.initView()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        val user = App().getFirebaseAuth().currentUser
        if (user != null) {
            navController.navigate(R.id.mainFragment)
        }
    }
    override val viewModel: MainViewModel
            by lazy { ViewModelProvider(this)[MainViewModel::class.java] }



}
