package com.example.recipesapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.Food
import com.example.recipesapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object{
        val INTENT_PARCELABLE = "OBJECT_INTENT"
    }

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickBack()

     //   val food = intent.getParcelableExtra<Food>(PostActivity.INTENT_PARCELABLE)
        showInformation()
        shareRecipe()
        SourceRecipes()
        Sourceyoutube()

       // imgSrc.setImageResource(food?.url!!)
//        imgTitle.text = food.foodName
//        imgDesc.text = food.Description

    }
    private fun showInformation() {
        val imageData = intent.getParcelableExtra<Food>(INTENT_PARCELABLE)

        with(binding) {
            Glide.with(imgDetail.context)
                .load(imageData?.url)
                .placeholder(R.drawable.ic_replay)
                .into(imgDetail)

            tvTwitterDetail.text = imageData?.Description
            tvInstagramDetail.text = imageData?.foodName
            tvUrl.text = imageData?.link
            tvSource.text = imageData?.source
            //tvYoutube.text = imageData?.sourceYoutube

        }
    }
    private fun clickBack(){
        binding.imgBack.setOnClickListener {
            finish()
        }

    }
    private fun shareRecipe(){
        binding.tvShareRecipe.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "share to: "))
        }
    }
    private fun SourceRecipes(){
        val Source = intent.getStringExtra(INTENT_PARCELABLE)
        binding.tvSource.setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Source)
            startActivity(intent)
        }

        binding.tvYoutube.setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Source)
            startActivity(intent)
        }
    }

    private fun Sourceyoutube(){
        val Source = intent.getStringExtra(INTENT_PARCELABLE)
        binding.tvYoutube.setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Source)
            startActivity(intent)
        }
    }
}