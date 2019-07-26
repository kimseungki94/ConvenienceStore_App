package com.example.icosmos.newproject;

// 물품 정렬을 위한 빠른정렬 알고리즘
public class QuickSort {

    public void quick_sort(GoodsInfo[] list, int low, int high){
        if(low<high){
            int pivot = partition(list, low, high);
            quick_sort(list, low,pivot-1);
            quick_sort(list,pivot+1, high);
        }
    }

    public int partition(GoodsInfo[] list, int low, int high){
        int left = low+1;
        int right = high;
        GoodsInfo tmp;

        while(left<=right){
            if(list[left].getPrice()<=list[low].getPrice()){
                left = left + 1;
            }
            else if(list[right].getPrice()>list[low].getPrice()){
                right = right - 1;
            }
            else {
                tmp = list[left];
                list[left] = list[right];
                list[right] = tmp;
                left = left + 1;
                right = right - 1;
            }
        }
        tmp = list[low];
        list[low] = list[right];
        list[right] = tmp;

        return right;
    }
};