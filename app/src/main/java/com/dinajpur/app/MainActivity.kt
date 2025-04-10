package com.dinajpur.app

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.dinajpur.app.adapter.BannerAdapter
import com.dinajpur.app.adapter.ServiceAdapter
import com.dinajpur.app.databinding.ActivityMainBinding
import com.dinajpur.app.model.Service
import com.dinajpur.app.utils.RecyclerItemClickListener
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the Toolbar
//        setSupportActionBar(binding.toolbar)
//
//        // Set up the Navigation Drawer
//        toggle = ActionBarDrawerToggle(
//            this,
//            binding.drawerLayout,
//            binding.toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle Navigation Drawer item clicks
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "হোম ক্লিক করা হয়েছে", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_profile -> {
                    Toast.makeText(this, "প্রোফাইল ক্লিক করা হয়েছে", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_settings -> {
                    Toast.makeText(this, "সেটিংস ক্লিক করা হয়েছে", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_about -> {
                    Toast.makeText(this, "আমাদের সম্পর্কে ক্লিক করা হয়েছে", Toast.LENGTH_SHORT)
                        .show()
                    true
                }

                R.id.nav_logout -> {
                    Toast.makeText(this, "লগআউট ক্লিক করা হয়েছে", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }.also {
                binding.drawerLayout.closeDrawers()
            }
        }

        // Handle window insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // List of banner images
        val banners = listOf(
            R.drawable.banner_image,
            R.drawable.banner_image_1,
            R.drawable.banner_image_2
        )

        // Setup ViewPager2 for Banner
//        binding.bannerViewPager.adapter = BannerAdapter(banners)
//        binding.bannerViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//
//        // Attach dots indicator
//        TabLayoutMediator(binding.bannerIndicator, binding.bannerViewPager) { _, _ -> }.attach()
//
//        // Auto-slide logic
//        binding.bannerViewPager.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                binding.bannerViewPager.postDelayed({
//                    binding.bannerViewPager.currentItem =
//                        (binding.bannerViewPager.currentItem + 1) % banners.size
//                }, 3000) // Auto-slide every 3 seconds
//            }
//        })
//
//        // Enable marquee effect for bannerText
//        binding.bannerText.isSelected = true
//
//        // Set up RecyclerView for services
//        val services = listOf(
//            Service("হাসপাতাল", R.drawable.ic_hospital),
//            Service("ক্লিনিক", R.drawable.ic_clinic),
//            Service("ডায়াগনস্টিক", R.drawable.ic_diagnostic),
//            Service("বিশেষজ্ঞ ডাক্তার", R.drawable.ic_doctor),
//            Service("হোমিও ডাক্তার", R.drawable.ic_homeo),
//            Service("পশু ডাক্তার", R.drawable.ic_vet),
//            Service("হাসপাতাল", R.drawable.ic_hospital),
//            Service("ক্লিনিক", R.drawable.ic_clinic),
//            Service("ডায়াগনস্টিক", R.drawable.ic_diagnostic),
//            Service("বিশেষজ্ঞ ডাক্তার", R.drawable.ic_doctor),
//            Service("হোমিও ডাক্তার", R.drawable.ic_homeo),
//            Service("পশু ডাক্তার", R.drawable.ic_vet),
//            Service("হাসপাতাল", R.drawable.ic_hospital),
//            Service("ক্লিনিক", R.drawable.ic_clinic),
//            Service("ডায়াগনস্টিক", R.drawable.ic_diagnostic),
//            Service("বিশেষজ্ঞ ডাক্তার", R.drawable.ic_doctor),
//            Service("হোমিও ডাক্তার", R.drawable.ic_homeo),
//            Service("পশু ডাক্তার", R.drawable.ic_vet)
//        )
//
//        val adapter = ServiceAdapter(services)
//        binding.servicesRecyclerView.layoutManager = GridLayoutManager(this, 4)
//        binding.servicesRecyclerView.adapter = adapter
//
//        // Add layout animation to RecyclerView
//        binding.servicesRecyclerView.layoutAnimation =
//            AnimationUtils.loadLayoutAnimation(this, R.anim.grid_layout_animation)
//
//        // Add click listener to RecyclerView items
//        binding.servicesRecyclerView.addOnItemTouchListener(
//            RecyclerItemClickListener(this) { _, position ->
//                val service = services[position]
//                Toast.makeText(this, "Clicked: ${service.name}", Toast.LENGTH_SHORT).show()
//            }
//        )

        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_work -> true
                R.id.nav_info -> true
                R.id.nav_engineering -> true
                else -> false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}