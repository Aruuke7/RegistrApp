package kg.geektech.registrapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class App : Application() {
    private lateinit var mAuth: FirebaseAuth

    fun getFirebaseAuth(): FirebaseAuth {
        mAuth = FirebaseAuth.getInstance()
        return mAuth
    }

}