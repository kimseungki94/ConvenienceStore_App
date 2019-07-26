package com.example.icosmos.newproject;

import java.io.Serializable;

// 물품의 정보들을 저장하는 클래스
// 물품정보를 직렬화하여 intent에 실어 보낼 수 있게함
public class GoodsInfo implements Serializable{

    // 물품의 정보는 이름, 분류, 가격, 할인정보로 구성된다.
    private String store;
    private String name;
    private String category;
    private int price;
    private String discount;
    private String detail;

    public GoodsInfo(String name, String category, int price, String discount, String detail, String store){
        this.store = store;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.detail = detail;
    }

    public String getName(){ return name; }

    public String getCategory(){
        return category;
    }

    public int getPrice(){
        return price;
    }

    public String getDiscount(){
        return discount;
    }

    public String getDetail() {
        return detail;
    }

    public String getStore() {
        return store;
    }
};
