package com.example.quizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quizapp.adapter.FoodAdapter
import com.example.quizapp.data.Food
import com.example.quizapp.databinding.ActivityPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var recycle: RecyclerView
    private lateinit var postList: ArrayList<Food>
    private lateinit var postAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycle = binding.rvPost
      //  recycle.layoutManager = LinearLayoutManager(this)

        recycle.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            initAdapter()
        recycle.setHasFixedSize(true)
        recycle.adapter = postAdapter
        postList = arrayListOf()

        getData()


        clickBack()
        openPostRecipe()

        binding.btnSearch.setOnClickListener {

            searchByName(binding.tvSeach.text.toString())
        }

    }

    private fun getData() {

        FirebaseFirestore.getInstance().collection("food")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){

                        val foodrp = document.toObject(Food::class.java)
//                    Log.d("Post", "${foodrp.foodName}")
                    postList.add(foodrp)
                }
                postAdapter.setFoods(postList)


            }.addOnFailureListener {
                Toast.makeText(this, " ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }
    private fun initAdapter() {
        postAdapter = FoodAdapter {
            openDetail(it)
        }
    }
    private fun openDetail(position: Int) {
        val intent = Intent(this@PostActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.INTENT_PARCELABLE , postList[position])
        startActivity(intent)
    }


    private fun clickBack(){

        binding.imgSignOut.setOnClickListener {
            val intent = Intent(this@PostActivity, AccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun openPostRecipe(){
        binding.btnPost.setOnClickListener {
            val intent = Intent(this@PostActivity, PostRecipeActivity::class.java )
            startActivity(intent)
            finish()
        }
    }

    private fun searchByName(name: String) {

        FirebaseFirestore.getInstance().collection("food")
            .get()
            .addOnSuccessListener { documents ->
                postList.clear()
                if (name == "") {
                    getData()
                } else {
                    for (document in documents){

                        val foodrp = document.toObject(Food::class.java)

                        if (foodrp.foodName!!.indexOf(name)!=-1){
                            postList.add(foodrp)
                        }

                    }
                    postAdapter.setFoods(postList)

                }


            }.addOnFailureListener {
                Toast.makeText(this, " ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

