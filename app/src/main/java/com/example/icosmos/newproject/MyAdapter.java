package com.example.icosmos.newproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

// ListView에 표현할 항목들을 구성하기 위한 Class
public class MyAdapter extends BaseAdapter{

    // 물품에 들어가는데 필요한 항목들
    private Context context;
    private ArrayList<GoodsInfo> listOfGoods;
    private LayoutInflater inflater;
    private ArrayList<String> favoriteList;
    private StorageReference mStorage;

    public MyAdapter(Context c, ArrayList<GoodsInfo> log, ArrayList<String> arrayList){
        this.context = c;
        this.listOfGoods = log;
        this.favoriteList = arrayList;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfGoods.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfGoods.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_layout,parent,false);
        }

        // 물품의 사진을 Firebase에서 받아와 띄운다.
        String filename = listOfGoods.get(position).getName();
        mStorage = FirebaseStorage.getInstance().getReference();
        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        Glide.with(context).using(new FirebaseImageLoader()).load(mStorage.child(filename+".jpg")).into(image);

        // 물품의 이름, 분류, 가격, 할인정보를 띄운다.
        TextView name = (TextView)convertView.findViewById(R.id.text_name);
        name.setText(listOfGoods.get(position).getName());

        TextView category = (TextView)convertView.findViewById(R.id.text_category);
        category.setText(listOfGoods.get(position).getCategory());

        TextView price = (TextView)convertView.findViewById(R.id.text_price);
        price.setText(String.valueOf(listOfGoods.get(position).getPrice())+"원");

        TextView discount = (TextView)convertView.findViewById(R.id.text_discount);
        discount.setText(listOfGoods.get(position).getDiscount());

        TextView detail = (TextView)convertView.findViewById(R.id.text_detail);
        detail.setText(listOfGoods.get(position).getDetail());

        ImageView Logo = (ImageView)convertView.findViewById(R.id.image_store);
        String store = listOfGoods.get(position).getStore();
        switch (store){
            case "cu":
                Logo.setImageResource(R.drawable.cu_logo);
                break;
            case "gs":
                Logo.setImageResource(R.drawable.gs_logo);
                break;
            case "se":
                Logo.setImageResource(R.drawable.se_logo);
                break;
            default:
                break;
        }


        // 물품이 즐겨찾기로 설정된 물품인지의 유무에따라 버튼의 활성화를 결정.
        ImageButton favoriteBtn = (ImageButton)convertView.findViewById(R.id.favoriteBtn);
        favoriteBtn.setSelected(false);
        for(String str : favoriteList) {
            if(listOfGoods.get(position).getName().equals(str)){
                favoriteBtn.setSelected(true);
                break;
            }
        }
        favoriteBtn.setTag(position);

        return convertView;
    }

};
