package com.example.quizapp.activities

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.data.User
import com.example.quizapp.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private  lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var mEmail: String
    private lateinit var mEtName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        mEmail = auth.currentUser?.email.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        binding.btnUpdate.setOnClickListener {
            showProgressBar()

            val phoneNumber = binding.tvGender.text.toString()
            val email = binding.tvEmail.text.toString()
            val etName = binding.tvname.text.toString()

            val user = User(phoneNumber, email, etName)
            if (uid != null) {
                databaseReference.child(etName).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful) {
                       uploadProfile()

                    } else {
                        hideProgressBar()
                        Toast.makeText(this@AccountActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        addRecipe()
        signOut()

        checkUser()
        clickBack()
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){

            val email =  binding.tvEmail
            val etName = binding.tvname
            val phone = binding.tvGender

            databaseReference = FirebaseDatabase.getInstance().getReference("Users")

            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val userProfile = dataSnapshot.getValue(User::class.java)

                        if (userProfile!!.email == mEmail){

                            email.setText(userProfile?.email)
                            etName.setText(userProfile?.etName)
                            phone.setText(userProfile?.phoneNumber)

                            mEtName = userProfile?.etName!!

                        }
                    }
//                    Log.v("Demo", snapshot.toString())




                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AccountActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })


        }
    else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun uploadProfile() {
        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.funny_quiz}")

        storageReference = FirebaseStorage.getInstance().getReference("Users/"+mEtName)
        storageReference.putFile(imageUri).addOnSuccessListener {

            hideProgressBar()
            Toast.makeText(this@AccountActivity, "Success to update profile", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(this@AccountActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar() {
        dialog = Dialog(this@AccountActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar() {
        dialog.dismiss()

    }
    private fun signOut(){
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun addRecipe(){
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddFoodRecipeActivity::class.java))
            finish()
        }

    }
    private fun clickBack(){
        binding.imgBack.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}