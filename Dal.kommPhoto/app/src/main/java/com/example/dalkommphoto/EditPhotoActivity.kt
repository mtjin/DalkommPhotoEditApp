package com.example.dalkommphoto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_photoedit.*
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.view.MenuItem
import com.example.dalkommphoto.R.id.gallery_editimage
import com.example.dalkommphoto.R.id.gallery_editsave
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter



class EditPhotoActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    var token: String? = ""
    var name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoedit2)

        // 하단 네비게이션 설정
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        bottom_navigation.selectedItemId = R.id.home

        // 저장소 객체생성
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent,PICK_IMAGE_FROM_ALBUM)

        gallery_editsave.setOnClickListener{
            contentUpload()
        }
        loadShared()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {    // 해당 네비게이션 메뉴 선택
        setToolbarDefault()
        when (item.itemId){
            R.id.action_home -> {
                var cutViewFragment = CutViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, cutViewFragment).commit()
                return true
            }
            R.id.action_search -> {
                var effectViewFragment = EffectViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, effectViewFragment).commit()
                return true
            }
            R.id.action_picture -> {
                var picktureViewFragment = PictureViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, picktureViewFragment).commit()
                return true
            }
            R.id.action_gallery -> {
                var galleryViewFragment = GalleryViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, galleryViewFragment).commit()
                return true
            }
            R.id.action_account -> {
                var accountViewFragment = AccountViewFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, accountViewFragment).commit()
                return true
            }
        }
        return false
    }

    fun setToolbarDefault(){
    }
       override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_FROM_ALBUM) {
            if(resultCode == Activity.RESULT_OK){
                // 선택된 이미지 경로
                photoUri = data?.data
                gallery_editimage.setImageURI(photoUri)
            }else {
                finish()
                // 선택없이 앨범을 떠날경우 액티비티 인텐트
            }
        }
    }

    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener{
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                var contentDTO = ContentDTO()
                contentDTO.imageUrl = uri.toString()
                contentDTO.uid = token
                contentDTO.userId = name
                contentDTO.timestamp = System.currentTimeMillis()
                firestore?.collection("images")?.document()?.set(contentDTO)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    /*쉐어드값 불러오기*/
    private fun loadShared() {
        val pref = getSharedPreferences("profile", Context.MODE_PRIVATE)
        token = pref.getString("token", "")
        name = pref.getString("name", "")
    }
}