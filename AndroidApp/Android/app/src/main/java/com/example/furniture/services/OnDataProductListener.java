package com.example.furniture.services;

import android.content.Context;
import android.view.View;

import com.example.furniture.models.Cart;
import com.example.furniture.models.Category;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;

import java.util.ArrayList;

public interface OnDataProductListener {
    void onCompleteDataProduct(Context view, ArrayList<Product> arrayList);

    void onErrorDataProduct(Context view,String error);

    void onCompleteDataFavProduct(Context view, ArrayList<Product> products, ArrayList<Favourite> favourites);

    void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts);

    void onCompleteDataOrderDetailProduct(Context view, ArrayList<Product> products, ArrayList<OrderDetail> carts);

}
