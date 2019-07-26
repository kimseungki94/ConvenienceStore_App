package com.example.icosmos.newproject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

// 사용자의 입력을 바탕으로 물품을 정렬하고 화면에 띄워주는 Class
public class ListActivity extends AppCompatActivity {

    // 사용자가 즐겨찾기한 물품에 대한 DB를 받기위한 객체변수
    private SQLiteDatabase favoriteDB;
    Cursor mCursor;

    private ImageView list_helper;

    // 화면을 구성하기 위한 객체변수
    ListView list;
    MyAdapter adapter;
    FloatingActionButton helpBtn, reverseBtn;

    // 물품을 정렬할 ArrayList
    ArrayList<GoodsInfo> listOfGoods;
    ArrayList<String> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        String detail = null;
        if(intent.getStringExtra("detail") != null)
            detail = intent.getStringExtra("detail");

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);


        // 즐겨찾기에 대한 DB를 생성한다. 이미 존재한다면 그 DB를 사용한다.
        DbHelper dbHelper = new DbHelper(this);
        favoriteDB = dbHelper.getWritableDatabase();
        dbHelper.onCreate(favoriteDB);

        // 사용자의 즐겨찾기한 물품 목록을 ArrayList에 저장한다.
        mCursor = favoriteDB.query("my_table",
                new String[]{"name"},
                null,null,null,null,
                "_id","20");
        favoriteList = new ArrayList<String>();
        if(mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    favoriteList.add(mCursor.getString(0));
                } while(mCursor.moveToNext());
            }
        }

        // MainActivity에서 가져온 물품들의 목록을 사용자의 입력에 맞춰 정렬한다.
        listOfGoods = (ArrayList<GoodsInfo>) intent.getSerializableExtra("DB");
        setData(key,detail);
        sortData();

        // 사용자를 위해 만들어진 목록을 화면에 Adapter를 이용해 List에 띄운다.
        adapter = new MyAdapter(this, listOfGoods, favoriteList);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list_helper = (ImageView)findViewById(R.id.list_helper);
        list_helper.setImageAlpha(0);
        list_helper.setClickable(false);
        list_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_helper.setImageAlpha(0);
                list_helper.setClickable(false);
            }
        });;


        // 화면의 도움말 버튼을 누르면 현재 화면에 대한 도움말을 띄운다.
        helpBtn = (FloatingActionButton)findViewById(R.id.helpBtn2);
        helpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                list_helper.setImageAlpha(1000);
                list_helper.setClickable(true);
            }
        });

        reverseBtn = (FloatingActionButton)findViewById(R.id.reverseBtn);
        reverseBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                reverseList();
            }
        });
    }

    // 사용자의 입력에 맞춰 물품을 추려낸다.
    public void setData(String key, String detail){

        // 사용자가 선택한 입력이 category라면 해당 category를 제외한 나머지 category를 목록에서 제거한다.
        if(key.equals("음료")||key.equals("간편식")||key.equals("유제품")) {
            Iterator<GoodsInfo> iter = listOfGoods.iterator();
            while (iter.hasNext()){
                GoodsInfo s = iter.next();

                if(!(s.getDetail().equals(detail))){
                    iter.remove();
                }
            }
        }

        // 사용자가 선택한 입력이 즐겨찾기라면 사용자의 DB에 저장되어있지 않은 이름은 삭제된다.
        else if(key.equals("즐겨찾기")){
            Iterator<GoodsInfo> iter = listOfGoods.iterator();
            while (iter.hasNext()){
                GoodsInfo s = iter.next();

                if(!(favoriteList.contains(s.getName()))){
                    iter.remove();
                }
            }
        }

        // 사용자가 선택한 입력이 검색이라면,
        // 검색을 위해 받아들인 입력값을 가지고 있지 않은 이름의 항목들을 제거한다.
        else {
            Iterator<GoodsInfo> iter = listOfGoods.iterator();
            while (iter.hasNext()){
                GoodsInfo s = iter.next();

                if(!(s.getName().contains(key))){
                    iter.remove();
                }
            }
        }
    }

    // 물품을 오름차순으로 정렬해주는 메소드
    public void sortData(){

        QuickSort quickSort = new QuickSort();

        // 배열을 이용한 빠른 정렬을 위해 현재의 ArrayList를 배열로 바꿔준다.
        GoodsInfo[] array = listOfGoods.toArray(new GoodsInfo[listOfGoods.size()]);

        // 배열을 빠른정렬로 정렬한다.
        quickSort.quick_sort(array,0,array.length-1);

        // 현재 물품리스트를 전부 제거하고, 오름차순으로 정렬된 배열을 새로 리스트에 넣어 정렬한다.
        listOfGoods.clear();
        for(GoodsInfo tmp : array){
            listOfGoods.add(tmp);
        }
    }

    public void reverseList(){
        Stack stack = new Stack(listOfGoods.size());

        for(GoodsInfo input : listOfGoods) {
            stack.push(input);
        }

        listOfGoods.clear();

        while(!stack.empty()){
            listOfGoods.add((GoodsInfo)stack.pop());
        }

        // 원래 화면에서 즐겨찾기가 추가된 후 물품을 재정렬 할 수 있으므로 즐겨찾기 목록도 새로 설정
        mCursor = favoriteDB.query("my_table",
                new String[]{"name"},
                null,null,null,null,
                "_id","20");
        favoriteList = new ArrayList<String>();
        if(mCursor != null){
            if(mCursor.moveToFirst()){
                do{
                    favoriteList.add(mCursor.getString(0));
                } while(mCursor.moveToNext());
            }
        }
        adapter = new MyAdapter(this, listOfGoods, favoriteList);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    // 사용자가 검색 후 목록에서 물품을 즐겨찾기에 넣거나 뺄때 사용하는 메소드
    public void pressFavorite(View view){

        // 즐겨찾기버튼은 선택되어있냐 아니냐의 2가지 경우밖에 없음
        boolean select = view.isSelected();

        // 물품은 listview로 정렬되어 있기 때문에 순서를 얻어야 해당 물품을 확인 가능.
        int position = (Integer)view.getTag();

        // 버튼이 눌렸을때 그 버튼이 가리키는 물품의 이름을 받는다.
        String name = listOfGoods.get(position).getName();

        // 버튼이 활성화 상태냐 아니냐에 따라 즐겨찾기 DB에 해당 물품을 삭제하거나 추가한다.
        if(select){
            view.setSelected(false);
            favoriteDB.delete("my_table","name="+"'"+name+"'",null);
            Toast.makeText(this,name + "이(가) 즐겨찾기에서 삭제되었습니다.",Toast.LENGTH_LONG).show();
        }
        else
        {
            view.setSelected(true);
            ContentValues input = new ContentValues();
            input.put("name", name);
            favoriteDB.insert("my_table",null,input);
            Toast.makeText(this,name + "이(가) 즐겨찾기에 추가되었습니다.",Toast.LENGTH_LONG).show();
        }
    }
}