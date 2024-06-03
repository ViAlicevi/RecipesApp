package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.quizapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    companion object{

        private const val TAG = "GOOG_SIGN_IN_TAG"
        fun newInstance() = MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logIn()
        signUp()
        forgotPassword()

    }

    private fun logIn(){

        binding.btnStart.setOnClickListener {

            val email = binding.edtName.text.toString().trim(){it <= ' '}
            val password = binding.edtPass.text.toString().trim(){it <= ' '}
            firebaseAuth = FirebaseAuth.getInstance()

            if (binding.edtName.text.toString().isEmpty()){
                binding.edtName.error = "Please enter your email"
            }

            if (binding.edtPass.text.toString().isEmpty()){
                binding.edtPass.error = "Please enter your password"
            }
            else{

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val user = firebaseAuth.currentUser
                        updateUI(user)
                    }else{
                        Log.d(TAG, "Login failed. ${task.exception}")
                        Toast.makeText(this, "Authentication failed. ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }
    }

    private fun forgotPassword(){

        binding.tvForgotpassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun signUp(){
        binding.tvSignin.setOnClickListener {
            val intent = Intent(this, RegisterAcitvity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            Log.d(TAG, "current user: $user")
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}