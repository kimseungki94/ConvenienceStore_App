package com.example.icosmos.newproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Boolean isDrinkOpen = false;
    private Boolean isMilkOpen = false;
    private Boolean isSandOpen = false;
    private Button drink1,drink2,drink3,drink4,drink5;
    private Button milk1, milk2, milk3;
    private Button sand1, sand2, sand3;

    ImageView mainHelper;

    private Animation open, close;

    DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDBVersion = mDB.child("version");
    DatabaseReference mDBCU = mDB.child("cu");
    DatabaseReference mDBGS = mDB.child("gs");
    DatabaseReference mDBSE = mDB.child("se");

    TextView tv;
    FloatingActionButton helpBtn;
    ImageButton searchBtn;
    ArrayList<GoodsInfo> listOfGoods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // 물품을 세분화 하기 위해 버튼을 추가
        drink1 = (Button)findViewById(R.id.carbon);
        drink2 = (Button)findViewById(R.id.ionic);
        drink3 = (Button)findViewById(R.id.coffee);
        drink4 = (Button)findViewById(R.id.juice);
        drink5 = (Button)findViewById(R.id.water);
        milk1 = (Button)findViewById(R.id.milk);
        milk2 = (Button)findViewById(R.id.ice_cream);
        milk3 = (Button)findViewById(R.id.fermented);
        sand1 = (Button)findViewById(R.id.noodle);
        sand2 = (Button)findViewById(R.id.rice);
        sand3 = (Button)findViewById(R.id.bread);
        open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.open);
        close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close);
        drink1.setOnClickListener(this);
        drink2.setOnClickListener(this);
        drink3.setOnClickListener(this);
        drink4.setOnClickListener(this);
        drink5.setOnClickListener(this);
        milk1.setOnClickListener(this);
        milk2.setOnClickListener(this);
        milk3.setOnClickListener(this);
        sand1.setOnClickListener(this);
        sand2.setOnClickListener(this);
        sand3.setOnClickListener(this);

        listOfGoods = new ArrayList<GoodsInfo>();

        // CU편의점의 물품들에 대한 DB를 연동하여 갱신함.
        // 초기 계획에서 ListActivity에서 연동하였으나 연동속도 문제와 정렬문제로 인해 메인액티비티에서 작업함.
        mDBCU.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    int price = snapshot.child("price").getValue(int.class);
                    String discount = snapshot.child("discount").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    String detail = snapshot.child("detail").getValue(String.class);
                    listOfGoods.add(new GoodsInfo(name, category, price, discount, detail,"cu"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDBGS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    int price = snapshot.child("price").getValue(int.class);
                    String discount = snapshot.child("discount").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    String detail = snapshot.child("detail").getValue(String.class);
                    listOfGoods.add(new GoodsInfo(name, category, price, discount, detail,"gs"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDBSE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    int price = snapshot.child("price").getValue(int.class);
                    String discount = snapshot.child("discount").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    String detail = snapshot.child("detail").getValue(String.class);
                    listOfGoods.add(new GoodsInfo(name, category, price, discount, detail,"se"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // 파이어베이스에 저장된 DB의 최종 수정일을 화면에 표시해줌.
        tv = (TextView)findViewById(R.id.version);
        mDBVersion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                tv.setText("최종 DB 수정일 : "+name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mainHelper = (ImageView)findViewById(R.id.main_helper);
        mainHelper.setImageAlpha(0);
        mainHelper.setClickable(false);
        mainHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHelper.setImageAlpha(0);
                mainHelper.setClickable(false);
            }
        });

        // 도움말 버튼 클릭시 일어나는 이벤트 정의
        // 선택시 안내문이 나옴.
        helpBtn = (FloatingActionButton)findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mainHelper.setImageAlpha(1000);
                mainHelper.setClickable(true);
            }
        });

        // 검색버튼을 클릭시 일어나는 이벤트 정의
        // 창이 하나 생성되고, 사용자가 검색하려는 내용을 입력받아 전달가능.
        searchBtn = (ImageButton)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("검색");
                final EditText editText = new EditText(MainActivity.this);
                ad.setView(editText);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Intent intent5 = new Intent(MainActivity.this, ListActivity.class);
                        String input = editText.getText().toString();
                        dialog.dismiss();
                        intent5.putExtra("key",input);
                        intent5.putExtra("DB",listOfGoods);
                        startActivity(intent5);
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
    }

    // 유제품 버튼을 눌렀을때의 메소드
    public void recommend_MilkProduct(View v){
        animate(v);
    }

    // 간편식 버튼을 눌렀을때의 메소드
    public void recommend_ConvenienceFood(View v){
        animate(v);
    }

    // 음료 버튼을 눌렀을때의 메소드
    public void recommend_Drink(View v){
        animate(v);
    }

    // 즐겨찾기 버튼을 눌렀을때의 메소드
    public void bookmark(View v){
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("key","즐겨찾기");
        intent.putExtra("DB",listOfGoods);
        startActivity(intent);
    }

    public void onClick(View v){
        int id = v.getId();
        Intent intent = new Intent(this, ListActivity.class);
        switch (id){
            case R.id.carbon:
                intent.putExtra("key","음료");
                intent.putExtra("detail","탄산");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.ionic:
                intent.putExtra("key","음료");
                intent.putExtra("detail","이온");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.coffee:
                intent.putExtra("key","음료");
                intent.putExtra("detail","커피");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.juice:
                intent.putExtra("key","음료");
                intent.putExtra("detail","쥬스");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.water:
                intent.putExtra("key","음료");
                intent.putExtra("detail","물");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.milk:
                intent.putExtra("key","유제품");
                intent.putExtra("detail","우유");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.ice_cream:
                intent.putExtra("key","유제품");
                intent.putExtra("detail","아이스크림");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.fermented:
                intent.putExtra("key","유제품");
                intent.putExtra("detail","발효유");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.noodle:
                intent.putExtra("key","간편식");
                intent.putExtra("detail","면");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.rice:
                intent.putExtra("key","간편식");
                intent.putExtra("detail","밥");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
            case R.id.bread:
                intent.putExtra("key","간편식");
                intent.putExtra("detail","빵");
                intent.putExtra("DB",listOfGoods);
                startActivity(intent);
                break;
        }
    }

    public void animate(View v){
        int id = v.getId();
        switch (id){
            case R.id.drinkBtn:
                if(isDrinkOpen){
                    drink1.startAnimation(close);
                    drink1.setClickable(false);
                    drink2.startAnimation(close);
                    drink2.setClickable(false);
                    drink3.startAnimation(close);
                    drink3.setClickable(false);
                    drink4.startAnimation(close);
                    drink4.setClickable(false);
                    drink5.startAnimation(close);
                    drink5.setClickable(false);
                    isDrinkOpen = false;
                }
                else {
                    if(isMilkOpen){
                        milk1.startAnimation(close);
                        milk1.setClickable(false);
                        milk2.startAnimation(close);
                        milk2.setClickable(false);
                        milk3.startAnimation(close);
                        milk3.setClickable(false);
                        isMilkOpen = false;
                    }
                    if(isSandOpen){
                        sand1.startAnimation(close);
                        sand1.setClickable(false);
                        sand2.startAnimation(close);
                        sand2.setClickable(false);
                        sand3.startAnimation(close);
                        sand3.setClickable(false);
                        isSandOpen = false;
                    }
                    drink1.startAnimation(open);
                    drink1.setClickable(true);
                    drink2.startAnimation(open);
                    drink2.setClickable(true);
                    drink3.startAnimation(open);
                    drink3.setClickable(true);
                    drink4.startAnimation(open);
                    drink4.setClickable(true);
                    drink5.startAnimation(open);
                    drink5.setClickable(true);
                    isDrinkOpen = true;
                }
                break;
            case R.id.milkBtn:
                if(isMilkOpen){
                    milk1.startAnimation(close);
                    milk1.setClickable(false);
                    milk2.startAnimation(close);
                    milk2.setClickable(false);
                    milk3.startAnimation(close);
                    milk3.setClickable(false);
                    isMilkOpen = false;
                }
                else{
                    if(isDrinkOpen){
                        drink1.startAnimation(close);
                        drink1.setClickable(false);
                        drink2.startAnimation(close);
                        drink2.setClickable(false);
                        drink3.startAnimation(close);
                        drink3.setClickable(false);
                        drink4.startAnimation(close);
                        drink4.setClickable(false);
                        drink5.startAnimation(close);
                        drink5.setClickable(false);
                        isDrinkOpen = false;
                    }
                    if(isSandOpen){
                        sand1.startAnimation(close);
                        sand1.setClickable(false);
                        sand2.startAnimation(close);
                        sand2.setClickable(false);
                        sand3.startAnimation(close);
                        sand3.setClickable(false);
                        isSandOpen = false;
                    }
                    milk1.startAnimation(open);
                    milk1.setClickable(true);
                    milk2.startAnimation(open);
                    milk2.setClickable(true);
                    milk3.startAnimation(open);
                    milk3.setClickable(true);
                    isMilkOpen = true;
                }
                break;
            case R.id.sandBtn:
                if(isSandOpen){
                    sand1.startAnimation(close);
                    sand1.setClickable(false);
                    sand2.startAnimation(close);
                    sand2.setClickable(false);
                    sand3.startAnimation(close);
                    sand3.setClickable(false);
                    isSandOpen = false;
                }
                else{
                    if(isDrinkOpen){
                        drink1.startAnimation(close);
                        drink1.setClickable(false);
                        drink2.startAnimation(close);
                        drink2.setClickable(false);
                        drink3.startAnimation(close);
                        drink3.setClickable(false);
                        drink4.startAnimation(close);
                        drink4.setClickable(false);
                        drink5.startAnimation(close);
                        drink5.setClickable(false);
                        isDrinkOpen = false;
                    }
                    if(isMilkOpen){
                        milk1.startAnimation(close);
                        milk1.setClickable(false);
                        milk2.startAnimation(close);
                        milk2.setClickable(false);
                        milk3.startAnimation(close);
                        milk3.setClickable(false);
                        isMilkOpen = false;
                    }
                    sand1.startAnimation(open);
                    sand1.setClickable(true);
                    sand2.startAnimation(open);
                    sand2.setClickable(true);
                    sand3.startAnimation(open);
                    sand3.setClickable(true);
                    isSandOpen = true;
                }
                break;
        }
    }
}
