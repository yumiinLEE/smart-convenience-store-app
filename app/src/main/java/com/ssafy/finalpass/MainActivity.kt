package com.ssafy.finalpass

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpass.databinding.ActivityMainBinding
import com.ssafy.finalpass.fragment.GptFragment
import com.ssafy.finalpass.fragment.HomeFragment
import com.ssafy.finalpass.fragment.MyPageFragment
import com.ssafy.finalpass.fragment.OrderFragment
import com.ssafy.finalpass.fragment.SearchFragment
import com.ssafy.finalpass.fragment.ShoppingCartFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var isAttendanceCheckedToday = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }

                R.id.nav_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment())
                        .commit()
                    true
                }

                R.id.nav_qr -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GptFragment())
                        .commit()
                    true
                }

                R.id.nav_order_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, OrderFragment())
                        .commit()
                    true
                }

                R.id.nav_my_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MyPageFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 배너 메뉴
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 장바구니
            R.id.shoppingList -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ShoppingCartFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            // 알림
            R.id.action_notification -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    fun setUIVisible(bottomVisible: Boolean) {
        // 하단 네비게이션
        binding.bottomNavigation.visibility = if (bottomVisible) View.VISIBLE else View.GONE
    }
}