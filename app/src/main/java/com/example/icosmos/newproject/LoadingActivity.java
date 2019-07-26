package com.example.icosmos.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// 앱이 켜지면 가장 먼저 나오게되는 화면. 로딩 시간이 지난 후 자동으로 화면이 메인화면으로 넘어감.
public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(1000); //대기 초 설정
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
