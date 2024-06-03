package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.adapter.RecipeAdapter
import com.example.quizapp.data.Recipe
import com.example.quizapp.databinding.ActivityPostRecipeBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference

class PostRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostRecipeBinding

    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storage = FirebaseStorage.getInstance()
         storageReference = storage.reference.child("images")
        val imageList: ArrayList<Recipe> = ArrayList()

        val listAllTask: Task<ListResult> = storageReference.listAll()
        listAllTask.addOnCompleteListener { result ->

            val items: List<StorageReference> = result.result!!.items
            items.forEachIndexed { index, item ->

                item.downloadUrl.addOnSuccessListener {

                    Log.d("item", "$it")
                    imageList.add(Recipe(it.toString()))
                }.addOnCompleteListener {

                    binding.rvPost.adapter = RecipeAdapter(imageList, this)
                    binding.rvPost.layoutManager = LinearLayoutManager(this)
                }
            }

        }
        openAccount()


    }

    private fun openAccount(){
        binding.imgSignOut.setOnClickListener {
            val intent = Intent(this@PostRecipeActivity,PostActivity::class.java )
            startActivity(intent)
            finish()
        }
    }
}

