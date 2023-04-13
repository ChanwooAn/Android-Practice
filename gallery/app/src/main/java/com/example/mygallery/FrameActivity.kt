package com.example.mygallery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mygallery.databinding.ActivityFrameBinding
import com.google.android.material.tabs.TabLayoutMediator


const val TAG="FrameActivityLog"
class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Detail"
            setDisplayHomeAsUpEnabled(true)
        }


        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString ->
            FrameItem(Uri.parse(uriString))
        }.toList()

        val frameAdapter = FrameAdapter(images)
        binding.viewPager.adapter = frameAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, postion ->
            binding.viewPager.currentItem = tab.position
        }.attach()
        Log.d(TAG, "onCreate")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionMenu")
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionItemSelected")
        return when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "")
                finish()
                true
            }
            R.id.action_add->{
                Toast.makeText(this,"check",Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                finish()
                true
            }
        }
    }

}