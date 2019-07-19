package com.example.dalkommphoto

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    // button 기능 설정


    // 첫줄 버튼
    // 버튼1 => 뒤로가기 버튼: 홈 엑티비티로

    // 버튼2 => 사진 데이터 베이스에 저장



    // 두번째 줄
    // 이미지 뷰 => 사진 보여주기 + 사진 편집 기능
    // 일단 사진 보여주는 기능 빌드 업

    // 사진 편집 부분
    //      => 홈 엑티비티에서 받아온 이미지를 show
    //      => 
    //      => 사진 위에 작은 원 만들어서
    //      => 원을 드레그해서, 붙인 이미지 사이즈 조정
    //      => 원을 누르고 돌려서, 붙인 이미지 회전 시키기
    //      => 만약 이미지가 겹치면 먼저 불러온 이미지가 아래에
    //      => 원을 더블클릭하면, 붙인 이미지 지우기



    // 붙이기 버튼과 연동된 기능은 나중에

    // 세번째 줄
    // 오리기 버튼 => 파이썬 모듈 함수들 사용 -> db에 사진(사진들) 저장)
    //          => 저장하기 전에 인식된 얼굴 보여주기 (팝업 메세지) 저장 확인
    //          => 얼굴 인식이 안 되면 팝업 메세지

    // 붙이기 버튼 => 슬라이드 업 판넬 생성 후 저장된 이미지 db에서 불러와 보여주기
    //              => 옵션1  판넬에서 이미지 선택해서 사진에 띄우기
    //              => 옵션2  판넬에서 이미지 드레그 해서 사진에 원하는 위치에 가져다 놓기

    // 필터 버튼   => 슬라이드 업 판넬 생성 후, 목록 보여주기
    //           => 목록에서 필터 적용하면
    //
    //



}