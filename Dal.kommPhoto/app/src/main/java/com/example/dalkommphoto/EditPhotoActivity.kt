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
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter



class EditPhotoActivity : AppCompatActivity() {

    var PICK_IMAGE_FROM_ALBUM = 0
    var storage : FirebaseStorage? = null
    var photoUri: Uri? = null
    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    var token: String? = ""
    var name: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoedit)

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