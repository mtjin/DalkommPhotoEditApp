package com.example.dalkommphoto;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 2016-01-26.
 */
public class LoginActivity extends Activity {

    private SessionCallback callback;      //콜백 선언
    //유저프로필
    String token = "";
    String name = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final static String TAG = "LoginActivityT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //카카오 로그인 콜백받기
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        //키값 알아내기(알아냈으면 등록하고 지워도 상관없다)
        getAppKeyHash();

        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
        loadShared();
    }

    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            requestMe();
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }


    protected void requestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
            } // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(final UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                Logger.d("UserProfile : " + userProfile);
                Log.d(TAG, "유저가입성공");
                // Create a new user with a first and last name
                // 유저 카카오톡 아이디 디비에 넣음(첫가입인 경우에만 디비에저장)

                Map<String, String> user = new HashMap<>();
                user.put("token", userProfile.getId() + "");
                user.put("name", userProfile.getNickname());
                db.collection("users")
                        .document(userProfile.getId() + "")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "유저정보 디비삽입 성공");
                                saveShared(userProfile.getId() + "", userProfile.getNickname());
                            }
                        });
                redirectHomeActivity(); // 로그인 성공시 MainActivity로

            }
        });
    }

    private void redirectHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    /*쉐어드에 입력값 저장*/
    private void saveShared( String id, String name) {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", id);
        editor.putString("name", name);
        editor.apply();
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        token = pref.getString("token", "");
        name = pref.getString("name", "");
    }
}
