package kg.geektech.registrapp.ui.login


import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kg.geektech.registrapp.App
import kg.geektech.registrapp.R
import kg.geektech.registrapp.base.BaseFragment
import kg.geektech.registrapp.databinding.FragmentLogInBinding


class LogInFragment : BaseFragment<FragmentLogInBinding>() {

    private var gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(R.string.default_web_client_id.toString()).requestEmail().build()


    override fun inflateVB(inflater: LayoutInflater): FragmentLogInBinding {
       return FragmentLogInBinding.inflate(inflater)
    }

    override fun initView() {
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("ResourceAsColor")
            override fun afterTextChanged(s: Editable?) {
                binding.btnLogIn.setBackgroundColor(R.color.custom_yellow)
            }
        })
    }

    override fun initListener() = with (binding) {
        btnLogIn.setOnClickListener {
            emailAndPasswordValidation()
        }

        btnFacebook.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.facebookAuthFragment)
        }

        btnGoogle.setOnClickListener {
            val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            launcher.launch(signInIntent)

        }

        tvGetRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.registerFragment)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d("ololo", "handleSignInResult: ${account.displayName}")
            firebaseAuthWithGoogle(account.idToken)
            Navigation.findNavController(requireView()).navigate(R.id.mainFragment)

        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        App().getFirebaseAuth().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    Log.w("ololo", "Login successful")
                    Navigation.findNavController(requireView()).navigate(R.id.mainFragment)
                } else {
                    Log.w("ololo", "Login failure" + it.exception!!.message)
                }
            }
    }

    private fun emailAndPasswordValidation() = with(binding) {
        if (email.text.isEmpty()) {
            email.error = "Адрес почты пустой"
        }
        if (password.text.isEmpty()) {
            password.error = "Введите пароль"
        }
    }

    private var launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("ololo", "launcher $task.toString()")
            handleSignInResult(task)
        }
}