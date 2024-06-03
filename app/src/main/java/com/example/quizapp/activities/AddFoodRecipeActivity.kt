package com.example.quizapp.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.databinding.ActivityAddFoodRecipeBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddFoodRecipeActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddFoodRecipeBinding
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChooseImg.setOnClickListener {
            selectImg()
        }
        binding.btnSave.setOnClickListener {
            uploadImg()
        }
        clickBack()
    }

    private fun uploadImg() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading file")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_DD_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageUri).addOnSuccessListener {
            binding.imgUpload.setImageURI(null)
            Toast.makeText(this@AddFoodRecipeActivity, "Sucessfully uploaded", Toast.LENGTH_SHORT)
                .show()
            if (progressDialog.isShowing) progressDialog.dismiss()

        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@AddFoodRecipeActivity, "Failed: ${it.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun selectImg() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            binding.imgUpload.setImageURI(imageUri)
        }
    }
    private fun clickBack(){
        binding.imgBack.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
        }

    }
}