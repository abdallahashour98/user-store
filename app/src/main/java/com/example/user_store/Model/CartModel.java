package com.example.user_store.Model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class CartModel implements Serializable {
    String ImageProduct;
    String Name;
    Object Price;
    @Exclude
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CartModel() {
    }

    public CartModel(String imageProduct, String name, Object price) {
        ImageProduct = imageProduct;
        Name = name;
        Price = price;
    }

    public String getImageProduct() {
        return ImageProduct;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Object getPrice() {
        return Price;
    }

    public void setPrice(Object price) {
        Price = price;
    }
}
