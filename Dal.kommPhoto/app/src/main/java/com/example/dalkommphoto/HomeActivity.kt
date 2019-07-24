package com.example.dalkommphoto

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast

class HomeActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var lastTimeBackPressed: Long = 0
    var myToolbar : Toolbar? = null
    var search_file : ImageView? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById<View>(R.id.drawerLayout) as DrawerLayout               // 드로어 레이아웃 id 가져오기
        navigationView = findViewById<View>(R.id.main_navigationView) as NavigationView     // 메인 네비게이션 id 가져오기
        navigationView!!.setNavigationItemSelectedListener(this)
        myToolbar = findViewById(R.id.my_toolbar)

        setSupportActionBar(myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)
        supportActionBar!!.setTitle("")  //해당 액티비티의 툴바에 있는 타이틀을 공백으로 처리

        search_file = findViewById<View>(R.id.search_file) as ImageView
        search_file!!.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(this, EditPhotoActivity::class.java))
            }
        }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu): Boolean {             // 메뉴 생성시 menu.main_menu 기본지정
        menuInflater.inflate(R.menu.main_navigation_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    @Override
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
        }

        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    @Override
    override fun onBackPressed() {
        if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawers()
        } else {
            super.onBackPressed()
        }
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish()
            return
        }
        Toast.makeText(this, "뒤로 버튼을 한 번 더 눌러 종료 합니다", Toast.LENGTH_SHORT).show()
        lastTimeBackPressed = System.currentTimeMillis()
    }
}
