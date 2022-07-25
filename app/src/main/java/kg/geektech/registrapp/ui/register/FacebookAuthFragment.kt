package kg.geektech.registrapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import kg.geektech.registrapp.App
import kg.geektech.registrapp.ui.main.MainFragment


class FacebookAuthFragment : RegisterFragment() {
    private lateinit var callbackManager: CallbackManager
    private val email = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()

    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("huh", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        activity?.let {
            App().getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        Log.d("huh", "signInWithCredential:success")
                        val user = task.result.user
                        if (user != null) {
                            App().getFirebaseAuth().updateCurrentUser(user)
                        }
                        updateUser(user)

                    } else {
                        Log.d("huh", "signInWithCredential:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun updateUser(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(requireContext(), "Hi ${user.displayName}", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), MainFragment::class.java)
            startActivity(intent)
        }
    }

    override fun initView() {
        with(LoginManager) {
            getInstance().logInWithReadPermissions(requireActivity(), listOf(email))
            getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.d("letsSee", "Facebook token: " + result.accessToken.token)
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Log.d("letsSee", "Facebook onCancel.")

                }

                override fun onError(error: FacebookException) {
                    Log.d("letsSee", "Facebook onError.")

                }
            })

        }
    }
}
