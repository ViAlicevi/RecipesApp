package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.quizapp.data.User
import com.example.quizapp.databinding.ActivityRegisterAcitvityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterAcitvityBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
   // private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvHaveAccount.setOnClickListener {
            onBackPressed()
        }

        binding.btnSignin.setOnClickListener {

            if(binding.phoneNumber.text.toString().isEmpty()){
                  binding.phoneNumber.error = "Please enter your phone"
            }
            if (binding.email.text.toString().isEmpty()){
                binding.email.error = "Please enter your email"
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.toString()).matches()){
                    binding.email.error = "Invalid email format"
                }
            }
            if (binding.etName.text.toString().isEmpty()){
                binding.etName.error = "Please enter your name"

            }
            if (binding.etPass.text.toString().isEmpty()){
                binding.etPass.error = "Please enter password"

                if (binding.etPass.length()<6){
                        Toast.makeText(this, "password atleast 6 character", Toast.LENGTH_SHORT).show()
                }
            }

            else {
                val phoneNumber: String = binding.phoneNumber.text.toString()
                val email: String = binding.email.text.toString()
                val etName: String = binding.etName.text.toString()
                val etPass: String = binding.etPass.text.toString()
                mAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(
                        email.trim(),
                        etPass.trim(),
                    )

                database = FirebaseDatabase.getInstance().getReference("Users")
                val User = User(phoneNumber, email, etName)
                database.child(etName).setValue(User).addOnSuccessListener {
                    binding.phoneNumber.text?.clear()
                    binding.email.text?.clear()
                    binding.etName.text?.clear()
                    binding.etPass.text?.clear()
                    val user = mAuth.currentUser
                    updateUI(user)

                    Toast.makeText(this, "successfully saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            println("current user: $user")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}