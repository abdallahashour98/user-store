package com.example.user_store.Model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class SearchModel implements Serializable {
    private String Name;
    private Object Price;
    private String ImageProduct;
    @Exclude
    private String Id;



    public SearchModel() {
    }


    public SearchModel(String name, Object price, String imageProduct) {
        Name = name;
        Price = price;
        ImageProduct = imageProduct;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getImageProduct() {
        return ImageProduct;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }




}
